class LoxFunction(private val declaration: Stmt.Function, private val closure: Environment) : LoxCallable {

    override fun call(interpreter: Interpreter, arguments: List<Any>) : Any {
        val environment = Environment(closure)

        for (i: Int in 0 until declaration.params.size) {
            environment.define(declaration.params[i].lexeme, arguments[i])
        }

        try {
            interpreter.executeBlock(declaration.body, environment)
        } catch (returnValue: Return) {
            return returnValue.value
        }


        //FIXME: should not return unit, specifies that null is returned in book
        return Unit
    }

    override fun arity(): Int {
            return declaration.params.size
    }

    override fun toString(): String {
        return "<fn ${declaration.name.lexeme}>"
    }
}

