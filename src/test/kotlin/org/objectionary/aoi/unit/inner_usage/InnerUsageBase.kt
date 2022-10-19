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

package org.objectionary.aoi.unit.inner_usage

import TestBase
import org.apache.commons.io.FileUtils
import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.aoi.process.InnerUsageProcessor
import org.objectionary.ddr.graph.AttributesSetter
import org.objectionary.ddr.graph.CondAttributesSetter
import org.objectionary.ddr.graph.InnerPropagator
import org.objectionary.ddr.launch.buildGraph
import org.objectionary.ddr.launch.documents
import org.objectionary.ddr.launch.launch
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.ByteArrayOutputStream
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Base class for graph builder testing
 */
open class InnerUsageBase : TestBase {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun doTest() {
        val path = getTestName()
        documents.clear()
        val graph = buildGraph(constructInPath(path), false)
        CondAttributesSetter(graph).processConditions()
        val attributesSetter = AttributesSetter(graph)
        attributesSetter.setDefaultAttributes()
        attributesSetter.pushAttributes()
        val innerPropagator = InnerPropagator(graph)
        innerPropagator.propagateInnerAttrs()
        InnerUsageProcessor(graph).processInnerUsages()
        FreeAttributesHolder.storage
        println()
    }

    override fun constructOutPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}unit${sep}out$sep$directoryName")
        ).absolutePath.replace("/", File.separator)

    override fun constructInPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}unit${sep}in$sep$directoryName")
        ).absolutePath.replace("/", File.separator)
}
