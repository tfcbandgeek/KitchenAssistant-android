package jgappsandgames.me.save.utility

// Java
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.File
import java.io.FileReader
import java.io.FileWriter

// Android OS
import android.content.Context
import android.os.Environment

/**
 * FileUtility
 * Version 1.1.0 (3/14/18)
 *
 * Created by Joshua Garner on 2/26/2018.
 */
// Data --------------------------------------------------------------------------------------------
var use_external: Boolean = false

private var data_directory: File? = null
private var cache_directory: File? = null
private var external_directory: File? = null

private val PATH = "kitchenassistent"

// Management Methods ------------------------------------------------------------------------------
fun isFirstRun(): Boolean {
    // Loaded Has Yet to be Called
    if (data_directory == null) throw RuntimeException("Data Directory Should be Loaded Already")
    if (cache_directory == null) throw RuntimeException("Cache Directory Should be Loaded Already")

    // The Application Has Already Been Ran
    if (data_directory!!.isDirectory) return false

    // The Application Has Not Been Ran
    data_directory!!.mkdirs()
    cache_directory!!.mkdirs()
    return true
}

fun isFirstRun(file: File): Boolean {
    if (file.isDirectory) return false
    return true
}

fun loadFilepaths(context: Context) {
    data_directory = File(context.filesDir, PATH)
    cache_directory = File(context.cacheDir, PATH)
}

fun loadExternalFilepaths() {
    external_directory =  File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), PATH)
}

// Path Getter Methods -----------------------------------------------------------------------------
fun getInternalFilepath(): File {
    return data_directory!!
}

fun getCacheDirectory(): File {
    return cache_directory!!
}

fun getExternalFilepath(): File {
    if (external_directory == null) loadExternalFilepaths()
    return external_directory!!
}

fun getApplicationFilepath(): File {
    if (use_external) return getExternalFilepath()
    return getInternalFilepath()
}

// File Methods ------------------------------------------------------------------------------------
fun moveFolder(input: File, output: File) {
    output.deleteRecursively()
    input.copyRecursively(output)
    input.deleteRecursively()
}

fun copyFolder(input: File, output: File) {
    output.deleteRecursively()
    input.copyRecursively(output)
}

fun deleteFolder(input: File) {
    input.deleteRecursively()
}

/**
 * FileExists(File): Boolean
 *
 * Version 1.1.0
 *
 * Returns Whether or not the File Exists
 */
fun fileExists(file: File): Boolean {
    return file.exists()
}

// File Status Methods -----------------------------------------------------------------------------
fun openFile(file: File, path: String, key: Int): File {
    val output: File = File(file, path)

    if (File(file, path + ".temp").isFile) throw Exception("File Currently Being Edited")

    val holder = BufferedWriter(FileWriter(File(file, path + ".temp")))
    holder.write(key.toString())
    holder.flush()
    holder.close()
    return output
}

fun closeFile(file: File, path: String, key: Int): Boolean {
    val reader = BufferedReader(FileReader(File(file, path + "temp")))
    if (reader.readLine() == key.toString()) {
        File(file, path + ".temp").deleteOnExit()
        reader.close()
        return true
    }

    return false
}