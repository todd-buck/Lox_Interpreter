package main

class LoxFunction(private var declaration: Stmt.Function, private var closure: Environment, private var isInitializer: Boolean) :
    LoxCallable {

    fun bind(instance: LoxInstance) : LoxFunction {
        val environment = Environment(closure)
        environment.define("this", instance)
        return LoxFunction(declaration, environment, isInitializer)
    }

    override fun call(interpreter: Interpreter, arguments: List<Any?>) : Any? {
        val environment = Environment(closure)

        for (i: Int in declaration.params.indices) {
            environment.define(declaration.params[i].lexeme, arguments[i])
        }

        try {
            interpreter.executeBlock(declaration.body, environment)
        } catch (returnValue: Return) {
            if(isInitializer) return closure.getAt(0,"this")
            return returnValue.value
        }

        if(isInitializer) return closure.getAt(0, "this")

        return null
    }

    override fun arity(): Int {
            return declaration.params.size
    }

    override fun toString(): String {
        return "<fn ${declaration.name.lexeme}>"
    }
}

