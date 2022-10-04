package com.craftinginterpreters.lox

import Expression
import Lox.runtimeError
import Statement
import Token
import TokenType


class Interpreter : Expression.Visitor<Any>, Statement.Visitor<Void> {
    private var environment = Environment()

    override fun visitAssignExpr(expression: Expression.Assign): Any {
        val value = evaluate(expression.value)
        environment.assign(expression.name, value)
        return value
    }

    override fun visitBinaryExpr(expression: Expression.Binary): Any {
        val left = evaluate(expression.left)
        val right = evaluate(expression.right)

        return when (expression.operator.type) {
            TokenType.MINUS -> {
                checkNumberOperand(expression.operator, right)
                return left as Double - right as Double
            }
            TokenType.SLASH -> {
                checkNumberOperands(expression.operator, left, right)
                return left as Double / right as Double
            }
            TokenType.STAR -> {
                checkNumberOperands(expression.operator, left, right)
                return left as Double * right as Double
            }
            TokenType.PLUS -> {
                if (left is Double && right is Double) return left + right
                if (left is String && right is String) return left + right
                throw RuntimeError(
                    expression.operator,
                    "Operands must be two numbers or two strings."
                )
            }
            TokenType.GREATER -> {
                checkNumberOperands(expression.operator, left, right)
                return left as Double > right as Double
            }
            TokenType.GREATER_EQUAL -> {
                checkNumberOperands(expression.operator, left, right)
                return left as Double >= right as Double
            }
            TokenType.LESS -> {
                checkNumberOperands(expression.operator, left, right)
                return right as Double > left as Double
            }
            TokenType.LESS_EQUAL -> {
                checkNumberOperands(expression.operator, left, right)
                return right as Double >= left as Double
            }
            TokenType.BANG_EQUAL -> return !isEqual(left, right)
            TokenType.EQUAL_EQUAL -> return isEqual(left, right)
            else -> {
                return Unit
            }
        }
    }

    private fun checkNumberOperand(operator: Token, right: Any) {
        if (right is Double) return
        throw RuntimeError(operator, "Operand must be a number.")
    }

    override fun visitCallExpr(expression: Expression.Call): Any {
        TODO("Not yet implemented")
    }

    override fun visitGetExpr(expression: Expression.Get): Any {
        TODO("Not yet implemented")
    }

    override fun visitGroupingExpr(expression: Expression.Grouping): Any {
        return evaluate(expression.expression)
    }

    override fun visitLiteralExpr(expression: Expression.Literal): Any {
        return expression.value!!

    }

    override fun visitLogicalExpr(expression: Expression.Logical): Any {
        TODO("Not yet implemented")
    }

    override fun visitSetExpr(expression: Expression.Set): Any {
        TODO("Not yet implemented")
    }

    override fun visitSuperExpr(expression: Expression.Super): Any {
        TODO("Not yet implemented")
    }

    override fun visitThisExpr(expression: Expression.This): Any {
        TODO("Not yet implemented")
    }

    override fun visitUnaryExpr(expression: Expression.Unary): Any {
        val right = evaluate(expression.right)

        when (expression.operator.type) {
            TokenType.MINUS -> return right
            TokenType.BANG -> return !isTruthy(right)
            else -> {
                return Unit;
            }
        }
    }

    override fun visitVariableExpr(expression: Expression.Variable): Any {
        return environment.get(expression.name)!!;
    }

    private fun evaluate(expression: Expression): Any {
        return expression.accept(this)
    }

    override fun visitBlockStmt(stmt: Statement.Block): Void {
        executeBlock(stmt.statements, Environment(environment));
    }

    fun executeBlock(
        statements: Statement,
        environment: Environment?
    ) {
        val previous = this.environment
        try {
            this.environment = environment!!
            for (statement in statements) {
                execute(statement)
            }
        } finally {
            this.environment = previous
        }
    }

    override fun visitClassStmt(stmt: Statement.Class): Void {
        TODO("Not yet implemented")
    }

    override fun visitExpressionStmt(stmt: Statement.Expression): Void {
        TODO("Not yet implemented")
    }

    override fun visitFunctionStmt(stmt: Statement.Function): Void {
        TODO("Not yet implemented")
    }

    override fun visitIfStmt(stmt: Statement.If): Void {
        TODO("Not yet implemented")
    }

    override fun visitPrintStmt(stmt: Statement.Print): Void? {
        val value = evaluate(stmt.expression)
        println(stringify(value))
        return null
    }

    override fun visitReturnStmt(stmt: Statement.Return): Void {
        TODO("Not yet implemented")
    }

    override fun visitVarStmt(stmt: Statement.Var): Void {
        var value: Any? = null
        if (stmt.initializer != null) {
            value = evaluate(stmt.initializer)
        }

        environment.define(stmt.name.lexeme, value!!)
        return null
    }

    override fun visitWhileStmt(stmt: Statement.While): Void {
        TODO("Not yet implemented")
    }

    private fun isTruthy(any: Any): Boolean {
        if (any is Unit) return false
        if (any is Boolean) return any as Boolean
        return true
    }

    private fun isEqual(a: Any, b: Any): Boolean {
        if (a is Unit && b is Unit) return true
        if (a is Unit) return false

        return a == b
    }

    private fun checkNumberOperands(operator: Token, left: Any, right: Any) {
        if (left is Double && right is Double) return
        throw RuntimeError(operator, "Operands must be numbers.")
    }

    fun interpret(statements: List<Statement>) {
        try {
            for (statement in statements) {
                execute(statement)
            }
        } catch (error: RuntimeError) {
            runtimeError(error)
        }
    }

    private fun execute(stmt: Statement) {
        stmt.accept(this)
    }

    private fun stringify(obj: Any?): String {
        if (obj == null) return "nil"
        if (obj is Double) {
            var text = obj.toString()
            if (text.endsWith(".0")) {
                text = text.substring(0, text.length - 2)
            }
            return text
        }
        return obj.toString()
    }
}