package com.craftinginterpreters.lox

class Token(type: TokenType, lexeme: String, literal: Any?, line: Int) {
    private val type: TokenType
    private val lexeme: String
    private val literal: Any?
    private val line: Int

    init {
        this.type = type
        this.lexeme = lexeme
        this.literal = literal
        this.line = line
    }

    override fun toString(): String {
        return "$type $lexeme $literal"
    }
}