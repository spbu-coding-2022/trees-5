package etrees

abstract class AbstractBST<K : Comparable<K>, Subtree : AbstractBST<K, Subtree>> {
    protected open var key: K? = null
    protected open var value: Any? = null
    protected open var right: Subtree? = null
    protected open var left: Subtree? = null

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

    private fun showOrderKeys() {
        left?.showOrderKeys()
        this.key?.let { print("$it ") }
        right?.showOrderKeys()
    }
}
