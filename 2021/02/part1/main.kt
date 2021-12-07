import java.io.File

fun main() {
    var hor = 0
    var depth = 0
    File("input").forEachLine {
        val (command, arg) = it.split(" ")
        val value = arg.toInt()
        when (command) {
            "forward" -> hor += value
            "down" -> depth += value
            "up" -> depth -= value
        }
    }
    println(hor * depth) // 2102357
}
