import Expr.*

abstract class Stmt {
    interface Visitor<R> {
        fun visitBlockStmt(stmt: Block): R
        fun visitClassStmt(stmt: Class): R
        fun visitExpressionStmt(stmt: Expression): R
        fun visitFunctionStmt(stmt: Function): R
        fun visitIfStmt(stmt: If): R
        fun visitPrintStmt(stmt: Print): R
        fun visitReturnStmt(stmt: Return): R
        fun visitVarStmt(stmt: Var): R
        fun visitWhileStmt(stmt: While): R
    }

    class Block(statements: List<Stmt>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>) : R {
            return visitor.visitBlockStmt(this)
        }

        val statements: List<Stmt>

        init {
            this.statements = statements
        }
    }

    class Class(name: Token, superclass: Variable, methods: List<Function>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitClassStmt(this)
        }

        private val name: Token
        private val superclass: Variable
        private val methods: List<Function>

        init {
            this.name = name
            this.superclass = superclass
            this.methods = methods
        }
    }

    class Expression(expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExpressionStmt(this)
        }

        val expression: Expr

        init {
                this.expression = expression
        }
    }

    class Function(name: Token, params: List<Token>, body: List<Stmt>) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitFunctionStmt(this)
        }

        val name: Token
        val params: List<Token>
        val body: List<Stmt>

        init {
            this.name = name
            this.params = params
            this.body = body
        }
    }

    class If(condition: Expr, thenBranch: Stmt, elseBranch: Stmt?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitIfStmt(this)
        }

        val condition: Expr
        val thenBranch: Stmt
        val elseBranch: Stmt?

        init {
            this.condition = condition
            this.thenBranch = thenBranch
            this.elseBranch = elseBranch
        }
    }

    class Print(expression: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitPrintStmt(this)
        }

        val expression: Expr

        init {
            this.expression = expression
        }
    }

    class Return(keyword: Token, value: Expr) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitReturnStmt(this)
        }

        val keyword: Token
        val value: Expr?

        init {
            this.keyword = keyword
            this.value = value
        }
    }

    class Var(name: Token, initializer: Expr?) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVarStmt(this)
        }

        val name: Token
        val initializer: Expr?

        init {
            this.name = name
            this.initializer = initializer
        }
    }

    class While(condition: Expr, body: Stmt) : Stmt() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitWhileStmt(this)
        }

        val condition: Expr
        val body: Stmt

        init {
            this.condition = condition
            this.body = body
        }
    }

    abstract fun <R> accept(visitor: Visitor<R>): R
}