package trees.database.json.bst


import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.type.MapType
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.KotlinModule
import BinarySearchTree
import java.io.File

data class Node(
    val key: Int,
    val value: String,
    var left: Node? = null,
    var right: Node? = null
)

fun toJson(root: BinarySearchTree<Int, String>): String {
    val mapper = ObjectMapper().apply {
        registerModule(KotlinModule())
        enable(SerializationFeature.INDENT_OUTPUT)
    }
    return mapper.writeValueAsString(root)
}
fun fromJson(jsonString: String): BinarySearchTree<Int, Any?>? {
    val mapper = ObjectMapper().apply {
        registerModule(KotlinModule())
    }
    val typeRef: TypeReference<Map<String, Any>> = object : TypeReference<Map<String, Any>>() {}
    val map: Map<String, Any> = mapper.readValue(jsonString, typeRef)
    val typeFactory: TypeFactory = TypeFactory.defaultInstance()
    val mapType: MapType = typeFactory.constructMapType(HashMap::class.java, String::class.java, Any::class.java)
    val nodeMap: Map<String, Any> = mapper.convertValue(map, mapType)

    fun toNode(node: Map<String, Any>): BinarySearchTree<Int, Any?> {
        val key = node["key"] as Int
        val value = node["value"] as String
        val left = node["left"]?.let { toNode(it as Map<String, Any>) }
        val right = node["right"]?.let { toNode(it as Map<String, Any>) }
        val bst = BinarySearchTree<Int, Any?>()
        bst.key = key
        bst.value = value
        bst.left = left
        bst.right = right
        return bst
    }

    return nodeMap.values.firstOrNull()?.let { toNode(it as Map<String, Any>) }
}}