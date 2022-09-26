package com.craftinginterpreters.lox

internal class Scanner(private val source: String) {
    private var start = 0
    private var current = 0
    private var line = 1

    private val tokens:
            MutableList<Token> = ArrayList()

    //FIXME: Determine proper storage container for data
    private val keywords:
            Map<String, TokenType> = mapOf (
                "and" to TokenType.AND,
                "class" to TokenType.CLASS,
                "else" to TokenType.ELSE,
                "false" to TokenType.FALSE,
                "for" to TokenType.FOR,
                "fun" to TokenType.FUN,
                "if" to TokenType.IF,
                "nil" to TokenType.NIL,
                "or" to TokenType.OR,
                "print" to TokenType.PRINT,
                "return" to TokenType.RETURN,
                "super" to TokenType.SUPER,
                "this" to TokenType.THIS,
                "true" to TokenType.TRUE,
                "var" to TokenType.VAR,
                "while" to TokenType.WHILE
            )

    fun scanTokens() : List<Token> {
        while (!isAtEnd()) {
            start = current
            scanToken()
        }

        tokens.add(Token(TokenType.EOF, "", null, line))
        return tokens
    }

    private fun isAtEnd() : Boolean {
        return current >= source.length
    }

    //FIXME: Determine if 'break' is implied in Kotlin switch statement, add to each line if not implicit
    private fun scanToken() {
        when (val c = advance()) {
            //single character lexemes
            '(' -> addToken(TokenType.LEFT_PAREN)
            ')' -> addToken(TokenType.RIGHT_PAREN)
            '{' -> addToken(TokenType.LEFT_BRACE)
            '}' -> addToken(TokenType.RIGHT_BRACE)
            ',' -> addToken(TokenType.COMMA)
            '.' -> addToken(TokenType.DOT)
            '-' -> addToken(TokenType.MINUS)
            '+' -> addToken(TokenType.PLUS)
            ';' -> addToken(TokenType.SEMICOLON)
            '*' -> addToken(TokenType.STAR)

            //two-character lexemes
            '!' -> addToken(if (match('=')) TokenType.BANG_EQUAL else TokenType.BANG)
            '=' -> addToken(if (match('=')) TokenType.EQUAL_EQUAL else TokenType.EQUAL)
            '<' -> addToken(if (match('=')) TokenType.LESS_EQUAL else TokenType.LESS)
            '>' -> addToken(if (match('=')) TokenType.GREATER_EQUAL else TokenType.GREATER)
            '/' -> if (match('/')) while(peek() != '\n' && !isAtEnd()) advance() else addToken(TokenType.SLASH)

            '"' -> string()

            //useless characters
            '\n' -> line++
            ' ' -> {}
            '\r' -> {}
            '\t' -> {}


            else -> if(isDigit(c)) number()
                    else if (isAlpha(c)) identifier()
                    else Lox.error(line, "Unexpected character.")
        }
    }

    private fun advance() : Char {
        return source[current++]
    }

    private fun addToken(type: TokenType) {
        addToken(type, null)
    }

    //FIXME: Review use of ANY? instead of OBJECT?/OBJECT in function declaration
    private fun addToken(type: TokenType, literal: Any?) {
        val text = source.substring(start, current)
        tokens.add(Token(type, text, literal, line))
    }

    private fun match(expected: Char) : Boolean {
        if(isAtEnd()) return false
        if(source[current] != expected) return false

        current++
        return true
    }

    private fun peek() : Char {
        if(isAtEnd()) return '\u0000'
        return source[current]
    }

    private fun string() {
        while(peek() != '"' && !isAtEnd())
            if(peek() == '\n') line++
            advance()

        if(isAtEnd())
            Lox.error(line, "Unterminated string.")

        advance()

        val value : String = source.substring(start + 1, current - 1)
        addToken(TokenType.STRING, value)
    }

    private fun isDigit(c: Char) : Boolean {
        return c in '0'..'9'
    }

    private fun number() {
        while(isDigit(peek())) advance()

        if(peek() == '.' && isDigit(peekNext()))
            advance()
            while(isDigit(peek())) advance()


        addToken(TokenType.NUMBER, (source.substring(start, current)).toDouble())
    }

    private fun peekNext() : Char {
        if(current + 1 >= source.length) return '\u0000'
        return source[current + 1]
    }

    private fun identifier() {
        while(isAlphaNumeric(peek())) advance()

        val text : String = source.substring(start, current)
        var type : TokenType? = keywords[text]
        if(type == null) type = TokenType.IDENTIFIER

        addToken(type)
    }

    private fun isAlpha(c: Char) : Boolean {
        return (c in 'a'..'z') || (c in 'A'..'Z') || (c == '_')
    }

    private fun isAlphaNumeric(c: Char) : Boolean {
        return isAlpha(c) || isDigit(c)
    }
}


