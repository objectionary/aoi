/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2022 Olesia Subbotina
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.objectionary.aoi.unit

import org.objectionary.aoi.TestBase
import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.ddr.graph.AttributesSetter
import org.objectionary.ddr.graph.CondAttributesSetter
import org.objectionary.ddr.graph.GraphBuilder
import org.objectionary.ddr.graph.InnerPropagator
import org.objectionary.ddr.graph.repr.Graph
import org.objectionary.ddr.sources.SrsTransformed
import org.objectionary.ddr.transform.XslTransformer
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

/**
 * Base for unit testing
 */
open class UnitTestBase : TestBase {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun doTest() {
        val path = getTestName()
        FreeAttributesHolder.storage.clear()
        val graph = GraphBuilder(
            SrsTransformed(constructInPath(path), XslTransformer(), "aoi").walk()).createGraph()
        CondAttributesSetter(graph).processConditions()
        AttributesSetter(graph).setAttributes()
        InnerPropagator(graph).propagateInnerAttrs()
        testSteps(graph)
        val out = ByteArrayOutputStream()
        printAttributes(out)
        val actual = String(out.toByteArray())
        val bufferedReader: BufferedReader = constructOutPath(path).toFile().bufferedReader()
        val expected = bufferedReader.use { it.readText() }
        logger.debug(actual)
        checkOutput(expected, actual)
        try {
            val tmpDir =
                Paths.get(
                    "${constructInPath(path).toString().replace('/', sep).substringBeforeLast(sep)}${sep}TMP"
                ).toString()
            FileUtils.deleteDirectory(File(tmpDir))
        } catch (e: Exception) {
            logger.error(e.printStackTrace().toString())
        }
    }

    /**
     * Steps to perform inside the test
     *
     * @param graph to be analyzed
     */
    @Suppress("EMPTY_BLOCK_STRUCTURE_ERROR")
    open fun testSteps(graph: Graph) {}

    /**
     * Prints attributes to output stream
     *
     * @param out output stream
     */
    protected open fun printAttributes(out: ByteArrayOutputStream) {
        FreeAttributesHolder.storage.forEach { attr ->
            out.writeBytes("ATTR: ${attr.name}".toByteArray())
            attr.appliedAttributes.forEach { out.writeBytes(it.name.toByteArray()) }
        }
    }

    override fun constructOutPath(directoryName: String): Path = Path.of("")
}
