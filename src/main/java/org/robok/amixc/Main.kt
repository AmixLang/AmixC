package org.robok.amixc

import org.robok.amix.Amix
import org.robok.amix.config.Config
import java.io.File
import java.io.IOException

fun main(args: Array<String>) {
  if (args.isEmpty()) {
    println("Please provide one or more file paths as arguments.")
    return
  }

  compile(args.toList().toFileList())
}

fun compile(files: List<File>) {
  files.forEach { amixFile ->
    try {
      val amixFileName = amixFile.nameWithoutExtension
      val amixFileCode = amixFile.readText()
      val xmlFileName = "${amixFileName}.xml"
      val xmlFile = File(amixFile.parent, xmlFileName)

      val amix = Amix.Builder()
        .setCode(amixFileCode)
        .setOnGenerateCode(object : Amix.OnGenerateCode {
          override fun call(generatedCode: String, config: Config) {
            xmlFile.writeText(generatedCode)
          }
        })
        .setOnError(object : Amix.OnError {
          override fun call(error: String) {
            println("Error compiling ${amixFile.name}: $error")
          }
        })
        .create()
        amix.compile()
    } catch (e: IOException) {
      println("Error processing file ${amixFile.name}: ${e.message}")
    }
  }
}

fun List<String>.toFileList(): List<File> {
  val list = mutableListOf<File>()
  this.forEach {
    list.add(File(it))
  }
  return list
}