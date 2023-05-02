package trees.bstree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import trees.correctReferencesParents
import trees.treeKeysCorrectOrder
import java.util.stream.Stream
import kotlin.random.Random

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class BinarySearchTreeTest {

    private fun checkBSTInvariant(tree: BinarySearchTree<Int, Int>): Boolean {
        return treeKeysCorrectOrder(tree) && correctReferencesParents(tree)
    }

    private val randomizer = Random(42)
    private val keys = Array(1000) { randomizer.nextInt() }.distinct()
    private val values = Array(keys.size) { randomizer.nextInt() }
    private lateinit var tree: BinarySearchTree<Int, Int>

    @BeforeEach
    fun recreateTree() {
        tree = BinarySearchTree()
    }

    private fun getData() = Stream.of(
        Pair(10, 68),
        Pair(10, 89),
        Pair(1000, 211),
        Pair(1000, 42),
        Pair(100000, 13),
        Pair(100000, 1337)
    )

    @ParameterizedTest
    @MethodSource("getData")
    fun `insert, remove and find values`(currentData: Pair<Int, Int>) {
        val arraySize = currentData.first
        val seed = currentData.second

        val currentRandomizer = Random(seed)
        val keys = List(arraySize) { currentRandomizer.nextInt() }
        val values = List(arraySize) { currentRandomizer.nextInt() }
        val map = mutableMapOf<Int, Int>()
        for (i in 0 until arraySize) {
            map[keys[i]] = values[i]
        }

        for (key in map.keys) {
            tree.insert(key)
        }
        assertTrue(checkBSTInvariant(tree))
        for (key in map.keys.shuffled()) {
            tree.remove(key)
            assertNull(tree.findByKey(key))
        }
        assertTrue(checkBSTInvariant(tree))
    }


    @Test
    fun `invariant and find after deletion`() {
        for (i in keys.indices) tree.insert(keys[i], values[i])
        val leftRange = (Math.random() * keys.size).toInt()
        val rightRange = (Math.random() * (keys.size - leftRange + 1)).toInt() + leftRange
        for (i in leftRange until rightRange) {
            assertEquals(tree.findByKey(keys[i]), values[i])
            tree.remove(keys[i])
            assertTrue(checkBSTInvariant(tree))
            assertNull(tree.findByKey(keys[i]))
        }
    }

    @Test
    fun `invariant and find after insertion`() {
        for (i in keys.indices) {
            tree.insert(keys[i], values[i])
            assertTrue(checkBSTInvariant(tree))
            assertEquals(tree.findByKey(keys[i]), values[i])
        }
    }

    @Test
    fun `invariant after insertion duplicates`() {
        for (i in keys.indices) {
            tree.insert(keys[i], values[i])
            assertTrue(checkBSTInvariant(tree))
            assertEquals(tree.findByKey(keys[i]), values[i])
        }
        val values = Array(keys.size) { randomizer.nextInt() }
        for (i in keys.indices) {
            tree.insert(keys[i], values[i])
            assertTrue(checkBSTInvariant(tree))
            assertEquals(tree.findByKey(keys[i]), values[i])
        }
    }

    @Test
    fun `overwriting the key value`() {
        tree.insert(10, 10)
        assertEquals(tree.value, 10)
        tree.insert(10, 20)
        assertEquals(tree.value, 20)
    }

    @Test
    fun `find by non-existent key and return null`() {
        tree.insert(10, 10)
        assertNull(tree.findByKey(5))
        assertNull(tree.findByKey(100))
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
    fun `removing the root with the right subtree only`() {
        val keys = listOf(10, 20, 30)
        keys.forEach { tree.insert(it) }
        tree.remove(10)
        assertEquals(tree.key, 20)
        assertEquals(tree.right?.key, 30)
    }

    @Test
    fun `removing the root with the left subtree only`() {
        val keys = listOf(30, 20, 10)
        keys.forEach { tree.insert(it) }
        tree.remove(30)
        assertEquals(tree.key, 20)
        assertEquals(tree.left?.key, 10)
    }

    @Test
    fun `removing a root with left and right subtrees`() {
        val keys = listOf(10, 20, 5, 1)
        keys.forEach { tree.insert(it) }
        tree.remove(10)
        assertEquals(tree.key, 20)
        assertNull(tree.right)
        assertEquals(tree.left?.key, 5)
        assertEquals(tree.left?.left?.key, 1)
    }

    @Test
    fun `removing a tree with the left subtree only`() {
        val keys = listOf(10, 20, 15, 14)
        keys.forEach { tree.insert(it) }
        tree.remove(20)
        assertEquals(tree.key, 10)
        assertNull(tree.left)
        assertEquals(tree.right?.key, 15)
        assertEquals(tree.right?.left?.key, 14)
    }

    @Test
    fun `removing a tree with the right subtree only`() {
        val keys = listOf(20, 15, 17, 16, 18)
        keys.forEach { tree.insert(it) }
        tree.remove(15)
        assertEquals(tree.key, 20)
        assertNull(tree.right)
        assertEquals(tree.left?.key, 17)
        assertEquals(tree.left?.left?.key, 16)
        assertEquals(tree.left?.right?.key, 18)
    }

    @Test
    fun `removing a tree with left and right subtrees`() {
        val keys = listOf(10, 20, 15, 30, 25, 50)
        keys.forEach { tree.insert(it) }
        tree.remove(20)
        assertEquals(tree.key, 10)
        assertEquals(tree.right?.key, 25)
        assertEquals(tree.right?.left?.key, 15)
        assertEquals(tree.right?.right?.key, 30)
        assertEquals(tree.right?.right?.right?.key, 50)
    }
}
