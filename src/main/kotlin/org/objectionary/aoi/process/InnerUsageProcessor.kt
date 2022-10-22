package org.objectionary.aoi.process

import org.objectionary.aoi.data.Parameter
import org.objectionary.aoi.data.FreeAttribute
import org.objectionary.aoi.data.FreeAttributesHolder
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
        graph.igNodes.forEach {obj ->
            val xmirNode = obj.body
            val children = xmirNode.childNodes
            for (i in 0..children.length) {
                val ch = children.item(i)
                if (base(ch) == null && name(ch) != null && line(ch) == line(xmirNode)) {
                    FreeAttributesHolder.storage.add(FreeAttribute(name(ch)!!, xmirNode))
                }
                base(ch)?.let {
                    FreeAttributesHolder.storage.find { it.name == base(ch) && it.holderObject == xmirNode }?.let { fa ->
                        base(ch.nextSibling.nextSibling)?.let {
                            fa.appliedAttributes.add(Parameter(it))
                        }
                    }
                }
            }
        }
    }
}
