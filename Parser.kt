import Expr.Logical
import TokenType.*


@Suppress("SameParameterValue")
class Parser(private val tokens: List<Token>) {
    private class ParseError : RuntimeException()
    private var current = 0

    private fun advance(): Token {
        if (!isAtEnd()) current++
        return previous()
    }

    private fun and() : Expr {
        var expr = equality()

        while(match(AND)) {
            val operator = previous()
            val right = equality()
            expr = Logical(expr, operator, right)
        }

        return expr
    }

    private fun assignment(): Expr {
        val expr = or()

        if (match(EQUAL)) {
            val equals = previous()
            val value = assignment()

            if (expr is Expr.Variable) {
                return Expr.Assign(expr.name, value)
            } else if(expr is Expr.Get) {
                return Expr.Set(expr.obj, expr.name, value)
            }
            error(equals, "Invalid assignment target.")
        }

        return expr
    }

    private fun block(): List<Stmt?> {
        val statements: MutableList<Stmt?> = arrayListOf()

        while (!check(RIGHT_BRACE) && !isAtEnd()) {
            statements.add(declaration())
        }

        consume(RIGHT_BRACE, "Expect '}' after block.")
        return statements
    }

    private fun call() : Expr {
        var expr = primary()

        while(true) {
            if(match(LEFT_PAREN)) {
                expr = finishCall(expr)
            } else if(match(DOT)) {
                val name = consume(IDENTIFIER, "Expect property name after '.'.")
                expr = Expr.Get(expr, name)
            } else {
                break
            }
        }

        return expr
    }

    private fun check(type: TokenType): Boolean {
        return if (isAtEnd()) false else (peek().type == type)
    }

    private fun classDeclaration() : Stmt.Class {
        val name = consume(IDENTIFIER,"Expect class name.")

        var superclass: Expr.Variable? = null

        if(match(LESS)) {
            consume(IDENTIFIER,"Expect superclass name.")
            superclass = Expr.Variable(previous())
        }

        consume(LEFT_BRACE,"Expect '{' before class body.")

        val methods: MutableList<Stmt.Function> = arrayListOf()
        while(!check(RIGHT_BRACE) && !isAtEnd()) {
            methods.add(function("method"))
        }

        consume(RIGHT_BRACE,"Expect '}' after class body.")

        return Stmt.Class(name, superclass ,methods)
    }

