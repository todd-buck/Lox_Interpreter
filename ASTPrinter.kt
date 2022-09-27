class ASTPrinter : Expression.Visitor<String> {
    internal fun print(expression: Expression): String {
        return expression.accept(this)
    }

    override fun visitAssignExpr(expression: Expression.Assign): String {
        TODO("Not yet implemented")
    }

    override fun visitBinaryExpr(expression: Expression.Binary): String {
        return parenthesize(expression.operator.lexeme, expression.left, expression.right
        )
    }

    override fun visitCallExpr(expression: Expression.Call): String {
        TODO("Not yet implemented")
    }

    override fun visitGetExpr(expression: Expression.Get): String {
        TODO("Not yet implemented")
    }

    override fun visitGroupingExpr(expression: Expression.Grouping): String {
        return parenthesize("group", expression.expression)
    }

    override fun visitLiteralExpr(expression: Expression.Literal): String {
        return if (expression.value == null) "nil" else expression.value.toString()
    }

    override fun visitLogicalExpr(expression: Expression.Logical): String {
        TODO("Not yet implemented")
    }

    override fun visitSetExpr(expression: Expression.Set): String {
        TODO("Not yet implemented")
    }

    override fun visitSuperExpr(expression: Expression.Super): String {
        TODO("Not yet implemented")
    }

    override fun visitThisExpr(expression: Expression.This): String {
        TODO("Not yet implemented")
    }

    override fun visitUnaryExpr(expression: Expression.Unary): String {
        return parenthesize(expression.operator.lexeme, expression.right)
    }

    override fun visitVariableExpr(expression: Expression.Variable): String {
        TODO("Not yet implemented")
    }

    private fun parenthesize(name: String, vararg expressions: Expression): String {
        val builder = StringBuilder()
        builder.append("(").append(name)

        for (expr in expressions) {
            builder.append(" ")
            builder.append(expr.accept(this))
        }

        builder.append(")")
        return builder.toString()
    }
}

fun main() {
    val expression: Expression = Expression.Binary(
        Expression.Unary(
            Token(TokenType.MINUS, "-", null, 1),
            Expression.Literal()
        ),
        Token(TokenType.STAR, "*", null, 1),
        Expression.Grouping(
            Expression.Literal()
        )
    )

    println(ASTPrinter().print(expression))
}