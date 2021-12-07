import java.io.File

fun main() {
    val fish = File("input").bufferedReader().readLine().split(",").map { it.toInt() }

    var born = Array(256) { 0L }

    for (specimen in fish) {
        var idx = specimen
        while (idx < born.size) {
            born[idx] += 1L
            idx += 7
        }
    }

    for (i in born.indices) {
        if (i == 0) {
            continue
        }
        var idx = i + 9
        while (idx < born.size) {
            born[idx] += 1L * born[i]
            idx += 7
        }
    }

    val count = fish.count() + born.fold(0L) { sum, el -> sum + el }
    println(count) // 1728611055389
}
