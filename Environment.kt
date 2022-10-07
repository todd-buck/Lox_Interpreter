class Environment {
    private var values: MutableMap<String, Any?> = hashMapOf()
    var enclosing: Environment?

    constructor() {
        enclosing = null
    }

    constructor(enclosing: Environment) {
        this.enclosing = enclosing
    }

    private fun ancestor(distance: Int): Environment {
        var environment: Environment = this

        for (i: Int in 0 until distance) {
            environment = environment.enclosing!!
        }

        return environment
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

    fun assignAt(distance: Int, name: Token, value: Any) {
        ancestor(distance).values[name.lexeme] = value
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

    fun getAt(distance: Int, name: String?): Any? {
        return ancestor(distance).values[name]
    }

}