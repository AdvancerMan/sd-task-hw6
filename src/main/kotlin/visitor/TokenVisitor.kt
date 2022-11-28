package visitor

import tokenizer.token.ParenthesisToken
import tokenizer.token.NumberToken
import tokenizer.token.OperationToken
import kotlin.jvm.Throws

interface TokenVisitor<R> {

    @Throws(VisitorException::class)
    fun visit(numberToken: NumberToken)

    @Throws(VisitorException::class)
    fun visit(parenthesisToken: ParenthesisToken)

    @Throws(VisitorException::class)
    fun visit(operationToken: OperationToken)

    @Throws(VisitorException::class)
    fun getResult(): R
}
