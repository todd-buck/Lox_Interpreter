package com.craftinginterpreters.lox

internal class Scanner(private val source: String) {
    private var start = 0
    private var current = 0
    private var line = 1

    private val tokens: MutableList<Token> = ArrayList()

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

    private fun scanToken() {

    }

    private fun advance() : Char {
        return source[current++]
    }

    private fun addToken(type: TokenType) {

    }
}


