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
import org.objectionary.ddr.graph.base
import org.objectionary.ddr.graph.findRef
import org.objectionary.ddr.graph.line
import org.objectionary.ddr.graph.name
import org.objectionary.ddr.graph.repr.Graph
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
            if (base(obj)?.startsWith(".") == true) {
                val parentAbstract =
                    findRef(obj.previousSibling.previousSibling, graph.initialObjects, graph) ?: return@forEach
                val igAbstract = graph.igNodes.find { it.name == name(parentAbstract) && it.body == parentAbstract }
                if (igAbstract != null && isParameter(parentAbstract, base(obj)?.removePrefix("."))) {
                    val application = obj.nextSibling.nextSibling
                    if (base(application)?.startsWith(".") == true &&
                        FreeAttributesHolder.storage.find {
                            it.name == base(obj)!!.removePrefix(".") && it.holderObject == parentAbstract
                        } == null
                    ) {
                        FreeAttributesHolder.storage.add(
                            FreeAttribute(
                                base(obj)!!.removePrefix("."),
                                parentAbstract
                            )
                        )
                        FreeAttributesHolder.storage.find {
                            it.name == base(obj)!!.removePrefix(".") && it.holderObject == parentAbstract
                        }?.appliedAttributes?.add(Parameter(base(application)!!))
                    }
                }
            }
        }
    }

    private fun isParameter(node: Node, param: String?): Boolean {
        val children = node.childNodes
        for (i in 0..children.length) {
            children.item(i)?.let { ch ->
                if ((line(ch) != null && name(ch) != null && base(ch) == null) || ch.attributes == null) {
                    if (name(ch) == param) {
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
