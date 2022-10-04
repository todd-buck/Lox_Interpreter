package com.craftinginterpreters.lox

import Token


class Environment {
    private var values: HashMap<String, Any> = HashMap()
    var enclosing: Environment? = null

    fun Environment() {
        enclosing = null
    }

    fun Environment(enclosing: Environment?) {
        this.enclosing = enclosing
    }

    fun define(name: String, value: Any) {
        values.put(name, value)

    }

    operator fun get(name: Token): Any? {
        if (values.containsKey(name.lexeme)) {
            return values[name.lexeme]
        }

        if (enclosing != null) return enclosing!!.get(name)

        throw RuntimeError(
            name,
            "Undefined variable '" + name.lexeme + "'."
        )
    }

    fun assign(name: Token, value: Any?) {
        if (values.containsKey(name.lexeme)) {
            values[name.lexeme] = value!!
            return
        }

        if (enclosing != null) {
            enclosing!!.assign(name, value);
            return;
        }

        throw RuntimeError(
            name,
            "Undefined variable '" + name.lexeme + "'."
        )
    }
}