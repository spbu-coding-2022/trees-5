package trees

abstract class AbstractBST<K : Comparable<K>, V, Subtree : AbstractBST<K, V, Subtree>> {
    internal var key: K? = null
    internal var value: V? = null
    internal var right: Subtree? = null
    internal var left: Subtree? = null
    internal var parent: Subtree? = null

    protected fun greatThan(x: K?, y: K?): Boolean = !(x == null || y == null || y >= x)
    protected fun lessThan(x: K?, y: K?): Boolean = !(x == null || y == null || y <= x)

    fun findByKey(key: K): V? {
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
}
