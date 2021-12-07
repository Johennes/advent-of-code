import java.io.File

fun main() {
    var sums = mutableListOf<Int>()
    var count = 0
    File("input").forEachLine {
        for (i in 0 until it.length) {
            if (i == sums.size) {
                sums.add(0)
            }
            sums[i] += Character.getNumericValue(it[i])
        }
        ++count
    }
    val gamma = Integer.parseInt(sums.map { if (count - it >= count / 2) 0 else 1 }.joinToString(""), 2)
    val eps = Integer.parseInt(sums.map { if (count - it >= count / 2) 1 else 0 }.joinToString(""), 2)
    println(gamma * eps) // 2035764
}
