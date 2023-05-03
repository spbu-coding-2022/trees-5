package trees.rbtree

import trees.AbstractBST

class RedBlackTree<K : Comparable<K>, V> : AbstractBST<K, V, RedBlackTree<K, V>>() {
    enum class Color {
        RED, BLACK
    }

    internal var root = this
    internal var color: Color = Color.BLACK

    override fun createNewTree(key: K?, value: V?): RedBlackTree<K, V> {
        val tmp = RedBlackTree<K, V>()
        tmp.value = value
        tmp.key = key
        return tmp
    }

    override fun findByKey(key: K): V? {
        return findByKey(root, key)
    }

    override fun replaceSubtree(wasTree: RedBlackTree<K, V>, newTree: RedBlackTree<K, V>?) {
        val parent = wasTree.parent
        if (parent == null) {
            if (newTree == null) {
                root.key = null
                root.value = null
                root.left = null
                root.right = null
            }
            else {
                root = newTree
            }
        } else if (parent.left == wasTree) {
            parent.left = newTree
        } else {
            parent.right = newTree
        }
        newTree?.parent = wasTree.parent
    }

    private fun reverseColor() {
        this.color = if (this.color == Color.BLACK) Color.RED else Color.BLACK
    }

    private fun getSibling(givenTree: RedBlackTree<K, V>): RedBlackTree<K, V>? {
        val currentParent = givenTree.parent ?: return null
        if (currentParent.left == givenTree) return currentParent.right
        return currentParent.left
    }

    private fun getUncle(givenTree: RedBlackTree<K, V>): RedBlackTree<K, V>? {
        val currentParent = givenTree.parent ?: return null
        return getSibling(currentParent)
    }

    /**
     * you can read more about balancing after adding and after removing a subtree here
     * https://neerc.ifmo.ru/wiki/
     */
    private fun balanceAfterInsert(givenTree: RedBlackTree<K, V>): RedBlackTree<K, V> {
        var currentTree = givenTree

        while (currentTree.parent != null && currentTree.parent?.color == Color.RED) {
            val currentParent = currentTree.parent ?: throw IllegalStateException("the parent must not be null")
            val currentGrandParent = currentParent.parent ?: throw IllegalStateException("grand parent must not be null")
            val currentUncle = getUncle(currentTree)
            if (currentUncle?.color == Color.RED) {
                currentParent.reverseColor()
                currentGrandParent.reverseColor()
                currentUncle.reverseColor()
                currentTree = currentGrandParent
            } else {
                if (currentGrandParent.left == currentParent) {
                    if (currentTree == currentParent.right)
                        leftRotate(currentParent)
                    currentTree = rightRotate(currentGrandParent)
                    currentTree.right?.reverseColor()
                } else {
                    if (currentTree == currentParent.left)
                        rightRotate(currentParent)
                    currentTree = leftRotate(currentGrandParent)
                    currentTree.left?.reverseColor()
                }
                currentTree.reverseColor()
                break
            }
        }

        if (currentTree.parent == null && currentTree.color == Color.RED) {
            currentTree.reverseColor()
            return currentTree
        }

        while (currentTree.parent != null) {
            currentTree = currentTree.parent ?: throw IllegalStateException("current tree must have parent")
        }
       return currentTree
    }

