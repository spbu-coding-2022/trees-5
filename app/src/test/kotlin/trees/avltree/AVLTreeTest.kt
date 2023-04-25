package trees.avltree

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test



class AvlTreeTest {

    private fun <K : Comparable<K>, V> correctBuildAVLTree(tree: AVLTree<K, V>): Boolean {
        if (tree.key == null) return true
        val stack = mutableListOf<AVLTree<K, V>>()
        var currentTree: AVLTree<K, V>? = tree
        var prevTree: AVLTree<K, V>? = null
        while (currentTree != null || stack.isNotEmpty()) {
            while (currentTree != null) {
                stack.add(currentTree)
                currentTree = currentTree.left
            }

            currentTree = stack.removeLast()
            val valueBalanceCurrentTree = currentTree.getBalanceValue(currentTree)
            if (prevTree != null && (currentTree.key == null || prevTree.key == null || currentTree.key!! <= prevTree.key!!
                        || valueBalanceCurrentTree > 1 || valueBalanceCurrentTree < -1)) return false

            prevTree = currentTree

            currentTree = currentTree.right
        }
        return true
    }

    @Test
    fun `overwriting the key value`() {
        val tree = AVLTree<Int, String>()
        tree.insert(50)
        tree.insert(100)
        tree.insert(5)
        tree.insert(10, "Good")
        assertEquals(tree.findByKey(10), "Good")
        tree.insert(10, "Super Good")
        assertEquals(tree.findByKey(10), "Super Good")
    }

    @Test
    fun `inserting huge number of type string keys`() {
        val tree = AVLTree<String, String>()
        val charset = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz0123456789"
        for (i in 1..1000) {
            val key = (1..100) .map { charset.random() } .joinToString("")
            val value = (1..100) .map { charset.random() } .joinToString("")
            tree.insert(key, value)
            assertTrue(correctBuildAVLTree(tree))
        }
    }

    @Test
    fun `checking correct calculate height`() {
        val AvlTree = AVLTree<Int, Int>()
        AvlTree.insert(10)
        AvlTree.insert(20)
        AvlTree.insert(30)
        AvlTree.insert(40)
        AvlTree.insert(5)
        assertEquals(AvlTree.height, 3)
        assertEquals(AvlTree.left?.height, 2)
        assertEquals(AvlTree.right?.height, 2)
        assertEquals(AvlTree.left?.left?.height, 1)
        assertEquals(AvlTree.right?.right?.height, 1)
    }

    @Test
    fun `checking correct getBalance`() {
        val AVLTree = AVLTree<Int, Int>()
        AVLTree.insert(10)
        AVLTree.insert(5)
        AVLTree.insert(15)
        AVLTree.insert(1)
        AVLTree.insert(20)
        assertEquals(AVLTree.getBalanceValue(AVLTree), 0)
        assertEquals(AVLTree.getBalanceValue(AVLTree.left), 1)
        assertEquals(AVLTree.getBalanceValue(AVLTree.right), -1)
        assertEquals(AVLTree.getBalanceValue(AVLTree.left?.left), 0)
        assertEquals(AVLTree.getBalanceValue(AVLTree.left?.right), 0)
        assertEquals(AVLTree.getBalanceValue(AVLTree.right?.right), 0)
    }

    @Test
    fun `adding a 10_000 number of sorted keys`() {
        val AVLTree = AVLTree<Int, Int>()
        for (i in 1..10_000) {
            AVLTree.insert(i, i)
            assertTrue(correctBuildAVLTree(AVLTree))
        }
    }

    @Test
    fun `right rotate testing`() {
        val AVLTree = AVLTree<Int, Int>()
        /*
            constructed tree before right rotate:
                  10
                /    \
              8       11
            /   \
           7     9

        */
        AVLTree.insert(10)
        AVLTree.insert(11)
        AVLTree.insert(8)
        AVLTree.insert(9)
        AVLTree.insert(7)
        assertEquals(AVLTree.key, 10)
        assertEquals(AVLTree.left?.key, 8)
        assertEquals(AVLTree.left?.left?.key, 7)
        assertEquals(AVLTree.left?.right?.key, 9)
        assertEquals(AVLTree.right?.key, 11)
        AVLTree.remove(11)
        /*
             constructed tree after remove `11`:
                    8
                  /   \
                 7     10
                       /
                      9
         */
        assertEquals(AVLTree.key, 8)
        assertEquals(AVLTree.left?.key, 7)
        assertEquals(AVLTree.right?.key, 10)
        assertEquals(AVLTree.right?.left?.key, 9)
        assertEquals(AVLTree.findByKey(11), null)
    }

