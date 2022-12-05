package Testing

import main.Lox.main
import com.github.stefanbirkner.systemlambda.SystemLambda
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import java.lang.Error

class ResolvingAndBindingTests {
    @Test
    fun staticScopeTest() {
        executeTestBlock("var test = \"outer\";\n" +
                "{\n" +
                "  fun showA() {\n" +
                "    print test;\n" +
                "  }\n" +
                "\n" +
                "  showA();\n" +
                "  var test = \"block\";\n" +
                "  showA();\n" +
                "}", "outer\r\nouter")
    }

    @Test
    fun localNameReassignmentTest() {
        executeErrorTestBlock("var a = \"outer\";\n" +
                "{\n" +
                "  var a = a;\n" +
                "}\n")
    }

    @Test
    fun resolutionErrorTest() {
        executeErrorTestBlock("fun resolution() {\n" +
                "    var a = \"first\";\n" +
                "    var a = \"second\";\n" +
                "}")
    }

    @Test
    fun topLevelReturnTest() {
        executeErrorTestBlock("{\n" +
                "    return \"at top level\";\n" +
                "}")
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