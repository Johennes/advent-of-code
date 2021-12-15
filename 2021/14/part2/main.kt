import java.io.File
import kotlin.math.max
import kotlin.math.min

fun main() {
    val reader = File("input").bufferedReader()
    val template = reader.readLine()

    var insertions = hashMapOf<String,String>()
    while (true) {
        val line = reader.readLine()
        if (line == null) {
            break
        }
        if (!line.isEmpty()) {
            val (key, value) = line.split(" -> ")
            insertions.put(key, value)
        }
    }

    var pairCounts = hashMapOf<String,Long>()
    for (i in 0..(template.length - 2)) {
        val pair = template.substring(i, i + 2)
        pairCounts[pair] = pairCounts.getOrDefault(pair, 0L) + 1L
    }

    for (i in 0..39) {
        var nextPairCounts = hashMapOf<String,Long>()
        for ((pair, count) in pairCounts) {
            insertions[pair]?.let {
                val left = pair[0] + it
                nextPairCounts[left] = nextPairCounts.getOrDefault(left, 0) + count
                val right = it + pair[1]
                nextPairCounts[right] = nextPairCounts.getOrDefault(right, 0) + count
            }
        }
        pairCounts = nextPairCounts
    }

    var charCounts = hashMapOf<Char,Long>()
    for ((pair, count) in pairCounts) {
        charCounts[pair[0]] = charCounts.getOrDefault(pair[0], 0) + count
        charCounts[pair[1]] = charCounts.getOrDefault(pair[1], 0) + count
    }

    var countMin = Long.MAX_VALUE
    var countMax = Long.MIN_VALUE
    for ((char, count) in charCounts) {
        val realCount = (if (char == template.first() || char == template.last()) count + 1L else count) / 2L
        countMin = min(countMin, realCount)
        countMax = max(countMax, realCount)
    }

    println(countMax - countMin) // 3447389044530
}
