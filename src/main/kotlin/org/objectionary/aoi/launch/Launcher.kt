package org.objectionary.aoi.launch

import org.objectionary.aoi.process.InnerUsageProcessor
import org.objectionary.aoi.process.InstanceUsageProcessor
import org.objectionary.aoi.transformer.XmirTransformer
import org.objectionary.ddr.graph.AttributesSetter
import org.objectionary.ddr.graph.CondAttributesSetter
import org.objectionary.ddr.graph.InnerPropagator
import org.objectionary.ddr.launch.buildGraph
import org.objectionary.ddr.launch.documents
import org.slf4j.LoggerFactory
import java.io.File

private val logger = LoggerFactory.getLogger("org.objectionary.oi.launch.Launcher")
private val sep = File.separatorChar

/**
 * Aggregates the whole pipeline.
 *
 * @param path to input directory
 */
fun launch(path: String, gather: Boolean = false) {
    documents.clear()
    val graph = buildGraph(path, gather)
    CondAttributesSetter(graph).processConditions()
    val attributesSetter = AttributesSetter(graph)
    attributesSetter.setDefaultAttributes()
    attributesSetter.pushAttributes()
    val innerPropagator = InnerPropagator(graph)
    innerPropagator.propagateInnerAttrs()
    InnerUsageProcessor(graph).processInnerUsages()
    InstanceUsageProcessor(graph).processInstanceUsages()
    val transformer = XmirTransformer(graph, documents)
    transformer.addAoiSection()
}
