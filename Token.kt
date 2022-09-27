class Token(type: TokenType, lexeme: String, literal: Any?, line: Int) {
    val type: TokenType
    val lexeme: String

    //FIXME: Review use of ANY? instead of ANY
    private val literal: Any?
    val line: Int

    init {
        this.type = type
        this.lexeme = lexeme
        this.literal = literal
        this.line = line
    }

    override fun toString(): String {
        return "$type $lexeme $literal"
    }

    @JvmName("getType1")
    fun getType(): TokenType {
        return this.type
    }

    @JvmName("getLexeme1")
    fun getLexeme(): String {
        return this.lexeme
    }

}