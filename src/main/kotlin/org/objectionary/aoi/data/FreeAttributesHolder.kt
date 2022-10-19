package org.objectionary.aoi.data

import org.w3c.dom.Node

object FreeAttributesHolder {
    val storage: MutableSet<FreeAttribute> = mutableSetOf()
}

data class FreeAttribute(val name: String, val holderObject: Node) {
    val appliedAttributes = mutableSetOf<Attribute>()
}

/**
 * Attribute representation.
 *
 * @param name attribute name
 */
data class Attribute(val name: String) {
    val parameters = mutableSetOf<String>()
}