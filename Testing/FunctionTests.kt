package Testing

import main.Lox.main
import org.junit.jupiter.api.Test
import com.github.stefanbirkner.systemlambda.SystemLambda
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import java.lang.Error


class FunctionTests {

    @Test
    fun functionAnonymousTest() {
        executeTestBlock("var add = fun (a, b) {\n" +
                "  print a + b;\n" +
                "};\n" +
                "\n" +
                "add(1, 2);", "3")
    }

    @Test
    fun functionDeclarationTest() {
        executeTestBlock("fun add(a, b, c) {\n" +
                "  print a + b + c;\n" +
                "}\n" +
                "\n" +
                "add(1, 2, 3);", "6")
    }

    @Test
    fun functionArityFailExcessTest() {
        executeErrorTestBlock("fun add(a, b, c) {\n" +
                "  print a + b + c;\n" +
                "}\n" +
                "\n" +
                "fun(1, 2, 3, 4);")
    }

    @Test
    fun functionArityFailFewTest() {
        executeErrorTestBlock("fun add(a, b, c) {\n" +
                "  print a + b + c;\n" +
                "}\n" +
                "\n" +
                "fun(1, 2);")
    }

    @Test
    fun functionNoReturnTest() {
        executeErrorTestBlock("fun procedure() {\n" +
                "  print \"No klox.Return\";\n" +
                "}\n" +
                "\n" +
                "var result = procedure();\n" +
                "print result;")
    }

    @Test
    fun functionReturnTest() {
        executeTestBlock("fun procedure() {\n" +
                "  return \"klox.Return\";\n" +
                "}\n" +
                "\n" +
                "var result = procedure();\n" +
                "print result;", "klox.Return")
    }

    @Test
    fun localFunctionTest() {
        executeTestBlock("fun makeCounter() {\n" +
                "  var i = 0;\n" +
                "  fun count() {\n" +
                "    i = i + 1;\n" +
                "    print i;\n" +
                "  }\n" +
                "\n" +
                "  return count;\n" +
                "}\n" +
                "\n" +
                "var counter = makeCounter();\n" +
                "counter();\n" +
                "counter();", "1\r\n2")
    }

    fun executeTestBlock(block: String, expectedOutput: String) {
        val execute = arrayOf<String>("block", block)
        val output = SystemLambda.tapSystemOut {
            main(execute)
        }

        Assertions.assertEquals(output, expectedOutput + "\r\n")
    }

    fun executeErrorTestBlock(block: String) {
        assertThrows<Error> {
            val execute = arrayOf<String>("block", block)
            val output = SystemLambda.tapSystemErr {
                main(execute)
            }
        }
    }
}