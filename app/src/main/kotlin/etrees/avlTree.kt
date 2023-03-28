package etrees

class avlTree() : BinarySearchTree(), balancer {
    private var key: Int? = null
    private var value: Any? = null
    private var diffHeight: Int = 0
    private var height: Int = 1
    private var left: avlTree? = null
    private var right: avlTree? = null
    private var parent: avlTree? = null

    constructor(_key: Int, _value: Any?) : this() {
        key = _key
        value = _value
    }

    override fun insert(key: Int, value: Any?) {
        TODO()
    }

    override fun remove(key: Int) {
        TODO()
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

}