package tokenizer.token

import visitor.TokenVisitor

class ParenthesisToken(

    val type: ParenthesisType,
) : Token {

    override fun accept(visitor: TokenVisitor<*>) {
        visitor.visit(this)
    }

    enum class ParenthesisType {
        LEFT,
        RIGHT,
    }
}
