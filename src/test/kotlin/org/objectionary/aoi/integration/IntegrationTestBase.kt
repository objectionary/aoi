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

package org.objectionary.aoi.integration

import org.objectionary.aoi.TestBase
import org.objectionary.aoi.launch.launch
import org.objectionary.ddr.launch.documents
import org.apache.commons.io.FileUtils
import org.slf4j.LoggerFactory
import java.io.BufferedReader
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Base class for testing decorators resolver
 */
open class IntegrationTestBase : TestBase {
    private val logger = LoggerFactory.getLogger(this.javaClass.name)

    override fun doTest() {
        val path = getTestName()
        documents.clear()
        launch(constructInPath(path))
        val actualFiles: MutableList<String> = mutableListOf()
        Files.walk(Paths.get(constructOutPath(path)))
            .filter(Files::isRegularFile)
            .forEach { actualFiles.add(it.toString()) }
        Files.walk(Paths.get(constructResultPath(path)))
            .filter(Files::isRegularFile)
            .forEach { file ->
                val actualBr: BufferedReader = File(file.toString()).bufferedReader()
                val actual = actualBr.use { it.readText() }.replace(" ", "")
                val expectedFile = actualFiles.find {
                    it.replace("out$sep", "in$sep").replaceFirst(path, "${path}_ddr") == file.toString()
                }
                val expectedBr: BufferedReader = File(expectedFile.toString()).bufferedReader()
                val expected = expectedBr.use { it.readText() }.replace(" ", "")
                checkOutput(expected, actual)
            }
        try {
            val tmpDir =
                Paths.get((constructResultPath(path))).toString()
            FileUtils.deleteDirectory(File(tmpDir))
        } catch (e: Exception) {
            logger.error(e.printStackTrace().toString())
        }
    }

    override fun constructOutPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}integration${sep}out$sep$directoryName")
        ).absolutePath.replace("/", File.separator)

    override fun constructInPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}integration${sep}in$sep$directoryName")
        ).absolutePath.replace("/", File.separator)

    private fun constructResultPath(directoryName: String): String =
        File(System.getProperty("user.dir")).resolve(
            File("src${sep}test${sep}resources${sep}integration${sep}in$sep${directoryName}_ddr")
        ).absolutePath.replace("/", File.separator)
}
