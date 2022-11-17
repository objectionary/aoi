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

package org.objectionary.aoi.process

import org.objectionary.aoi.data.FreeAtomAttribute
import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.ddr.graph.abstract
import org.objectionary.ddr.graph.base
import org.objectionary.ddr.graph.line
import org.objectionary.ddr.graph.name
import org.objectionary.ddr.graph.repr.Graph
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilderFactory

/**
 * Class for processing atoms and their restrictions
 */
class AtomsProcessor(private val graph: Graph) {
    private val logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Processes atoms and their restrictions
     */
    @Suppress("MAGIC_NUMBER")
    fun processAtoms() {
        graph.initialObjects.forEach { obj ->
            obj.attributes?.getNamedItem("atom")?.textContent?.let { atom ->
                name(obj)?.let { name ->
                    val docObjects = getAtomsDocument()?.getElementsByTagName("atom") ?: return@forEach
                    val fqn = "$atom.$name"
                    for (i in 0 until docObjects.length) {
                        val atomNode = docObjects.item(i)
                        val atomName = atomNode.childNodes.item(1).textContent
                        if (fqn == atomName) {
                            val children = obj.childNodes ?: return@forEach
                            for (j in 0 until children.length) {
                                val ch = children.item(j)
                                if (ch.attributes == null || ch.attributes.length == 0) {
                                    continue
                                }
                                if (base(ch) == null && name(ch) != null && abstract(ch) == null &&
                                        (line(ch) == line(obj) || line(ch)?.toInt() == line(obj)?.toInt()?.plus(1))
                                ) {
                                    val freeAtomAttr = FreeAtomAttribute(name(ch)!!, obj)
                                    val objects = atomNode.childNodes.item(3).childNodes
                                    for (k in 0 until objects.length) {
                                        val objNode = objects.item(k)
                                        name(objNode)?.let { freeAtomAttr.atomRestrictions.add(it) }
                                    }
                                    FreeAttributesHolder.storage.add(freeAtomAttr)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getAtomsDocument(): Document? {
        val inputStream = this.javaClass.getResourceAsStream("atoms_desc.xml")
        try {
            val factory = DocumentBuilderFactory.newInstance()
            inputStream.use { return factory.newDocumentBuilder().parse(it) }
        } catch (e: Exception) {
            logger.error(e.printStackTrace().toString())
        }
        return null
    }
}
