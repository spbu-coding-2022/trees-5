package trees.bstree

import trees.AbstractBST

class BinarySearchTree<K : Comparable<K>, V> : AbstractBST<K, V, BinarySearchTree<K, V>>() {
    override fun createNewTree(key: K?, value: V?): BinarySearchTree<K, V> {
        val tmpTree = BinarySearchTree<K, V>()
        tmpTree.key = key
        tmpTree.value = value
        return tmpTree
    }

    override fun replaceSubtree(wasTree: BinarySearchTree<K, V>, newTree: BinarySearchTree<K, V>?): Unit =
        if (wasTree.parent == null) replaceTreeRoot(wasTree, newTree)
        else super.replaceSubtree(wasTree, newTree)
}
