package trees.rbtree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import trees.correctReferencesParents
import trees.treeKeysCorrectOrder
import java.util.*
import java.util.stream.Stream
import kotlin.collections.HashMap
import kotlin.random.Random
import kotlin.reflect.jvm.internal.impl.resolve.scopes.GivenFunctionsMemberScope

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedBlackTreeTest {

    private fun <K : Comparable<K>, V> checkRBTInvariant(tree: RedBlackTree<K, V>): Boolean {
        /**
        checking that the sons of the red vertex are black
         */
        fun checkSonsRedTree(givenTree: RedBlackTree<K, V>): Boolean {
            return if (givenTree.color == RedBlackTree.Color.RED) ((givenTree.left?.color
                ?: RedBlackTree.Color.BLACK) == RedBlackTree.Color.BLACK &&
                    (givenTree.right?.color ?: RedBlackTree.Color.BLACK) == RedBlackTree.Color.BLACK)
                else true
        }

        /**
         * checking that the root of the tree is always black
         */
        fun rootIsBlack(givenTree: RedBlackTree<K, V>): Boolean {
            return givenTree.color == RedBlackTree.Color.BLACK
        }

        val blackHeights = HashMap<RedBlackTree<K, V>, Int>()

        /**
         * checking that each tree has the same black depth
         */
        fun checkBlackHeightsTrees(givenTree: RedBlackTree<K, V>?): Boolean {
            if (givenTree == null) return true
            val leftBooleanResult = checkBlackHeightsTrees(givenTree.left)
            val leftBlackHeight = blackHeights[givenTree.left] ?: 1
            val rightBooleanResult = checkBlackHeightsTrees(givenTree.right)
            val rightBlackHeight = blackHeights[givenTree.right] ?: 1
            blackHeights[givenTree] =
                if (givenTree.color == RedBlackTree.Color.BLACK) leftBlackHeight + 1 else leftBlackHeight

            return leftBooleanResult && rightBooleanResult && (leftBlackHeight == rightBlackHeight) && checkSonsRedTree(
                givenTree)
        }

        return correctReferencesParents(tree.root) && treeKeysCorrectOrder(tree.root) && rootIsBlack(tree.root)
                && checkBlackHeightsTrees(tree.root)
    }


    private val randomizer = Random(87)
    private val keys = Array(1000) { randomizer.nextInt() }.distinct()
    private val values = Array(keys.size) { randomizer.nextInt() }.distinct()
    private lateinit var tree: RedBlackTree<Int, Int>

    @BeforeEach
    fun recreateTree() {
        tree = RedBlackTree()
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
        assertTrue(checkRBTInvariant(tree))
        for (key in map.keys.shuffled()) {
            tree.remove(key)
            assertNull(tree.findByKey(key))
        }
        assertTrue(checkRBTInvariant(tree))
    }

    @Test
    fun `invariant and find after deletion`() {
        for (i in keys.indices) tree.insert(keys[i], values[i])
        val leftRange = (Math.random() * keys.size).toInt()
        val rightRange = (Math.random() * (keys.size - leftRange + 1)).toInt() + leftRange
        for (i in leftRange until rightRange) {
            assertEquals(tree.findByKey(keys[i]), values[i])
            tree.remove(keys[i])
            assertTrue(checkRBTInvariant(tree))
            assertNull(tree.findByKey(keys[i]))
        }
    }

    @Test
    fun `invariant and find after insertion`() {
        for (i in keys.indices) {
            tree.insert(keys[i], values[i])
            assertEquals(tree.findByKey(keys[i]), values[i])
            assertTrue(checkRBTInvariant(tree))
        }
    }

    @Test
    fun `invariant after insertion duplicates`() {
        for (i in keys.indices) {
            tree.insert(keys[i], values[i])
            assertTrue(checkRBTInvariant(tree))
            assertEquals(tree.findByKey(keys[i]), values[i])
        }
        val values = Array(keys.size) { randomizer.nextInt() }
        for (i in keys.indices) {
            tree.insert(keys[i], values[i])
            assertTrue(checkRBTInvariant(tree))
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
}