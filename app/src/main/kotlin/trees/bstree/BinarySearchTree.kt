package trees.bstree

import trees.AbstractBST

class BinarySearchTree<K : Comparable<K>, V> : AbstractBST<K, V, BinarySearchTree<K, V>>() {
    fun insert(key: K, value: V? = null) {
        val currentKey = this.key
        when {
            currentKey == null || currentKey == key -> {
                this.key = key
                this.value = value
            }

            currentKey > key -> {
                this.left = this.left ?: BinarySearchTree()
                this.left?.insert(key, value)
            }

            currentKey < key -> {
                this.right = this.right ?: BinarySearchTree()
                this.right?.insert(key, value)
            }
        }
    }

    private fun findMinimum(currentTree: BinarySearchTree<K, V>): BinarySearchTree<K, V> {
        return when {
            currentTree.left == null || currentTree.left?.key == null -> currentTree
            else -> findMinimum(currentTree.left ?: throw Exception("left subtree can not be null"))
        }
    }

    private fun remove(tree: BinarySearchTree<K, V>?, key: K): BinarySearchTree<K, V>? {
        when {
            tree == null -> return null
            greatThan(tree.key, key) -> tree.left = remove(tree.left, key)
            greatThan(key, tree.key) -> tree.right = remove(tree.right, key)
            tree.left != null && tree.right != null -> {
                val tmpMinimum = findMinimum(tree.right ?: throw Exception("right subtree can not be null"))
                tree.key = tmpMinimum.key
                tree.value = tmpMinimum.value
                tree.right = remove(tree.right, tmpMinimum.key ?: throw Exception("this key can not be null"))
            }

            tree.left != null -> return tree.left
            tree.right != null -> return tree.right
            else -> return null
        }
        return tree
    }

    fun remove(key: K) {
        when {
            greatThan(this.key, key) -> this.left = remove(this.left, key)
            lessThan(this.key, key) -> this.right = remove(this.right, key)
            else -> {
                when {
                    this.left != null && this.right != null -> {
                        val tmpMinimum = findMinimum(this.right ?: throw Exception("right subtree can not be null"))
                        this.key = tmpMinimum.key
                        this.value = tmpMinimum.value
                        this.right = remove(this.right, this.key ?: throw Exception("this key can not be null"))
                    }

                    this.left != null -> this.copyFields(this.left ?: throw Exception("left subtree can not be null"))
                    this.right != null -> this.copyFields(this.right ?: throw Exception("right subtree can not be null"))
                    else -> {
                        this.key = null
                        this.value = null
                    }
                }
            }
        }
    }
}
