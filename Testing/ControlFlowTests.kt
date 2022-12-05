package Testing


import main.Lox.main
import org.junit.jupiter.api.Test
import com.github.stefanbirkner.systemlambda.SystemLambda.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import java.lang.Error

class ControlFlowTests {
    @Test
    fun ifTrueTest() {
        executeTestBlock("if (0 == 0) {\n" +
                "    print \"true\";\n" +
                "}", "true")
    }

    @Test
    fun ifFalseTest() {
        executeTestBlock("if (0 == 1) {\n" +
                "    print \"false\";\n" +
                "}", "")
    }

    @Test
    fun ifElseTest() {
        executeTestBlock("if (0 == 1) {\n" +
                "    print \"if\";\n" +
                "} else {\n" +
                "    print \"else\";\n" +
                "}", "else")
    }

    @Test
    fun ifDanglingElseTest() {
        executeTestBlock("var a = \"\";\n" +
                "if (1 == 1) {\n" +
                "    a = \"outer else\";\n" +
                "    if (0 == 1) {\n" +
                "        a = \"inner else\";\n" +
                "    } else {\n" +
                "        print a;\n" +
                "    }\n" +
                "}", "outer else")
    }

    @Test
    fun ifLogicalAndTrueTest() {
        executeTestBlock("if (0 == 0 and 0 == 0) {\n" +
                "    print \"true\";\n" +
                "}", "true")
    }

    @Test
    fun ifLogicalAndFalseTest() {
        executeTestBlock("if (0 == 0 and 0 == 1) {\n" +
                "    print \"true\";\n" +
                "}", "")
    }

    @Test
    fun ifLogicalOrTest() {
        executeTestBlock("if (0 == 0 or 0 == 1) {\n" +
                "    print \"true\";\n" +
                "}", "true")
    }

    @Test
    fun whileTest() {
        executeTestBlock("{\n" +
                "  var i = 1;\n" +
                "  while (i < 4) {\n" +
                "    print i;\n" +
                "    i = i + 1;\n" +
                "  }\n" +
                "}", "1\r\n2\r\n3")
    }

    @Test
    fun forLoopTest() {
        executeTestBlock("{\n" +
                "    for (var i = 1; 1 <=3; i = i + 1) {\n" +
                "        print i;\n" +
                "    }\n" +
                "}", "1\r\n2\r\n3")
    }

    @Test
    fun ifEmptyTest() {
        executeErrorTestBlock("if () {}")
    }

    fun executeTestBlock(block: String, expectedOutput: String) {
        val execute = arrayOf<String>("block", block)
        val output = tapSystemOut {
            main(execute)
        }

        if (expectedOutput == ""){
            Assertions.assertEquals(output, expectedOutput)
        } else {
            Assertions.assertEquals(output, expectedOutput + "\r\n")
        }

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