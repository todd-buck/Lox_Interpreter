package com.craftinginterpreters.lox


class Token(type: TokenType, lexeme: String, literal: Any?, line: Int) {
    private val type: TokenType
    private val lexeme: String

    //FIXME: Review use of ANY? instead of ANY
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

    override fun getTokenType(): String {
        when() {
            this.TokenType == LEFT_PAREN -> return "LEFT_PAREN"
            this.TokenType == RIGHT_PAREN -> return "RIGHT_PAREN"
            this.TokenType == LEFT_BRACE -> return "LEFT_BRACE"
            this.TokenType == RIGHT_BRACE -> return "RIGHT_BRACE"
            this.TokenType == COMMA -> return "COMMA"
            this.TokenType == DOT -> return "DOT"
            this.TokenType == BANG -> return "BANG"
            this.TokenType == BANG_EQUAL -> return "BANG_EQUAL"
            this.TokenType == EQUAL -> return "EQUAL"
            this.TokenType == EQUAL_EQUAL -> return "EQUAL_EQUAL"
            this.TokenType == GREATER -> return "GREATER"
            this.TokenType == GREATER_EQUAL -> return "GREATER_EQUAL"
            this.TokenType == LESS -> return "LESS"
            this.TokenType == LESS_EQUAL -> return "LESS_EQUAL"
            this.TokenType == IDENTIFIER -> return "IDENTIFIER"
            this.TokenType == STRING -> return "STRING"
            this.TokenType == NUMBER -> return "NUMBER"
            this.TokenType == AND -> return "AND"
            this.TokenType == CLASS -> return "CLASS"
            this.TokenType == ELSE -> return "ELSE"
            this.TokenType == FALSE -> return "FALSE"
            this.TokenType == FUN -> return "FUN"
            this.TokenType == FOR -> return "FOR"
            this.TokenType == IF -> return "IF"
            this.TokenType == NIL -> return "NIL"
            this.TokenType == OR -> return "OR"
            this.TokenType == PRINT -> return "PRINT"
            this.TokenType == RETURN -> return "RETURN"
            this.TokenType == SUPER -> return "SUPER"
            this.TokenType == THIS -> return "THIS"
            this.TokenType == TRUE -> return "TRUE"
            this.TokenType == VAR -> return "VAR"
            this.TokenType == WHILE -> return "WHILE"
            this.TokenType == EOF -> return "EOF"

        }
    }

}