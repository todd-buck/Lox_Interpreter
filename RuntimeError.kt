class RuntimeError(token: Token, message: String?) :
    RuntimeException(message) {
    val token: Token

    init {
        this.token = token
    }
}