package etrees

class RedBlackTree<K : Comparable<K>> : AbstractBST<K, RedBlackTree<K>>(), Balancer {
    enum class Color {
        RED, BLACK
    }

    private var parent: RedBlackTree<K>? = null
    var color: Color = Color.BLACK

    override fun balance() {
        TODO("Not yet implemented")
    }

    override fun rightRotate() {
        TODO("Not yet implemented")
    }

    override fun leftRotate() {
        TODO("Not yet implemented")
    }

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
