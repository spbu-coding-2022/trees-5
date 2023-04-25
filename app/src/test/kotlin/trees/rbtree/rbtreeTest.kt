package trees.rbtree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import trees.avltree.AVLTree
import java.util.*

class RedBlackTreeTest {

    private fun <K : Comparable<K>, V> correctBuildRBTree(tree: RedBlackTree<K, V>): Boolean {

        val stack = mutableListOf<RedBlackTree<K, V>>()
        var currentTree: RedBlackTree<K, V>? = tree.get_root()
        if (currentTree?.color != RedBlackTree.Color.BLACK) return false
        val rootKey =  currentTree.key ?: return true
        var prevTree: RedBlackTree<K, V>? = null
        while (currentTree != null || stack.isNotEmpty()) {
            while (currentTree != null) {
                stack.add(currentTree)
                currentTree = currentTree.left
            }

            currentTree = stack.removeLast()
            if (prevTree != null && (currentTree.key == null || prevTree.key == null || currentTree.key!! <= prevTree.key!!))
                return false

            if (currentTree.key != rootKey && currentTree != currentTree.parent?.left && currentTree != currentTree.parent?.right)
                return false

            prevTree = currentTree
            currentTree = currentTree.right
        }
        return true
    }

    @Test
    fun `inserting huge number of type string keys`() {
        val tree = RedBlackTree<String, String>()
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        for (i in 1..1000) {
            val key = (1..100) .map { charset.random() } .joinToString("")
            val value = (1..100) .map { charset.random() } .joinToString("")
            tree.insert(key, value)
            assertTrue(correctBuildRBTree(tree))
        }
    }

    @Test
    fun `inserting huge number of type int keys`() {
        val tree = RedBlackTree<Int, Int>()
        val randomizer = Random(42)
        for (i in 1..1000) {
            val key = randomizer.nextInt()
            val value = randomizer.nextInt()
            tree.insert(key, value)
            assertTrue(correctBuildRBTree(tree))
        }
    }

    @Test
    fun `adding a 10_000 number of sorted keys`() {
        val RBTree = RedBlackTree<Int, Int>()
        for (i in 1..10_000) {
            RBTree.insert(i, i)
            assertTrue(correctBuildRBTree(RBTree))
        }
    }

    @Test
    fun `overwriting the key value`() {
        val tree = AVLTree<Int, String>()
        tree.insert(10, "one")
        assertEquals(tree.findByKey(10), "one")
        tree.insert(10, "two")
        assertEquals(tree.findByKey(10), "two")
    }

}