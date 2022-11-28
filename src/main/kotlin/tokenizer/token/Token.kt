package tokenizer.token

import visitor.TokenVisitor

sealed interface Token {

    fun accept(visitor: TokenVisitor<*>)
}