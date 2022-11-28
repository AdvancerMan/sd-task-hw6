package visitor

import tokenizer.token.ParenthesisToken
import tokenizer.token.NumberToken
import tokenizer.token.OperationToken

class CalcVisitor : TokenVisitor<Int> {

    private val buffer = mutableListOf<Int>()

    override fun visit(numberToken: NumberToken) {
        buffer.add(numberToken.value)
    }

    override fun visit(parenthesisToken: ParenthesisToken) {
        throw CalcVisitorException("Unexpected parenthesis visited $parenthesisToken, expected reverse polish notation")
    }

    override fun visit(operationToken: OperationToken) {
        if (buffer.size < 2) {
            throw CalcVisitorException(
                "Not enough arguments (have ${buffer.size}) to apply operation ${operationToken.type}"
            )
        }

        val secondOperand = buffer.removeLast()
        val firstOperand = buffer.removeLast()
        val result = when (operationToken.type) {
            OperationToken.OperationType.PLUS -> firstOperand + secondOperand
            OperationToken.OperationType.MINUS -> firstOperand - secondOperand
            OperationToken.OperationType.DIV -> {
                if (secondOperand == 0) {
                    throw CalcVisitorException("Division by zero: $firstOperand / 0")
                }

                firstOperand / secondOperand
            }
            OperationToken.OperationType.MULTIPLY -> firstOperand * secondOperand
        }

        buffer.add(result)
    }

    override fun getResult(): Int {
        if (buffer.size != 1) {
            throw CalcVisitorException("Expected exactly one number remaining after reducing, got ${buffer.size}")
        }
        return buffer.first()
    }

    class CalcVisitorException(message: String? = null, cause: Throwable? = null) : VisitorException(message, cause)
}
