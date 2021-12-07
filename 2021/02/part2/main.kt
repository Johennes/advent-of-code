import java.io.File

fun main() {
    var hor = 0
    var depth = 0
    var aim = 0
    File("input").forEachLine {
        val (command, arg) = it.split(" ")
        val value = arg.toInt()
        when (command) {
            "forward" -> {
                hor += value
                depth += aim * value
            }
            "down" -> aim += value
            "up" -> aim -= value
        }
    }
    println(hor * depth) // 2101031224
}
