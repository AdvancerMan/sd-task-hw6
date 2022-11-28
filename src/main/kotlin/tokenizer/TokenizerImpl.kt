package tokenizer

import tokenizer.token.NumberToken
import tokenizer.token.OperationToken
import tokenizer.token.ParenthesisToken
import tokenizer.token.Token

class TokenizerImpl : Tokenizer {

    private var state = State.START
    private val tokens = mutableListOf<Token>()
    private var parenthesisBalance = 0
    private val numberBuffer = StringBuilder()

    override fun handleCharacter(character: Char?) {
        if (character?.isWhitespace() == true) {
            return
        }

        when (state) {
            State.START -> handleStartState(character)
            State.NUMBER -> handleNumberState(character)
            State.ERROR -> throw getUnexpectedCharacterException(character)
            State.END -> throw getUnexpectedCharacterException(character)
        }
    }

    private fun handleStartState(character: Char?) {
        if (character == '(') {
            tokens.add(ParenthesisToken(ParenthesisToken.ParenthesisType.LEFT))
            parenthesisBalance++
            return
        }

        if (character?.isDigit() == true) {
            numberBuffer.append(character)
            state = State.NUMBER
            return
        }

        throw getUnexpectedCharacterException(character)
    }

    private fun handleNumberState(character: Char?) {
        if (character == null) {
            flushNumberBuffer()
            state = State.END
            return
        }

        if (character == ')') {
            flushNumberBuffer()

            parenthesisBalance--
            if (parenthesisBalance == 0) {
                throw getUnexpectedCharacterException(character, "too many closing parenthesis handled, balance became negative")
            }

            tokens.add(ParenthesisToken(ParenthesisToken.ParenthesisType.RIGHT))
            state = State.START
            return
        }

        if (character.isDigit()) {
            numberBuffer.append(character)
            return
        }

        val operationToken = parseOperationToken(character)
        if (operationToken != null) {
            flushNumberBuffer()
            tokens.add(operationToken)
            state = State.START
            return
        }

        throw getUnexpectedCharacterException(character)
    }

    private fun parseOperationToken(character: Char): OperationToken? {
        return when (character) {
            '+' -> OperationToken(OperationToken.OperationType.PLUS)
            '-' -> OperationToken(OperationToken.OperationType.MINUS)
            '/' -> OperationToken(OperationToken.OperationType.DIV)
            '*' -> OperationToken(OperationToken.OperationType.MULTIPLY)
            else -> null
        }
    }

    private fun flushNumberBuffer() {
        val number = try {
            numberBuffer.toString().toInt()
        } catch (e: NumberFormatException) {
            throw getUnexpectedCharacterException(
                numberBuffer.lastOrNull(),
                "Too long integer not fitting java int $numberBuffer",
                e,
            )
        }

        tokens.add(NumberToken(number))
        numberBuffer.clear()
    }

    private fun getUnexpectedCharacterException(
        character: Char?,
        additionalMessage: String? = null,
        cause: Exception? = null,
    ): Exception {
        val message = "Unexpected character '$character' on state $state"
            .plus(additionalMessage?.let { ", $it" } ?: "")
        state = State.ERROR
        return TokenizerException(message, cause)
    }

    override fun getResult(): List<Token> {
        if (parenthesisBalance != 0) {
            throw TokenizerException("Invalid parenthesis balancer handled, expected 0, got $parenthesisBalance")
        }
        if (state != State.END) {
            throw TokenizerException("Requested result at unexpected state, expected END, got $state")
        }

        return tokens
    }

    private enum class State {
        START,
        NUMBER,
        ERROR,
        END,
    }
}
