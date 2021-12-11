import java.io.File

fun main() {
    val openers = setOf('(', '[', '{', '<')
    val closersByOpeners = mapOf('(' to ')', '[' to ']', '{' to '}', '<' to '>')
    val scoresByClosers = mapOf(')' to 1L, ']' to 2L, '}' to 3L, '>' to 4L)

    var scores = mutableListOf<Long>()

    File("input").forEachLine { line ->
        var stack = mutableListOf<Char>()

        for (char in line) {
            if (openers.contains(char)) {
                stack.add(char)
                continue
            }
            if (stack.removeLastOrNull()?.let { closersByOpeners[it] } != char) {
                return@forEachLine // Line corrupted, ignore
            }
        }

        scores.add(stack.map { closersByOpeners[it] }.asReversed().fold(0L) { sum, closer -> sum * 5L + scoresByClosers[closer]!! })
    }

    println(scores.sorted()[scores.count() / 2]) // 2762335572
}
