package visitor

import tokenizer.token.ParenthesisToken
import tokenizer.token.NumberToken
import tokenizer.token.OperationToken
import java.io.PrintWriter

class PrintVisitor(

    private val writer: PrintWriter,
) : TokenVisitor<Unit> {

    override fun visit(numberToken: NumberToken) {
        write(numberToken.value.toString())
    }

    override fun visit(parenthesisToken: ParenthesisToken) {
        val stringValue = when (parenthesisToken.type) {
            ParenthesisToken.ParenthesisType.LEFT -> "("
            ParenthesisToken.ParenthesisType.RIGHT -> ")"
        }
        write(stringValue)
    }

    override fun visit(operationToken: OperationToken) {
        val stringValue = when (operationToken.type) {
            OperationToken.OperationType.PLUS -> "+"
            OperationToken.OperationType.MINUS -> "-"
            OperationToken.OperationType.DIV -> "/"
            OperationToken.OperationType.MULTIPLY -> "*"
        }
        write(stringValue)
    }

    private fun write(a: String) {
        writer.write(a)
        writer.write(" ")
    }

    override fun getResult() {
        // no result
    }
}
