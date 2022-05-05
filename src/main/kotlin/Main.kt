import java.io.File
import java.net.URI
// TODO - Add kotlinx-cli

class BannerCreator(private val name: String,
                    private val status: String,
                    private val nameFont: AsciiFont,
                    private val statusFont: AsciiFont = nameFont,
                    private val borderChar: String = "8",
                    private val printToConsole: Boolean = false,
                    private val outputFileName: String = "banner.txt") {

    private val banner = mutableListOf<String>()

    fun create() {
        var nameLines: List<String> = convertStringToFont(name, nameFont)
        var statusLines: List<String> = convertStringToFont(status, statusFont)

        var nameLength: Int = nameLines[0].length
        val statusLength: Int = statusLines[0].length

        nameLines = if (nameLength < statusLength) {
            val leftOffset = (statusLength - nameLength) / 2
            val rightOffset = if ((statusLength - nameLength) % 2 == 0) leftOffset else leftOffset + 1
            List(nameLines.size) { "${" ".repeat(leftOffset)}${nameLines[it]}${" ".repeat(rightOffset)}" }
        } else {
            nameLines
        }
        nameLength = nameLines[0].length

        statusLines = List(statusLines.size) {
            val leftOffset = (nameLength - statusLength) / 2
            val rightOffset = nameLength - (statusLength + leftOffset)
            " ".repeat(leftOffset) + statusLines[it] + " ".repeat(rightOffset)
        }

        val borderLength = nameLength + (borderChar.length + 3) * 2
        val border = borderChar.repeat(borderLength)

        banner.add(border)
        nameLines.forEach {
            banner.add("$borderChar$borderChar  $it  $borderChar$borderChar")
        }
        statusLines.forEach {
            banner.add("$borderChar$borderChar  $it  $borderChar$borderChar")
        }
        banner.add(border)

        if (printToConsole) {
            printToConsole()
        }
        writeToFile()
    }

    private fun convertStringToFont(string: String, font: AsciiFont): List<String> {
        return List(font.size) { listElement ->
            val fontBuilder = StringBuilder()
            string.toCharArray().forEach { char ->
                fontBuilder.append(font.dictionary[char]!![listElement])
            }
            fontBuilder.toString()
        }
    }

    private fun writeToFile() {
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
    val status = readln()

    val bannerCreator = BannerCreator(name, status, roman, medium, printToConsole = true)
    bannerCreator.create()
}