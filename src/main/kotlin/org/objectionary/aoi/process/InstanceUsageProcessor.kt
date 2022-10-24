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
                    if (base(application)?.startsWith(".") == true) {
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
            val ch = children.item(i)
            if ((line(ch) != null && name(ch) != null && base(ch) == null) || ch.attributes == null) {
                if (name(ch) == param) {
                    return true
                }
            } else {
                return false
            }
        }
        return false
    }
}
