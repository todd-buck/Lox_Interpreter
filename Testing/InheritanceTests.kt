package Testing

import main.Lox.main
import org.junit.jupiter.api.Test
import com.github.stefanbirkner.systemlambda.SystemLambda.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertThrows
import java.lang.Error

class InheritanceTests {

        /*
       @Test
       fun Test() {
           executeTestBlock()
       }
       */

    @Test
    fun inheritanceDeclarationTest() {
        executeTestBlock("class Inheritance {}\n" +
                "class Inheritor < Inheritance {}\n" +
                "print Inheritor;", "Inheritor")
    }

    @Test
    fun inheritanceMethodTest() {
        executeTestBlock("class ToInherit {\n" +
                "  test() {\n" +
                "    print \"Method Inherited\";\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "class Inherits < ToInherit {}\n" +
                "\n" +
                "Inherits().test();", "Method Inherited")
    }

    @Test
    fun superClassMethodCallTest() {
        executeTestBlock("class Inheritance {\n" +
                "  test() {\n" +
                "    print \"Super\";\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "class Inheritor < Inheritance {\n" +
                "  test() {\n" +
                "    super.test();\n" +
                "    print \"Inherited\";\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "Inheritor().test();", "Super\r\n" +
                "Inherited")
    }

    @Test
    fun nestedInheritanceTest() {
        executeTestBlock("class A {\n" +
                "  method() {\n" +
                "    print \"A method\";\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "class B < A {\n" +
                "  method() {\n" +
                "    print \"B method\";\n" +
                "  }\n" +
                "\n" +
                "  test() {\n" +
                "    super.method();\n" +
                "  }\n" +
                "}\n" +
                "\n" +
                "class C < B {}\n" +
                "\n" +
                "C().test();", "A method")
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