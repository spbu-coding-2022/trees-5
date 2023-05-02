package trees

/**
 * checks if the tree keys are ascending
 */
fun <K: Comparable<K>, V, Subtree: AbstractBST<K, V, Subtree>> treeKeysCorrectOrder(tree: Subtree): Boolean {
    val listKeys = traverseInOrderTree(tree)
    for (i in 0 until listKeys.size - 1) {
        val prevKey = listKeys[i].key ?: throw IllegalStateException("prev key can not be null")
        val currentKey = listKeys[i + 1].key ?: throw IllegalStateException("current key can not be null")
        if (prevKey >= currentKey)
            return false
    }
    return true
}

/**
 * checking that links to parents are specified correctly
 */
fun <K: Comparable<K>, V, Subtree: AbstractBST<K, V, Subtree>> correctReferencesParents(tree: Subtree?, parent: Subtree? = null): Boolean {
    if (tree == null) return true
    return (tree.parent == parent) && correctReferencesParents(tree.left, tree) && correctReferencesParents(tree.right, tree)
}


/**
 * passes through all trees by the InOrder crawl type
 */
fun <K: Comparable<K>, V, Subtree: AbstractBST<K, V, Subtree>> traverseInOrderTree(tree: Subtree): List<Subtree> {
    val treeList = mutableListOf<Subtree>()

    fun recursionAdding(givenTree: Subtree?) {
        if (givenTree == null) return
        recursionAdding(givenTree.left)
        treeList.add(givenTree)
        recursionAdding(givenTree.right)
    }

    recursionAdding(tree)

    return treeList
}
