package org.robok.amixc

import java.io.File
import org.robok.amix.Amix

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    println("Please provide one or more file paths as arguments.")
    return
  }

  args.forEach { amixFilePath ->
    val amixFile = File(amixFilePath)
    if (!amixFile.exists()) {
      println("File not found: $amixFilePath")
      continue
    }

    val reader = amixFile.bufferedReader()
    val amixFileLines = reader.readLines()
    val amixCode = amixFileLines.joinToString("\n")

    val outputFolder = File("output/")
    outputFolder.mkdirs()

    generateXmlFromAmix(
      amixCode = amixCode,
      onGenerateCode = { code, config ->
        val xmlFile = File(outputFolder, "${amixFile.nameWithoutExtension}.xml")
        xmlFile.writeText(code)
      },
      onError = { error ->
        println(error)
      }
    )
  }
}

fun generateXmlFromAmix(
  onGenerateCode: Amix.OnGenerateCode,
  onError: Amix.OnError,
  amixCode: String
): String {
  var generatedXml: String? = null
  val amix = Amix.Builder(context)
    .setUseComments(false)
    .setCode(amixCode)
    .setOnGenerateCode(onGenerateCode)
    .setOnError(onError)
    .create()
  amix.compile()
  return generatedXml ?: "Failed to generate XML"
}