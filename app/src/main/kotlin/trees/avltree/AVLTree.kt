package trees.avltree

import trees.AbstractBST
import kotlin.math.max

class AVLTree<K : Comparable<K>> : AbstractBST<K, AVLTree<K>>() {
    internal var height: Int = 1

    fun insert(key: K, value: Any? = null) {
        val currentKey = this.key
        when {
            currentKey == null || currentKey == key -> {
                this.key = key
                this.value = value
            }

            currentKey < key -> {
                this.right = this.right ?: AVLTree()
                this.right?.insert(key, value)
            }

            currentKey > key -> {
                this.left = this.left ?: AVLTree()
                this.left?.insert(key, value)
            }
        }
        this.balance()
    }


    private fun findMinimum(currentTree: AVLTree<K>): AVLTree<K> {
        return when {
            currentTree.left == null || currentTree.left?.key == null -> currentTree
            else -> findMinimum(currentTree.left!!)
        }
    }

    private fun remove(tree: AVLTree<K>?, key: K): AVLTree<K>? {
        when {
            tree == null -> return null
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
        tree.balance()
        return tree
    }

    fun remove(key: K) {
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
        this.balance()
    }

    private fun updateHeight() {
        this.height = 1 + max(this.left?.height ?: 0, this.right?.height ?: 0)
    }

    private fun getBalanceValue(tree: AVLTree<K>?): Int {
        if (tree == null) return 0
        return (tree.left?.height ?: 0) - (tree.right?.height ?: 0)
    }

    private fun balance() {
        this.updateHeight()
        val valueBalanceThisTree = getBalanceValue(this)
        val valueBalanceRightSubtree = getBalanceValue(this.right)
        val valueBalanceLeftSubtree = getBalanceValue(this.left)

        if (valueBalanceThisTree > 1 && valueBalanceLeftSubtree >= 0) this.rightRotate()
        if (valueBalanceThisTree > 1 && valueBalanceLeftSubtree < 0) this.bigRightRotate()
        if (valueBalanceThisTree < -1 && valueBalanceRightSubtree <= 0) this.leftRotate()
        if (valueBalanceThisTree < -1 && valueBalanceRightSubtree > 0) this.bigLeftRotate()
    }

    private fun bigRightRotate() {
        this.left?.leftRotate()
        this.rightRotate()
    }

    private fun bigLeftRotate() {
        this.right?.rightRotate()
        this.leftRotate()
    }

    private fun rightRotate() {
        val bTree = this.left
        val correctRight = AVLTree<K>()
        val correctLeft = bTree?.left
        correctRight.key = this.key
        correctRight.value = this.value
        correctRight.right = this.right
        correctRight.left = bTree?.right
        this.value = bTree?.value
        this.key = bTree?.key
        this.right = correctRight
        this.left = correctLeft

        this.updateHeight()
        this.right?.updateHeight()
    }

    private fun leftRotate() {
        val bTree = this.right
        val correctRight = bTree?.right
        val correctLeft = AVLTree<K>()
        correctLeft.key = this.key
        correctLeft.value = this.value
        correctLeft.left = this.left
        correctLeft.right = bTree?.left
        this.value = bTree?.value
        this.key = bTree?.key
        this.right = correctRight
        this.left = correctLeft

        this.updateHeight()
        this.left?.updateHeight()
    }
}