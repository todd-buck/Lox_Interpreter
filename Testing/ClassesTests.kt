package Testing

import main.Lox.main
import org.junit.jupiter.api.Test
import com.github.stefanbirkner.systemlambda.SystemLambda.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import java.lang.Error


class ClassesTests {
    /*
    @Test
    fun Test() {
        executeTestBlock()
    }
    */

    @Test
    fun classDeclarationTest() {
        executeTestBlock("class ClassDeclared {\n" +
                "}\n" +
                "\n" +
                "print ClassDeclared;", "ClassDeclared")
    }

    @Test
    fun classInstancedTest() {
        executeTestBlock("class ClassInstanced{}\n" +
                "var testclass = ClassInstanced();\n" +
                "print testclass;", "ClassInstanced instance")
    }

    @Test
    fun classGetExpressionTest() {
        executeTestBlock("class GetExpression {\n" +
                "  init() {\n" +
                "    this.a = \"got\";\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "var testclass = GetExpression();\n" +
                "print testclass.a;", "got")
    }

    @Test
    fun classNestedGetExpressionTest() {
        executeTestBlock("class GetExpression {\n" +
                "    init() {\n" +
                "      this.inner = Inner();\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "class Inner {\n" +
                "  init() {\n" +
                "    this.a = \"got\";\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "var testclass = GetExpression();\n" +
                "print testclass.inner.a;", "got")
    }

    @Test
    fun classMethodDeclarationTest() {
        executeTestBlock("class Test {\n" +
                "    testing() {\n" +
                "        print \"declared\";\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "var a = Test();\n" +
                "a.testing();", "declared")
    }

    @Test
    fun classMethodDeclarationWithArgsTest() {
        executeTestBlock("class Test {\n" +
                "    testing(argument) {\n" +
                "        print \"declared \" + argument;\n" +
                "    }\n" +
                "}\n" +
                "\n" +
                "var a = Test();\n" +
                "a.testing(\"success\");", "declared success")
    }

    @Test
    fun methodThisTest() {
        executeTestBlock("class Test {\n" +
                "  testing() {\n" +
                "    var a = \"Test \";\n" +
                "    print a + this.b;\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "var testinstance = Test();\n" +
                "testinstance.b = \"Success\";\n" +
                "testinstance.testing();", "Test Success")
    }

    @Test
    fun topLevelThisTest() {
        executeErrorTestBlock("print this;")
    }

    fun executeErrorTestBlock(block: String) {
        assertThrows<Error> {
            val execute = arrayOf<String>("block", block)
            val output = tapSystemErr {
                main(execute)
            }
        }
    }

    fun executeTestBlock(block: String, expectedOutput: String) {
        val execute = arrayOf<String>("block", block)
        val output = tapSystemOut {
            main(execute)
        }

        Assertions.assertEquals(output, expectedOutput + "\r\n")
    }


}