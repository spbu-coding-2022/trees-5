package etrees

open class BinarySearchTree<K: Comparable<K>>() {

    private var key: K? = null
    private var value: Any? = null
    private var left: BinarySearchTree<K>? = null
    private var right: BinarySearchTree<K>? = null

    fun findByKey(key: K): Any? {
        val currentKey = this.key
        return when {
            currentKey == null -> null
            currentKey > key -> left?.findByKey(key)
            currentKey < key -> right?.findByKey(key)
            else -> this.value
        }
    }

    open fun insert(key: K, value: Any?) {
        val currentKey = this.key
        when {
            currentKey == null || currentKey == key -> {
                this.key = key
                this.value = value
            }
            currentKey > key -> {
                this.left = this.left ?: BinarySearchTree()
                this.left?.insert(key, value)
            }

            currentKey < key -> {
                this.right = this.right ?: BinarySearchTree()
                this.right?.insert(key, value)
            }
        }
    }

    private fun findMinimum(currentTree: BinarySearchTree<K>): BinarySearchTree<K> {
        return when {
            currentTree.left == null || currentTree.left?.key == null -> currentTree
            else -> findMinimum(currentTree.left!!)
        }
    }

    protected fun greatThan(x: K?, y: K?): Boolean = !(x == null || y == null || y >= x)
    protected fun lessThan(x: K?, y: K?): Boolean = !(x == null || y == null || y <= x)

    private fun copyFields(tree: BinarySearchTree<K>) {
        this.key = tree.key
        this.value = tree.value
        this.left = tree.left
        this.right = tree.right
    }

    private fun remove(tree: BinarySearchTree<K>?, key: K): BinarySearchTree<K>? {
        when {
            tree == null -> return tree
            greatThan(tree.key, key) -> tree.left = remove(tree.left, key)
            greatThan(key, tree.key) -> tree.right = remove(tree.right, key)
            tree.left != null && tree.right != null -> {
                val tmpMinimum = findMinimum(tree.right!!)
                tree.key = tmpMinimum.key
                tree.value = tmpMinimum.value
                tree.right = remove(tree.right, tmpMinimum.key!!)
            }
            tree.left != null -> return tree.left
            tree.right != null -> return tree.right
            else -> return null
        }
        return tree
    }

    open fun remove(key: K) {
        when {
            greatThan(this.key, key) -> this.left = remove(this.left, key)
            lessThan(this.key, key) -> this.right = remove(this.right, key)
            else -> {
                when {
                    this.left != null && this.right != null -> {
                        val tmpMinimum = findMinimum(this.right!!)
                        this.key = tmpMinimum.key
                        this.value = tmpMinimum.value
                        this.right = remove(this.right, this.key!!)
                    }
                    this.left != null -> this.copyFields(this.left!!)
                    this.right != null -> this.copyFields(this.right!!)
                    else -> {
                        this.key = null
                        this.value = null
                    }
                }
            }
        }
    }

    private fun showOrderKeys() {
        left?.showOrderKeys()
        this.key?. let {print("$it ") }
        right?.showOrderKeys()
    }
}