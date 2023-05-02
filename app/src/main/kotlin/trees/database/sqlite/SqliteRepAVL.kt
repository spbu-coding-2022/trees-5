package trees.database.sqlite

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import trees.avltree.AVLTree
import java.io.File


class SqliteRepAVL(private val dbName: String) {
    private var db: Database = Database.connect("jdbc:sqlite:${File(dbName)}", "org.sqlite.JDBC")

    init {
        transaction(db) {
            SchemaUtils.create(TreesTable)
            SchemaUtils.create(SubtreesAvlTable)
        }
    }

    fun getNamesTrees(): List<String> = transaction(db) {
        val listNames = mutableListOf<String>()
        TreeDatabase.all().forEach { listNames.add(it.name) }
        listNames
    }

    fun remove(treeName: String): Boolean = transaction(db) {
        val currentTree = TreeDatabase.find(TreesTable.name eq treeName).firstOrNull() ?: return@transaction false
        AvlSubtree.find(SubtreesAvlTable.tree eq currentTree.id).forEach { it.delete() }
        currentTree.delete()
        true
    }

    fun getKeyTypeOfTree(treeName: String): String? =
        transaction(db) { TreeDatabase.find(TreesTable.name eq treeName).firstOrNull()?.typeOfKey }

    fun getTreeWithIntKey(treeName: String): AVLTree<Int, String> = transaction(db) {
        val root = TreeDatabase.find(TreesTable.name eq treeName).firstOrNull()?.root ?: return@transaction AVLTree()
        val createdTree = AVLTree<Int, String>()
        createdTree.key = root.key.toInt()
        createdTree.value = root.value
        createdTree.height = root.height.toInt()
        createdTree.left = root.left?.convertTreeWithIntKey(createdTree)
        createdTree.right = root.right?.convertTreeWithIntKey(createdTree)
        createdTree
    }

    fun getTreeWithStringKey(treeName: String): AVLTree<String, String> = transaction(db){
        val root = TreeDatabase.find(TreesTable.name eq treeName).firstOrNull()?.root ?: return@transaction AVLTree()
        val createdTree = AVLTree<String, String>()
        createdTree.key = root.key
        createdTree.value = root.value
        createdTree.height = root.height.toInt()
        createdTree.left = root.left?.convertTreeWithStringKey(createdTree)
        createdTree.right = root.right?.convertTreeWithStringKey(createdTree)
        createdTree
    }

    private fun AvlSubtree.convertTreeWithIntKey(parent: AVLTree<Int, String>? = null): AVLTree<Int, String> {
        val createdTree = AVLTree<Int, String>()
        createdTree.key = key.toInt()
        createdTree.value = value
        createdTree.height = height.toInt()
        createdTree.parent = parent
        createdTree.left = left?.convertTreeWithIntKey(createdTree)
        createdTree.right = right?.convertTreeWithIntKey(createdTree)
        return createdTree
    }

    private fun AvlSubtree.convertTreeWithStringKey(parent: AVLTree<String, String>? = null): AVLTree<String, String> {
        val createdTree = AVLTree<String, String>()
        createdTree.key = key
        createdTree.value = value
        createdTree.height = height.toInt()
        createdTree.parent = parent
        createdTree.left = left?.convertTreeWithStringKey(createdTree)
        createdTree.right = right?.convertTreeWithStringKey(createdTree)
        return createdTree
    }

    fun setTree(treeName: String, currentTree: AVLTree<*, String>, typeKey: String): Unit = transaction(db) {
        if (typeKey != "Int" && typeKey != "String") throw IllegalStateException("Saving this type of key is not supported in the tree")
        remove(treeName)
        val dbTree = TreeDatabase.new {
            name = treeName
            typeOfKey = typeKey
        }
        val avlRoot = AvlSubtree.new {
            key = currentTree.key.toString()
            value = currentTree.value.toString()
            height = currentTree.height.toString()
            tree = dbTree
            left = currentTree.left?.toAvlSubtree(dbTree)
            right = currentTree.right?.toAvlSubtree(dbTree)
        }
        dbTree.root = avlRoot

    }

    private fun AVLTree<*, String>.toAvlSubtree(avlTree: TreeDatabase): AvlSubtree =
        AvlSubtree.new {
            key = this@toAvlSubtree.key.toString()
            value = this@toAvlSubtree.value.toString()
            height = this@toAvlSubtree.height.toString()
            left = this@toAvlSubtree.left?.toAvlSubtree(avlTree)
            right = this@toAvlSubtree.right?.toAvlSubtree(avlTree)
            tree = avlTree
        }


}
