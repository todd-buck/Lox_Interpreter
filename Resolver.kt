import java.util.*

class Resolver(private val interpreter: Interpreter) : Expr.Visitor<Unit>, Stmt.Visitor<Unit> {
    private val scopes = Stack<MutableMap<String, Boolean>>()
    private var currentFunction: FunctionType = FunctionType.NONE
    private var currentClass: ClassType = ClassType.NONE

    private enum class FunctionType {
        NONE,
        FUNCTION,
        INITIALIZER,
        METHOD
    }

    private enum class ClassType {
        NONE,
        CLASS,
        SUBCLASS
    }

    private fun beginScope() {
        scopes.push(mutableMapOf())
    }

    private fun declare(name: Token) {
        if(scopes.isEmpty()) return

        val scope = scopes.peek()

        if(scope.containsKey(name.lexeme)) Lox.error(name, "Already a variable with this name in this scope.")

        scope[name.lexeme] = false
    }

    private fun define(name: Token) {
        if(scopes.isEmpty()) return
        scopes.peek()[name.lexeme] = true
    }

    private fun endScope() {
        scopes.removeLast()
    }

    private fun resolve(expr: Expr) {
        expr.accept(this)
    }

    private fun resolve(stmt: Stmt) {
        stmt.accept(this)
    }

    fun resolve(statements: List<Stmt?>) {
        for(statement in statements) {
            resolve(statement!!)
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
        val enclosingFunction = currentFunction
        currentFunction = type

        beginScope()

        for(param in function.params) {
            declare(param)
            define(param)
        }

        resolve(function.body)

        endScope()

        currentFunction = enclosingFunction
    }

    override fun visitAssignExpr(expr: Expr.Assign) {
        resolve(expr.value)
        resolveLocal(expr, expr.name)
        return
    }

    override fun visitBinaryExpr(expr: Expr.Binary) {
        resolve(expr.left)
        resolve(expr.right)
    }

    override fun visitCallExpr(expr: Expr.Call) {
        resolve(expr.callee)

        for(argument in expr.arguments) {
            resolve(argument)
        }
    }

    override fun visitGetExpr(expr: Expr.Get) {
        resolve(expr.obj)
    }

    override fun visitGroupingExpr(expr: Expr.Grouping) {
        resolve(expr.expression)
    }

    override fun visitLiteralExpr(expr: Expr.Literal) {}

    override fun visitLogicalExpr(expr: Expr.Logical) {
        resolve(expr.left)
        resolve(expr.right)
    }

    override fun visitSetExpr(expr: Expr.Set) {
        if(expr.value != null) resolve(expr.value)
        resolve(expr.obj)
    }

    override fun visitSuperExpr(expr: Expr.Super) {
        if(currentClass == ClassType.NONE) {
            Lox.error(expr.keyword,"Can't use 'super' outside of a class.")
        } else if (currentClass != ClassType.SUBCLASS) {
            Lox.error(expr.keyword,"Can't use 'super' in a class with no superclass.")
        }

        resolveLocal(expr, expr.keyword)
    }

    override fun visitThisExpr(expr: Expr.This) {
        if(currentClass == ClassType.NONE) {
            Lox.error(expr.keyword,"Can't use 'this' outside of a class.")
            return
        }

        resolveLocal(expr, expr.keyword)
    }

    override fun visitUnaryExpr(expr: Expr.Unary) {
        resolve(expr.right)
    }

    override fun visitVariableExpr(expr: Expr.Variable) {
        if(!scopes.isEmpty() && scopes.peek()[expr.name.lexeme] == false) {
            Lox.error(expr.name,"Can't read local variable in its own initializer.")
        }

        resolveLocal(expr, expr.name)
    }

    override fun visitBlockStmt(stmt: Stmt.Block) {
        beginScope()
        resolve(stmt.statements)
        endScope()
    }

    override fun visitClassStmt(stmt: Stmt.Class) {
        val enclosingClass = currentClass
        currentClass = ClassType.CLASS

        declare(stmt.name)
        define(stmt.name)

        if(stmt.superclass != null){
            if(stmt.name.lexeme == stmt.superclass!!.name.lexeme) {
                Lox.error((stmt.superclass as Expr.Variable).name,"A class can't inherit from itself.")
            }
            currentClass = ClassType.SUBCLASS
            resolve(stmt.superclass!!)
            beginScope()
            scopes.peek()["super"] = true
        }

        beginScope()
        scopes.peek()["this"] = true

        for(method in stmt.methods) {
            var declaration = FunctionType.METHOD
            if(method.name.lexeme == "init") declaration = FunctionType.INITIALIZER
            resolveFunction(method, declaration)
        }

        endScope()

        if(stmt.superclass != null) endScope()

        currentClass = enclosingClass
    }

    override fun visitExpressionStmt(stmt: Stmt.Expression) {
        resolve(stmt.expression)
    }

    override fun visitFunctionStmt(stmt: Stmt.Function) {
        declare(stmt.name)
        define(stmt.name)

        resolveFunction(stmt, FunctionType.FUNCTION)
    }

    override fun visitIfStmt(stmt: Stmt.If) {
        resolve(stmt.condition)
        resolve(stmt.thenBranch)
        if(stmt.elseBranch != null) resolve(stmt.elseBranch)
    }

    override fun visitPrintStmt(stmt: Stmt.Print) {
        resolve(stmt.expression)
    }

    override fun visitReturnStmt(stmt: Stmt.Return) {
        if(currentFunction == FunctionType.NONE) Lox.error(stmt.keyword,"Can't return from top-level code.")
        if(stmt.value != null) {
            if(currentFunction == FunctionType.INITIALIZER) Lox.error(stmt.keyword,"Can't return a value from an initializer.")
            resolve(stmt.value)
        }
    }

    override fun visitVarStmt(stmt: Stmt.Var) {
        declare(stmt.name)
        if(stmt.initializer != null) resolve(stmt.initializer)
        define(stmt.name)
        return
    }

    override fun visitWhileStmt(stmt: Stmt.While) {
        resolve(stmt.condition)
        resolve(stmt.body)
    }
}


