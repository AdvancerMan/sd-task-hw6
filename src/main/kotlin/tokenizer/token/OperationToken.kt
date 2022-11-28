package tokenizer.token

import visitor.TokenVisitor

data class OperationToken(

    val type: OperationType,
) : Token {

    override fun accept(visitor: TokenVisitor<*>) {
        visitor.visit(this)
    }

    enum class OperationType {
        PLUS,
        MINUS,
        DIV,
        MULTIPLY,
    }
}
