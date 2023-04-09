package etrees

class AvlTree<K : Comparable<K>> : AbstractBST<K, AvlTree<K>>(), Balancer {
    private var height: Int = 1

    fun insert(key: Int, value: Any?) {
        TODO()
    }

    fun remove(key: Int) {
        TODO()
    }

    override fun balance() {
        TODO("Not yet implemented")
    }

    override fun rightRotate() {
        TODO("Not yet implemented")
    }

    override fun leftRotate() {
        TODO("Not yet implemented")
    }
}
