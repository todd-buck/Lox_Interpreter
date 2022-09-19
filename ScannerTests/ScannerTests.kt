package com.craftinginterpreters.lox

import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class ScannerTests {
    @Test
    fun predefinedOutput_String_SUCCESS() {
        val input = Scanner("4 * 2 + 6")
        val test: MutableList<String> = ArrayList()
        val target: MutableList<String> = mutableListOf("4","*","2","+","6")

        for(x in input.tokens())
            test.add(x.toString())

        assert(test == target)
    }

    @Test
    fun predefinedOutput_NULL_SUCCESS() {
        val input = Scanner("")
        val test: MutableList<String> = ArrayList()
        val target: MutableList<String> = mutableListOf()

        for (x in input.tokens())
            test.add(x.toString())

        assert(test == target)
    }

}