package me.banner.creator

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required
import java.io.File
import java.io.InputStream
import java.nio.file.Path

class BannerCreator(private val name: String,
                    private val description: String,
                    private val nameFont: AsciiFont,
                    private val descriptionFont: AsciiFont,
                    private val borderChar: String,
                    private val printToConsole: Boolean,
                    private val outputFileName: String) {

    private val banner = mutableListOf<String>()

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
        return List(font.size) { listElementNum ->
            val fontBuilder = StringBuilder()
            string.toCharArray().forEach { char ->
                fontBuilder.append(font.dictionary[char]!![listElementNum])
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
}

class AsciiFont(fontPath: String) {
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

fun main(args: Array<String>) {
    val parser = ArgParser("Banner Creator")

    val name by parser.option(ArgType.String, shortName = "n", description = "Project name").required()
    val description by parser.option(ArgType.String, shortName = "d", description = "Project short description").required()
    val singleFont by parser.option(ArgType.Boolean, shortName = "sf", description = "Use one font for name and description (name font for default)").default(false)
    val nameFontPath by parser.option(ArgType.String, shortName = "nf", description = "Path to font file for Name") .default("font/roman.txt")
    val descFontPath by parser.option(ArgType.String, shortName = "df", description = "Path to font file for Description") .default("font/medium.txt")
    val print by parser.option(ArgType.Boolean, shortName = "p", description = "Print banner to console").default(false)
    val border by parser.option(ArgType.String, shortName = "b", description = "Border symbol").default("8")
    val output by parser.option(ArgType.String, shortName = "o", description = "Output file").default("banner.txt")

    parser.parse(args)

    val nameFont: AsciiFont
    val descriptionFont: AsciiFont

    if (singleFont) {
        nameFont = AsciiFont(nameFontPath)
        descriptionFont = nameFont
    } else {
        nameFont = AsciiFont(nameFontPath)
        descriptionFont = AsciiFont(descFontPath)
    }

    val bannerCreator = BannerCreator(name, description, nameFont, descriptionFont, printToConsole = print, borderChar = border, outputFileName = output)
    bannerCreator.create()
    bannerCreator.writeToFile()
}
