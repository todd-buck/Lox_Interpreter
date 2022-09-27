abstract class Expression {
    interface Visitor<R> {
        fun visitAssignExpr(expression: Assign): R
        fun visitBinaryExpr(expression: Binary): R
        fun visitCallExpr(expression: Call): R
        fun visitGetExpr(expression: Get): R
        fun visitGroupingExpr(expression: Grouping): R
        fun visitLiteralExpr(expression: Literal): R
        fun visitLogicalExpr(expression: Logical): R
        fun visitSetExpr(expression: Set): R
        fun visitSuperExpr(expression: Super): R
        fun visitThisExpr(expression: This): R
        fun visitUnaryExpr(expression: Unary): R
        fun visitVariableExpr(expression: Variable): R
    }

    class Assign(name: Token, value: Expression) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitAssignExpr(this)
        }

        private val name: Token
        private val value: Expression

        init {
            this.name = name
            this.value = value
        }
    }

    class Binary(left: Expression, operator: Token, right: Expression) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinaryExpr(this)
        }

        val left: Expression
        val operator: Token
        val right: Expression

        init {
            this.left = left
            this.operator = operator
            this.right = right
        }
    }

    class Call(callee: Expression, paren: Token, arguments: List<Expression>) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitCallExpr(this)
        }

        private val callee: Expression
        private val paren: Token
        private val arguments: List<Expression>

        init {
            this.callee = callee
            this.paren = paren
            this.arguments = arguments
        }
    }

    class Get(obj: Expression, name: Token) : Expression() {
        override fun <R> accept(visitor: Visitor<R>) : R {
            return visitor.visitGetExpr(this)
        }

        private val obj: Expression
        private val name: Token

        init {
            this.obj = obj
            this.name = name
        }
    }

    class Grouping(expression: Expression) : Expression() {
        override fun <R> accept(visitor: Visitor<R>) : R {
            return visitor.visitGroupingExpr(this)
        }

        val expression: Expression

        init {
            this.expression = expression
        }
    }

    class Literal : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLiteralExpr(this)
        }

        val value : Any? = null
    }

    class Logical(left: Expression, operator: Token, right: Expression) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLogicalExpr(this)
        }

        private val left: Expression
        private val operator: Token
        private val right: Expression

        init {
            this.left = left
            this.operator = operator
            this.right = right
        }
    }

    class Set(obj: Expression, name: Token, value: Expression) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitSetExpr(this)
        }

        private val obj: Expression
        private val name: Token
        private val value: Expression

        init {
            this.obj = obj
            this.name = name
            this.value = value
        }

    }

    class Super(keyword: Token, method: Token) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitSuperExpr(this)
        }

        private val keyword: Token
        private val method: Token

        init {
            this.keyword = keyword
            this.method = method
        }
    }

    class This(keyword: Token) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitThisExpr(this)
        }

        private val keyword: Token

        init {
            this.keyword = keyword
        }
    }

    class Unary(operator: Token, right: Expression) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitUnaryExpr(this)
        }

        val operator: Token
        val right: Expression

        init {
            this.operator = operator
            this.right = right
        }
    }

    class Variable(name: Token) : Expression() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVariableExpr(this)
        }

        private val name: Token

        init {
            this.name = name
        }
    }

    abstract fun <R> accept(visitor: Visitor<R>): R
}