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

package org.objectionary.aoi.unit.init

import org.objectionary.aoi.process.InitializationProcessor
import org.objectionary.aoi.process.InnerUsageProcessor
import org.objectionary.aoi.process.InstanceUsageProcessor
import org.objectionary.aoi.unit.UnitTestBase
import org.objectionary.ddr.graph.repr.Graph
import java.io.File
import java.nio.file.Path

/**
 * Base class for graph builder testing
 */
open class InitBase : UnitTestBase() {
    override fun testSteps(graph: Graph) {
        InnerUsageProcessor(graph).processInnerUsages()
        InstanceUsageProcessor(graph).processInstanceUsages()
        InitializationProcessor(graph).processInitializations()
    }

    override fun constructOutPath(directoryName: String): Path =
        Path.of(File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}unit${sep}out${sep}init$sep$directoryName.txt")
        ).absolutePath.replace("/", File.separator))

    override fun constructInPath(directoryName: String): Path =
        Path.of(File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}unit${sep}in${sep}init$sep$directoryName")
        ).absolutePath.replace("/", File.separator))
}
