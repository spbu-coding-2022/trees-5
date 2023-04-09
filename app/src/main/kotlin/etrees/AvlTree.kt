package etrees

import kotlin.math.max

class AvlTree<K : Comparable<K>> : AbstractBST<K, AvlTree<K>>(), Balancer {
    private var height: Int = 1

    fun insert(key: Int, value: Any?) {
        TODO()
    }

    fun remove(key: Int) {
        TODO()
    }

    private fun updateHeight() {
        this.height = 1 + max(this.left?.height ?: 0, this.right?.height ?: 0)
    }

    private fun getBalanceValue(tree: AvlTree<K>?): Int {
        if (tree == null) return 0
        return (tree.left?.height ?: 0) - (tree.right?.height ?: 0)
    }

    override fun balance() {
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

    override fun rightRotate() {
        val bTree = this.left
        val correctRight = AvlTree<K>()
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

    override fun leftRotate() {
        val bTree = this.right
        val correctRight = bTree?.right
        val correctLeft = AvlTree<K>()
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
