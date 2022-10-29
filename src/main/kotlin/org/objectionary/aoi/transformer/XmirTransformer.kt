package org.objectionary.aoi.transformer

import org.objectionary.aoi.data.FreeAttributesHolder
import org.objectionary.ddr.graph.name
import org.objectionary.ddr.graph.repr.Graph
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerException
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * Transforms xmir documents by adding an aoi section to each of them
 */
class XmirTransformer(
    private val graph: Graph,
    private val documents: MutableMap<Document, String>
) {
    /**
     * Aggregates the process of adding an aoi section to xmir documents
     */
    fun addAoiSection() {
        documents.forEach { doc ->
            val program = doc.key.getElementsByTagName("program").item(0)
            val aoiChild: Element = doc.key.createElement("aoi")
            addAoiChildren(aoiChild)
            program.appendChild(aoiChild)
        }
        transformDocuments()
    }

    @Suppress("CUSTOM_LABEL")
    private fun addAoiChildren(parent: Element) {
        FreeAttributesHolder.storage
            .filter { it.holderObject.ownerDocument == parent.ownerDocument }
            .forEach { el ->
                val obj = parent.ownerDocument.createElement("obj")
                val fqn = getFqn(el.name, el.holderObject)
                obj.setAttribute("fqn", fqn)
                val inferred: Element = parent.ownerDocument.createElement("inferred")
                graph.igNodes.filter { node ->
                    node.name ?: return@filter false
                    for (attr in el.appliedAttributes) {
                        // @todo #14:30min differentiate not only by name but also by the number of parameters
                        node.attributes.find { it.name == attr.name.substring(1) } ?: return@filter false
                    }
                    return@filter true
                }.forEach {
                    val element = parent.ownerDocument.createElement("obj")
                    val name = getFqn(it.name!!, it.body.parentNode)
                    element.setAttribute("fqn", name)
                    inferred.appendChild(element)
                }
                obj.appendChild(inferred)
                parent.appendChild(obj)
            }
    }

    private fun getFqn(name: String, par: Node): String {
        var fqn = name
        var parent = par
        while (name(parent) != null) {
            fqn = "${name(parent)}.$fqn"
            parent = parent.parentNode
        }
        return fqn
    }

    private fun transformDocuments() {
        documents.forEach { doc ->
            val outputStream = FileOutputStream(doc.value)
            outputStream.use { writeXml(it, doc.key) }
        }
    }

    /**
     * Writes transformed [document] to [output]
     *
     * @param document transformed document
     * @param output where to write the result
     */
    @Throws(TransformerException::class, UnsupportedEncodingException::class)
    private fun writeXml(output: OutputStream, document: Document) {
        val prettyPrintXlst = this.javaClass.getResourceAsStream("pretty_print.xslt")
        val transformer = TransformerFactory.newInstance().newTransformer(StreamSource(prettyPrintXlst))
        transformer.setOutputProperty(OutputKeys.INDENT, "yes")
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no")
        val source = DOMSource(document)
        val result = StreamResult(output)
        transformer.transform(source, result)
    }
}
