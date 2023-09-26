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

import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.aoi.data.Parameter
import org.objectionary.ddr.graph.repr.Graph
import org.objectionary.ddr.util.findRef
import org.objectionary.ddr.util.getAttrContent

/**
 * Processes objects which are used to initialize free attributes
 */
class InitializationProcessor(private val graph: Graph) {
    /**
     * Processes objects which are used to initialize free attributes
     */
    fun processInitializations() {
        graph.initialObjects.forEach { obj ->
            if (graph.igNodes.map { it.name }.contains(obj.getAttrContent("base"))) {
                val abstract = findRef(obj, graph.initialObjects, graph) ?: return@forEach
                val children = obj.childNodes
                for (i in 0 until children.length) {
                    val ch = children.item(i)
                    val param = FreeAttributesHolder.storage.find { it.name == abstract.childNodes.item(i).getAttrContent("name") && it.holderObject == abstract } ?: continue
                    val ref = findRef(ch, graph.initialObjects, graph) ?: continue
                    val dgAbstract = graph.igNodes.find { it.body == ref } ?: continue
                    dgAbstract.attributes.forEach { param.appliedAttributes.add(Parameter(".${it.name}")) }
                }
            }
        }
    }
}
