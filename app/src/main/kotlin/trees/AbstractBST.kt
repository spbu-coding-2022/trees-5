package trees

abstract class AbstractBST<K : Comparable<K>, V, Subtree : AbstractBST<K, V, Subtree>> {
    internal var key: K? = null
    internal var value: V? = null
    internal var right: Subtree? = null
    internal var left: Subtree? = null
    open var parent: Subtree? = null

    protected abstract fun createNewTree(key: K, value: V?): Subtree

    /**
     * deletes the desired subtree by key, if there is no key in the tree, it does nothing
     */
    open fun remove(key: K) {
        val needSubtree = searchTree(key) ?: return
        removeSubtree(needSubtree)
    }

    private fun replaceSubtree(wasTree: Subtree, newTree: Subtree?) {
        val parent = wasTree.parent
        if (parent == null) {
            this.key = newTree?.key
            this.value = newTree?.value
            this.right = newTree?.right
            this.left = newTree?.left
            this.left?.parent = this as Subtree
            this.right?.parent = this

        } else if (parent.left == wasTree) {
            parent.left = newTree
        } else {
            parent.right = newTree
        }

        newTree?.parent = parent
    }

    /**
     * deletes a subtree by key and returns the deleted subtree
     */
    protected fun removeSubtree(givenTree: Subtree): Subtree {
        return when {
            givenTree.left != null && givenTree.right != null -> deleteTreeWithTwoSubtrees(givenTree)
            givenTree.left != null || givenTree.right != null -> deleteTreeWithOneSubtree(givenTree)
            else -> deleteTreeWithNoSubtree(givenTree)
        }
    }

    protected fun searchTree(key: K): Subtree? {
        if (this.key == null) return null
        var currentTree: Subtree? = this as Subtree
        while (currentTree != null) {
            val resultCompare =
                key.compareTo(currentTree.key ?: throw IllegalStateException("this key can not be null"))
            currentTree = when {
                resultCompare > 0 -> currentTree.right
                resultCompare < 0 -> currentTree.left
                else -> return currentTree
            }
        }
        return null
    }

    private fun deleteTreeWithOneSubtree(givenTree: Subtree): Subtree {
        val necessaryTree = if (givenTree.left == null) givenTree.right else givenTree.left
        replaceSubtree(givenTree, necessaryTree)
        return givenTree
    }

    private fun deleteTreeWithTwoSubtrees(givenTree: Subtree): Subtree {
        val tmpMinimumTree = findMinimumTree(givenTree.right ?: throw Exception("right subtree can not be null"))
        givenTree.key = tmpMinimumTree.key
        givenTree.value = tmpMinimumTree.value
        return removeSubtree(tmpMinimumTree)
    }

    private fun deleteTreeWithNoSubtree(givenTree: Subtree): Subtree {
        replaceSubtree(givenTree, null)
        return givenTree
    }

    /**
     * inserts the key, value, and returns the inserted subtree
     */
    protected fun insertTree(key: K, value: V? = null): Subtree? {
        if (this.key == null) {
            this.key = key
            this.value = value
        }
        var currentTree = this
        while (true) {
            val resultCompare = key.compareTo(currentTree.key ?: throw IllegalStateException("this key can not be null"))
            when {
                resultCompare > 0 -> {
                    if (currentTree.right == null) {
                        val insertedTree = createNewTree(key, value)
                        insertedTree.parent = currentTree as Subtree
                        currentTree.right = insertedTree
                        return insertedTree
                    }
                    currentTree = currentTree.right ?: throw IllegalStateException("right subtree can not be null")
                }
                resultCompare < 0 -> {
                    if (currentTree.left == null) {
                        val insertedTree = createNewTree(key, value)
                        insertedTree.parent = currentTree as Subtree
                        currentTree.left = insertedTree
                        return insertedTree
                    }
                    currentTree = currentTree.left ?: throw IllegalStateException("left subtree can not be null")
                }
                else -> {
                    currentTree.key = key
                    currentTree.value = value
                    return null
                }
            }
        }
    }

    /**
     * Adds a value to the tree by this key, if the key already exists, then overwrites the value
     */
    open fun insert(key: K, value: V? = null) {
        insertTree(key, value)
    }

    /**
     * Returns the found value by key, if there is no key, returns null
     */
    fun findByKey(key: K): V? {
        val currentKey = this.key
        if (currentKey == null || currentKey == key) {
            return this.value
        }
        var currentTree = when {
            key > currentKey -> this.right
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
