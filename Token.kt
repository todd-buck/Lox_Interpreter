class Token(type: TokenType, lexeme: String, literal: Any?, line: Int) {
    val type: TokenType
    val lexeme: String

    val literal: Any?
    val line: Int

    init {
        this.type = type
        this.lexeme = lexeme
        this.literal = literal
        this.line = line
    }

    @JvmName("getLexeme1")
    fun getLexeme(): String {
        return this.lexeme
    }

    @JvmName("getType1")
    fun getType(): TokenType {
        return this.type
    }

    override fun toString(): String {
        return "$type $lexeme $literal"
    }

}