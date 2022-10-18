package org.objectionary.aoi.data

import org.w3c.dom.Node

class FreeAttributesHolder {
    val storage: MutableList<FreeAttribute> = mutableListOf()
}

data class FreeAttribute(val name: String, val holderObject: Node) {

}