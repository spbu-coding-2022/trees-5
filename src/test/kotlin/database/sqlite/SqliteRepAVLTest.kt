package database.sqlite

import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import trees.avltree.AVLTree
import java.io.File

@TestMethodOrder(MethodOrderer.OrderAnnotation::class)
class SqliteRepAVLTest {
    private val repo = SqliteRepAVL("src/main/kotlin/database/sqlite/TestDatabase")

    @Test
    @Order(1)
    fun `save avl tree Int key in database #1`() {
        val tree = AVLTree<Int, String>()
        tree.insert(10, "a")
        tree.insert(5, "b")
        tree.insert(15, "c")
        repo.setTree("Avl1", tree, "Int")
    }

    @Test
    @Order(2)
    fun `save avl tree Int key in database #2`() {
        val tree = AVLTree<Int, String>()
        tree.insert(15, "a")
        tree.insert(10, "b")
        tree.insert(20, "c")
        repo.setTree("Avl2", tree, "Int")
    }

    @Test
    @Order(3)
    fun `get names trees`() {
        val names = repo.getNamesTrees()
        assertEquals(names.size, 2)
        assertEquals(names[0], "Avl1")
        assertEquals(names[1], "Avl2")
    }

    @Test
    @Order(4)
    fun `save avl tree String key in database #1`() {
        val tree = AVLTree<String, String>()
        tree.insert("b")
        tree.insert("a")
        tree.insert("c")
        repo.setTree("Avl3", tree, "String")
    }

    @Test
    @Order(5)
    fun `save avl tree String key in database #2`() {
        val tree = AVLTree<String, String>()
        tree.insert("c")
        tree.insert("d")
        tree.insert("b")
        repo.setTree("Avl4", tree, "String")
    }

    @Test
    @Order(6)
    fun `get avl tree Int key in database #1`() {
        val tree = repo.getTreeWithIntKey("Avl1")
        // Check correct height tree
        assertEquals(tree.height, 2)
        assertEquals(tree.right?.height, 1)
        assertEquals(tree.left?.height, 1)
        // Check correct value in tree
        assertEquals(tree.value, "a")
        assertEquals(tree.right?.value, "c")
        assertEquals(tree.left?.value, "b")
        // Check correct key in tree
        assertEquals(tree.key, 10)
        assertEquals(tree.right?.key, 15)
        assertEquals(tree.left?.key, 5)
        // Check correct leaf
        assertNull(tree.right?.right)
        assertNull(tree.right?.left)
        assertNull(tree.left?.left)
        assertNull(tree.left?.right)
    }

    @Test
    @Order(7)
    fun `get avl tree Int key in database #2`() {
        val tree = repo.getTreeWithIntKey("Avl2")
        // Check correct height tree
        assertEquals(tree.height, 2)
        assertEquals(tree.right?.height, 1)
        assertEquals(tree.left?.height, 1)
        // Check correct value in tree
        assertEquals(tree.value, "a")
        assertEquals(tree.right?.value, "c")
        assertEquals(tree.left?.value, "b")
        // Check correct key in tree
        assertEquals(tree.key, 15)
        assertEquals(tree.right?.key, 20)
        assertEquals(tree.left?.key, 10)
        // Check correct leaf
        assertNull(tree.right?.right)
        assertNull(tree.right?.left)
        assertNull(tree.left?.left)
        assertNull(tree.left?.right)
    }

    @Test
    @Order(8)
    fun `get avl tree String key in database #1`() {
        val tree = repo.getTreeWithStringKey("Avl3")
        // Check correct height tree
        assertEquals(tree.height, 2)
        assertEquals(tree.right?.height, 1)
        assertEquals(tree.left?.height, 1)
        // Check correct key in tree
        assertEquals(tree.key, "b")
        assertEquals(tree.right?.key, "c")
        assertEquals(tree.left?.key, "a")
        // Check correct leaf
        assertNull(tree.right?.right)
        assertNull(tree.right?.left)
        assertNull(tree.left?.left)
        assertNull(tree.left?.right)
    }

    @Test
    @Order(9)
    fun `get names tree after get tree in database`() {
        val names = repo.getNamesTrees()
        assertEquals(names.size, 4)
        assertEquals(names[0], "Avl1")
        assertEquals(names[1], "Avl2")
        assertEquals(names[2], "Avl3")
        assertEquals(names[3], "Avl4")
    }

    @Test
    @Order(10)
    fun `test get type key of tree`() {
        assertEquals(repo.getKeyTypeOfTree("Avl1"), "Int")
        assertEquals(repo.getKeyTypeOfTree("Avl3"), "String")
    }

    @Test
    @Order(11)
    fun `correct deletion from the database`() {
        repo.remove("Avl1")
        repo.remove("Avl3")
        repo.remove("non-Exist")
        val names = repo.getNamesTrees()
        assertEquals(names.size, 2)
        assertEquals(names[0], "Avl2")
        assertEquals(names[1], "Avl4")
    }

    companion object {
        @JvmStatic
        @AfterAll
        fun `delete testing database`(): Unit {
            val file = File("src/main/kotlin/database/sqlite/TestDatabase")

            val resultDelete = file.delete()
            assertTrue(resultDelete)
        }
    }
}