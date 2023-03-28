package etrees

open class BinarySearchTree() {

    private var key: Int? = null
    private var value: Any? = null
    private var left: BinarySearchTree? = null
    private var right: BinarySearchTree? = null
    private var parent: BinarySearchTree? = null

    constructor(_key: Int, _value: Any?) : this() {
        key = _key
        value = _value
    }

    fun findByKey(key: Int): Any? {
        TODO()
    }

    open fun insert(key: Int, value: Any?) {
        TODO()
    }

    open fun remove(key: Int) {
        TODO()
    }

    fun showOrderKeys() {
        TODO()
    }
}