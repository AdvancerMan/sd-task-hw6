package tokenizer

import tokenizer.token.Token
import kotlin.jvm.Throws

interface Tokenizer {

    @Throws(TokenizerException::class)
    fun handleCharacter(character: Char?)

    @Throws(TokenizerException::class)
    fun getResult(): List<Token>
}
