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

    fun get(treeName: String): AVLTree<Int, String> {
        val AVLTreeNew = AVLTree<Int, String>()
        val root = transaction(db) { DBTree.find(TreesTable.name eq treeName).firstOrNull()?.root } ?: return AVLTree()
        AVLTreeNew.key = root.key.toInt()
        AVLTreeNew.value = root.value
        AVLTreeNew.height = root.height.toInt()
        transaction(db) {
            AVLTreeNew.left = root.left?.convert()
            AVLTreeNew.right = root.right?.convert()
        }
        return AVLTreeNew
    }

    fun set(treeName: String, currentAVLTree: AVLTree<Int, String>): Unit = transaction(db) {
        remove(treeName)

        val dbTree = DBTree.new {
            name = treeName
        }
        val rootNode = DBNode.new {
            key = currentAVLTree.key.toString()
            value = currentAVLTree.value.toString()
            height = currentAVLTree.height.toString()
            tree = dbTree
            left = currentAVLTree.left?.toDBNode(dbTree)
            right = currentAVLTree.right?.toDBNode(dbTree)
        }
        dbTree.root = rootNode

    }

    private fun AVLTree<Int, String>.toDBNode(dbTree: DBTree): DBNode =
        DBNode.new {
            key = this@toDBNode.key.toString()
            value = this@toDBNode.value.toString()
            height = this@toDBNode.height.toString()
            left = this@toDBNode.left?.toDBNode(dbTree)
            right = this@toDBNode.right?.toDBNode(dbTree)
            tree = dbTree
        }

    private fun DBNode.convert(): AVLTree<Int, String> {
        val newAVLTree = AVLTree<Int, String>()
        newAVLTree.key = key.toInt()
        newAVLTree.value = value
        newAVLTree.height = height.toInt()
        newAVLTree.left = left?.convert()
        newAVLTree.right = right?.convert()

        return newAVLTree
    }
}
