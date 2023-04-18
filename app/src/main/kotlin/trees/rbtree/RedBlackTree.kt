package trees.rbtree

import trees.AbstractBST

class RedBlackTree<K : Comparable<K>> : AbstractBST<K, RedBlackTree<K>>() {
    enum class Color {
        RED, BLACK
    }

    private var parent: RedBlackTree<K>? = null
    var color: Color = Color.BLACK

    private fun grandparent(): RedBlackTree<K>? {
        TODO()
    }

    private fun uncle(): RedBlackTree<K>? {
        TODO()
    }

    private fun insert(): Boolean {
        TODO()
    }

    fun insert(key: Int, value: Any?) {
        TODO()
    }

    private fun sibling(): RedBlackTree<K>? {
        TODO()
    }

    fun remove(key: Int) {
        TODO()
    }
}
