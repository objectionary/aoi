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

package org.objectionary.aoi.data

import org.w3c.dom.Node

/**
 * Storage for free attributes
 */
object FreeAttributesHolder {
    /**
     * Set of free attributes to be analyzed
     */
    val storage: MutableSet<FreeAttribute> = mutableSetOf()
}

/**
 * Representation of a free attribute
 *
 * @property name attribute name
 * @property holderObject parent object body
 */
data class FreeAttribute(val name: String, val holderObject: Node) {
    /**
     * Set of the attributes of this free attribute that were applied in the program
     */
    val appliedAttributes: MutableSet<Parameter> = mutableSetOf()
}

/**
 * Attribute representation.
 *
 * @property name attribute name
 */
data class Parameter(val name: String) {
    /**
     * Free attributes which this attribute uses (not used yet)
     */
    val parameters: MutableSet<String> = mutableSetOf()
}
