package com.craftinginterpreters.lox

import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*


object Lox {
    @Throws(IOException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        if (args.size > 1) {
            println("Usage: klox [script]")
            Runtime.getRuntime().exit(64)
        } else if (args.size == 1) {
            runFile(args[0])
        } else {
            runPrompt()
        }
    }

    @Throws(IOException::class)
    @JvmStatic
    private fun runFile(path: String) {
        val bytes = Files.readAllBytes(Paths.get(path))
        runFile(String(bytes, Charset.defaultCharset()))
    }

    @Throws(IOException::class)
    @JvmStatic
    private fun runPrompt() {
        val input = InputStreamReader(System.`in`)
        val reader = BufferedReader(input)

        while (true) {
            print("> ")
            val line = reader.readLine() ?: break
            run(line)
        }
    }

    @JvmStatic
    private fun run(source: String) {
        val scanner = Scanner(source)

    }
}
