package com.craftinginterpreters.lox

import com.sun.org.apache.bcel.internal.generic.RETURN
import com.sun.tools.example.debug.expr.ExpressionParserConstants.ELSE
import com.sun.tools.example.debug.expr.ExpressionParserConstants.FOR
import com.sun.tools.example.debug.expr.ExpressionParserConstants.IF
import com.sun.tools.example.debug.expr.ExpressionParserConstants.WHILE
import com.sun.tools.javac.code.Kinds.KindSelector.NIL
import java.util.HashMap


class Token(type: TokenType, lexeme: String, literal: Any?, line: Int) {
    private val type: TokenType
    private val lexeme: String


    //NOTE: REVIEW WITH @MITHUL TO ENSURE NULL-PROTECTED
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