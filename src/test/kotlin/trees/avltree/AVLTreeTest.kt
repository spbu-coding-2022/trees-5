package trees.avltree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import trees.correctReferencesParents
import trees.treeKeysCorrectOrder
import java.util.stream.Stream
import kotlin.math.abs
import kotlin.math.max
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AvlTreeTest {
    private fun checkAVLInvariant(tree: AVLTree<Int, Int>): Boolean {

        /**
         * recursively checks that the height of the right and left subtree differs by less than 1
         */
        fun isBalanced(currentTree: AVLTree<Int, Int>?): Int? {
            if (currentTree == null) return 0
            val heightLeft = isBalanced(currentTree.left) ?: return null
            val heightRight = isBalanced(currentTree.right) ?: return null

            if (abs(heightLeft - heightRight) > 1) return null
            return max(heightLeft, heightRight) + 1
        }

        return (isBalanced(tree) != null) && treeKeysCorrectOrder(tree) && correctReferencesParents(tree)
    }

    private val randomizer = Random(87)
    private val keys = Array(1000) { randomizer.nextInt() }.distinct()
    private val values = Array(keys.size) { randomizer.nextInt() }.distinct()
    private lateinit var tree: AVLTree<Int, Int>

    @BeforeEach
    fun recreateTree() {
        tree = AVLTree()
    }

    private fun getData() = Stream.of(
        Pair(10, 68),
        Pair(10, 89),
        Pair(1000, 211),
        Pair(1000, 42),
        Pair(100000, 20),
        Pair(100000, 1337)
    )

    @ParameterizedTest
    @MethodSource("getData")
    fun `insert, remove and find values`(currentData: Pair<Int, Int>) {
        val arraySize = currentData.first
        val seed = currentData.second

        val currentRandomizer = Random(seed)
        val keys = List(arraySize) { currentRandomizer.nextInt() }.distinct()
        val values = List(keys.size) { currentRandomizer.nextInt() }
        val map = mutableMapOf<Int, Int>()
        for (i in keys.indices) {
            map[keys[i]] = values[i]
        }

        for (key in map.keys) {
            tree.insert(key)
        }
        assertTrue(checkAVLInvariant(tree))
        for (key in map.keys.shuffled()) {
            tree.remove(key)
            assertNull(tree.findByKey(key))
        }
        assertTrue(checkAVLInvariant(tree))
    }

    @Test
    fun `invariant and find after deletion`() {
        for (i in keys.indices) tree.insert(keys[i], values[i])
        val leftRange = (Math.random() * keys.size).toInt()
        val rightRange = (Math.random() * (keys.size - leftRange + 1)).toInt() + leftRange
        for (i in leftRange until rightRange) {
            assertEquals(tree.findByKey(keys[i]), values[i])
            tree.remove(keys[i])
            assertTrue(checkAVLInvariant(tree))
            assertNull(tree.findByKey(keys[i]))
        }
    }

    @Test
    fun `invariant and find after insertion`() {
        for (i in keys.indices) {
            tree.insert(keys[i], values[i])
            assertTrue(checkAVLInvariant(tree))
            assertEquals(tree.findByKey(keys[i]), values[i])
        }
    }

    @Test
    fun `invariant after insertion duplicates`() {
        for (i in keys.indices) {
            tree.insert(keys[i], values[i])
            assertTrue(checkAVLInvariant(tree))
            assertEquals(tree.findByKey(keys[i]), values[i])
        }
        val values = Array(keys.size) { randomizer.nextInt() }
        for (i in keys.indices) {
            tree.insert(keys[i], values[i])
            assertTrue(checkAVLInvariant(tree))
            assertEquals(tree.findByKey(keys[i]), values[i])
        }
    }

    @Test
    fun `find by non-existent key and return null`() {
        tree.insert(10)
        assertNull(tree.findByKey(5))
        assertNull(tree.findByKey(100))
    }

    @Test
    fun `overwriting the key value`() {
        tree.insert(10, 10)
        assertEquals(tree.value, 10)
        tree.insert(10, 20)
        assertEquals(tree.value, 20)
    }

    @Test
    fun `removing a non-existent key`() {
        val keys = listOf(10, 20, 30)
        keys.forEach { tree.insert(it, it) }
        val deletedKeys = listOf(2, 40, 25)
        deletedKeys.forEach { tree.remove(it) }
        keys.forEach { assertEquals(tree.findByKey(it), it) }
        deletedKeys.forEach { assertNull(tree.findByKey(it)) }
    }

    @Test
    fun `right rotate testing`() {
        /*
        constructed tree before right rotate:
                  10
                /    \
              8       11
            /   \
           7     9
        */
        tree.insert(10)
        tree.insert(11)
        tree.insert(8)
        tree.insert(9)
        tree.insert(7)
        assertEquals(tree.key, 10)
        assertEquals(tree.left?.key, 8)
        assertEquals(tree.left?.left?.key, 7)
        assertEquals(tree.left?.right?.key, 9)
        assertEquals(tree.right?.key, 11)
        tree.remove(11)
        /*
        constructed tree after remove `11`:
                8
              /   \
             7     10
                   /
                  9
         */
        assertEquals(tree.key, 8)
        assertEquals(tree.left?.key, 7)
        assertEquals(tree.right?.key, 10)
        assertEquals(tree.right?.left?.key, 9)
        assertTrue(checkAVLInvariant(tree))
    }

    @Test
    fun `left rotate testing`() {
        /*
        constructed tree before left rotate:
              10
            /    \
           8      12
                 /  \
                11   13

        */
        tree.insert(10)
        tree.insert(12)
        tree.insert(8)
        tree.insert(11)
        tree.insert(13)
        assertEquals(tree.key, 10)
        assertEquals(tree.left?.key, 8)
        assertEquals(tree.right?.key, 12)
        assertEquals(tree.right?.left?.key, 11)
        assertEquals(tree.right?.right?.key, 13)
        tree.remove(8)
        /*
        constructed tree after remove `8`:
              12
            /   \
           10    13
            \
             11

        */
        assertEquals(tree.key, 12)
        assertEquals(tree.left?.key, 10)
        assertEquals(tree.left?.right?.key, 11)
        assertEquals(tree.right?.key, 13)
        assertTrue(checkAVLInvariant(tree))
    }

    @Test
    fun `big right rotate testing`() {
        tree.insert(10)
        tree.insert(15)
        tree.insert(7)
        tree.insert(9)
        tree.insert(6)
        /*
        constructed tree:
                   10
                 /    \
               7       15
             /   \
            6     9
                 /
                +8?
         */
        assertEquals(tree.key, 10)
        assertEquals(tree.right?.key, 15)
        assertEquals(tree.left?.key, 7)
        assertEquals(tree.left?.left?.key, 6)
        assertEquals(tree.left?.right?.key, 9)
        tree.insert(8)
        /*
         constructed tree:
               9
             /   \
            7     10
          /  \      \
         6    8     15
         */
        assertEquals(tree.key, 9)
        assertEquals(tree.right?.key, 10)
        assertEquals(tree.right?.right?.key, 15)
        assertEquals(tree.left?.key, 7)
        assertEquals(tree.left?.left?.key, 6)
        assertEquals(tree.left?.right?.key, 8)
        assertTrue(checkAVLInvariant(tree))
    }

    @Test
    fun `big left rotate testing`() {
        tree.insert(10)
        tree.insert(8)
        tree.insert(20)
        tree.insert(15)
        tree.insert(22)
        /*
        constructed tree before insert 16:
              10
            /    \
           8      20
                 /  \
                15   22
                 \
                  +16?

        */
        assertEquals(tree.key, 10)
        assertEquals(tree.left?.key, 8)
        assertEquals(tree.right?.key, 20)
        assertEquals(tree.right?.left?.key, 15)
        assertEquals(tree.right?.right?.key, 22)
        tree.insert(16)
        /*
         constructed tree after insert 16:
               15
             /    \
           10      20
          /       /  \
         8       16   22
         */
        assertEquals(tree.key, 15)
        assertEquals(tree.right?.key, 20)
        assertEquals(tree.left?.key, 10)
        assertEquals(tree.left?.left?.key, 8)
        assertEquals(tree.right?.right?.key, 22)
        assertEquals(tree.right?.left?.key, 16)
        assertTrue(checkAVLInvariant(tree))
    }
}
