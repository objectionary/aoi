package org.objectionary.aoi.process

import org.objectionary.aoi.data.FreeAtomAttribute
import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.ddr.graph.abstract
import org.objectionary.ddr.graph.base
import org.objectionary.ddr.graph.line
import org.objectionary.ddr.graph.name
import org.objectionary.ddr.graph.repr.Graph
import org.slf4j.LoggerFactory
import org.w3c.dom.Document
import javax.xml.parsers.DocumentBuilderFactory

class AtomsProcessor(private val graph: Graph) {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    /**
     * Processes atoms and their restrictions
     */
    fun processAtoms() {
        graph.initialObjects.forEach { obj ->
            obj.attributes?.getNamedItem("atom")?.textContent?.let { atom ->
                name(obj)?.let {name ->
                    val docObjects = getAtomsDocument()?.getElementsByTagName("atom") ?: return@forEach
                    val fqn = "${atom}.$name"
                    for (i in 0 until docObjects.length) {
                        val atomNode = docObjects.item(i)
                        val atomName = atomNode.childNodes.item(0).nodeName
                        if (fqn == atomName) {
                            val children = obj.childNodes ?: return@forEach
                            for (j in 0 until children.length) {
                                val ch = children.item(j)
                                if (ch.attributes == null || ch.attributes.length == 0) {
                                    continue
                                }
                                if (base(ch) == null && name(ch) != null && abstract(ch) == null &&
                                    (line(ch) == line(obj) || line(ch)?.toInt() == line(obj)?.toInt()?.plus(1))
                                ) {
                                    val freeAtomAttr = FreeAtomAttribute(name(ch)!!, obj)
                                    val objects = atomNode.childNodes.item(1).childNodes
                                    for (k in 0 until objects.length) {
                                        val oNode = objects.item(k)
                                        name(oNode)?.let { freeAtomAttr.atomRestrictions.add(it) }
                                    }
                                    FreeAttributesHolder.storage.add(freeAtomAttr)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getAtomsDocument(): Document? {
        val inputStream = this.javaClass.getResourceAsStream("atoms_desc.xml")
        try {
            val factory = DocumentBuilderFactory.newInstance()
            inputStream.use { return factory.newDocumentBuilder().parse(it) }
        } catch (e: Exception) {
            logger.error(e.printStackTrace().toString())
        }
        return null
    }
}