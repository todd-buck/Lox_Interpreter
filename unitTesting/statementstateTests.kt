package unitTesting

import Lox.main
import Scanner
import TokenType
import org.junit.jupiter.api.Test
import com.github.stefanbirkner.systemlambda.SystemLambda.*
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.assertThrows
import java.lang.Error

class statementstateTests {
    @Test
    fun printNullTest() {
        executeErrorTestBlock("print;")
    }

    @Test
    fun printEmptyTest() {
        executeTestBlock("print \"\";", "")
    }

    @Test
    fun printLiteralTest() {
        executeTestBlock("print \"a\";", "a")
    }

    @Test fun printVariableTest() {
        executeTestBlock(
                "  var vartest = 1;\n" +
                "  print vartest;\n", "1")
    }

    @Test
    fun variableDeclarationTest() {
        executeTestBlock("{\n" +
                "  var dectest;\n" +
                "  print dectest;\n" +
                "}", "nil")
    }

    @Test
    fun variableAssignmentNilTest() {
        executeTestBlock("{\n" +
                "  var niltest = nil;\n" +
                "  print niltest;\n" +
                "}", "NIL")
    }

    @Test
    fun variableAssignmentEmptyTest() {
        executeTestBlock("{\n" +
                "  var testempty = \"\";\n" +
                "  print testempty;\n" +
                "}", "")
    }

    @Test
    fun variableAssignmentLiteralTest() {
        executeTestBlock("{\n" +
                "  var asltest = 1;\n" +
                "  print asltest;\n" +
                "}", "1")
    }

    @Test
    fun variableAssignmentCrossAssign() {
        executeTestBlock("var vactest1 = 2;\n" +
                "var vactest2 = vactest1;\n" +
                "print vactest2;\n", "2")
    }

    @Test
    fun localVariableShadowTest() {
        executeTestBlock("var vastest = 11;\n" +
                "\n" +
                "vastest = 0;\n" +
                "\n" +
                "{\n" +
                "  var vastest = 3 * 4 * 5;\n" +
                "  print vastest;\n" +
                "}", "60")
    }

    @Test
    fun globalVariableInLocalScopeTest() {
        executeTestBlock("var global = \"global\";\n" +
                "{\n" +
                "    var local = \"local\";\n" +
                "    print global + local;\n" +
                "}", "globallocal")
    }

    fun executeTestBlock(block: String, expectedOutput: String) {
        val execute = arrayOf<String>("block", block)
        val output = tapSystemOut {
            main(execute)
        }

        Assertions.assertEquals(output, expectedOutput + "\r\n")
    }

    fun executeErrorTestBlock(block: String) {
        assertThrows<Error> {
            val execute = arrayOf<String>("block", block)
            val output = tapSystemErr {
                main(execute)
            }
        }
    }

}