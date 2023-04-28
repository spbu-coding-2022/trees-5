package trees.avltree

import trees.AbstractBST
import kotlin.math.max

class AVLTree<K : Comparable<K>, V> : AbstractBST<K, V, AVLTree<K, V>>() {
    internal var height: Int = 1

    override fun insert(key: K, value: V?) {
        var currentTree = insertTree(key, value)
        while (currentTree != null) {
            balance(currentTree)
            currentTree = currentTree.parent
        }
    }

    override fun remove(key: K) {
        val necessarySubtree = searchTree(key) ?: return
        val deletedSubtree = removeSubtree(necessarySubtree)

        var currentTree = deletedSubtree.parent
        while (currentTree != null) {
            balance(currentTree)
            currentTree = currentTree.parent
        }
    }

    private fun updateHeight(givenTree: AVLTree<K, V>) {
        givenTree.height = 1 + max(givenTree.left?.height ?: 0, givenTree.right?.height ?: 0)
    }

    private fun getBalanceValue(tree: AVLTree<K, V>?): Int {
        if (tree == null) return 0
        return (tree.left?.height ?: 0) - (tree.right?.height ?: 0)
    }

    private fun balance(givenTree: AVLTree<K, V>) {
        updateHeight(givenTree)
        val valueBalanceThisTree = getBalanceValue(givenTree)
        val valueBalanceRightSubtree = getBalanceValue(givenTree.right)
        val valueBalanceLeftSubtree = getBalanceValue(givenTree.left)

        if (valueBalanceThisTree > 1 && valueBalanceLeftSubtree >= 0) rightRotate(givenTree)
        if (valueBalanceThisTree > 1 && valueBalanceLeftSubtree < 0) bigRightRotate(givenTree)
        if (valueBalanceThisTree < -1 && valueBalanceRightSubtree <= 0) leftRotate(givenTree)
        if (valueBalanceThisTree < -1 && valueBalanceRightSubtree > 0) bigLeftRotate(givenTree)
    }

    private fun bigRightRotate(givenTree: AVLTree<K, V>) {
        leftRotate(givenTree.left ?: throw IllegalStateException("The tree must have a left subtree in this rotate"))
        rightRotate(givenTree)
    }

    private fun bigLeftRotate(givenTree: AVLTree<K, V>) {
        rightRotate(givenTree.right ?: throw IllegalStateException("The tree must have a right subtree in this rotate"))
        leftRotate(givenTree)
    }

    private fun rightRotate(givenTree: AVLTree<K, V>) {
        val leftSubtree = givenTree.left ?: throw IllegalStateException("The tree must have a left subtree in this rotate")
        if (givenTree.parent == null) {
            val thisTree = AVLTree<K, V>()
            thisTree.value = this.value
            thisTree.key = this.key
            thisTree.right = this.right
            thisTree.left = leftSubtree.right
            thisTree.right?.parent = thisTree
            thisTree.parent = this
            thisTree.left?.parent = thisTree
            this.value = leftSubtree.value
            this.key = leftSubtree.key
            this.left = leftSubtree.left
            this.left?.parent = this
            this.right = thisTree
            this.right?.let { updateHeight(it) }
            updateHeight(this)
            return
        }

        leftSubtree.parent = givenTree.parent
        if (givenTree.parent?.left == givenTree) givenTree.parent?.left = leftSubtree
        else givenTree.parent?.right = leftSubtree

        givenTree.left = leftSubtree.right
        leftSubtree.right?.parent = givenTree

        leftSubtree.right = givenTree
        givenTree.parent = leftSubtree

        updateHeight(givenTree)
        updateHeight(leftSubtree)
    }

    private fun leftRotate(givenTree: AVLTree<K, V>) {
        val rightSubtree = givenTree.right ?: throw IllegalStateException("The tree must have a right subtree in this rotate")
        if (givenTree.parent == null) {
            val thisTree = AVLTree<K, V>()
            thisTree.value = this.value
            thisTree.key = this.key
            thisTree.left = this.left
            thisTree.right = rightSubtree.left
            thisTree.parent = this
            thisTree.left?.parent = thisTree
            thisTree.right?.parent = thisTree
            this.value = rightSubtree.value
            this.key = rightSubtree.key
            this.right = rightSubtree.right
            this.right?.parent = this
            this.left = thisTree
            this.left?.let { updateHeight(it) }
            updateHeight(this)
            return
        }


        rightSubtree.parent = givenTree.parent
        if (givenTree.parent?.left == givenTree) givenTree.parent?.left = rightSubtree
        else givenTree.parent?.right = rightSubtree

        givenTree.right = rightSubtree.left
        rightSubtree.left?.parent = givenTree

        rightSubtree.left = givenTree
        givenTree.parent = rightSubtree

        updateHeight(givenTree)
        updateHeight(rightSubtree)
    }

    override fun createNewTree(key: K, value: V?): AVLTree<K, V> {
        val tmpTree = AVLTree<K, V>()
        tmpTree.key = key
        tmpTree.value = value
        return tmpTree
    }
}
