package org.objectionary.aoi.unit

import org.apache.commons.io.FileUtils
import org.objectionary.aoi.TestBase
import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.aoi.process.InnerUsageProcessor
import org.objectionary.ddr.graph.AttributesSetter
import org.objectionary.ddr.graph.CondAttributesSetter
import org.objectionary.ddr.graph.InnerPropagator
import org.objectionary.ddr.graph.repr.Graph
import org.objectionary.ddr.launch.buildGraph
import org.objectionary.ddr.launch.documents
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Paths

open class UnitTestBase : TestBase {

    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun doTest() {
        val path = getTestName()
        documents.clear()
        FreeAttributesHolder.storage.clear()
        val graph = buildGraph(constructInPath(path))
        CondAttributesSetter(graph).processConditions()
        val attributesSetter = AttributesSetter(graph)
        attributesSetter.setDefaultAttributes()
        attributesSetter.pushAttributes()
        val innerPropagator = InnerPropagator(graph)
        innerPropagator.propagateInnerAttrs()
        testSteps(graph)
        val out = ByteArrayOutputStream()
        printAttributes(out)
        val actual = String(out.toByteArray())
        val bufferedReader: BufferedReader = File(constructOutPath(path)).bufferedReader()
        val expected = bufferedReader.use { it.readText() }
        logger.debug(actual)
        checkOutput(expected, actual)
        try {
            val tmpDir =
                Paths.get("${constructInPath(path).replace('/', sep).substringBeforeLast(sep)}${sep}TMP").toString()
            FileUtils.deleteDirectory(File(tmpDir))
        } catch (e: Exception) {
            logger.error(e.printStackTrace().toString())
        }
    }

    open fun testSteps(graph: Graph) {}

    private fun printAttributes(out: ByteArrayOutputStream) {
        FreeAttributesHolder.storage.forEach { attr ->
            out.writeBytes("ATTR: ${attr.name}".toByteArray())
            attr.appliedAttributes.forEach { out.writeBytes(it.name.toByteArray()) }
        }
    }

    override fun constructOutPath(directoryName: String) = ""

}