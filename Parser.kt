import Expr.Logical
import TokenType.*


@Suppress("SameParameterValue")
class Parser(tokens: List<Token>) {
    private class ParseError : RuntimeException()
    private var tokens : List<Token> = emptyList()
    private var current : Int = 0

    init {
        this.tokens = tokens
    }

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    //ch 9
    private fun and() : Expr {
        var expr: Expr = equality()

        while(match(AND)) {
            val operator: Token = previous()
            val right: Expr = equality()
            expr = Logical(expr, operator, right)
        }

        return expr
    }
    //

    private fun assignment(): Expr {
        val expr: Expr = or()

        if (match(EQUAL)) {
            val equals: Token = previous()
            val value: Expr = assignment()

            if (expr is Expr.Variable) {
                val name: Token = expr.name
                return Expr.Assign(name, value)
            }
            error(equals, "Invalid assignment target.")
        }

        return expr
    }

    private fun block(): List<Stmt> {
        val statements: MutableList<Stmt> = arrayListOf()

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration()!!)
        }

        consume(RIGHT_BRACE, "Expect '}' after block.")
        return statements
    }

    private fun call() : Expr {
        var expr: Expr = primary()

        while(true) {
            if(match(LEFT_PAREN)) {
                expr = finishCall(expr)
            } else {
                break
            }
        }

        return expr
    }

    private fun check(type: TokenType): Boolean {
        return if (isAtEnd()) false else peek().type == type
    }

    private fun comparison(): Expr {
        var expr: Expr = term()
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            val operator = previous()
            val right: Expr = term()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun consume(type: TokenType, message: String): Token {
        if (check(type)) return advance()
        throw error(peek(), message)
    }

    private fun declaration(): Stmt? {
        try {
            if(match(FUN)) return function("function")

            return if(match(VAR)) varDeclaration() else statement()
        } catch (error: ParseError) {
            synchronize()
            //FIXME: put something other than null
            return null
        }
    }

    private fun error(token: Token, message: String): ParseError {
        Lox.error(token, message)
        return ParseError()
    }

    private fun equality(): Expr {
        var expr: Expr = comparison()

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            val operator: Token = previous()
            val right: Expr = comparison()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun expression() : Expr {
        return assignment()
    }

    private fun expressionStatement(): Stmt {
        val expr: Expr = expression()
        consume(SEMICOLON, "Expect ';' after expression.")
        return Stmt.Expression(expr)
    }

    private fun factor(): Expr {
        var expr: Expr = unary()
        while (match(SLASH, STAR)) {
            val operator = previous()
            val right: Expr = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun finishCall(callee: Expr) : Expr {
        val arguments: MutableList<Expr> = arrayListOf()

        if(!check(RIGHT_PAREN)) {
            do {
                if(arguments.size >= 255) error(peek(),"Can't have more than 255 arguments.")
                arguments.add(expression())
            } while(match(COMMA))
        }

        val paren: Token = consume(RIGHT_PAREN,"Expect ')' after arguments.")

        return Expr.Call(callee, paren, arguments)
    }

    private fun forStatement(): Stmt {
        consume(LEFT_PAREN, "Expect '(' after 'for'.")

        val initializer: Stmt?
        if (match(SEMICOLON)) {
            initializer = null
        } else if (match(VAR)) {
            initializer = varDeclaration()
        } else {
            initializer = expressionStatement()
        }

        var condition: Expr? = null
        if (!check(SEMICOLON)) {
            condition = expression()
        }
        consume(SEMICOLON, "Expect ';' after loop condition.")

        var increment: Expr? = null
        if (!check(RIGHT_PAREN)) {
            increment = expression()
        }
        consume(RIGHT_PAREN, "Expect ')' after for clauses.")

        var body: Stmt = statement()

        if(increment != null) {
            body = Stmt.Block(listOf(body, Stmt.Expression(increment)))
        }

        if(condition == null) condition = Expr.Literal(true)

        body = Stmt.While(condition, body)

        if(initializer != null) {
            body = Stmt.Block(listOf(initializer, body))
        }

        return body
    }

    private fun function(kind: String) : Stmt.Function {
        val name: Token = consume(IDENTIFIER, "Expect $kind name.")
        consume(LEFT_PAREN, "Expect '(' after $kind name.")

        val parameters: MutableList<Token> = arrayListOf()

        if(!check(RIGHT_PAREN)) {
            do {
                if(parameters.size >= 255) error(peek(), "Can't have more than 255 parameters.")

                parameters.add(consume(IDENTIFIER, "Expect parameter name."))
            } while(match(COMMA))
        }
        consume(RIGHT_PAREN,"Expect ')' after parameters.")

        consume(LEFT_BRACE,"Expect '{' before $kind body.")

        val body: List<Stmt> = block()
        return Stmt.Function(name, parameters, body)
    }

    private fun ifStatement() : Stmt {
        consume(LEFT_PAREN, "Expect '(' after 'if'.")
        val condition: Expr = expression()
        consume(RIGHT_PAREN, "Expect ')' after if condition.")

        val thenBranch: Stmt = statement()
        var elseBranch: Stmt? = null
        if (match(ELSE)) {
            elseBranch = statement()
        }

        return Stmt.If(condition, thenBranch, elseBranch)
    }

    private fun isAtEnd(): Boolean {
        return peek().type === EOF
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

    private fun or() : Expr {
        var expr: Expr = and()

        while (match(OR)) {
            val operator: Token = previous()
            val right: Expr = and()
            expr = Logical(expr, operator, right)
        }

        return expr
    }

    fun parse() : List<Stmt> {
        val statements: MutableList<Stmt> = arrayListOf()
        while (!isAtEnd()) {
            statements.add(declaration()!!)
        }

        return statements
    }

    private fun peek(): Token {
        return tokens[current]
    }

    private fun previous(): Token {
        return tokens[current - 1]
    }

    private fun primary(): Expr {
        if (match(FALSE)) return Expr.Literal(false)
        if (match(TRUE)) return Expr.Literal(true)
        if (match(NIL)) return Expr.Literal(null)

        if (match(NUMBER, STRING)) {
            return Expr.Literal(previous().literal)
        }

        if (match(IDENTIFIER)) {
            return Expr.Variable(previous())
        }

        if (match(LEFT_PAREN)) {
            val expr: Expr = expression()
            consume(RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }

        throw error(peek(), "Expect expression.")
    }

    private fun printStatement() : Stmt {
        val value: Expr = expression()
        consume(SEMICOLON, "Expect ':' after value.")
        return Stmt.Print(value)
    }

    private fun returnStatement(): Stmt {
        val keyword: Token = previous()
        var value: Expr? = null

        if(!check(SEMICOLON)) {
            value = expression()
        }

        consume(SEMICOLON, "Expect ';' after return value.")
        //FIXME: Make sure !! can be used
        return Stmt.Return(keyword, value!!)
    }

    private fun statement() : Stmt {
        if(match(FOR)) return forStatement()
        if(match(IF)) return ifStatement()
        if(match(LEFT_BRACE)) return Stmt.Block(block())
        if(match(PRINT)) return printStatement()
        if(match(RETURN)) return returnStatement()
        if(match(WHILE)) return whileStatement()

        return expressionStatement()
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

    private fun term(): Expr {
        var expr: Expr = factor()
        while (match(MINUS, PLUS)) {
            val operator = previous()
            val right: Expr = factor()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun unary(): Expr {
        if (match(BANG, MINUS)) {
            val operator: Token = previous()
            val right: Expr = unary()
            return Expr.Unary(operator, right)
        }
        return call()
    }

    private fun varDeclaration(): Stmt {
        val name: Token = consume(IDENTIFIER, "Expect variable name.")

        var initializer: Expr? = null

        if (match(EQUAL)) {
            initializer = expression()
        }

        consume(SEMICOLON, "Expect ';' after variable declaration.")
        return Stmt.Var(name, initializer)
    }

    private fun whileStatement(): Stmt {
        consume(LEFT_PAREN, "Expect '(' after 'while'.")
        val condition: Expr = expression()
        consume(RIGHT_PAREN, "Expect ')' after condition.")
        val body: Stmt = statement()

        return Stmt.While(condition, body)
    }

}