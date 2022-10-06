class Resolver(private val interpreter: Interpreter) : Expr.Visitor<Any?>, Stmt.Visitor<Any?> {
    private val scopes: ArrayDeque<HashMap<String, Boolean>> = ArrayDeque()
    private var currentFunction: FunctionType = FunctionType.NONE

    private enum class FunctionType {
        NONE,
        FUNCTION
    }

    private fun beginScope() {
        scopes.addFirst(HashMap())
        return
    }

    private fun declare(name: Token) {
        if(scopes.isEmpty()) return

        val scope: HashMap<String, Boolean> = scopes.first()

        if(scope.containsKey(name.lexeme)) Lox.error(name, "Already a variable with this name in this scope.")

        scope[name.lexeme] = false
    }

    private fun define(name: Token) {
        if(scopes.isEmpty()) return
        scopes.first()[name.lexeme] = true
    }

    private fun endScope() {
        scopes.removeFirst()
        return
    }

    private fun resolve(expr: Expr) {
        expr.accept(this)
    }

    private fun resolve(stmt: Stmt) {
        stmt.accept(this)
    }

    fun resolve(statements: List<Stmt>) {
        for(statement: Stmt in statements) {
            resolve(statement)
        }
    }

    private fun resolveLocal(expr: Expr, name: Token) {
        for (i in scopes.indices.reversed()) {
            if (scopes[i].containsKey(name.lexeme)) {
                interpreter.resolve(expr, scopes.size - 1 - i)
                return
            }
        }
    }

    private fun resolveFunction(function: Stmt.Function, type: FunctionType) {
        val enclosingFunction: FunctionType = currentFunction
        currentFunction = type
        beginScope()

        for(param: Token in function.params) {
            declare(param)
            define(param)
        }

        resolve(function.body)
        endScope()
        currentFunction = enclosingFunction
    }

    override fun visitAssignExpr(expr: Expr.Assign): Any? {
        resolve(expr.value)
        resolveLocal(expr, expr.name)
        return null
    }

    override fun visitBinaryExpr(expr: Expr.Binary): Any? {
        resolve(expr.left)
        resolve(expr.right)
        return null
    }

    override fun visitCallExpr(expr: Expr.Call): Any? {
        resolve(expr.callee)

        for(argument: Expr in expr.arguments) {
            resolve(argument)
        }

        return null
    }

    override fun visitGroupingExpr(expr: Expr.Grouping): Any? {
        resolve(expr.expression)
        return null
    }

    override fun visitLiteralExpr(expr: Expr.Literal): Any? {
        return null
    }

    override fun visitLogicalExpr(expr: Expr.Logical): Any? {
        resolve(expr.left)
        resolve(expr.right)
        return null
    }

    override fun visitUnaryExpr(expr: Expr.Unary): Any? {
        resolve(expr.right)
        return null
    }

    override fun visitBlockStmt(stmt: Stmt.Block): Any? {
        beginScope()
        resolve(stmt.statements)
        endScope()
        return null
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression): Any? {
        resolve(stmt.expression)
        return null
    }

    override fun visitFunctionStmt(stmt: Stmt.Function): Any? {
        declare(stmt.name)
        define(stmt.name)

        resolveFunction(stmt, FunctionType.FUNCTION)
        return null
    }

    override fun visitIfStmt(stmt: Stmt.If): Any? {
        resolve(stmt.condition)
        resolve(stmt.thenBranch)
        if(stmt.elseBranch != null) resolve(stmt.elseBranch)
        return null
    }

    override fun visitPrintStmt(stmt: Stmt.Print): Any? {
        resolve(stmt.expression)
        return null
    }

    override fun visitReturnStmt(stmt: Stmt.Return): Any? {
        if(currentFunction == FunctionType.NONE) Lox.error(stmt.keyword,"Can't return from top-level code.")
        if(stmt.value != null) resolve(stmt.value)

        return null
    }

    override fun visitVarStmt(stmt: Stmt.Var): Any? {
        declare(stmt.name)
        if(stmt.initializer != null) {
            resolve(stmt.initializer)
        }
        define(stmt.name)
        return null
    }

    override fun visitWhileStmt(stmt: Stmt.While): Any? {
        resolve(stmt.condition)
        resolve(stmt.body)
        return null
    }

    /////////////////////////////////////////////////////////////////////////
    // Unfinished Expressions
    override fun visitGetExpr(expr: Expr.Get): Any? {
        TODO("Not yet implemented")
    }

    override fun visitSetExpr(expr: Expr.Set): Any? {
        TODO("Not yet implemented")
    }

    override fun visitSuperExpr(expr: Expr.Super): Any? {
        TODO("Not yet implemented")
    }

    override fun visitThisExpr(expr: Expr.This): Any? {
        TODO("Not yet implemented")
    }

    override fun visitVariableExpr(expr: Expr.Variable): Any? {
        TODO("Not yet implemented")
    }

    //Unfinished Statements
    override fun visitClassStmt(stmt: Stmt.Class): Any? {
        TODO("Not yet implemented")
    }

    ////////////////////////////////////////////////////////////////////////
}


