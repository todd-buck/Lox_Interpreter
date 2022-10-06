import Lox.runtimeError


class Interpreter : Expr.Visitor<Any>, Stmt.Visitor<Any> {
    private val globals: Environment = Environment()
    private var environment: Environment = globals
    private val locals: HashMap<Expr, Int> = HashMap()

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

    //Completed Expressions
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
                checkNumberOperand(expr.operator, right)
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

    override fun visitCallExpr(expr: Expr.Call): Any {

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

    override fun visitGroupingExpr(expr: Expr.Grouping): Any {

        return evaluate(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any {
        return expr.value
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

    override fun visitVariableExpr(expr: Expr.Variable): Any {
        return lookUpVariable(expr.name, expr)!!
    }

    //FIXME: Evaluate use of 'Unit' for return value
    override fun visitUnaryExpr(expr: Expr.Unary): Any {
        val right: Any = evaluate(expr.right)

        return when (expr.operator.type) {
            TokenType.MINUS -> -(right as Double)
            TokenType.BANG -> !isTruthy(right)
            else -> {
                return Unit
            }
        }
    }

    //Completed Statements
    override fun visitBlockStmt(stmt: Stmt.Block) {
        executeBlock(stmt.statements, Environment(environment))
        return
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression) {
        evaluate(stmt.expression)
        return
    }

    override fun visitFunctionStmt(stmt: Stmt.Function) {
        val function = LoxFunction(stmt, environment)
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

        throw Return(value!!)

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
    private fun checkNumberOperand(operator: Token, right: Any) {
        if (right is Double) return
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

    //FIXME: Review accuracy of logic
    private fun isTruthy(any: Any): Boolean {
        if (any is Boolean) return any
        return false
    }

    private fun isEqual(a: Any, b: Any): Boolean {
        if (a is Unit && b is Unit) return true
        if (a is Unit) return false

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

    /////////////////////////////////////////////////////////////////////
    //Unfinished Expressions

    override fun visitGetExpr(expr: Expr.Get): Any {
        TODO("Not yet implemented")
    }

    override fun visitSetExpr(expr: Expr.Set): Any {
        TODO("Not yet implemented")
    }

    override fun visitSuperExpr(expr: Expr.Super): Any {
        TODO("Not yet implemented")
    }

    override fun visitThisExpr(expr: Expr.This): Any {
        TODO("Not yet implemented")
    }

    /////////////////////////////////////////////////////////////////////
    //Unfinished Statements

    override fun visitClassStmt(stmt: Stmt.Class) {
        TODO("Not yet implemented")
    }

}