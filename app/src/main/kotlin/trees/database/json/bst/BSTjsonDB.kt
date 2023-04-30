package trees.database.json.bst


import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File
import java.io.FileNotFoundException
import trees.bstree.BinarySearchTree


class JsonRepository(private val dirPath: String) {
    private val mapper = ObjectMapper().apply {
        registerModule(KotlinModule())
        enable(SerializationFeature.INDENT_OUTPUT)
    }
    fun getInt(treeName: String): BinarySearchTree<Int>? {

        val json = try {
            File(dirPath, "${treeName}.json").readText()
        } catch (_: FileNotFoundException) {
            return null
        }
        return mapper.readValue(json)
    }

    fun getStr(treeName: String): BinarySearchTree<String>? {

        val json = try {
            File(dirPath, "${treeName}.json").readText()
        } catch (_: FileNotFoundException) {
            return null
        }
        return mapper.readValue(json)
    }

    fun setInt(treeName: String, bst: BinarySearchTree<Int>){
        val serialized = mapper.writeValueAsString(bst)
        val file = File(dirPath, "${treeName}.json")
        file.writeText(serialized)
    }

    fun setStr(treeName: String, bst: BinarySearchTree<String>){
        val serialized = mapper.writeValueAsString(bst)
        val file = File(dirPath, "${treeName}.json")
        file.writeText(serialized)
    }

    fun getNames(): Any {
        val directory = File(dirPath)

        return if (directory.isDirectory) {
            directory.listFiles()?.filter { it.isFile }?.map { it.name } ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun remove(treeName: String) {
        File(dirPath, "${treeName}.json").delete()
    }
}