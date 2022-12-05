package main//class ASTPrinter : klox.Expr.Visitor<String> {
//    internal fun print(expression: klox.Expr): String {
//        return expression.accept(this)
//    }
//
//    override fun visitAssignExpr(expression: klox.Expr.Assign): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitBinaryExpr(expression: klox.Expr.Binary): String {
//        return parenthesize(expression.operator.lexeme, expression.left, expression.right
//        )
//    }
//
//    override fun visitCallExpr(expression: klox.Expr.Call): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitGetExpr(expression: klox.Expr.Get): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitGroupingExpr(expression: klox.Expr.Grouping): String {
//        return parenthesize("group", expression.expression)
//    }
//
//    override fun visitLiteralExpr(expression: klox.Expr.Literal): String {
//        return if (expression.value == null) "nil" else expression.value.toString()
//    }
//
//    override fun visitLogicalExpr(expression: klox.Expr.Logical): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitSetExpr(expression: klox.Expr.Set): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitSuperExpr(expression: klox.Expr.Super): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitThisExpr(expression: klox.Expr.This): String {
//        TODO("Not yet implemented")
//    }
//
//    override fun visitUnaryExpr(expression: klox.Expr.Unary): String {
//        return parenthesize(expression.operator.lexeme, expression.right)
//    }
//
//    override fun visitVariableExpr(expression: klox.Expr.Variable): String {
//        TODO("Not yet implemented")
//    }
//
//    private fun parenthesize(name: String, vararg expressions: klox.Expr): String {
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
//    val expression: klox.Expr = klox.Expr.Binary(
//        klox.Expr.Unary(
//            klox.Token(klox.TokenType.MINUS, "-", null, 1),
//            klox.Expr.Literal()
//        ),
//        klox.Token(klox.TokenType.STAR, "*", null, 1),
//        klox.Expr.Grouping(
//            klox.Expr.Literal()
//        )
//    )
//
//    println(ASTPrinter().print(expression))
//}