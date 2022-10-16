package org.objectionary.oi.launch

import org.objectionary.ddr.graph.AttributesSetter
import org.objectionary.ddr.graph.CondAttributesSetter
import org.objectionary.ddr.graph.InnerPropagator
import org.objectionary.ddr.launch.buildGraph
import org.objectionary.ddr.launch.documents
import org.slf4j.LoggerFactory
import java.io.File

private val logger = LoggerFactory.getLogger("org.objectionary.oi.launch.Launcher")
private val sep = File.separatorChar

fun launch(path: String) {
    documents.clear()
    val graph = buildGraph(path, false)
    CondAttributesSetter(graph).processConditions()
    val attributesSetter = AttributesSetter(graph)
    attributesSetter.setDefaultAttributes()
    attributesSetter.pushAttributes()
    val innerPropagator = InnerPropagator(graph)
    innerPropagator.propagateInnerAttrs()
}
