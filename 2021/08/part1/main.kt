import java.io.File

fun main() {
    val unique = arrayOf(2, 3, 4, 7)
    var count = 0

    File("input").forEachLine {
        var length = 0
        for (i in it.length - 1 downTo 0) {
            if (it[i] == '|') {
                break
            }
            if (it[i] != ' ') {
                ++length
                continue
            }
            if (unique.contains(length)) {
                ++count
            }
            length = 0
        }
    }

    println(count) // 288
}
