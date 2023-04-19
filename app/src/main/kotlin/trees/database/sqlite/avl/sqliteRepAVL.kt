package trees.database.sqlite.avl

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import trees.avltree.AVLTree


class sqliteRepAVL(private val db: Database) {

    init {
        transaction(db) {
            SchemaUtils.create(TreesTable)
            SchemaUtils.create(NodesTable)
        }
    }

    fun namesTrees(): MutableList<String> = transaction(db) {
        val listNames = mutableListOf<String>()
        DBTree.all().sortedBy { it.name }.forEach { listNames.add(it.name) }
        listNames
    }

    fun remove(treeName: String): Boolean = transaction(db) {
        val currentTree = DBTree.find(TreesTable.name eq treeName).firstOrNull() ?: return@transaction false
        DBNode.find(
            NodesTable.tree eq currentTree.id
        ).forEach { it.delete() }

        currentTree.delete()
        true
    }

    fun get(treeName: String): AVLTree<Int> {
        val treeNew = AVLTree<Int>()
        val root = transaction(db) { DBTree.find(TreesTable.name eq treeName).firstOrNull()?.root } ?: return AVLTree()
        treeNew.key = root.key.toInt()
        treeNew.value = root.value
        treeNew.height = root.height.toInt()
        transaction(db) {
            treeNew.left = root.left?.convert()
            treeNew.right = root.right?.convert()
        }
        return treeNew
    }

    fun set(treeName: String, currentTree: AVLTree<Int>): Unit = transaction(db) {
        remove(treeName)

        val dbTree = DBTree.new {
            name = treeName
        }
        val rootNode = DBNode.new {
            key = currentTree.key.toString()
            value = currentTree.value.toString()
            height = currentTree.height.toString()
            tree = dbTree
            left = currentTree.left?.toDBNode(dbTree)
            right = currentTree.right?.toDBNode(dbTree)
        }
        dbTree.root = rootNode

    }

    private fun AVLTree<Int>.toDBNode(dbTree: DBTree): DBNode =
        DBNode.new {
            key = this@toDBNode.key.toString()
            value = this@toDBNode.value.toString()
            height = this@toDBNode.height.toString()
            left = this@toDBNode.left?.toDBNode(dbTree)
            right = this@toDBNode.right?.toDBNode(dbTree)
            tree = dbTree
        }

    private fun DBNode.convert(): AVLTree<Int> {
        val newTree = AVLTree<Int>()
        newTree.key = key.toInt()
        newTree.value = value
        newTree.height = height.toInt()
        newTree.left = left?.convert()
        newTree.right = right?.convert()

        return newTree
    }
}
