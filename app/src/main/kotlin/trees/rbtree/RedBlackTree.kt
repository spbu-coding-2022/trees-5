package trees.rbtree

import trees.AbstractBST
import java.lang.IllegalStateException

class RedBlackTree<K : Comparable<K>> : AbstractBST<K, RedBlackTree<K>>() {
    enum class Color {
        RED, BLACK
    }

    private var parent: RedBlackTree<K>? = null
    var color: Color = Color.RED

    private fun get_root() : RedBlackTree<K> {
        var n = this
        var parent = n.parent
        while (parent != null) {
            n = parent
            parent = n.parent
        }
        return n
    }

    private fun leftRotate() {
        val pivot = this.right ?: throw IllegalArgumentException("Node to rotate must have a right child")
        val pivotLeft = pivot.left
        val currentParent = this.parent
        this.right = pivotLeft

        if (pivotLeft != null)
            pivot.left?.parent = this
        pivot.parent = currentParent

        if (this == currentParent?.left)
            this.parent?.left = pivot
        else
            this.parent?.right = pivot

        pivot.left = this
        this.parent = pivot
    }

    private fun rightRotate() {
        val pivot = this.left ?: throw IllegalArgumentException("Node to rotate must have a left child")
        val pivotRight = pivot.right
        val currentParent = this.parent
        this.left = pivotRight

        if (pivotRight != null)
            pivot.right?.parent = this
        pivot.parent = currentParent

        if (this == currentParent?.right)
            this.parent?.right = pivot
        else
            this.parent?.left = pivot

        pivot.right = this
        this.parent = pivot
    }

    private fun grandparent() : RedBlackTree<K>? {
        val currentParent = this.parent
        return currentParent?.parent
    }

    private fun uncle() : RedBlackTree<K>? {
        val gp = this.grandparent() ?: return null
        return if (this.parent == gp.left)
            gp.right
        else
            gp.left
    }

    fun insert(key: K, value: Any?) {
        val root = get_root()
        var n = RedBlackTree<K>()
        n.key = key
        n.value = value
        val rootkey = root.key
        if (rootkey == null || rootkey == key) {
            root.key = key
            root.value = value
            root.color = Color.BLACK
        } else {
            n.color = Color.RED
            var p : RedBlackTree<K>? = root
            var q : RedBlackTree<K>? = null
            while (p != null) {
                q = p
                val pKey = p.key ?: throw IllegalStateException("Key must be non-nullable")
                p = if (pKey < key)
                    p.right
                else
                    p.left
            }
            n.parent = q
            val qKey = q?.key ?: throw IllegalStateException("Key must be non-nullable")
            if (qKey < key)
                q.right = n
            else
                q.left = n
        }
        val parent = n.parent
        val gp = n.grandparent()
        val u = n.uncle()

        if (parent == null) {
            n.color = Color.BLACK
            return
        }
        while (parent.color == Color.RED) {
            if (parent == gp?.left) {
                if (u != null && u.color == Color.RED) {
                    parent.color = Color.BLACK
                    u.color = Color.BLACK
                    gp.color = Color.RED
                    n = gp
                } else {
                    if (n == parent.right) {
                        n = parent
                        n.leftRotate()
                    }
                    parent.color = Color.BLACK
                    gp.color = Color.RED
                    gp.rightRotate()
                }
            } else {
                if (u != null && u.color == Color.RED) {
                    parent.color = Color.BLACK
                    u.color = Color.BLACK
                    gp?.color = Color.RED
                    n = gp ?: throw IllegalStateException("Grandparent can not be null, becauce uncle is not null")
                } else {
                    if (n == parent.left) {
                        n = parent
                        n.rightRotate()
                    }
                    parent.color = Color.BLACK
                    gp?.color = Color.RED
                    gp?.leftRotate()
                }
            }
        }
        get_root().color = Color.BLACK
    }

    private fun transplant(Node: RedBlackTree<K>?, newNode: RedBlackTree<K>?) {
        val parent = Node?.parent

        if (Node == parent?.left)
            parent?.left = newNode
        else
            parent?.right = newNode
        newNode?.parent = parent
    }

    private fun treeMinimum() : RedBlackTree<K> {
        var x = this
        var left = x.left
        while (left != null) {
            x = left
            left = x.left
        }
        return x
    }

    fun remove(key: K) {
        val root = get_root()
        var node: RedBlackTree<K>? = root
        while (node?.key != key) {
            val k = node?.key ?: throw IllegalStateException("Key must be non-nullable")
            node = if (k < key)
                node.right
            else
                node.left
        }
        var nodeToRemove = node
        var colorNodeToRemove = nodeToRemove.color
        val nodeRight = node.right
        var currentNode: RedBlackTree<K>?
        if (node.left == null) {
            currentNode = node.right
            transplant(node, node.right)
        } else if (nodeRight == null) {
            currentNode = node.left
            transplant(node, node.left)
        } else {
            nodeToRemove = nodeRight.treeMinimum()
            colorNodeToRemove = nodeToRemove.color
            currentNode = nodeToRemove.right
            if (nodeToRemove.parent == node)
                currentNode?.parent = nodeToRemove
            else {
                transplant(nodeToRemove, nodeToRemove.right)
                nodeToRemove.right = node.right
                nodeToRemove.right?.parent = nodeToRemove
            }
            transplant(node, nodeToRemove)
            nodeToRemove.left = node.left
            nodeToRemove.left?.parent = nodeToRemove
            nodeToRemove.color = node.color
        }
        if (colorNodeToRemove == Color.BLACK) {
            val currentNodeParent = currentNode?.parent
            while (currentNodeParent != null && currentNode?.color == Color.BLACK) {
                if (currentNode == currentNodeParent.left) {
                    var child = currentNodeParent.right
                    if (child?.color == Color.RED) {
                        child.color = Color.BLACK
                        currentNode.parent?.color = Color.RED
                        currentNode.parent?.leftRotate()
                        child = currentNodeParent.right
                    }
                    if (child?.left?.color == Color.BLACK && child.right?.color == Color.BLACK) {
                        child.color = Color.RED
                        currentNode = currentNode.parent
                    } else {
                        if (child?.right?.color == Color.BLACK) {
                            child.left?.color = Color.BLACK
                            child.color = Color.RED
                            child.rightRotate()
                            child = currentNodeParent.right
                        }
                        child?.color = currentNodeParent.color
                        currentNode.parent?.color = Color.BLACK
                        child?.right?.color = Color.BLACK
                        currentNode.parent?.leftRotate()
                        currentNode = root
                    }
                } else {
                    var child = currentNodeParent.left
                    if (child?.color == Color.RED) {
                        child.color = Color.BLACK
                        currentNode.parent?.color = Color.RED
                        currentNode.parent?.rightRotate()
                        child = currentNodeParent.left
                    }
                    if (child?.left?.color == Color.BLACK && child.right?.color == Color.BLACK) {
                        child.color = Color.RED
                        currentNode = currentNode.parent
                    } else {
                        if (child?.left?.color == Color.BLACK) {
                            child.right?.color = Color.BLACK
                            child.color = Color.RED
                            child.leftRotate()
                            child = currentNodeParent.left
                        }
                        child?.color = currentNodeParent.color
                        currentNode.parent?.color = Color.BLACK
                        child?.left?.color = Color.BLACK
                        currentNode.parent?.rightRotate()
                        currentNode = root
                    }
                }
            }
        }
        currentNode?.color = Color.BLACK
    }
}
