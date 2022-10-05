//class ASTPrinter : Expr.Visitor<String> {
//    internal fun print(expression: Expr): String {
//        return expression.accept(this)
//    }
//
//    override fun visitAssignExpr(expression: Expr.Assign): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitBinaryExpr(expression: Expr.Binary): String {
//        return parenthesize(expression.operator.lexeme, expression.left, expression.right
//        )
//    }
//
//    override fun visitCallExpr(expression: Expr.Call): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitGetExpr(expression: Expr.Get): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitGroupingExpr(expression: Expr.Grouping): String {
//        return parenthesize("group", expression.expression)
//    }
//
//    override fun visitLiteralExpr(expression: Expr.Literal): String {
//        return if (expression.value == null) "nil" else expression.value.toString()
//    }
//
//    override fun visitLogicalExpr(expression: Expr.Logical): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitSetExpr(expression: Expr.Set): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitSuperExpr(expression: Expr.Super): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitThisExpr(expression: Expr.This): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitUnaryExpr(expression: Expr.Unary): String {
//        return parenthesize(expression.operator.lexeme, expression.right)
//    }
//
//    override fun visitVariableExpr(expression: Expr.Variable): String {
//        TODO("Not yet implemented")
//    }
//
//    private fun parenthesize(name: String, vararg expressions: Expr): String {
//        val builder = StringBuilder()
//        builder.append("(").append(name)
//
//        for (expr in expressions) {
//            builder.append(" ")
//            builder.append(expr.accept(this))
//        }
//
//        builder.append(")")
//        return builder.toString()
//    }
//}
//
//fun main() {
//    val expression: Expr = Expr.Binary(
//        Expr.Unary(
//            Token(TokenType.MINUS, "-", null, 1),
//            Expr.Literal()
//        ),
//        Token(TokenType.STAR, "*", null, 1),
//        Expr.Grouping(
//            Expr.Literal()
//        )
//    )
//
//    println(ASTPrinter().print(expression))
//}