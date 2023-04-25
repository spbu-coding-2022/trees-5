package trees

abstract class AbstractBST<K : Comparable<K>, V, Subtree : AbstractBST<K, V, Subtree>> {
    internal var key: K? = null
    internal var value: V? = null
    internal var right: Subtree? = null
    internal var left: Subtree? = null
    open var parent: Subtree? = null

    protected fun greatThan(x: K?, y: K?): Boolean = (x != null && y != null && x > y)
    protected fun lessThan(x: K?, y: K?): Boolean = (x != null && y != null && x < y)

    abstract fun insert(key: K, value: V? = null)
    abstract fun remove(key: K)



    /**
     * Returns the found value by key, if there is no key, returns null
     */
    fun findByKey(key: K): V? {
        if (this.key == null || this.key == key) {
            return this.value
        }
        var currentTree = when {
            greatThan(key, this.key) -> this.right
            else -> this.left
        }
        while (currentTree != null) {
            val resultCompare = key.compareTo(currentTree.key ?: throw IllegalStateException("the current key cannot be null"))
            currentTree = when {
                resultCompare > 0 -> currentTree.right
                resultCompare < 0 -> currentTree.left
                else -> return currentTree.value
            }
        }
        return null
    }

    protected fun copyFields(tree: Subtree) {
        this.key = tree.key
        this.value = tree.value
        this.left = tree.left
        this.right = tree.right
    }

    fun traverseInOrder(): List<Subtree> {
        val treesList = mutableListOf<Subtree>()
        treesList.add(this as Subtree)
        return treesList
    }
}

/**
 * Searches for the minimum subtree of the argument
 */
internal fun <K: Comparable<K>, V, Subtree: AbstractBST<K, V, Subtree>> findMinimumTree(givenTree: Subtree): Subtree {
    var currentTree = givenTree
    var leftSubtree = currentTree.left
    while (leftSubtree != null) {
        currentTree = leftSubtree
        leftSubtree = currentTree.left
    }
    return currentTree
}
