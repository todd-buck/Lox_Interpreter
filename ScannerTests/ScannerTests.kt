package com.craftinginterpreters.lox

import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class ScannerTests {
    @Test
    fun predefinedInput_basicInput_SUCCESS() {
        val input = Scanner("4 * 2 + 6")
        val test: MutableList<String> = ArrayList()
        val target: MutableList<String> = mutableListOf("4","*","2","+","6")

        for(x in input.tokens())
            test.add(x.toString())

        assert(test == target)
    }

    @Test
    fun predefinedInput_NULL_SUCCESS() {
        val input = Scanner("")
        val test: MutableList<String> = ArrayList()
        val target: MutableList<String> = mutableListOf()

        for (x in input.tokens())
            test.add(x.toString())

        assert(test == target)
    }

    @Test
    fun predefinedInput_trimUselessCharacters_SUCCESS() {
        val input = Scanner("newline\n tab\t carriage\r")
        val test: MutableList<String> = ArrayList()
        val target: MutableList<String> = mutableListOf("newline", "tab", "carriage")

        for (x in input.tokens())
            test.add(x.toString())

        assert(test == target)
    }

    @Test
    fun predefinedInput_TokenTypeVerification_SUCCESS() {
        //FIXME: Need to access first element of tokens to perform check
        //TODO: Fix tokens access, put in checks for rest of tokens (one or two character tokens, literals, keywords)

//        var input = Scanner("(")
//        assert(input.tokens[0].getTokenType == "LEFT_PAREN")
//
//        input = Scanner(")")
//        assert(input.tokens[0].getTokenType == "RIGHT_PAREN")
//
//        input = Scanner("{")
//        assert(input.tokens[0].getTokenType == "LEFT_BRACE")
//
//        input = Scanner("}")
//        assert(input.tokens[0].getTokenType == "RIGHT_BRACE")
//
//        input = Scanner("'")
//        assert(input.tokens[0].getTokenType == "COMMA")
//
//        input = Scanner(".")
//        assert(input.tokens[0].getTokenType == "DOT")
//
//        input = Scanner("-")
//        assert(input.tokens[0].getTokenType == "MINUS")
//
//        input = Scanner("+")
//        assert(input.tokens[0].getTokenType == "PLUS")
//
//        input = Scanner(";")
//        assert(input.tokens[0].getTokenType == "SEMICOLON")
//
//        input = Scanner("/")
//        assert(input.tokens[0].getTokenType == "SLASH")
//
//        input = Scanner("*")
//        assert(input.tokens[0].getTokenType == "STAR")
    }

}