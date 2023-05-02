package trees.avltree

import trees.AbstractBST
import kotlin.math.max

class AVLTree<K : Comparable<K>, V> : AbstractBST<K, V, AVLTree<K, V>>() {
    internal var height: Int = 1

    override fun createNewTree(key: K?, value: V?): AVLTree<K, V> {
        val tmpTree = AVLTree<K, V>()
        tmpTree.key = key
        tmpTree.value = value
        return tmpTree
    }

    override fun insert(key: K, value: V?) {
        var currentTree = insertTree(key, value, this)
        while (currentTree != null) {
            balance(currentTree)
            currentTree = currentTree.parent
        }
    }

    override fun replaceSubtree(wasTree: AVLTree<K, V>, newTree: AVLTree<K, V>?): Unit =
        if (wasTree.parent == null) replaceTreeRoot(wasTree, newTree)
        else super.replaceSubtree(wasTree, newTree)

    override fun remove(key: K) {
        val necessarySubtree = searchTree(this, key) ?: return
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

    private fun getBalanceValue(tree: AVLTree<K, V>?): Int =
        if (tree == null) 0 else (tree.left?.height ?: 0) - (tree.right?.height ?: 0)


    private fun balance(givenTree: AVLTree<K, V>) {
        updateHeight(givenTree)
        val valueBalanceThisTree = getBalanceValue(givenTree)
        val valueBalanceRightSubtree = getBalanceValue(givenTree.right)
        val valueBalanceLeftSubtree = getBalanceValue(givenTree.left)

        val flagRoot: Boolean = givenTree.parent == null

        if (valueBalanceThisTree > 1 && valueBalanceLeftSubtree >= 0) {
            if (flagRoot) givenTree.rightAvlRotateRootTree()
            else rightRotate(givenTree)
        }
        if (valueBalanceThisTree > 1 && valueBalanceLeftSubtree < 0) {
            leftRotate(givenTree.left ?: throw IllegalStateException("The tree must have a left subtree in this rotate"))
            if (flagRoot) givenTree.rightAvlRotateRootTree()
            else rightRotate(givenTree)
        }
        if (valueBalanceThisTree < -1 && valueBalanceRightSubtree <= 0) {
            if (flagRoot) givenTree.leftAvlRotateRootTree()
            else leftRotate(givenTree)
        }
        if (valueBalanceThisTree < -1 && valueBalanceRightSubtree > 0) {
            rightRotate(givenTree.right ?: throw IllegalStateException("The tree must have a right subtree in this rotate"))
            if (flagRoot) givenTree.leftAvlRotateRootTree()
            else leftRotate(givenTree)
        }
    }

    /**
     * right turn for the case when the tree is the root
     */
    private fun rightAvlRotateRootTree() {
        val leftSubtree = this.left ?: throw IllegalStateException("The tree must have a left subtree in this rotate")
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
    }


    /**
     * left turn for the case when the tree is the root
     */
    private fun leftAvlRotateRootTree() {
        val rightSubtree = this.right ?: throw IllegalStateException("The tree must have a right subtree in this rotate")
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
    }

    override fun rightRotate(givenTree: AVLTree<K, V>): AVLTree<K, V> {
        val returnedRightRotateTree = super.rightRotate(givenTree)
        updateHeight(givenTree)
        updateHeight(returnedRightRotateTree)
        return returnedRightRotateTree
    }

    override fun leftRotate(givenTree: AVLTree<K, V>): AVLTree<K, V> {
        val returnedLeftRotateTree = super.leftRotate(givenTree)
        updateHeight(givenTree)
        updateHeight(returnedLeftRotateTree)
        return returnedLeftRotateTree
    }
}
