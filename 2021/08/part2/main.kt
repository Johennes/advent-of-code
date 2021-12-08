import java.io.File

val segmentsByDigit = mapOf(
        0 to setOf('a', 'b', 'c', 'e', 'f', 'g'),
        1 to setOf('c', 'f'),
        2 to setOf('a', 'c', 'd', 'e', 'g'),
        3 to setOf('a', 'c', 'd', 'f', 'g'),
        4 to setOf('b', 'c', 'd', 'f'),
        5 to setOf('a', 'b', 'd', 'f', 'g'),
        6 to setOf('a', 'b', 'd', 'e', 'f', 'g'),
        7 to setOf('a', 'c', 'f'),
        8 to setOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
        9 to setOf('a', 'b', 'c', 'd', 'f', 'g')
)

val digitsBySegment = mapOf(
        setOf('a', 'b', 'c', 'e', 'f', 'g') to 0,
        setOf('c', 'f') to 1,
        setOf('a', 'c', 'd', 'e', 'g') to 2,
        setOf('a', 'c', 'd', 'f', 'g') to 3,
        setOf('b', 'c', 'd', 'f') to 4,
        setOf('a', 'b', 'd', 'f', 'g') to 5,
        setOf('a', 'b', 'd', 'e', 'f', 'g') to 6,
        setOf('a', 'c', 'f') to 7,
        setOf('a', 'b', 'c', 'd', 'e', 'f', 'g') to 8,
        setOf('a', 'b', 'c', 'd', 'f', 'g') to 9
)

val digitCandidatesBySampleLength = mapOf(
        2 to arrayOf(1),
        3 to arrayOf(7),
        4 to arrayOf(4),
        5 to arrayOf(2, 3, 5),
        6 to arrayOf(0, 6, 9),
        7 to arrayOf(8)
)

fun main() {
    var sum = 0

    File("input").forEachLine {
        val (samples, measurements) = it.split(" | ").map { it.split(" ").map{ it.toCharArray().toSet() } }

        var segments = samples
                .map { digitCandidatesBySampleLength[it.count()]!!.map { segmentsByDigit[it]!! } }
                .toMutableList()

        var units = samples.indices
                .filter { segments[it].count() == 1 }
                .map { samples[it] to segments[it][0]}
                .toMap()
                .toMutableMap()

        canonicalize(units)

        var todo = samples.count() - segments.filter { it.count() == 1 }.count()

        while (todo > 0) {
            for (i in samples.indices) {
                if (segments[i].count() == 1) {
                    continue
                }
                segments[i] = segments[i].filter { it.containsAll(intersectAndMap(samples[i], units)) }
                if (segments[i].count() == 1) {
                    units[samples[i]] = segments[i][0]
                    canonicalize(units)
                    --todo
                }
            }
        }

        sum += measurements.map {
            digitsBySegment[intersectAndMap(it, units)]!!
        }.joinToString("").toInt()
    }

    println(sum) // 940724
}

// Reduce units by subtracting them from each other until the smallest possible pieces are reached
fun canonicalize(units: MutableMap<Set<Char>, Set<Char>>) {
    var onceMore = true
    while (onceMore) {
        onceMore = false
        val keys = units.keys.sortedBy { it.count() }
        for (i in keys.count() - 1 downTo 1) {
            for (j in i - 1 downTo 0) {
                if (!keys[i].containsAll(keys[j])) {
                    continue
                }
                units[keys[i].subtract(keys[j])] = units[keys[i]]!!.subtract(units[keys[j]]!!)
                units.remove(keys[i])
                onceMore = true
                break
            }
        }
    }
}

// Compute the mapped intersection of a sample and canonical units
fun intersectAndMap(sample: Set<Char>, units: MutableMap<Set<Char>, Set<Char>>): Set<Char> {
    var intersection = mutableSetOf<Char>()
    for (key in units.keys) {
        if (key.count() > sample.count()) {
            continue
        }
        if (!sample.containsAll(key)) {
            continue
        }
        intersection.addAll(units[key]!!)
    }
    return intersection.toSet()
}
