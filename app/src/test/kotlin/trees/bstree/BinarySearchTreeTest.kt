package trees.bstree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import java.util.Random

class BinarySearchTreeTest {

    private fun <K: Comparable<K>> correctBuildBSTree(tree: BinarySearchTree<K>): Boolean {
        if (tree.key == null) return true
        val stack = mutableListOf<BinarySearchTree<K>>()
        var currentTree: BinarySearchTree<K>? = tree
        var prevTree: BinarySearchTree<K>? = null
        while (currentTree != null || stack.isNotEmpty()) {
            while (currentTree != null) {
                stack.add(currentTree)
                currentTree = currentTree.left
            }

            currentTree = stack.removeLast()
            if (prevTree != null && (currentTree.key == null || prevTree.key == null || currentTree.key!! <= prevTree.key!!)) {
                return false
            }
            prevTree = currentTree

            currentTree = currentTree.right
        }
        return true
    }

    @Test
    fun `overwriting the key value`() {
        val tree = BinarySearchTree<Int>()
        tree.insert(50)
        tree.insert(100)
        tree.insert(5)
        tree.insert(10, "Good")
        assertEquals(tree.findByKey(10), "Good")
        tree.insert(10, "Super Good")
        assertEquals(tree.findByKey(10), "Super Good")
    }

    @Test
    fun `find by non-existent key and return null`() {
        val tree = BinarySearchTree<Int>()
        assertNull(tree.findByKey(100))
    }


    @Test
    fun `removing non-existent key`() {
        val tree = BinarySearchTree<Int>()
        tree.insert(10, 10)
        tree.insert(5, 5)
        tree.insert(12, 12)
        tree.remove(20)
        tree.remove(4)
        assertTrue(tree.contains(10))
        assertTrue(tree.contains(12))
        assertTrue(tree.contains(5))
    }

    @Test
    fun `inserting huge number of type string keys`() {
        val tree = BinarySearchTree<String>()
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        for (i in 1..1000) {
            val key = (1..100) .map { charset.random() } .joinToString("")
            val value = (1..100) .map { charset.random() } .joinToString("")
            tree.insert(key, value)
            assertTrue(correctBuildBSTree(tree))
        }


    }

    @Test
    fun `inserting huge number of type int keys`() {
        val tree = BinarySearchTree<Int>()
        val randomizer = Random(42)
        for (i in 1..1000) {
            val key = randomizer.nextInt()
            val value = randomizer.nextInt()
            tree.insert(key, value)
            assertTrue(correctBuildBSTree(tree))
        }
    }

    @Test
    fun `getting the value of the key`() {
        val bst = BinarySearchTree<Int>()
        bst.key = 4
        bst.value = 1

        val right = BinarySearchTree<Int>()
        val left = BinarySearchTree<Int>()

        bst.left = left
        bst.right = right

        right.key = 5
        right.value = 3
        left.key = 3
        left.value = 2

        assertEquals(1, bst.findByKey(4))
        assertEquals(2, bst.findByKey(3))
        assertEquals(3, bst.findByKey(5))
        assertEquals(null, bst.findByKey(1))

    }

    @Test
    fun `inserting a new key`() {

        val bst1 = BinarySearchTree<Int>()

        for (i in 1..9)
            bst1.insert(i, 10 - i)

        for (i in 1..9)
            assertTrue(bst1.contains(i))

        for (i in 1..9)
            assertEquals(i, bst1.findByKey(10 - i))
    }

    @Test
    fun `removing a key`() {

        val bst = BinarySearchTree<Int>()

        for (i in 1..9)
            bst.insert(i, 10 - i)

        bst.remove(5)

        assertEquals(null, bst.findByKey(5))
        for (i in 1..9)
            if (i == 5)
                assertFalse(bst.contains(5))
            else
                assertTrue(bst.contains(i))

    }
}