    private fun balanceAfterRemove(givenTree: RedBlackTree<K, V>): RedBlackTree<K, V> {
        var currentTree = givenTree
        while (currentTree.parent != null && currentTree.color == Color.BLACK) {
            val currentParent = currentTree.parent ?: throw IllegalStateException("parent can not be null")
            if (currentParent.left == currentTree) {
                val rightSibling = currentParent.right ?: throw IllegalStateException("parent must have right child")
                if (currentParent.color == Color.RED) {
                    if (rightSibling.left?.color == Color.RED || rightSibling.right?.color == Color.RED) {
                        currentParent.reverseColor()
                        if (rightSibling.left?.color == Color.RED) {
                            rightRotate(rightSibling)
                        } else {
                            rightSibling.reverseColor()
                            rightSibling.right?.reverseColor()
                        }
                        currentTree = leftRotate(currentParent)
                    } else {
                        currentParent.reverseColor()
                        rightSibling.reverseColor()
                    }
                    break
                } else {
                    if (rightSibling.color == Color.RED) {
                        var leftChildOfRightSibling = rightSibling.left ?: throw IllegalStateException("left child of right sibling can not be null")
                        if (leftChildOfRightSibling.left?.color == Color.RED || leftChildOfRightSibling.right?.color == Color.RED) {
                            if (leftChildOfRightSibling.right == null || leftChildOfRightSibling.right?.color == Color.BLACK) {
                                leftChildOfRightSibling.reverseColor()
                                leftChildOfRightSibling.left?.reverseColor()
                                leftChildOfRightSibling = rightRotate(leftChildOfRightSibling)
                            }
                            leftChildOfRightSibling.right?.reverseColor()
                            rightRotate(rightSibling)
                        } else {
                            rightSibling.reverseColor()
                            leftChildOfRightSibling.reverseColor()
                        }
                        currentTree = leftRotate(currentParent)
                        break
                    } else {
                        if (rightSibling.left?.color == Color.RED || rightSibling.right?.color == Color.RED) {
                            if (rightSibling.left?.color == Color.RED) {
                                rightSibling.left?.reverseColor()
                                rightRotate(rightSibling)
                            } else {
                                rightSibling.right?.reverseColor()
                            }
                            currentTree = leftRotate(currentParent)
                            break
                        } else {
                            rightSibling.reverseColor()
                            currentTree = currentParent
                        }
                    }
                }
            } else {
                val leftSibling = currentParent.left ?: throw IllegalStateException("parent must have left child")
                if (currentParent.color == Color.RED) {
                    if (leftSibling.left?.color == Color.RED || leftSibling.right?.color == Color.RED) {
                        currentParent.reverseColor()
                        if (leftSibling.right?.color == Color.RED) {
                            leftRotate(leftSibling)
                        } else {
                            leftSibling.reverseColor()
                            leftSibling.left?.reverseColor()
                        }
                        currentTree = rightRotate(currentParent)
                    } else {
                        currentParent.reverseColor()
                        leftSibling.reverseColor()
                    }
                    break
                } else {
                    if (leftSibling.color == Color.RED) {
                        var rightChildOfLeftSibling = leftSibling.right ?: throw IllegalStateException("right child of left sibling can not be null")
                        if (rightChildOfLeftSibling.left?.color == Color.RED || rightChildOfLeftSibling.right?.color == Color.RED) {
                            if (rightChildOfLeftSibling.left == null || rightChildOfLeftSibling.left?.color == Color.BLACK) {
                                rightChildOfLeftSibling.reverseColor()
                                rightChildOfLeftSibling.right?.reverseColor()
                                rightChildOfLeftSibling = leftRotate(rightChildOfLeftSibling)
                            }
                            rightChildOfLeftSibling.left?.reverseColor()
                            leftRotate(leftSibling)
                        } else {
                            leftSibling.reverseColor()
                            rightChildOfLeftSibling.reverseColor()
                        }
                        currentTree = rightRotate(currentParent)
                        break
                    } else {
                        if (leftSibling.left?.color == Color.RED || leftSibling.right?.color == Color.RED) {
                            if (leftSibling.right?.color == Color.RED) {
                                leftSibling.right?.reverseColor()
                                leftRotate(leftSibling)
                            } else {
                                leftSibling.left?.reverseColor()
                            }
                            currentTree = rightRotate(currentParent)
                            break
                        } else {
                            leftSibling.reverseColor()
                            currentTree = currentParent
                        }
                    }
                }
            }
        }

        while (currentTree.parent != null) {
            currentTree = currentTree.parent ?: throw IllegalStateException("current tree must have parent")
        }

        return currentTree
    }

    override fun insert(key: K, value: V?) {
        val insertedTree = insertTree(key, value, root) ?: return
        insertedTree.color = Color.RED
        root = balanceAfterInsert(insertedTree)
    }

    override fun deleteTreeWithNoSubtree(givenTree: RedBlackTree<K, V>): RedBlackTree<K, V> {
        if (givenTree.color == Color.BLACK) {
            root = balanceAfterRemove(givenTree)
        }
        replaceSubtree(givenTree, null)
        return givenTree
    }

    override fun deleteTreeWithOneSubtree(givenTree: RedBlackTree<K, V>): RedBlackTree<K, V> {
        if (givenTree.left != null) {
            givenTree.left?.reverseColor()
            replaceSubtree(givenTree, givenTree.left)
        } else {
            givenTree.right?.reverseColor()
            replaceSubtree(givenTree, givenTree.right)
        }
        return givenTree
    }

    override fun remove(key: K) {
        val necessaryTree = searchTree(root, key) ?: return
        removeSubtree(necessaryTree)
    }
}
