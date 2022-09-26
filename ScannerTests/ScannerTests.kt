import org.junit.Test
import com.craftinginterpreters.lox.*
class ScannerTests {
    //TODO: Verify that Scanner() should be reading last empty token (for EOF)
    @Test
    fun predefinedInput_basicInput_SUCCESS() {
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

//        input = Scanner("'").scanTokens()
//        assert(input[0].getType() == TokenType.COMMA)


//      TODO: Fix DOT test case

//        input = Scanner(".").scanTokens()
//        assert(input[0].getType() == TokenType.DOT)

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
    }

}