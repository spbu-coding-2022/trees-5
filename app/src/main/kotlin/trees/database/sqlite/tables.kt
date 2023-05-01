package trees.database.sqlite

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable


internal object SubtreesAvlTable : IntIdTable("nodes") {
    val key = varchar("key", 255)
    val value = varchar("value", 255)
    val height = varchar("height", 255)
    val left = reference("left_id", SubtreesAvlTable).nullable()
    val right = reference("right_id", SubtreesAvlTable).nullable()
    val tree = reference("tree_id", TreesTable)

}

internal class AvlSubtree(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<AvlSubtree>(SubtreesAvlTable)

    var key by SubtreesAvlTable.key
    var value by SubtreesAvlTable.value
    var height by SubtreesAvlTable.height
    var left by AvlSubtree optionalReferencedOn SubtreesAvlTable.left
    var right by AvlSubtree optionalReferencedOn SubtreesAvlTable.right
    var tree by TreeDatabase referencedOn SubtreesAvlTable.tree
}

internal object TreesTable : IntIdTable("trees") {
    val name = varchar("name", 255).uniqueIndex()
    val root = reference("root_node_id", SubtreesAvlTable).nullable()
    val typeOfKey = varchar("typeKey", 255)
}

internal class TreeDatabase(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<TreeDatabase>(TreesTable)

    var name by TreesTable.name
    var root by AvlSubtree optionalReferencedOn TreesTable.root
    var typeOfKey by TreesTable.typeOfKey
}
