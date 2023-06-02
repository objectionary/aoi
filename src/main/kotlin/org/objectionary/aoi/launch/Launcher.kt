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

package org.objectionary.aoi.launch

import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.aoi.process.AtomsProcessor
import org.objectionary.aoi.process.InnerUsageProcessor
import org.objectionary.aoi.process.InstanceUsageProcessor
import org.objectionary.aoi.transformer.XmirTransformer
import org.objectionary.ddr.graph.AttributesSetter
import org.objectionary.ddr.graph.CondAttributesSetter
import org.objectionary.ddr.graph.GraphBuilder
import org.objectionary.ddr.graph.InnerPropagator
import org.objectionary.ddr.sources.SrsTransformed
import org.objectionary.ddr.transform.XslTransformer
import org.slf4j.LoggerFactory
import java.io.File

/**
 * Aggregates the whole pipeline.
 *
 * @param path to input directory
 */
fun launch(path: String) {
    FreeAttributesHolder.storage.clear()
    val documents = SrsTransformed(path, XslTransformer(), "aoi", false).walk()
    val graph = GraphBuilder(documents).createGraph()
    CondAttributesSetter(graph).processConditions()
    AttributesSetter(graph).setAttributes()
    AtomsProcessor(graph).processAtoms()
    InnerPropagator(graph).propagateInnerAttrs()
    InnerUsageProcessor(graph).processInnerUsages()
    InstanceUsageProcessor(graph).processInstanceUsages()
    XmirTransformer(graph, documents).addAoiSection()
}
