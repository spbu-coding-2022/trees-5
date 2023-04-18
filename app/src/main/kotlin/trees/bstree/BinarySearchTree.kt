package trees.bstree

import trees.AbstractBST

class BinarySearchTree<K : Comparable<K>> : AbstractBST<K, BinarySearchTree<K>>() {
    fun insert(key: K, value: Any? = null) {
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

    private fun findMinimum(currentTree: BinarySearchTree<K>): BinarySearchTree<K> {
        return when {
            currentTree.left == null || currentTree.left?.key == null -> currentTree
            else -> findMinimum(currentTree.left!!)
        }
    }

    private fun remove(tree: BinarySearchTree<K>?, key: K): BinarySearchTree<K>? {
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
    }
}
