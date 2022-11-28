package visitor

import tokenizer.token.NumberToken
import tokenizer.token.OperationToken
import tokenizer.token.ParenthesisToken
import java.io.PrintStream

class PrintVisitor(

    private val writer: PrintStream,
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
        writer.print(a)
        writer.print(" ")
    }

    override fun getResult() {
        writer.flush()
    }
}
