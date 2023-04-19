package trees.database.neo4j

import org.neo4j.driver.*
import org.neo4j.driver.exceptions.SessionExpiredException
import org.neo4j.driver.exceptions.value.Uncoercible
import trees.rbtree.RedBlackTree
import trees.rbtree.RedBlackTree.Color.*
import java.io.Closeable
import java.io.IOException

class Neo4jDataBase() : Closeable {
    private var driver: Driver? = null

    fun open(uri: String, username: String, password: String) {
        try {
            driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password))
        } catch (exeption: IllegalArgumentException) {
            throw IOException("Wrong URI", exeption)
        } catch (exeption: SessionExpiredException) {
            throw IOException("Session failed", exeption)
        }

    } override fun close() {
        driver?.close()
    }
    private fun cleanDataBase(tx: TransactionContext) {
        tx.run("MATCH (n: node) DETACH DELETE n")
    }

    private fun createNode(sb: StringBuilder, node: RedBlackTree<String>) {
        with(node) {
            sb.append(
                "CREATE (:node {key : \"${node.key}\", " +
                        "value: \"${node.value ?: ""}\", " +
                        "isBlack: ${node.color == BLACK}, " +
                        "lkey: \"${left?.key ?: ""}\", " +
                        "rkey: \"${right?.key ?: ""}\"}) "
            )
            left?.let {
                createNode(sb, it)
            }
            right?.let {
                createNode(sb, it)
            }
        }
    }

    private fun genExportTree(root: RedBlackTree<String>): String {
        val sb = StringBuilder()
        createNode(sb, root)
        return sb.toString()
    }

    fun loadTree(root: RedBlackTree<String>) {
        val session = driver?.session() ?: throw IOException("Driver is not open")
        session.executeWrite { transaction ->
            cleanDataBase(transaction)
            transaction.run(genExportTree(root))
            transaction.run(
                "MATCH (p: node) " +
                        "MATCH (l: node {key: p.lkey}) " +
                        "CREATE (p)-[:LEFT_CHILD]->(l) " +
                        "REMOVE p.lkey"
            )
            transaction.run(
                "MATCH (p: node) " +
                        "MATCH (r: node {key: p.rkey}) " +
                        "CREATE (p)-[:RIGHT_CHILD]->(r) " +
                        "REMOVE p.rkey"
            )
        }
        session.close()
    }

    private class NodeKeys(val n: RedBlackTree<String>, val lKey: String?, val rKey: String?)

    private fun parse(nodesRecords: Result) : RedBlackTree<String>? {
        val NodeKeysMap = mutableMapOf<String, NodeKeys>()
        for (record in nodesRecords) {
            try {
                val key = record["key"].asString()
                val value = record["value"].asString()
                val node = RedBlackTree<String>()
                node.key = key
                node.value = value

                val isBlack = record["isBlack"].asBoolean()
                node.color = if (isBlack) BLACK else RED

                val lkey = if (record["lKey"].isNull) null else record["lKey"].asString()
                val rkey = if (record["rKey"].isNull) null else record["rKey"].asString()

                NodeKeysMap[key] = NodeKeys(node, lkey, rkey)
            } catch (exeption: Uncoercible) {
                throw IOException("Invalid nodes record in the database", exeption)
            }
        }
        val nodeKeysArray = NodeKeysMap.values.toTypedArray()
        if (nodeKeysArray.isEmpty())
            return null

        for (nodeKey in nodeKeysArray) {
            nodeKey.lKey?.let {
                nodeKey.n.left = NodeKeysMap[it]?.n
                NodeKeysMap.remove(it)
            }
            nodeKey.n.left?.let {
                nodeKey.n.left = it
                it.parent = nodeKey.n
            }

            nodeKey.rKey?.let {
                nodeKey.n.right = NodeKeysMap[it]?.n
                NodeKeysMap.remove(it)
            }
            nodeKey.n.right?.let {
                nodeKey.n.right = it
                it.parent = nodeKey.n
            }
        }

        val root = NodeKeysMap.values.first().n
        return root
    }

    private fun unloadNode(transaction: TransactionContext): RedBlackTree<String>? {
        val nodesRecords = transaction.run(
            "MATCH (p: RBNode)" +
                    "OPTIONAL MATCH (p)-[:LEFT_CHILD]->(l: node) " +
                    "OPTIONAL MATCH (p)-[:RIGHT_CHILD]->(r: node) " +
                    "RETURN p.isBlack AS isBlack, p.key AS key, p.value AS value, " +
                    "l.key AS lKey, r.key AS rKey"
        )
        return parse(nodesRecords)
    }
    fun unloadTree(): RedBlackTree<String>? {
        val session = driver?.session() ?: throw IOException("Driver is not open")
        val res: RedBlackTree<String>? = session.executeRead { transaction ->
            unloadNode(transaction)
        }
        session.close()
        return res
    }
}