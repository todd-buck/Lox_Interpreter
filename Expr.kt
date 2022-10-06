abstract class Expr {
    interface Visitor<R> {
        fun visitAssignExpr(expr: Assign): R
        fun visitBinaryExpr(expr: Binary): R
        fun visitCallExpr(expr: Call): R
        fun visitGetExpr(expr: Get): R
        fun visitGroupingExpr(expr: Grouping): R
        fun visitLiteralExpr(expr: Literal): R
        fun visitLogicalExpr(expr: Logical): R
        fun visitSetExpr(expr: Set): R
        fun visitSuperExpr(expr: Super): R
        fun visitThisExpr(expr: This): R
        fun visitUnaryExpr(expr: Unary): R
        fun visitVariableExpr(expr: Variable): R
    }

    class Assign(name: Token, value: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitAssignExpr(this)
        }

        val name: Token
        val value: Expr

        init {
            this.name = name
            this.value = value
        }
    }

    class Binary(left: Expr, operator: Token, right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitBinaryExpr(this)
        }

        val left: Expr
        val operator: Token
        val right: Expr

        init {
            this.left = left
            this.operator = operator
            this.right = right
        }
    }

    class Call(callee: Expr, paren: Token, arguments: List<Expr>) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitCallExpr(this)
        }

        val callee: Expr
        val paren: Token
        val arguments: List<Expr>

        init {
            this.callee = callee
            this.paren = paren
            this.arguments = arguments
        }
    }

    class Get(obj: Expr, name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) : R {
            return visitor.visitGetExpr(this)
        }

        private val obj: Expr
        private val name: Token

        init {
            this.obj = obj
            this.name = name
        }
    }

    class Grouping(expression: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>) : R {
            return visitor.visitGroupingExpr(this)
        }

        val expression: Expr

        init {
            this.expression = expression
        }
    }

    class Literal(value: Any?) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLiteralExpr(this)
        }

        var value : Any = Unit

        init {
            if (value != null) {
                this.value = value
            }
        }
    }

    class Logical(left: Expr, operator: Token, right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitLogicalExpr(this)
        }

        val left: Expr
        val operator: Token
        val right: Expr

        init {
            this.left = left
            this.operator = operator
            this.right = right
        }
    }

    class Set(obj: Expr, name: Token, value: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitSetExpr(this)
        }

        private val obj: Expr
        private val name: Token
        private val value: Expr

        init {
            this.obj = obj
            this.name = name
            this.value = value
        }

    }

    class Super(keyword: Token, method: Token) : Expr() {
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

    class This(keyword: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitThisExpr(this)
        }

        private val keyword: Token

        init {
            this.keyword = keyword
        }
    }

    class Unary(operator: Token, right: Expr) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitUnaryExpr(this)
        }

        val operator: Token
        val right: Expr

        init {
            this.operator = operator
            this.right = right
        }
    }

    class Variable(name: Token) : Expr() {
        override fun <R> accept(visitor: Visitor<R>): R {
            return visitor.visitVariableExpr(this)
        }

        val name: Token

        init {
            this.name = name
        }
    }

    abstract fun <R> accept(visitor: Visitor<R>): R
}