import Expression.*

abstract class Statement {
    internal interface Visitor<R> {
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

    internal class Block(statements: Statement) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBlockStmt(this)
        }

        private val statements: Statement

        init {
            this.statements = statements
        }
    }

    internal class Class(name: Token, superclass: Variable, methods: List<Function>) : Statement() {
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

    internal class Expression(expression: Expression) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitExpressionStmt(this)
        }

        private val expression: Expression

        init {
            this.expression = expression
        }
    }

    internal class Function(name: Token, params: List<Token>, body: List<Statement>) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitFunctionStmt(this)
        }

        private val name: Token
        private val params: List<Token>
        private val body: List<Statement>

        init {
            this.name = name
            this.params = params
            this.body = body
        }
    }

    internal class If(condition: Expression, thenBranch: Statement, elseBranch: Statement) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitIfStmt(this)
        }

        private val condition: Expression
        private val thenBranch: Statement
        private val elseBranch: Statement

        init {
            this.condition = condition
            this.thenBranch = thenBranch
            this.elseBranch = elseBranch
        }
    }

    internal class Print(expression: Expression) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitPrintStmt(this)
        }

        private val expression: Expression

        init {
            this.expression = expression
        }
    }

    internal class Return(keyword: Token, value: Expression) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitReturnStmt(this)
        }

        private val keyword: Token
        private val value: Expression

        init {
            this.keyword = keyword
            this.value = value
        }
    }

    internal class Var(name: Token, initializer: Expression) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVarStmt(this)
        }

        private val name: Token
        private val initializer: Expression

        init {
            this.name = name
            this.initializer = initializer
        }
    }

    internal class While(condition: Expression, body: Statement) : Statement() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitWhileStmt(this)
        }

        private val condition: Expression
        private val body: Statement

        init {
            this.condition = condition
            this.body = body
        }
    }

    abstract fun <R> accept(visitor: Visitor<R>): R
}