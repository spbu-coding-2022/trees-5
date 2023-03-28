package etrees

class RedBlackTree() : balancer, BinarySearchTree() {
    private var key: Int? = null
    private var value: Any? = null
    private var left: RedBlackTree? = null
    private var right: RedBlackTree? = null
    private var parent: RedBlackTree? = null

    enum class Color {
        RED, BLACK
    }
    var color : Color = Color.BLACK

    constructor(_key: Int, _value: Any?) : this() {
        key = _key
        value = _value
    }

    override fun balance() {
        TODO("Not yet implemented")
    }

    override fun rightRotate() {
        TODO("Not yet implemented")
    }

    override fun leftRotate() {
        TODO("Not yet implemented")
    }

    private fun grandparent() : RedBlackTree? {
        TODO()
    }

    private fun uncle() : RedBlackTree? {
        TODO()
    }

    private fun insert() : Boolean {
        TODO()
    }

    override fun insert(key: Int, value: Any?) {
        TODO()
    }

    private fun sibling() : RedBlackTree? {
        TODO()
    }

    override fun remove(key: Int) {
        TODO()
    }
}