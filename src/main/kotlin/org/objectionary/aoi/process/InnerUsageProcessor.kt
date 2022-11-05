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
import org.objectionary.ddr.graph.abstract
import org.objectionary.ddr.graph.base
import org.objectionary.ddr.graph.line
import org.objectionary.ddr.graph.name
import org.objectionary.ddr.graph.repr.Graph
import org.w3c.dom.Node

/**
 * Processes usages of free attributes that occur inside the parent object body
 */
class InnerUsageProcessor(private val graph: Graph) {
    /**
     * Processes inner usages
     */
    fun processInnerUsages() {
        graph.igNodes.forEach { obj ->
            deepTraversal(obj.body, obj.body)
        }
    }

    private fun deepTraversal(node: Node, origNode: Node) {
        if (node.childNodes.length == 0 || (graph.igNodes.find { it.body == node } != null && graph.igNodes.find { it.body == node }?.body != origNode)) {
            return
        } else {
            val children = node.childNodes
            for (i in 0 until children.length) {
                val ch = children.item(i)
                if (ch.attributes == null || ch.attributes.length == 0) {
                    continue
                }
                deepTraversal(ch, origNode)
                if (base(ch) == null && name(ch) != null && abstract(ch) == null
                    && (line(ch) == line(node) || line(ch)?.toInt() == line(node)?.toInt()?.plus(1))
                ) {
                    FreeAttributesHolder.storage.add(FreeAttribute(name(ch)!!, origNode))
                }
                base(ch)?.let {
                    FreeAttributesHolder.storage.find { it.name == base(ch) && it.holderObject == origNode }
                        ?.let { fa ->
                            base(ch.nextSibling.nextSibling)?.let {
                                fa.appliedAttributes.add(Parameter(it))
                            }
                        }
                }
            }
        }
    }
}
