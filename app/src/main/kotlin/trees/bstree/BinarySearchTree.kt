package trees.bstree

import trees.AbstractBST
import trees.findMinimumTree

class BinarySearchTree<K : Comparable<K>, V> : AbstractBST<K, V, BinarySearchTree<K, V>>() {
    /**
     * inserts the key, value, and returns the inserted subtree
     */
    private fun insertTree(key: K, value: V? = null): BinarySearchTree<K, V>? {
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
                        val insertedTree = BinarySearchTree<K, V>()
                        insertedTree.key = key
                        insertedTree.value = value
                        insertedTree.parent = currentTree
                        currentTree.right = insertedTree
                        return insertedTree
                    }
                    currentTree = currentTree.right ?: throw IllegalStateException("right subtree can not be null")
                }
                resultCompare < 0 -> {
                    if (currentTree.left == null) {
                        val insertedTree = BinarySearchTree<K, V>()
                        insertedTree.key = key
                        insertedTree.value = value
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
    override fun insert(key: K, value: V?) {
        insertTree(key, value)
    }

    private fun deleteTreeWithOneSubtree(givenTree: BinarySearchTree<K, V>): BinarySearchTree<K, V> {
        val necessaryTree = if (givenTree.left == null) givenTree.right else givenTree.left
        replaceSubtree(givenTree, necessaryTree)
        return givenTree
    }

    private fun deleteTreeWithTwoSubtrees(givenTree: BinarySearchTree<K, V>): BinarySearchTree<K, V> {
        val tmpMinimumTree = findMinimumTree(givenTree.right ?: throw Exception("right subtree can not be null"))
        givenTree.key = tmpMinimumTree.key
        givenTree.value = tmpMinimumTree.value
        return removeSubtree(tmpMinimumTree)
    }

    private fun deleteTreeWithNoSubtree(givenTree: BinarySearchTree<K, V>): BinarySearchTree<K, V> {
        replaceSubtree(givenTree, null)
        return givenTree
    }

    private fun replaceSubtree(wasTree: BinarySearchTree<K, V>, newTree: BinarySearchTree<K, V>?) {
        val parent = wasTree.parent
        if (parent == null) {
            this.key = newTree?.key
            this.value = newTree?.value
            this.right = newTree?.right
            this.left = newTree?.left
            this.left?.parent = this
            this.right?.parent = this

        } else if (parent.left == wasTree) {
            parent.left = newTree
        } else {
            parent.right = newTree
        }

        newTree?.parent = parent
    }

    private fun searchTree(key: K): BinarySearchTree<K, V>? {
        if (this.key == null) return null
        var currentTree: BinarySearchTree<K, V>? = this
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

    /**
     * deletes a subtree by key and returns the deleted subtree
     */
    private fun removeSubtree(givenTree: BinarySearchTree<K, V>): BinarySearchTree<K, V> {
        return when {
            givenTree.left != null && givenTree.right != null -> deleteTreeWithTwoSubtrees(givenTree)
            givenTree.left != null || givenTree.right != null -> deleteTreeWithOneSubtree(givenTree)
            else -> deleteTreeWithNoSubtree(givenTree)
        }
    }

    /**
     * deletes the desired subtree by key, if there is no key in the tree, it does nothing
     */
    override fun remove(key: K) {
        val needSubtree = searchTree(key) ?: return
        removeSubtree(needSubtree)
    }
}
