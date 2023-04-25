package trees

fun <K: Comparable<K>, V, Subtree: AbstractBST<K, V, Subtree>> treeKeysCorrectOrder(tree: Subtree): Boolean {
    val listKeys = traverseInOrder(tree)
    for (i in 0 until listKeys.size - 1) {
        val prevKey = listKeys[i].key ?: throw IllegalStateException("prev key can not be null")
        val currentKey = listKeys[i + 1].key ?: throw IllegalStateException("current key can not be null")
        if (prevKey >= currentKey)
            return false
    }
    return true
}

fun <K: Comparable<K>, V, Subtree: AbstractBST<K, V, Subtree>> correctReferencesParents(tree: Subtree?, parent: Subtree? = null): Boolean {
    if (tree == null) return true
    return (tree.parent == parent) && correctReferencesParents(tree.left, tree) && correctReferencesParents(tree.right, tree)
}

fun <K: Comparable<K>, V, Subtree: AbstractBST<K, V, Subtree>> traverseInOrder(tree: Subtree): List<Subtree> {
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

