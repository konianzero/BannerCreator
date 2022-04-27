import java.io.File
import java.net.URI

class BannerCreator(private val name: String,
                    private val status: String,
                    private val nameFont: AsciiFont,
                    private val statusFont: AsciiFont = nameFont,
                    private val border: String = "8") {

    fun create() {
        val name: List<String> = convertStringToFont(name, nameFont)
        val status: List<String> = convertStringToFont(status, statusFont)

        var nameLength: Int = name[0].length
        var statusLength: Int = status[0].length

        val nameLines: List<String> = if (nameLength < statusLength) {
            val leftOffset = (statusLength - nameLength) / 2
            val rightOffset = if ((statusLength - nameLength) % 2 == 0) leftOffset else leftOffset + 1
            List(name.size) { "${" ".repeat(leftOffset)}${name[it]}${" ".repeat(rightOffset)}" }
        } else {
            name
        }
        nameLength = nameLines[0].length

        val statusLines: List<String> = List(status.size) {
            val leftOffset = (nameLength - statusLength) / 2
            val rightOffset = nameLength - (statusLength + leftOffset)
            " ".repeat(leftOffset) + status[it] + " ".repeat(rightOffset)
        }
        statusLength = statusLines[0].length

        val borderGap = (border.length + 1 + 2) * 2
        val borderLength = (nameLength + borderGap)

        println(border.repeat(borderLength))
        nameLines.forEach {
            println("$border$border  $it  $border$border")
        }
        statusLines.forEach {
            println("$border$border  $it  $border$border")
        }
        println(border.repeat(borderLength))
    }

    private fun convertStringToFont(string: String, font: AsciiFont): List<String> {
        return List(font.size) {
            val str = StringBuilder()
            for (char in string.toCharArray()) {
                str.append(font.dictionary[char]?.get(it).toString())
            }
            str.toString()
        }
    }
}

class AsciiFont(fontPath: String) {
    val dictionary = hashMapOf<Char, List<String>?>()
    val size: Int
    private val numberOfChars: Int

    init {
        var lineNum = 0
        var space = 10
        var char = ' '
        val charSequence = mutableListOf<String>()

        val font = resourceAsURI(fontPath).let { File(it).bufferedReader() }
        val fontMetaData = font.readLine().split(" ").map { it.toInt() }
        size = fontMetaData[0]
        numberOfChars = fontMetaData[1]

        while (true) {
            val string = font.readLine() ?: break

            if (lineNum % (size + 1) == 0) {
                char = string.split(" ")[0].first()
                if ('a' == char) {
                    space = string.split(" ")[1].toInt()
                }
            }

            repeat(size) {
                charSequence.add(font.readLine())
            }
            dictionary[char] = charSequence.toList()
            charSequence.clear()

            lineNum += size + 1
        }

        dictionary[' '] = MutableList(size) { " ".repeat(space) }
    }

    private fun resourceAsURI(path: String): URI? = object {}.javaClass.getResource(path)?.toURI()
}

fun main() {
    val roman = AsciiFont("roman.txt")
    val medium = AsciiFont("medium.txt")

    print("Enter project name: ")
    val name = readln()

    print("Enter short description: ")
    val status = readln()

    val bannerCreator = BannerCreator(name, status, roman, medium)
    bannerCreator.create()
}