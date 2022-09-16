package com.craftinginterpreters.lox

import org.junit.Test
import java.util.*
import kotlin.collections.ArrayList

class ScannerTests {
    @Test
    fun firstTest() {
        val input = Scanner("4 * 2 + 6")
        val test: MutableList<String> = ArrayList()
        val target: MutableList<String> = mutableListOf("4","*","2","+","6")

        for(x in input.tokens())
            test.add(x.toString())

        assert(test == target)
    }

}