import tokenizer.TokenizerException
import tokenizer.TokenizerImpl
import visitor.CalcVisitor
import visitor.ParserVisitor
import visitor.PrintVisitor
import visitor.VisitorException

fun main() {
    val reader = System.`in`.bufferedReader()

    try {
        val tokenizer = TokenizerImpl()
        while (true) {
            val currentCharacter = reader.read()
            if (currentCharacter == -1) {
                tokenizer.handleCharacter(null)
                break
            }

            tokenizer.handleCharacter(currentCharacter.toChar())
        }

        val parserVisitor = ParserVisitor()
        tokenizer.getResult().forEach { it.accept(parserVisitor) }

        val printVisitor = PrintVisitor(System.out)
        parserVisitor.getResult().forEach { it.accept(printVisitor) }
        println()

        val calcVisitor = CalcVisitor()
        parserVisitor.getResult().forEach { it.accept(calcVisitor) }
        println("Result is ${calcVisitor.getResult()}")
    } catch (e: VisitorException) {
        println("Visitor error occurred: ${e.message}")
    } catch (e: TokenizerException) {
        println("Tokenizer error occurred: ${e.message}")
    }
}
