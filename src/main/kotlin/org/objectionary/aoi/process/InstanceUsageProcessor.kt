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

import org.objectionary.aoi.data.FreeAttribute
import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.aoi.data.Parameter
import org.objectionary.ddr.graph.repr.Graph
import org.objectionary.ddr.util.containsAttr
import org.objectionary.ddr.util.findRef
import org.objectionary.ddr.util.getAttrContent
import org.w3c.dom.Node

/**
 * Processes usages of free attributes that are reached via instances of objects
 */
class InstanceUsageProcessor(private val graph: Graph) {
    /**
     * Processes instance usages
     */
    fun processInstanceUsages() {
        graph.initialObjects.forEach { obj ->
            if (obj.getAttrContent("base")?.startsWith(".") == true) {
                val parentAbstract =
                    findRef(obj.previousSibling.previousSibling, graph.initialObjects, graph) ?: return@forEach
                val igAbstract = graph.igNodes.find { it.name == parentAbstract.getAttrContent("name") && it.body == parentAbstract }
                if (igAbstract != null && isParameter(parentAbstract, obj.getAttrContent("base")?.removePrefix("."))) {
                    val application = obj.nextSibling.nextSibling
                    if (application.getAttrContent("base")?.startsWith(".") == true) {
                        FreeAttributesHolder.storage
                            .find {
                                it.name == obj.getAttrContent("base")!!.removePrefix(".") && it.holderObject == parentAbstract
                            }
                            ?: FreeAttributesHolder.storage.add(
                                FreeAttribute(
                                    obj.getAttrContent("base")!!.removePrefix("."),
                                    parentAbstract
                                )
                            )
                        FreeAttributesHolder.storage.find {
                            it.name == obj.getAttrContent("base")!!.removePrefix(".") && it.holderObject == parentAbstract
                        }?.appliedAttributes?.add(Parameter(application.getAttrContent("base")!!))
                    }
                }
            }
        }
    }

    private fun isParameter(node: Node, param: String?): Boolean {
        val children = node.childNodes
        for (i in 0..children.length) {
            children.item(i)?.let { ch ->
                if ((ch.containsAttr("line") && ch.containsAttr("name") && !ch.containsAttr("base")) || ch.attributes == null) {
                    if (ch.getAttrContent("name") == param) {
                        return true
                    }
                } else {
                    return false
                }
            }
        }
        return false
    }
}
