package database.json


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
    fun getTreeWithIntKey(treeName: String): BinarySearchTree<Int, String>? {

        val json = try {
            File(dirPath, "${treeName}.json").readText()
        } catch (_: FileNotFoundException) {
            return null
        }
        return mapper.readValue(json)
    }

    fun getTreeWithStringKey(treeName: String): BinarySearchTree<String, String>? {

        val json = try {
            File(dirPath, "${treeName}.json").readText()
        } catch (_: FileNotFoundException) {
            return null
        }
        return mapper.readValue(json)
    }

    fun setTreeWithIntKey(treeName: String, bst: BinarySearchTree<Int, String>){
        val serialized = mapper.writeValueAsString(bst)
        val file = File(dirPath, "${treeName}.json")
        file.writeText(serialized)
    }

    fun setTreeWithStringKey(treeName: String, bst: BinarySearchTree<String, String>){
        val serialized = mapper.writeValueAsString(bst)
        val file = File(dirPath, "${treeName}.json")
        file.writeText(serialized)
    }

    fun getNamesTrees(): Any {
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