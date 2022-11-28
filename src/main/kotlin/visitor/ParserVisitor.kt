package visitor

import tokenizer.token.ParenthesisToken
import tokenizer.token.NumberToken
import tokenizer.token.OperationToken
import tokenizer.token.Token

class ParserVisitor : TokenVisitor<List<Token>> {

    private val result = mutableListOf<Token>()
    private val buffer = mutableListOf<Token>()

    override fun visit(numberToken: NumberToken) {
        result.add(numberToken)
    }

    override fun visit(parenthesisToken: ParenthesisToken) {
        when (parenthesisToken.type) {
            ParenthesisToken.ParenthesisType.LEFT -> {
                buffer.add(parenthesisToken)
            }

            ParenthesisToken.ParenthesisType.RIGHT -> {
                while (buffer.last() !is ParenthesisToken) {
                    result.add(buffer.removeLast())
                }
                buffer.removeLast()
            }
        }
    }

    private fun getOperationTypePriority(type: OperationToken.OperationType): Int {
        return when (type) {
            OperationToken.OperationType.PLUS -> 1
            OperationToken.OperationType.MINUS -> 1
            OperationToken.OperationType.DIV -> 2
            OperationToken.OperationType.MULTIPLY -> 2
        }
    }

    private fun Token.checkHasGreaterOrEqualPriorityTo(operationToken: OperationToken): Boolean {
        if (this !is OperationToken) {
            return false
        }

        val thisPriority = getOperationTypePriority(this.type)
        val otherPriority = getOperationTypePriority(operationToken.type)
        return thisPriority >= otherPriority
    }

    override fun visit(operationToken: OperationToken) {
        while (buffer.isNotEmpty() && buffer.last().checkHasGreaterOrEqualPriorityTo(operationToken)) {
            result.add(buffer.removeLast())
        }
        buffer.add(operationToken)
    }

    override fun getResult(): List<Token> {
        result.addAll(buffer.asReversed())
        return result
    }
}
