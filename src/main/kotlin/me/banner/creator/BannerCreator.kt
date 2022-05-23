package me.banner.creator

import java.io.File
import java.io.InputStream
import java.nio.file.Path

internal class BannerCreator(private val name: String,
                             private val description: String,
                             private val nameFontPath: String,
                             private val descriptionFontPath: String,
                             private val borderChar: String,
                             private val printToConsole: Boolean,
                             private val outputFileName: String) {

    private val banner = mutableListOf<String>()
    private val nameFont = AsciiFont(nameFontPath)
    private val descriptionFont = AsciiFont(descriptionFontPath)

    fun create() {
        var nameLines: List<String> = convertStringToFont(name, nameFont)
        var descriptionLines: List<String> = convertStringToFont(description, descriptionFont)

        var nameLength: Int = nameLines[0].length
        val descriptionLength: Int = descriptionLines[0].length

        nameLines = if (nameLength < descriptionLength) {
            val leftOffset = (descriptionLength - nameLength) / 2
            val rightOffset = if ((descriptionLength - nameLength) % 2 == 0) leftOffset else leftOffset + 1
            List(nameLines.size) { "${" ".repeat(leftOffset)}${nameLines[it]}${" ".repeat(rightOffset)}" }
        } else {
            nameLines
        }
        nameLength = nameLines[0].length

        descriptionLines = List(descriptionLines.size) {
            val leftOffset = (nameLength - descriptionLength) / 2
            val rightOffset = nameLength - (descriptionLength + leftOffset)
            " ".repeat(leftOffset) + descriptionLines[it] + " ".repeat(rightOffset)
        }

        val borderLength = nameLength + (borderChar.length + 3) * 2
        val border = borderChar.repeat(borderLength)

        banner.add(border)
        nameLines.forEach {
            banner.add("$borderChar$borderChar  $it  $borderChar$borderChar")
        }
        descriptionLines.forEach {
            banner.add("$borderChar$borderChar  $it  $borderChar$borderChar")
        }
        banner.add(border)

        if (printToConsole) {
            printToConsole()
        }
    }

    private fun convertStringToFont(string: String, font: AsciiFont): List<String> {
        return List(font.size) { elementNum ->
            val fontBuilder = StringBuilder()
            string.toCharArray().forEach { char ->
                fontBuilder.append(font.dictionary[char]!![elementNum])
            }
            fontBuilder.toString()
        }
    }

    fun writeToFile() {
        File(outputFileName).bufferedWriter().use { writer ->
            writer.appendLine()
            banner.forEach { writer.appendLine(it) }
        }
    }

    private fun printToConsole() {
        banner.forEach { println(it) }
    }

    private class AsciiFont(fontPath: String) {
        val dictionary = hashMapOf<Char, List<String>>()
        val size: Int

        init {
            val charSequence = mutableListOf<String>()
            var lineNum = 0
            var space = 10
            var char = ' '

            asInputStream(fontPath).bufferedReader().use { fontFile ->
                val fontMetaData = fontFile.readLine().split(" ").map { it.toInt() }
                size = fontMetaData[0]

                while (true) {
                    val line = fontFile.readLine() ?: break

                    if (lineNum % (size + 1) == 0) {
                        val charMetaData = line.split(" ")
                        char = charMetaData[0].first()
                        if ('a' == char) {
                            space = charMetaData[1].toInt()
                        }
                    }

                    repeat(size) {
                        charSequence.add(fontFile.readLine())
                    }
                    dictionary[char] = charSequence.toList()
                    charSequence.clear()

                    lineNum += size + 1
                }
            }

            dictionary[' '] = List(size) { " ".repeat(space) }
        }

        private fun asInputStream(path: String): InputStream {
            if (Path.of(path).isAbsolute) {
                return File(path).inputStream()
            }
            return object {}.javaClass.classLoader.getResourceAsStream(path)!!
        }
    }
}