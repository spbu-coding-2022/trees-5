package trees.database.sqlite.avl

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption


internal object NodesTable : IntIdTable("nodes") {
    val key = varchar("key", 255)
    val value = varchar("value", 255)
    val height = varchar("height", 255)
    val left = reference("left_id", NodesTable).nullable()
    val right = reference("right_id", NodesTable).nullable()
    val tree = reference("tree_id", TreesTable, onDelete = ReferenceOption.CASCADE)

}

internal class DBNode(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DBNode>(NodesTable)

    var key by NodesTable.key
    var value by NodesTable.value
    var height by NodesTable.height
    var left by DBNode optionalReferencedOn NodesTable.left
    var right by DBNode optionalReferencedOn NodesTable.right
    var tree by DBTree referencedOn NodesTable.tree
}

internal object TreesTable : IntIdTable("trees") {
    val name = varchar("name", 255).uniqueIndex()
    val root = reference("root_node_id", NodesTable).nullable()
}

internal class DBTree(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DBTree>(TreesTable)

    var name by TreesTable.name
    var root by DBNode optionalReferencedOn TreesTable.root
}
