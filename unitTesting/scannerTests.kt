package unitTesting

//import org.junit.Test
import Lox.main
import Scanner
import TokenType
import org.junit.jupiter.api.Test

class ScannerTests {
    //TODO: Verify that Scanner() should be reading last empty token (for EOF)
    @Test
    fun predefinedInput_basic_SUCCESS() {
        val input = Scanner("4 * 2 + 6").scanTokens()
        val target: MutableList<String> = mutableListOf("4","*","2","+","6", "")
        val test: MutableList<String> = ArrayList()

        for(x in input)
            test.add(x.getLexeme())

        assert(test == target)
    }

    @Test
    fun predefinedInput_NULL_SUCCESS() {
        val input = Scanner("").scanTokens()
        val test: MutableList<String> = ArrayList()
        val target: MutableList<String> = mutableListOf("")

        for (x in input)
            test.add(x.getLexeme())


        assert(test == target)
    }

    @Test
    fun predefinedInput_trimUselessCharacters_SUCCESS() {
        val input = Scanner("newline\n tab\t carriage\r").scanTokens()
        val test: MutableList<String> = ArrayList()
        val target: MutableList<String> = mutableListOf("newline", "tab", "carriage", "")

        for (x in input)
            test.add(x.getLexeme())

        assert(test == target)
    }

    @Test
    fun predefinedInput_TokenTypeVerification_SUCCESS() {
        //Single character tokens
        var input = Scanner("(").scanTokens()
        assert(input[0].getType() == TokenType.LEFT_PAREN)

        input = Scanner(")").scanTokens()
        assert(input[0].getType() == TokenType.RIGHT_PAREN)

        input = Scanner("{").scanTokens()
        assert(input[0].getType() == TokenType.LEFT_BRACE)

        input = Scanner("}").scanTokens()
        assert(input[0].getType() == TokenType.RIGHT_BRACE)

        input = Scanner(",").scanTokens()
        assert(input[0].getType() == TokenType.COMMA)

        input = Scanner(".").scanTokens()
        assert(input[0].getType() == TokenType.DOT)

        input = Scanner("-").scanTokens()
        assert(input[0].getType() == TokenType.MINUS)

        input = Scanner("+").scanTokens()
        assert(input[0].getType() == TokenType.PLUS)

        input = Scanner(";").scanTokens()
        assert(input[0].getType() == TokenType.SEMICOLON)

        input = Scanner("/").scanTokens()
        assert(input[0].getType() == TokenType.SLASH)

        input = Scanner("*").scanTokens()
        assert(input[0].getType() == TokenType.STAR)

        //One or two character tokens
        input = Scanner("!").scanTokens()
        assert(input[0].getType() == TokenType.BANG)

        input = Scanner("!=").scanTokens()
        assert(input[0].getType() == TokenType.BANG_EQUAL)

        input = Scanner("=").scanTokens()
        assert(input[0].getType() == TokenType.EQUAL)

        input = Scanner("==").scanTokens()
        assert(input[0].getType() == TokenType.EQUAL_EQUAL)

        input = Scanner(">").scanTokens()
        assert(input[0].getType() == TokenType.GREATER)

        input = Scanner(">=").scanTokens()
        assert(input[0].getType() == TokenType.GREATER_EQUAL)

        input = Scanner("<").scanTokens()
        assert(input[0].getType() == TokenType.LESS)

        input = Scanner("<=").scanTokens()
        assert(input[0].getType() == TokenType.LESS_EQUAL)

        // literals
        input = Scanner("2.2").scanTokens()
        assert(input[0].getType() == TokenType.NUMBER)

        input = Scanner("\"example\"").scanTokens()
        assert(input[0].getType() == TokenType.STRING)

        input = Scanner("val").scanTokens()
        assert(input[0].getType() == TokenType.IDENTIFIER)

        // identifiers that are not user-defined
        input = Scanner("and").scanTokens()
        assert(input[0].getType() == TokenType.AND)

        input = Scanner("class").scanTokens()
        assert(input[0].getType() == TokenType.CLASS)

        input = Scanner("else").scanTokens()
        assert(input[0].getType() == TokenType.ELSE)

        input = Scanner("false").scanTokens()
        assert(input[0].getType() == TokenType.FALSE)

        input = Scanner("fun").scanTokens()
        assert(input[0].getType() == TokenType.FUN)

        input = Scanner("for").scanTokens()
        assert(input[0].getType() == TokenType.FOR)

        input = Scanner("if").scanTokens()
        assert(input[0].getType() == TokenType.IF)

        input = Scanner("nil").scanTokens()
        assert(input[0].getType() == TokenType.NIL)

        input = Scanner("or").scanTokens()
        assert(input[0].getType() == TokenType.OR)

        input = Scanner("print").scanTokens()
        assert(input[0].getType() == TokenType.PRINT)

        input = Scanner("return").scanTokens()
        assert(input[0].getType() == TokenType.RETURN)

        input = Scanner("super").scanTokens()
        assert(input[0].getType() == TokenType.SUPER)

        input = Scanner("this").scanTokens()
        assert(input[0].getType() == TokenType.THIS)

        input = Scanner("true").scanTokens()
        assert(input[0].getType() == TokenType.TRUE)

        input = Scanner("var").scanTokens()
        assert(input[0].getType() == TokenType.VAR)

        input = Scanner("while").scanTokens()
        assert(input[0].getType() == TokenType.WHILE)

    }

    @Test
    fun fileInput_basic_RUN() {
        val path = arrayOf("unitTesting/InputFiles/basic.txt")
        println("\nBASIC TEST FROM FILE (\"4 * 2 + 6\"):")
        main(path)
    }

    @Test
    fun fileInput_NULL_RUN() {
        val path = arrayOf("unitTesting/InputFiles/null.txt")
        println("\nNULL TEST FROM FILE (\"\"):")
        //main(path)

    }

    @Test
    fun fileInput_trimUselessCharacters_RUN() {
        val path = arrayOf("unitTesting/InputFiles/uselessCharacters.txt")
        println("\nTRIM TEST FROM FILE (\"newline\\n tab\\t carriage\\r\"):")
        main(path)

    }
}