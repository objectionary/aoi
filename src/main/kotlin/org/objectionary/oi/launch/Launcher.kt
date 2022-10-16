package org.objectionary.oi.launch

import org.objectionary.oi.transform.XslTransformer
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import java.io.File
import java.io.FileInputStream
import java.nio.file.Files
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory

private val logger = LoggerFactory.getLogger("org.objectionary.oi.launch.Launcher")
private val sep = File.separatorChar
val documents: MutableMap<Document, String> = mutableMapOf()

fun launch(path: String) {
    val transformer = XslTransformer()
    Files.walk(Paths.get(path))
        .filter(Files::isRegularFile)
        .forEach {
            val tmpPath = createTempDirectories(path, it.toString())
            transformer.transformXml(it.toString(), tmpPath)
            documents[getDocument(tmpPath)!!] = tmpPath
        }
}

/**
 * @param filename source xml filename
 * @return generated Document
 */
fun getDocument(filename: String): Document? {
    try {
        val factory = DocumentBuilderFactory.newInstance()
        FileInputStream(filename).use { return factory.newDocumentBuilder().parse(it) }
    } catch (e: Exception) {
        logger.error(e.printStackTrace().toString())
    }
    return null
}

private fun createTempDirectories(
    path: String,
    filename: String
): String {
    val tmpPath =
            "${path.substringBeforeLast(sep)}$sep${path.substringAfterLast(sep)}_ddr${filename.substring(path.length)}"
    val forDirs = File(tmpPath.substringBeforeLast(sep)).toPath()
    Files.createDirectories(forDirs)
    val newFilePath = Paths.get(tmpPath)
    try {
        Files.createFile(newFilePath)
    } catch (e: Exception) {
        logger.error(e.message)
    }
    return tmpPath
}
