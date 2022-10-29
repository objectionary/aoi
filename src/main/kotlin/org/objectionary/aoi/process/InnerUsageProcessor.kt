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
import org.objectionary.ddr.graph.line
import org.objectionary.ddr.graph.name
import org.objectionary.ddr.graph.repr.Graph

/**
 * Processes usages of free attributes that occur inside the parent object body
 */
class InnerUsageProcessor(private val graph: Graph) {
    /**
     * Processes inner usages
     */
    fun processInnerUsages() {
        graph.igNodes.forEach { obj ->
            val xmirNode = obj.body
            val children = xmirNode.childNodes
            for (i in 0..children.length) {
                val ch = children.item(i)
                if (base(ch) == null && name(ch) != null && line(ch) == line(xmirNode)) {
                    FreeAttributesHolder.storage.add(FreeAttribute(name(ch)!!, xmirNode))
                }
                base(ch)?.let {
                    FreeAttributesHolder.storage.find { it.name == base(ch) && it.holderObject == xmirNode }
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
