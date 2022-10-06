class Environment {
    var values: HashMap<String, Any?> = HashMap()

    private var enclosing: Environment? = null

    constructor() {
        enclosing = null
    }

    constructor(enclosing: Environment) {
        this.enclosing = enclosing
    }

    fun assign(name: Token, value: Any) {
        if (values.containsKey(name.lexeme)) {
            values[name.lexeme] = value
            return
        }

        if (enclosing != null) {
            enclosing!!.assign(name, value)
            return
        }

        throw RuntimeError(name, "Undefined variable '" + name.lexeme + "'.")
    }

    fun define(name: String, value: Any?) {
        values[name] = value
    }

    operator fun get(name: Token): Any? {
        if (values.containsKey(name.lexeme)) {
            return values[name.lexeme]
        }

        if (enclosing != null) return enclosing!![name]

        throw RuntimeError(name, "Undefined variable '" + name.lexeme + "'.")
    }


}