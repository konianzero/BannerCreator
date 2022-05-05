import java.io.File
import java.net.URI
// TODO - Add kotlinx-cli

class BannerCreator(private val name: String,
                    private val description: String,
                    private val nameFont: AsciiFont,
                    private val descriptionFont: AsciiFont = nameFont,
                    private val borderChar: String = "8",
                    private val printToConsole: Boolean = false,
                    private val outputFileName: String = "banner.txt") {

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

        resourceAsURI(fontPath).let { File(it).bufferedReader() }.use { fontFile ->
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

    private fun resourceAsURI(path: String): URI = object {}.javaClass.getResource(path)!!.toURI()
}

fun main() {
    val roman = AsciiFont("roman.txt")
    val medium = AsciiFont("medium.txt")

    print("Enter project name: ")
    val name = readln()

    print("Enter short description: ")
    val description = readln()

    val bannerCreator = BannerCreator(name, description, roman, medium, printToConsole = true)
    bannerCreator.create()
    bannerCreator.writeToFile()
}
