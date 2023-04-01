package etrees

open class BinarySearchTree<K: Comparable<K>>() {

    private var key: K? = null
    private var value: Any? = null
    private var left: BinarySearchTree<K>? = null
    private var right: BinarySearchTree<K>? = null
    private var parent: BinarySearchTree<K>? = null

    constructor(_key: K, _value: Any?) : this() {
        key = _key
        value = _value
    }

    fun findByKey(key: K): Any? {
        return when {
            this.key == null -> null
            this.key!! > key -> left?.findByKey(key)
            this.key!! < key -> right?.findByKey(key)
            else -> this.value
        }
    }

    fun insert(key: K, value: Any?, parent: BinarySearchTree<K>? = null) {
        when {
            this.key == null || this.key == key -> {
                this.key = key
                this.value = value
                this.parent = parent
            }
            this.key!! > key -> {
                this.left = this.left ?: BinarySearchTree()
                this.left?.insert(key, value, this)
            }

            this.key!! < key -> {
                this.right = this.right ?: BinarySearchTree()
                this.right?.insert(key, value, this)
            }
        }
    }

    open fun remove(key: Int) {
        TODO()
    }

    fun showOrderKeys() {
        TODO()
    }
}