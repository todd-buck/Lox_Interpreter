import TokenType.*


@Suppress("SameParameterValue")
class Parser(tokens: List<Token>) {
    private class ParseError : RuntimeException()
    private var tokens : List<Token> = emptyList()
    private var current : Int = 0

    init {
        this.tokens = tokens
    }

    fun parse() : List<Statement> {
        val statements: MutableList<Statement> = ArrayList<Statement>()
        while (!isAtEnd()) {
            statements.add(statement())
        }

        return statements
    }

    private fun expression() : Statement.Expression {
        return equality()
    }

    private fun equality(): Expression {
        var expression: Expression = comparison()

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            val operator: Token = previous()
            val right: Expression = comparison()
            expression = Expression.Binary(expression, operator, right)
        }

        return expression
    }

    private fun match(vararg types: TokenType): Boolean {
        for (type in types) {
            if (check(type)) {
                advance()
                return true
            }
        }
        return false
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw error(peek(), message)
    }

    private fun check(type: TokenType): Boolean {
        return if (isAtEnd()) false else peek().type == type
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun isAtEnd(): Boolean {
        return peek().type === EOF
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    private fun error(token: Token, message: String): ParseError {
        Lox.error(token, message)
        return ParseError()
    }

    private fun synchronize() {
        advance()
        while (!isAtEnd()) {
            if (previous().type === SEMICOLON) return
            when (peek().type) {
                CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN -> return
                else -> {}
            }
            advance()
        }
    }

    private fun comparison(): Expression {
        var expression: Expression = term()
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            val operator = previous()
            val right: Expression = term()
            expression = Expression.Binary(expression, operator, right)
        }
        return expression
    }

    private fun term(): Expression {
        var expression: Expression = factor()
        while (match(MINUS, PLUS)) {
            val operator = previous()
            val right: Expression = factor()
            expression = Expression.Binary(expression, operator, right)
        }
        return expression
    }

    private fun factor(): Expression {
        var expression: Expression = unary()
        while (match(SLASH, STAR)) {
            val operator = previous()
            val right: Expression = unary()
            expression = Expression.Binary(expression, operator, right)
        }
        return expression
    }

    private fun unary(): Expression {
        if (match(BANG, MINUS)) {
            val operator = previous()
            val right: Expression = unary()
            return Expression.Unary(operator, right)
        }
        return primary()
    }

    private fun primary(): Expression {
        if (match(FALSE)) return Expression.Literal()
        if (match(TRUE)) return Expression.Literal()
        if (match(NIL)) return Expression.Literal()

        if (match(NUMBER, STRING)) {
            return Expression.Literal()
        }

        if (match(LEFT_PAREN)) {
            val expression: Statement.Expression = expression()
            consume(RIGHT_PAREN, "Expect ')' after expression.")
            return Expression.Grouping(expression)
        }

        throw error(peek(), "Expect expression.")
    }

    private fun statement() : Statement {
        if (match(PRINT)) return printStatement()

        return expressionStatement();
    }

    private fun printStatement() : Statement {
        val value: Statement.Expression = expression()
        consume(SEMICOLON, "Expect ':' after value.")
        return Statement.Print(value)
    }

    private fun expressionStatement(): Statement {
        val expr: Statement.Expression = expression()
        consume(SEMICOLON, "Expect ';' after expression.")
        return Statement.Expression(expr)
    }
}