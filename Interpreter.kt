import Lox.runtimeError

class Interpreter : Expr.Visitor<Any>, Stmt.Visitor<Any> {
    private val globals: Environment = Environment()
    private var environment: Environment = globals
    private val locals: MutableMap<Expr, Int> = HashMap()

    init {
        globals.define("clock", object : LoxCallable {
            override fun arity(): Int {
                return 0
            }

            override fun call(interpreter: Interpreter, arguments: List<Any>): Any {
                return System.currentTimeMillis().toDouble() / 1000.0
            }

            override fun toString(): String {
                return "<native fn>"
            }
        })
    }

    override fun visitAssignExpr(expr: Expr.Assign): Any {
        val value: Any = evaluate(expr.value)
        val distance: Int? = locals[expr]

        if(distance != null) {
            environment.assignAt(distance, expr.name, value)
        } else {
            globals.assign(expr.name, value)
        }

        return value
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any {
        val left: Any = evaluate(expr.left)
        val right: Any = evaluate(expr.right)

        when (expr.operator.type) {
            TokenType.MINUS -> {
                checkNumberOperands(expr.operator, left, right)
                return (left as Double) - (right as Double)
            }

            TokenType.SLASH -> {
                checkNumberOperands(expr.operator, left, right)
                return (left as Double) / (right as Double)
            }

            TokenType.STAR -> {
                checkNumberOperands(expr.operator, left, right)
                return (left as Double) * (right as Double)
            }

            TokenType.PLUS -> {
                if (left is Double && right is Double) return left + right
                if (left is String && right is String) return left + right
                throw RuntimeError(
                    expr.operator,
                    "Operands must be two numbers or two strings."
                )
            }

            TokenType.GREATER -> {
                checkNumberOperands(expr.operator, left, right)
                return (left as Double) > (right as Double)
            }

            TokenType.GREATER_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                return (left as Double) >= (right as Double)
            }

            TokenType.LESS -> {
                checkNumberOperands(expr.operator, left, right)
                return (left as Double) < (right as Double)
            }

            TokenType.LESS_EQUAL -> {
                checkNumberOperands(expr.operator, left, right)
                return (left as Double) <= (right as Double)
            }

            TokenType.BANG_EQUAL -> return !isEqual(left, right)

            TokenType.EQUAL_EQUAL -> return isEqual(left, right)

            else -> {
                return Unit
            }
        }
    }

    override fun visitCallExpr(expr: Expr.Call): Any? {

        val callee: Any = evaluate(expr.callee)
        val arguments: MutableList<Any> = arrayListOf()

        for(argument: Expr in expr.arguments) {
            arguments.add(evaluate(argument))
        }

        if(callee !is LoxCallable) throw RuntimeError(expr.paren,"Can only call functions and classes.")

        val function: LoxCallable = callee

        if(arguments.size != function.arity()) throw RuntimeError(expr.paren, "Expected ${function.arity()} but got ${arguments.size}.")

        return function.call(this, arguments)
    }

