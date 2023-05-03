import org.openrndr.*
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.LineCap
import org.openrndr.draw.loadFont
import org.openrndr.panel.ControlManager
import org.openrndr.panel.layout
import kotlin.math.pow
import org.openrndr.panel.elements.*
import trees.*
import trees.avltree.AVLTree
import trees.bstree.BinarySearchTree
import trees.rbtree.RedBlackTree

fun main() = application {
    configure {
        width = 1280
        height = 720
        windowResizable = true
        title = "Trees Renderer"
    }

    var input = ""
    program {
        // States of drawing trees
        var stateDrawingBST = true
        var stateDrawingAVL = false
        var stateDrawingRBT = false

        // Radius of drawing nodes
        val radius = width / 90.0 + height / 50.0
        // State of drawing value of node
        var stateDrawingValue = false

        // Flags of first input of nodes
        var firstInputBST = true
        var firstInputAVL = true
        var firstInputRBT = true
        // Value of key to find in tree
        var keyStringFinding: String? = null
        var keyIntFinding: Int? = null

        // Trees to type matching of key
        val BSTString = BinarySearchTree<String, String>()
        val BSTInt = BinarySearchTree<Int, String>()

        val RBTString = RedBlackTree<String, String>()
        val RBTInt = RedBlackTree<Int, String>()

        val AVLString = AVLTree<String, String>()
        val AVLInt = AVLTree<Int, String>()

        // Flag of using type of tree (if false we use string key bst else int key bst)
        var stringOrIntBSTFlag = false
        var stringOrIntAVLFlag = false
        var stringOrIntRBTFlag = false

        keyboard.character.listen {
            input += it.character
        }

        fun insertNode(input: String) {
            val str = input.split(";")
            val key: String?
            val value: String?
            if (str.size > 1) {
                key = str[0]
                value = str[1]
            } else {
                key = input
                value = ""
            }
            if (stateDrawingBST) {
                if (firstInputBST) {
                    firstInputBST = false
                    if (input.toIntOrNull() != null)
                        stringOrIntBSTFlag = true
                }
                if (input.isNotEmpty()) {
                    if (stringOrIntBSTFlag) {
                        if (input.toIntOrNull() != null)
                            BSTInt.insert(key.toInt(), value)
                    } else
                        BSTString.insert(key, value)
                }
            } else if (stateDrawingAVL) {
                if (firstInputAVL) {
                    firstInputAVL = false
                    if (input.toIntOrNull() != null)
                        stringOrIntAVLFlag = true
                }
                if (input.isNotEmpty()) {
                    if (stringOrIntAVLFlag) {
                        if (input.toIntOrNull() != null)
                            AVLInt.insert(key.toInt(), value)
                    } else
                        AVLString.insert(key, value)
                }
            } else {
                if (firstInputRBT) {
                    firstInputRBT = false
                    if (input.toIntOrNull() != null)
                        stringOrIntRBTFlag = true
                }
                if (input.isNotEmpty()) {
                    if (stringOrIntRBTFlag) {
                        if (input.toIntOrNull() != null)
                            RBTInt.insert(key.toInt(), value)
                    } else
                        RBTString.insert(key, value)
                }
            }
        }

        fun removeNode(input: String) {
            if (stateDrawingBST) {
                if (stringOrIntBSTFlag)
                    BSTInt.remove(input.toInt())
                else
                    BSTString.remove(input)
            } else if (stateDrawingAVL) {
                if (stringOrIntAVLFlag)
                    AVLInt.remove(input.toInt())
                else
                    AVLString.remove(input)
            } else {
                if (stringOrIntRBTFlag)
                    RBTInt.remove(input.toInt())
                else
                    RBTString.remove(input)
            }
        }

        fun findNode(input: String) {
            if (stateDrawingBST) {
                if (stringOrIntBSTFlag)
                    keyIntFinding = input.toInt()
                else
                    keyStringFinding = input
            } else if (stateDrawingAVL) {
                if (stringOrIntAVLFlag)
                    keyIntFinding = input.toInt()
                else
                    keyStringFinding = input
            } else {
                if (stringOrIntRBTFlag)
                    keyIntFinding = input.toInt()
                else
                    keyStringFinding = input
            }
        }

        keyboard.keyDown.listen {
            if (it.key == KEY_ENTER) {
                insertNode(input)
                input = ""
            }
            if (it.key == KEY_BACKSPACE) {
                if (input.isNotEmpty()) {
                    input = input.substring(0, input.length - 1)
                }
            }
        }

        extend(ControlManager()) {
            layout {
                button {
                    label = "Binary Search Tree"
                    clicked {
                        stateDrawingBST = stateDrawingBST xor true
                        if (stateDrawingBST) {
                            stateDrawingAVL = false
                            stateDrawingRBT = false
                        }
                    }
                }
                button {
                    label = "         AVL Tree        "
                    clicked {
                        stateDrawingAVL = stateDrawingAVL xor true
                        if (stateDrawingAVL) {
                            stateDrawingBST = false
                            stateDrawingRBT = false
                        }
                    }
                }
                button {
                    label = "   Red Black Tree    "
                    clicked {
                        stateDrawingRBT = stateDrawingRBT xor true
                        if (stateDrawingRBT) {
                            stateDrawingBST = false
                            stateDrawingAVL = false
                        }
                    }
                }
                button {
                    label = "      Insert Node       "
                    clicked {
                        insertNode(input)
                        input = ""
                    }
                }
                button {
                    label = "    Remove Node     "
                    clicked {
                        if (input.isNotEmpty())
                            removeNode(input)
                        input = ""
                    }
                }
                button {
                    label = "        Find Node        "
                    clicked {
                        findNode(input)
                        input = ""
                    }
                }
            }
        }
        val font = loadFont("data/fonts/default.otf", width / 100.0 + height / 100.0)
        fun textDrawer(text: String, x: Double, y: Double, color: ColorRGBa = ColorRGBa.BLACK) {
            drawer.fontMap = font
            drawer.fill = color
            drawer.text(text, x, y)
        }

        fun nodeDrawer(x: Double, y: Double, radius: Double, color: ColorRGBa = ColorRGBa.WHITE) {
            drawer.fill = color
            drawer.stroke = null
            drawer.strokeWeight = 1.0
            drawer.circle(x, y, radius)
        }

        fun lineDrawer(x1: Double, y1: Double, x2: Double, y2: Double) {
            drawer.stroke = ColorRGBa.BLACK
            drawer.strokeWeight = 1.0
            drawer.lineCap = LineCap.SQUARE
            drawer.lineSegment(x1, y1, x2, y2)
        }

        fun squareDraw(x: Double, y: Double, w: Double) {
            drawer.fill = ColorRGBa.GRAY
            drawer.stroke = null
            drawer.strokeWeight = 1.0
            drawer.rectangle(x, y, w, w)
        }
        keyboard.keyDown.listen {
            if (it.key == KEY_LEFT_SHIFT)
                stateDrawingValue = stateDrawingValue xor true
        }
        fun <K : Comparable<K>> drawBinarySearchTree(
            treeList: MutableList<BinarySearchTree<K, String>?>,
            state: Boolean,
            keyToFind: K? = null
        ) {
            if (state == false || treeList[0]?.key == null)
                return
            val numberOfLevels = height(treeList[0])
            drawer.clear(ColorRGBa.PINK)
            var nodeIndex = 1
            for (level in 1..numberOfLevels) {
                val power = 2.0.pow(level).toInt()
                if (level == 1)
                    continue
                var t = 0
                var cnt = 0
                for (k in 0 until power / 2 step 2) {
                    for (j in t until power step 2) {
                        if (treeList[nodeIndex] == null) {
                            nodeIndex++
                            cnt++
                            if (cnt == 2) {
                                t = j + 2
                                cnt = 0
                                break
                            }
                            continue
                        }
                        lineDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            (width / (power / 2) + k * width / (power / 2)).toDouble(),
                            (level - 1) * height / (numberOfLevels + 1).toDouble()
                        )
                        cnt++
                        nodeIndex++
                        if (cnt == 2) {
                            t = j + 2
                            cnt = 0
                            break
                        }
                    }
                }
            }
            nodeIndex = 0
            for (level in 1..numberOfLevels) {
                val power = 2.0.pow(level).toInt()
                for (j in 0 until power step 2) {
                    if (treeList[nodeIndex] == null) {
                        nodeIndex++
                        continue
                    }
                    if (treeList[nodeIndex]?.key == keyToFind) {
                        nodeDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            radius,
                            ColorRGBa.BLUE
                        )
                    } else
                        nodeDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            radius
                        )
                    nodeIndex++
                }
            }
            writer {
                nodeIndex = 0
                for (level in 1..numberOfLevels) {
                    val power = 2.0.pow(level).toInt()
                    for (j in 0 until power step 2) {
                        if (treeList[nodeIndex] == null) {
                            nodeIndex++
                            continue
                        }
                        val text = treeList[nodeIndex]?.key.toString()
                        val value = treeList[nodeIndex]?.value.toString()
                        nodeIndex++
                        val widthText = font.let {
                            drawer.fontMap = font
                            textWidth(text)
                        }
                        val widthValueText = font.let {
                            drawer.fontMap = font
                            textWidth(value)
                        }
                        val x = (width / power + j * width / power).toDouble()
                        val y = level * height / (numberOfLevels + 1).toDouble()
                        if (stateDrawingValue)
                            textDrawer(
                                value,
                                x - widthValueText / 2,
                                y + font.height / 2.0
                            )
                        else
                            textDrawer(
                                text,
                                x - widthText / 2,
                                y + font.height / 2.0
                            )
                    }
                }
            }
        }

        fun <K : Comparable<K>> drawRedBlackTree(
            treeList: MutableList<RedBlackTree<K, String>?>,
            state: Boolean,
            keyToFind: K? = null
        ) {
            if (state == false || treeList[0]?.key == null)
                return
            val numberOfLevels = height(treeList[0])
            drawer.clear(ColorRGBa.PINK)
            var nodeIndex = 1
            for (level in 1..numberOfLevels) {
                val power = 2.0.pow(level).toInt()
                if (level == 1)
                    continue
                var t = 0
                var cnt = 0
                for (k in 0 until power / 2 step 2) {
                    for (j in t until power step 2) {
                        if (treeList[nodeIndex] == null) {
                            nodeIndex++
                            cnt++
                            if (cnt == 2) {
                                t = j + 2
                                cnt = 0
                                break
                            }
                            continue
                        }
                        lineDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            (width / (power / 2) + k * width / (power / 2)).toDouble(),
                            (level - 1) * height / (numberOfLevels + 1).toDouble()
                        )
                        cnt++
                        nodeIndex++
                        if (cnt == 2) {
                            t = j + 2
                            cnt = 0
                            break
                        }
                    }
                }
            }
            nodeIndex = 0
            for (level in 1..numberOfLevels) {
                val power = 2.0.pow(level).toInt()
                for (j in 0 until power step 2) {
                    if (treeList[nodeIndex] == null) {
                        nodeIndex++
                        continue
                    }
                    if (treeList[nodeIndex]?.key == keyToFind) {
                        nodeDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            radius,
                            ColorRGBa.BLUE
                        )
                    } else if (treeList[nodeIndex]?.color == RedBlackTree.Color.BLACK)
                        nodeDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            radius,
                            ColorRGBa.BLACK
                        )
                    else
                        nodeDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            radius,
                            ColorRGBa.RED
                        )
                    nodeIndex++
                }
            }
            writer {
                nodeIndex = 0
                for (level in 1..numberOfLevels) {
                    val power = 2.0.pow(level).toInt()
                    for (j in 0 until power step 2) {
                        if (treeList[nodeIndex] == null) {
                            nodeIndex++
                            continue
                        }
                        val text = treeList[nodeIndex]?.key.toString()
                        val value = treeList[nodeIndex]?.value.toString()
                        nodeIndex++
                        val widthText = font.let {
                            drawer.fontMap = font
                            textWidth(text)
                        }
                        val widthValueText = font.let {
                            drawer.fontMap = font
                            textWidth(value)
                        }
                        val x = (width / power + j * width / power).toDouble()
                        val y = level * height / (numberOfLevels + 1).toDouble()
                        if (stateDrawingValue)
                            textDrawer(
                                value,
                                x - widthValueText / 2,
                                y + font.height / 2.0,
                                ColorRGBa.WHITE
                            )
                        else
                            textDrawer(
                                text,
                                x - widthText / 2,
                                y + font.height / 2.0,
                                ColorRGBa.WHITE
                            )
                    }
                }
            }
        }

        fun <K : Comparable<K>> drawAVLtree(
            treeList: MutableList<AVLTree<K, String>?>,
            state: Boolean,
            keyToFind: K? = null
        ) {
            if (state == false || treeList[0]?.key == null)
                return
            val numberOfLevels = height(treeList[0])
            drawer.clear(ColorRGBa.PINK)
            var nodeIndex = 1
            for (level in 1..numberOfLevels) {
                val power = 2.0.pow(level).toInt()
                if (level == 1)
                    continue
                var t = 0
                var cnt = 0
                for (k in 0 until power / 2 step 2) {
                    for (j in t until power step 2) {
                        if (treeList[nodeIndex] == null) {
                            nodeIndex++
                            cnt++
                            if (cnt == 2) {
                                t = j + 2
                                cnt = 0
                                break
                            }
                            continue
                        }
                        lineDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            (width / (power / 2) + k * width / (power / 2)).toDouble(),
                            (level - 1) * height / (numberOfLevels + 1).toDouble()
                        )
                        cnt++
                        nodeIndex++
                        if (cnt == 2) {
                            t = j + 2
                            cnt = 0
                            break
                        }
                    }
                }
            }
            nodeIndex = 0
            for (level in 1..numberOfLevels) {
                val power = 2.0.pow(level).toInt()
                for (j in 0 until power step 2) {
                    if (treeList[nodeIndex] == null) {
                        nodeIndex++
                        continue
                    }
                    if (treeList[nodeIndex]?.key == keyToFind) {
                        nodeDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            radius,
                            ColorRGBa.BLUE
                        )
                    } else
                        nodeDrawer(
                            (width / power + j * width / power).toDouble(),
                            level * height / (numberOfLevels + 1).toDouble(),
                            radius
                        )
                    nodeIndex++
                    squareDraw(
                        (width / power + j * width / power).toDouble() - font.height,
                        level * height / (numberOfLevels + 1).toDouble() - font.height * 3.5,
                        font.height * 2
                    )
                }
            }
            writer {
                nodeIndex = 0
                for (level in 1..numberOfLevels) {
                    val power = 2.0.pow(level).toInt()
                    for (j in 0 until power step 2) {
                        if (treeList[nodeIndex] == null) {
                            nodeIndex++
                            continue
                        }
                        val text = treeList[nodeIndex]?.key.toString()
                        val heightText = treeList[nodeIndex]?.height.toString()
                        val value = treeList[nodeIndex]?.value.toString()
                        val widthText = font.let {
                            drawer.fontMap = font
                            textWidth(text)
                        }
                        val widthHeightText = font.let {
                            drawer.fontMap = font
                            textWidth(heightText)
                        }
                        val widthValueText = font.let {
                            drawer.fontMap = font
                            textWidth(value)
                        }
                        val x = (width / power + j * width / power).toDouble()
                        val y = level * height / (numberOfLevels + 1).toDouble()
                        if (stateDrawingValue)
                            textDrawer(
                                value,
                                x - widthValueText / 2,
                                y + font.height / 2.0
                            )
                        else
                            textDrawer(
                                text,
                                x - widthText / 2,
                                y + font.height / 2.0
                            )
                        textDrawer(
                            heightText,
                            (width / power + j * width / power).toDouble() - widthHeightText / 2,
                            level * height / (numberOfLevels + 1).toDouble() - font.height * 2,
                            ColorRGBa.WHITE
                        )
                        nodeIndex++
                    }
                }
            }
        }

        extend {
            drawer.clear(ColorRGBa.PINK)
            if (stringOrIntBSTFlag)
                drawBinarySearchTree(treeLevelOrder(BSTInt), stateDrawingBST, keyIntFinding)
            else
                drawBinarySearchTree(treeLevelOrder(BSTString), stateDrawingBST, keyStringFinding)
            if (stringOrIntRBTFlag)
                drawRedBlackTree(treeLevelOrder(RBTInt.root), stateDrawingRBT, keyIntFinding)
            else
                drawRedBlackTree(treeLevelOrder(RBTString.root), stateDrawingRBT, keyStringFinding)
            if (stringOrIntAVLFlag)
                drawAVLtree(treeLevelOrder(AVLInt), stateDrawingAVL, keyIntFinding)
            else
                drawAVLtree(treeLevelOrder(AVLString), stateDrawingAVL, keyStringFinding)
            writer {
                val widthText = font.let {
                    drawer.fontMap = font
                    textWidth(input)
                }
                drawer.fill = ColorRGBa.WHITE
                drawer.stroke = null
                drawer.strokeWeight = 1.0
                drawer.rectangle(0.0, height - font.height * 3.0, widthText + font.height * 2, font.height * 3.0)
                textDrawer(input, 0 + font.height, height - font.height)
            }
        }
    }
}
