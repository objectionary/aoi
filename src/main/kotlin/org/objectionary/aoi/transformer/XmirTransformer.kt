package org.objectionary.aoi.transformer

import org.objectionary.ddr.graph.base
import org.objectionary.ddr.graph.line
import org.objectionary.ddr.graph.packageName
import org.objectionary.ddr.graph.pos
import org.objectionary.ddr.graph.repr.Graph
import org.objectionary.ddr.transform.XslTransformer
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

class XmirTransformer(
    private val graph: Graph,
    private val documents: MutableMap<Document, String>
) {
    fun addAoiSection() {
        documents.forEach { doc ->
            val program = doc.key.getElementsByTagName("program").item(0)
            val aoiChild: Element = doc.key.createElement("aoi")

            program.appendChild(aoiChild)
        }
        transformDocuments()
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