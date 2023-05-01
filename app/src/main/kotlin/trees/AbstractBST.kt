package trees

import trees.avltree.AVLTree
import trees.rbtree.RedBlackTree

abstract class AbstractBST<K : Comparable<K>, V, Subtree : AbstractBST<K, V, Subtree>> {
    internal var key: K? = null
    internal var value: V? = null
    internal var right: Subtree? = null
    internal var left: Subtree? = null
    open var parent: Subtree? = null

    protected abstract fun createNewTree(key: K?, value: V?): Subtree

    /**
     * deletes the desired subtree by key, if there is no key in the tree, it does nothing
     */
    open fun remove(key: K) {
        val needSubtree = searchTree(this as Subtree, key) ?: return
        removeSubtree(needSubtree)
    }

    protected fun replaceTreeRoot(wasTree: Subtree, newTree: Subtree?) {
        val parent = wasTree.parent
        this.key = newTree?.key
        this.value = newTree?.value
        this.right = newTree?.right
        this.left = newTree?.left
        this.left?.parent = this as Subtree
        this.right?.parent = this
        newTree?.parent = parent
    }

    protected open fun replaceSubtree(wasTree: Subtree, newTree: Subtree?) {
        val parent = wasTree.parent
        if (parent?.left == wasTree) parent.left = newTree
        else parent?.right = newTree
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

    protected fun searchTree(givenTree: Subtree, key: K): Subtree? {
        if (givenTree.key == null) return null
        var currentTree: Subtree? = givenTree
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

    protected open fun deleteTreeWithOneSubtree(givenTree: Subtree): Subtree {
        val necessaryTree = if (givenTree.left == null) givenTree.right else givenTree.left
        replaceSubtree(givenTree, necessaryTree)
        return givenTree
    }

    protected open fun deleteTreeWithTwoSubtrees(givenTree: Subtree): Subtree {
        val tmpMinimumTree = findMinimumTree(givenTree.right ?: throw Exception("right subtree can not be null"))
        givenTree.key = tmpMinimumTree.key
        givenTree.value = tmpMinimumTree.value
        return removeSubtree(tmpMinimumTree)
    }

    protected open fun deleteTreeWithNoSubtree(givenTree: Subtree): Subtree {
        replaceSubtree(givenTree, null)
        return givenTree
    }

    /**
     * inserts the key, value, and returns the inserted subtree
     */
    protected fun insertTree(key: K, value: V? = null, givenTree: Subtree): Subtree? {
        if (givenTree.key == null) {
            givenTree.key = key
            givenTree.value = value
        }
        var currentTree = givenTree
        while (true) {
            val resultCompare = key.compareTo(currentTree.key ?: throw IllegalStateException("this key can not be null"))
            when {
                resultCompare > 0 -> {
                    if (currentTree.right == null) {
                        val insertedTree = createNewTree(key, value)
                        insertedTree.parent = currentTree
                        currentTree.right = insertedTree
                        return insertedTree
                    }
                    currentTree = currentTree.right ?: throw IllegalStateException("right subtree can not be null")
                }
                resultCompare < 0 -> {
                    if (currentTree.left == null) {
                        val insertedTree = createNewTree(key, value)
                        insertedTree.parent = currentTree
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
        insertTree(key, value, this as Subtree)
    }

    /**
     * Returns the found value by key, if there is no key, returns null
     */

    protected fun findByKey(givenTree: Subtree, key: K): V? {
        val currentKey = givenTree.key
        if (currentKey == null || currentKey == key) {
            return givenTree.value
        }
        var currentTree = when {
            key > currentKey -> givenTree.right
            else -> givenTree.left
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

    open fun findByKey(key: K): V? {
        return findByKey(this as Subtree, key)
    }

    protected open fun rightRotate(givenTree: Subtree): Subtree {
        val leftSubtree = givenTree.left ?: throw IllegalStateException("The tree must have a left subtree in this rotate")
        leftSubtree.parent = givenTree.parent
        if (givenTree.parent?.left == givenTree) givenTree.parent?.left = leftSubtree
        else givenTree.parent?.right = leftSubtree

        givenTree.left = leftSubtree.right
        leftSubtree.right?.parent = givenTree

        leftSubtree.right = givenTree
        givenTree.parent = leftSubtree
        return leftSubtree
    }

    protected open fun leftRotate(givenTree: Subtree): Subtree {
        val rightSubtree = givenTree.right ?: throw IllegalStateException("The tree must have a right subtree in this rotate")
        rightSubtree.parent = givenTree.parent
        if (givenTree.parent?.left == givenTree) givenTree.parent?.left = rightSubtree
        else givenTree.parent?.right = rightSubtree

        givenTree.right = rightSubtree.left
        rightSubtree.left?.parent = givenTree

        rightSubtree.left = givenTree
        givenTree.parent = rightSubtree
        return rightSubtree
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
