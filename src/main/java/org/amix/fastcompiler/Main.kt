package org.amix.fastcompiler

import java.io.File
import java.io.IOException
import org.amix.Amix

fun main(args: Array<String>) {
  val useVerticalRoot = parseUseVerticalRoot(args)
  val filePaths = args.dropWhile { it != "-verticalroot" }.drop(2)

  if (filePaths.isEmpty()) {
    println("Error: No file paths provided.")
    return
  }

  compile(filePaths.toFileList(), useVerticalRoot)
}

fun compile(files: List<File>, useVerticalRoot: Boolean) {
  files.forEach { amixFile ->
    try {
      val amixFileName = amixFile.nameWithoutExtension
      val amixFileCode = amixFile.readText()
      val xmlFileName = "${amixFileName}.xml"
      val xmlFile = File(amixFile.parent, xmlFileName)

      val amix =
        Amix.Builder()
          .setUseVerticalRoot(useVerticalRoot)
          .setCode(amixFileCode)
          .setOnGenerateCode { code, _ -> xmlFile.writeText(code) }
          .setOnError { error -> println("Error compiling ${amixFile.name}: $error") }
          .create()

      amix.compile()
      println("File ${amixFile.name} compiled successfully to ${xmlFile.absolutePath}")
    } catch (e: IOException) {
      println("Error processing file ${amixFile.name}: ${e.message}")
    }
  }
}

fun List<String>.toFileList(): List<File> {
  return this.map { File(it) }
}

fun parseUseVerticalRoot(args: Array<String>): Boolean {
  val index = args.indexOf("-verticalroot")
  return if (index != -1 && index + 1 < args.size) args[index + 1].toBooleanStrictOrNull() ?: false
  else false
}
