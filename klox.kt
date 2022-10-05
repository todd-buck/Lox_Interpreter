import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.system.exitProcess


object Lox {
    private val interpreter: Interpreter = Interpreter()

    @JvmStatic
    var hadError : Boolean = false
    private var hadRuntimeError = false


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
        run(String(bytes, Charset.defaultCharset()))
        if(hadError) exitProcess(65)
        if (hadRuntimeError) exitProcess(70)
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
            hadError = false
        }
    }

    @JvmStatic
    private fun run(source: String) {
        val scanner = Scanner(source)
        val tokens = scanner.scanTokens()

        val parser = Parser(tokens)
        val statements : List<Stmt> = parser.parse()

        if(hadError) return

        interpreter.interpret(statements)
    }

    @JvmStatic
    fun error(line: Int, message: String) {
        report(line, "", message)
    }

    fun error(token: Token, message: String) {
        if (token.type === TokenType.EOF) {
            report(token.line, " at end", message)
        } else {
            report(token.line, " at '" + token.lexeme + "'", message)
        }
    }

    @JvmStatic
    private fun report(line: Int, where: String, message: String) {
        System.err.println("[line $line] Error$where: $message")
        hadError = true
    }

    fun runtimeError(error: RuntimeError) {
        System.err.println(
            """
            ${error.message}
            [line ${error.token.line}]
            """.trimIndent()
        )
        hadRuntimeError = true
    }
}
