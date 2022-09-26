import org.junit.Test
import com.craftinginterpreters.lox.*
import com.craftinginterpreters.lox.Lox.main

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
        var input = Scanner("(").scanTokens()
        assert(input[0].getType() == TokenType.LEFT_PAREN)

        input = Scanner(")").scanTokens()
        assert(input[0].getType() == TokenType.RIGHT_PAREN)

        input = Scanner("{").scanTokens()
        assert(input[0].getType() == TokenType.LEFT_BRACE)

        input = Scanner("}").scanTokens()
        assert(input[0].getType() == TokenType.RIGHT_BRACE)


//      TODO: Fix COMMA test case

//        input = Scanner("'Test'").scanTokens()
//        assert(input[0].getType() == TokenType.COMMA)

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

        //TODO: Code test case for literals

        //TODO: Code test case for identifiers

//        input = Scanner("AND").scanTokens()
//        assert(input[0].getType() == TokenType.AND)
//
//        input = Scanner("CLASS").scanTokens()
//        assert(input[0].getType() == TokenType.CLASS)
//
//        input = Scanner("ELSE").scanTokens()
//        assert(input[0].getType() == TokenType.ELSE)
//
//        input = Scanner("FALSE").scanTokens()
//        assert(input[0].getType() == TokenType.FALSE)
//
//        input = Scanner("FUN").scanTokens()
//        assert(input[0].getType() == TokenType.FUN)
//
//        input = Scanner("FOR").scanTokens()
//        assert(input[0].getType() == TokenType.FOR)
//
//        input = Scanner("IF").scanTokens()
//        assert(input[0].getType() == TokenType.IF)
//
//        input = Scanner("NIL").scanTokens()
//        assert(input[0].getType() == TokenType.NIL)
//
//        input = Scanner("OR").scanTokens()
//        assert(input[0].getType() == TokenType.OR)
//
//        input = Scanner("PRINT").scanTokens()
//        assert(input[0].getType() == TokenType.PRINT)
//
//        input = Scanner("RETURN").scanTokens()
//        assert(input[0].getType() == TokenType.RETURN)
//
//        input = Scanner("SUPER").scanTokens()
//        assert(input[0].getType() == TokenType.SUPER)
//
//        input = Scanner("THIS").scanTokens()
//        assert(input[0].getType() == TokenType.THIS)
//
//        input = Scanner("TRUE").scanTokens()
//        assert(input[0].getType() == TokenType.TRUE)
//
//        input = Scanner("VAR").scanTokens()
//        assert(input[0].getType() == TokenType.VAR)
//
//        input = Scanner("WHILE").scanTokens()
//        assert(input[0].getType() == TokenType.WHILE)
//
//        input = Scanner("AND").scanTokens()
//        assert(input[0].getType() == TokenType.AND)

    }

    @Test
    fun fileInput_basic_RUN() {
        val path = arrayOf("ScannerTests/InputFiles/basic.txt")
        println("\nBASIC TEST FROM FILE (\"4 * 2 + 6\"):")
        main(path)
    }
    @Test
    fun fileInput_NULL_RUN() {
        val path = arrayOf("ScannerTests/InputFiles/null.txt")
        println("\nNULL TEST FROM FILE (\"\"):")
        main(path)

    }

    @Test
    fun fileInput_trimUselessCharacters_RUN() {
        val path = arrayOf("ScannerTests/InputFiles/uselessCharacters.txt")
        println("\nTRIM TEST FROM FILE (\"newline\\n tab\\t carriage\\r\"):")
        main(path)

    }
}