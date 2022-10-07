class LoxClass(var name: String, private var superclass: LoxClass?, private var methods: MutableMap<String, LoxFunction>) : LoxCallable {

    override fun arity(): Int {
        val initializer: LoxFunction = findMethod("init") ?: return 0
        return initializer.arity()
    }

    override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
        val instance = LoxInstance(this)
        val initializer: LoxFunction? = findMethod("init")

        initializer?.bind(instance)?.call(interpreter, arguments)

        return instance
    }

    fun findMethod(name: String): LoxFunction? {
        if(methods.containsKey(name)) return methods[name]
        if(superclass != null) return superclass!!.findMethod(name)

        return null
    }

    override fun toString(): String {
        return name
    }
}