    private fun comparison(): Expr {
        var expr: Expr = term()
        while (match(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            val operator = previous()
            val right = term()
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
            if(match(CLASS)) return classDeclaration()
            if(match(FUN)) return function("function")
            if(match(VAR)) return varDeclaration()

            return statement()
        } catch (error: ParseError) {
            synchronize()
            return null
        }
    }

    private fun error(token: Token, message: String): ParseError {
        Lox.error(token, message)
        return ParseError()
    }

    private fun equality(): Expr {
        var expr = comparison()

        while (match(BANG_EQUAL, EQUAL_EQUAL)) {
            val operator = previous()
            val right = comparison()
            expr = Expr.Binary(expr, operator, right)
        }

        return expr
    }

    private fun expression() : Expr {
        return assignment()
    }

    private fun expressionStatement(): Stmt {
        val expr = expression()
        consume(SEMICOLON, "Expect ';' after expression.")
        return Stmt.Expression(expr)
    }

    private fun factor(): Expr {
        var expr = unary()
        while (match(SLASH, STAR)) {
            val operator = previous()
            val right: Expr = unary()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun finishCall(callee: Expr) : Expr {
        val arguments: MutableList<Expr> = mutableListOf()

        if(!check(RIGHT_PAREN)) {
            do {
                if(arguments.size >= 255) error(peek(),"Can't have more than 255 arguments.")
                arguments.add(expression())
            } while(match(COMMA))
        }

        val paren = consume(RIGHT_PAREN,"Expect ')' after arguments.")

        return Expr.Call(callee, paren, arguments)
    }

    private fun forStatement(): Stmt {
        consume(LEFT_PAREN, "Expect '(' after 'for'.")

        val initializer = when {
            match(SEMICOLON) -> null
            match(VAR) -> varDeclaration()
            else -> expressionStatement()
        }

        var condition = if (!check(SEMICOLON)) expression() else null

        consume(SEMICOLON, "Expect ';' after loop condition.")

        val increment = if(!check(RIGHT_PAREN)) expression() else null

        consume(RIGHT_PAREN, "Expect ')' after for clauses.")

        var body = statement()

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
        val name = consume(IDENTIFIER, "Expect $kind name.")
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

        val body = block()
        return Stmt.Function(name, parameters, body)
    }

    private fun ifStatement() : Stmt {
        consume(LEFT_PAREN, "Expect '(' after 'if'.")
        val condition: Expr = expression()
        consume(RIGHT_PAREN, "Expect ')' after if condition.")

        val thenBranch = statement()
        val elseBranch = if(match(ELSE)) statement() else null

        return Stmt.If(condition, thenBranch, elseBranch)
    }

    private fun isAtEnd(): Boolean {
        return (peek().type == EOF)
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
        var expr = and()

        while (match(OR)) {
            val operator = previous()
            val right = and()
            expr = Logical(expr, operator, right)
        }

        return expr
    }

    fun parse() : List<Stmt?> {
        val statements: MutableList<Stmt?> = arrayListOf()
        while (!isAtEnd()) {
            statements.add(declaration())
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
        if (match(FALSE)) return Expr.Literal(FALSE)
        if (match(TRUE)) return Expr.Literal(TRUE)
        if (match(NIL)) return Expr.Literal(NIL)
        if (match(NUMBER, STRING)) return Expr.Literal(previous().literal!!)
        if (match(THIS)) return Expr.This(previous())
        if (match(IDENTIFIER)) return Expr.Variable(previous())

        if (match(LEFT_PAREN)) {
            val expr = expression()
            consume(RIGHT_PAREN, "Expect ')' after expression.")
            return Expr.Grouping(expr)
        }

        if(match(SUPER)) {
            val keyword = previous()
            consume(DOT,"Expect '.' after 'super'.")
            val method: Token = consume(IDENTIFIER,"Expect superclass method name.")
            return Expr.Super(keyword, method)
        }

        throw error(peek(), "Expect expression.")
    }

    private fun printStatement() : Stmt {
        val value = expression()
        consume(SEMICOLON, "Expect ':' after value.")
        return Stmt.Print(value)
    }

    private fun returnStatement(): Stmt {
        val keyword: Token = previous()
        val value = if(!(check(SEMICOLON))) expression() else null
        consume(SEMICOLON, "Expect ';' after return value.")

        return Stmt.Return(keyword, value)
    }

    private fun statement() : Stmt {
        if(match(FOR)) return forStatement()
        if(match(IF)) return ifStatement()
        if(match(PRINT)) return printStatement()
        if(match(RETURN)) return returnStatement()
        if(match(WHILE)) return whileStatement()
        if(match(LEFT_BRACE)) return Stmt.Block(block())

        return expressionStatement()
    }

    private fun synchronize() {
        advance()
        while (!isAtEnd()) {
            if (previous().type === SEMICOLON) return
            when (peek().type) {
                CLASS, FUN, VAR, FOR, IF, WHILE, PRINT, RETURN -> return
                else -> advance()
            }
        }
    }

    private fun term(): Expr {
        var expr: Expr = factor()
        while (match(MINUS, PLUS)) {
            val operator = previous()
            val right = factor()
            expr = Expr.Binary(expr, operator, right)
        }
        return expr
    }

    private fun unary(): Expr {
        if (match(BANG, MINUS)) {
            val operator = previous()
            val right = unary()
            return Expr.Unary(operator, right)
        }
        return call()
    }

    private fun varDeclaration(): Stmt {
        val name = consume(IDENTIFIER, "Expect variable name.")

        val initializer = if (match(EQUAL)) expression() else null

        consume(SEMICOLON, "Expect ';' after variable declaration.")
        return Stmt.Var(name, initializer)
    }

    private fun whileStatement(): Stmt {
        consume(LEFT_PAREN, "Expect '(' after 'while'.")
        val condition = expression()
        consume(RIGHT_PAREN, "Expect ')' after condition.")
        val body = statement()

        return Stmt.While(condition, body)
    }

}