    @Test
    fun `left rotate testing`() {
        val AVLTree = AVLTree<Int, Int>()
        /*
            constructed tree before left rotate:
                  10
                /    \
               8      12
                     /  \
                    11   13

        */
        AVLTree.insert(10)
        AVLTree.insert(12)
        AVLTree.insert(8)
        AVLTree.insert(11)
        AVLTree.insert(13)
        assertEquals(AVLTree.key, 10)
        assertEquals(AVLTree.left?.key, 8)
        assertEquals(AVLTree.right?.key, 12)
        assertEquals(AVLTree.right?.left?.key, 11)
        assertEquals(AVLTree.right?.right?.key, 13)
        AVLTree.remove(8)
        /*
            constructed tree after remove `8`:
                  12
                /   \
               10    13
                \
                 11

        */
        assertEquals(AVLTree.key, 12)
        assertEquals(AVLTree.left?.key, 10)
        assertEquals(AVLTree.left?.right?.key, 11)
        assertEquals(AVLTree.right?.key, 13)
        assertEquals(AVLTree.findByKey(8), null)
    }

    @Test
    fun `big right rotate testing`() {
        val AVLTree = AVLTree<Int, Int>()
        AVLTree.insert(10)
        AVLTree.insert(15)
        AVLTree.insert(7)
        AVLTree.insert(9)
        AVLTree.insert(6)
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
        assertEquals(AVLTree.key, 10)
        assertEquals(AVLTree.right?.key, 15)
        assertEquals(AVLTree.left?.key, 7)
        assertEquals(AVLTree.left?.left?.key, 6)
        assertEquals(AVLTree.left?.right?.key, 9)
        AVLTree.insert(8)
        /*
             constructed tree:
                   9
                 /   \
                7     10
              /  \      \
             6    8     15
         */
        assertEquals(AVLTree.key, 9)
        assertEquals(AVLTree.right?.key, 10)
        assertEquals(AVLTree.right?.right?.key, 15)
        assertEquals(AVLTree.left?.key, 7)
        assertEquals(AVLTree.left?.left?.key, 6)
        assertEquals(AVLTree.left?.right?.key, 8)
    }

    @Test
    fun `big left rotate testing`() {
        val AVLTree = AVLTree<Int, Int>()
        AVLTree.insert(10)
        AVLTree.insert(8)
        AVLTree.insert(20)
        AVLTree.insert(15)
        AVLTree.insert(22)
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
        assertEquals(AVLTree.key, 10)
        assertEquals(AVLTree.left?.key, 8)
        assertEquals(AVLTree.right?.key, 20)
        assertEquals(AVLTree.right?.left?.key, 15)
        assertEquals(AVLTree.right?.right?.key, 22)
        AVLTree.insert(16)

        /*
             constructed tree after insert 16:
                   15
                 /    \
               10      20
              /       /  \
             8       16   22
         */

        assertEquals(AVLTree.key, 15)
        assertEquals(AVLTree.right?.key, 20)
        assertEquals(AVLTree.left?.key, 10)
        assertEquals(AVLTree.left?.left?.key, 8)
        assertEquals(AVLTree.right?.right?.key, 22)
        assertEquals(AVLTree.right?.left?.key, 16)
    }

    @Test
    fun `getting the value of the key`() {
        val avl = AVLTree<Int, Int>()
        avl.key = 4
        avl.value = 1

        val right = AVLTree<Int, Int>()
        val left = AVLTree<Int, Int>()

        avl.left = left
        avl.right = right

        right.key = 5
        right.value = 3
        left.key = 3
        left.value = 2

        assertEquals(1, avl.findByKey(4))
        assertEquals(2, avl.findByKey(3))
        assertEquals(3, avl.findByKey(5))
        assertEquals(null, avl.findByKey(1))

    }

    @Test
    fun `balance testing`() {
        val avl1 = AVLTree<Int, Int>()
        avl1.insert(5)
        avl1.insert(3)
        avl1.insert(7)
        avl1.insert(2)
        avl1.insert(4)
        avl1.insert(6)
        avl1.insert(8)

        assertTrue(avl1.getBalanceValue(avl1) <= 1)


        val avl2 = AVLTree<Int, Int>()

        avl2.insert(4)
        avl2.insert(3)
        avl2.insert(2)
        assertEquals(3, avl2.key)
        assertEquals(2, avl2.left!!.key)
        assertEquals(4, avl2.right!!.key)

        avl2.insert(1)
        assertEquals(3, avl2.key)
        assertEquals(2, avl2.left!!.key)
        assertEquals(4, avl2.right!!.key)
        assertEquals(1, avl2.left!!.left!!.key)

        avl2.remove(4)
        assertEquals(2, avl2.key)
        assertEquals(1, avl2.left!!.key)
        assertEquals(3, avl2.right!!.key)
    }
}