    override fun visitGetExpr(expr: Expr.Get): Any? {
        val obj: Any = evaluate(expr.obj)
        if(obj is LoxInstance) {
            return obj.get(expr.name)
        }

        throw RuntimeError(expr.name,"Only instances have properties.")
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any {
        return evaluate(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any {
        return expr.value as Any
    }

    override fun visitLogicalExpr(expr: Expr.Logical): Any {
        val left: Any = evaluate(expr.left)

        if(expr.operator.type == TokenType.OR) {
            if(isTruthy(left)) return left
        } else {
            if(!isTruthy(left)) return left
        }

        return evaluate(expr.right)
    }

    override fun visitSetExpr(expr: Expr.Set): Any {
        val obj: Any = evaluate(expr.obj)

        if(obj !is LoxInstance) throw RuntimeError(expr.name,"Only instances have fields.")

        val value: Any = evaluate(expr.value)
        obj.set(expr.name, value)

        return value
    }

    //Note: Deviations with nullable distance and non-nullable bind() call
    override fun visitSuperExpr(expr: Expr.Super): Any {
        val distance: Int? = locals[expr]
        //FIXME: null distance could cause NullPointerExcept
        val superclass: LoxClass? = environment.getAt(distance!!, "super") as LoxClass?
        val obj: LoxInstance? = environment.getAt(distance - 1, "this") as LoxInstance?

        println("distance == null: $distance")
        println("superclass == null: ${superclass == null}")
        println("expr.method.lexeme: ${expr.method.lexeme}")

        val method: LoxFunction? = superclass?.findMethod(expr.method.lexeme)

        if(method == null) throw RuntimeError(expr.method,"Undefined property '${expr.method.lexeme}'.")

        return method.bind(obj!!)
    }

    override fun visitThisExpr(expr: Expr.This): Any? {
        val test: Any? = lookUpVariable(expr.keyword, expr)
        return lookUpVariable(expr.keyword, expr)

    }

    //FIXME: "!!" causing NullPointerException, recognizing "this as variable expression". Could be problem with Parser/Resolver
    override fun visitVariableExpr(expr: Expr.Variable): Any? {
        val test: Any? = lookUpVariable(expr.name, expr)
        return lookUpVariable(expr.name, expr)

    }

    //Note: Evaluate use of 'Unit' for return value
    override fun visitUnaryExpr(expr: Expr.Unary): Any {
        val right: Any = evaluate(expr.right)

        return when (expr.operator.type) {
            TokenType.MINUS -> {
                checkNumberOperand(expr.operator, right)
                -(right as Double)
            }
            TokenType.BANG -> !isTruthy(right)
            else -> {
                return Unit
            }
        }
    }

    override fun visitBlockStmt(stmt: Stmt.Block) {
        executeBlock(stmt.statements, Environment(environment))
        return
    }

    override fun visitClassStmt(stmt: Stmt.Class) {
        var superclass: Any? = null

        if(stmt.superclass != null) {
            superclass = evaluate(stmt.superclass as Expr.Variable)
            if(superclass !is LoxClass) throw RuntimeError(stmt.superclass!!.name,"Superclass must be a class.")
        }

        environment.define(stmt.name.lexeme, null)

        if(stmt.superclass != null) {
            environment = Environment(environment)
            environment.define("super", superclass)
        }

        val methods: MutableMap<String, LoxFunction> = HashMap()
        for(method: Stmt.Function in stmt.methods) {
            val function = LoxFunction(method, environment, method.name.lexeme == "init")
            methods[method.name.lexeme] = function
        }

        val klass = LoxClass(stmt.name.lexeme, superclass as LoxClass?, methods)

        if(superclass != null) environment = environment.enclosing as Environment

        environment.assign(stmt.name, klass)
        return
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression) {
        evaluate(stmt.expression)
        return
    }

    override fun visitFunctionStmt(stmt: Stmt.Function) {
        val function = LoxFunction(stmt, environment, false)
        environment.define(stmt.name.lexeme, function)
        return
    }

    override fun visitIfStmt(stmt: Stmt.If) {
        if (isTruthy(evaluate(stmt.condition))) {
            execute(stmt.thenBranch)
        } else if (stmt.elseBranch != null) {
            execute(stmt.elseBranch)
        }
        return
    }

    override fun visitPrintStmt(stmt: Stmt.Print) {
        val value: Any = evaluate(stmt.expression)
        println(stringify(value))
        return
    }

    override fun visitReturnStmt(stmt: Stmt.Return) {
        var value: Any? = null

        if(stmt.value != null) value = evaluate(stmt.value)

        throw Return(value)
    }

    override fun visitVarStmt(stmt: Stmt.Var) {
        var value: Any? = null
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer)
        }

        environment.define(stmt.name.lexeme, value)
        return
    }

    override fun visitWhileStmt(stmt: Stmt.While) {
        while(isTruthy(evaluate(stmt.condition))) {
            execute(stmt.body)
        }
        return
    }

    //Helpers
    private fun checkNumberOperand(operator: Token, operand: Any) {
        if (operand is Double) return
        throw RuntimeError(operator, "Operand must be a number.")
    }

    private fun checkNumberOperands(operator: Token, left: Any, right: Any) {
        if (left is Double && right is Double) return
        throw RuntimeError(operator, "Operands must be numbers.")
    }

    private fun evaluate(expr: Expr): Any {
        return expr.accept(this)
    }

    private fun execute(stmt: Stmt) {
        stmt.accept(this)
    }

    fun executeBlock(statements: List<Stmt>, environment: Environment) {
        val previous: Environment = this.environment
        try {
            this.environment = environment
            for(statement: Stmt in statements) {
                execute(statement)
            }
        } finally {
            this.environment = previous
        }
    }

    private fun isTruthy(any: Any?): Boolean {
        if(any == null) return false
        if (any is Boolean) return any
        return true
    }

    private fun isEqual(a: Any?, b: Any?): Boolean {
        if (a == null && b == null) return true
        if (a == null) return false

        return a == b
    }

    fun interpret(statements: List<Stmt>) {
        try {
            for(statement: Stmt in statements) {
                execute(statement)
            }
        } catch (error: RuntimeError) {
            runtimeError(error)
        }
    }

    //Note: "as Any" calls on return values may be incorrect
    private fun lookUpVariable(name: Token, expr: Expr): Any? {
        val distance: Int? = locals[expr]
        if(distance != null) {
            return environment.getAt(distance, name.lexeme)
        } else {
            return globals[name]
        }
    }

    fun resolve(expr: Expr, depth: Int) {
        locals[expr] = depth
    }

    private fun stringify(obj: Any?): String {
        if (obj == null) return "nil"
        if (obj is Double) {
            var text: String = obj.toString()
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length - 2)
            }
            return text
        }
        return obj.toString()
    }
}