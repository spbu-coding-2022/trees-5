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

    private fun findMinThanCurrent(currentTree: BinarySearchTree<K>): BinarySearchTree<K> {
        return when (currentTree.left) {
            null -> currentTree
            else -> findMinThanCurrent(currentTree.left!!)
        }
    }

    open fun remove(key: K) {
        if (this.key == null) return

        if (this.key!! > key)
            this.left?.remove(key)
        else if (this.key!! < key)
            this.right?.remove(key)
        else if (this.left != null && this.right != null) {
            val minimumMore = findMinThanCurrent(this.right!!)
            this.key = minimumMore.key
            this.value = minimumMore.value
            this.right!!.remove(this.key!!)
        }
        else {
            if (this.left != null) {
                if (this.parent == null) {
                    this.key = this.left!!.key
                    this.value = this.left!!.value
                    val tmp = this.left!!.right
                    this.left = this.left!!.left
                    this.right = tmp
                } else {
                    this.left!!.parent = this.parent
                    if (this.parent!!.left!!.key == this.key) {
                        this.parent!!.left = this.left
                    } else {
                        this.parent!!.right = this.left
                    }
                }
            } else if (this.right != null) {
                if (this.parent == null) {
                    this.key = this.right!!.key
                    this.value = this.right!!.value
                    val tmp = this.right!!.right
                    this.left = this.right!!.left
                    this.right = tmp
                } else {
                    this.right!!.parent = this.parent
                    if (this.parent!!.left!!.key == this.key) {
                        this.parent!!.left = this.right
                    } else {
                        this.parent!!.right = this.right
                    }
                }
            } else {
                if (this.parent == null) {
                    this.key = null
                    this.value = null
                } else {
                    if (this.parent!!.left != null && this.parent!!.left == this) {
                        this.parent!!.left = null
                    } else {
                        this.parent!!.right = null
                    }
                }
            }
        }
    }

    fun showOrderKeys() {
        left?.showOrderKeys()
        this.key?. let {print("$it ") }
        right?.showOrderKeys()
    }
}