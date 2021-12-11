import java.io.File

fun main() {
    val openers = setOf('(', '[', '{', '<')
    val openersByClosers = mapOf(')' to '(', ']' to '[', '}' to '{', '>' to '<')
    val scoresByClosers = mapOf(')' to 3, ']' to 57, '}' to 1197, '>' to 25137)

    var score = 0

    File("input").forEachLine { line ->
        var stack = mutableListOf<Char>()
        for (char in line) {
            if (openers.contains(char)) {
                stack.add(char)
                continue
            }
            if (stack.removeLastOrNull() != openersByClosers[char]) {
                score += scoresByClosers[char]!!
                break
            }
        }

        // Line incomplete, ignore
    }

    println(score) // 469755
}
