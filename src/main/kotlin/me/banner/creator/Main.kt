package me.banner.creator

import kotlinx.cli.ArgParser
import kotlinx.cli.ArgType
import kotlinx.cli.default
import kotlinx.cli.required

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

    val nfPath: String
    val dfPath: String

    if (singleFont) {
        nfPath = nameFontPath
        dfPath = nfPath
    } else {
        nfPath = nameFontPath
        dfPath = descFontPath
    }

    val bannerCreator = BannerCreator(name, description, nfPath, dfPath, printToConsole = print, borderChar = border, outputFileName = output)
    bannerCreator.create()
    bannerCreator.writeToFile()
}
