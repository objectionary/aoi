package org.objectionary.aoi.process

import org.objectionary.ddr.graph.repr.Graph

class AtomsProcessor(private val graph: Graph) {
    /**
     * Processes atoms
     */
    fun processAtoms() {
        graph.initialObjects.forEach { obj ->
            obj.attributes?.getNamedItem("atom")?.textContent?.let {

            }
        }
    }
}