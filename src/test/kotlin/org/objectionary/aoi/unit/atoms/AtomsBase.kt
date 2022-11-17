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

package org.objectionary.aoi.unit.atoms

import org.objectionary.aoi.data.FreeAtomAttribute
import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.aoi.process.AtomsProcessor
import org.objectionary.aoi.process.InnerUsageProcessor
import org.objectionary.aoi.process.InstanceUsageProcessor
import org.objectionary.aoi.unit.UnitTestBase
import org.objectionary.ddr.graph.name
import org.objectionary.ddr.graph.repr.Graph
import java.io.ByteArrayOutputStream
import java.io.File

/**
 * Base class for graph builder testing
 */
open class AtomsBase : UnitTestBase() {
    override fun testSteps(graph: Graph) {
        AtomsProcessor(graph).processAtoms()
        InnerUsageProcessor(graph).processInnerUsages()
        InstanceUsageProcessor(graph).processInstanceUsages()
    }

    override fun constructOutPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}unit${sep}out${sep}atoms$sep$directoryName.txt")
        ).absolutePath.replace("/", File.separator)

    override fun constructInPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}unit${sep}in${sep}atoms$sep$directoryName")
        ).absolutePath.replace("/", File.separator)

    override fun printAttributes(out: ByteArrayOutputStream) {
        FreeAttributesHolder.storage.forEach { attr ->
            out.writeBytes("ATTR (${if (attr is FreeAtomAttribute) "atom" else "not atom"}): _${name(attr.holderObject)}.${attr.name}_".toByteArray())
            attr.appliedAttributes.forEach { out.writeBytes(it.name.toByteArray()) }
        }
    }
}
