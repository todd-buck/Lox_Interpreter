class Token(val type: TokenType, val lexeme: String, val literal: Any?, val line: Int) {

    @JvmName("getLexeme1")
    fun getLexeme(): String {
        return lexeme
    }

    @JvmName("getType1")
    fun getType(): TokenType {
        return type
    }

    override fun toString(): String {
        return "$type $lexeme $literal"
    }
}