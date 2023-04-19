package trees

abstract class AbstractBST<K : Comparable<K>, Subtree : AbstractBST<K, Subtree>> {
    internal var key: K? = null
    internal var value: Any? = null
    internal var right: Subtree? = null
    internal var left: Subtree? = null

    protected fun greatThan(x: K?, y: K?): Boolean = !(x == null || y == null || y >= x)
    protected fun lessThan(x: K?, y: K?): Boolean = !(x == null || y == null || y <= x)

    fun findByKey(key: K): Any? {
        val currentKey = this.key
        return when {
            currentKey == null -> null
            currentKey > key -> left?.findByKey(key)
            currentKey < key -> right?.findByKey(key)
            else -> this.value
        }
    }

    protected fun copyFields(tree: Subtree) {
        this.key = tree.key
        this.value = tree.value
        this.left = tree.left
        this.right = tree.right
    }

    fun contains(key: K): Boolean {
        val currentKey = this.key
        return when {
            currentKey == null -> false
            currentKey > key -> left?.contains(key) ?: false
            currentKey < key -> right?.contains(key) ?: false
            else -> true
        }
    }

    fun showOrderKeys() {
        left?.showOrderKeys()
        this.key?.let { print("$it ") }
        right?.showOrderKeys()
    }
}
