import java.io.File

fun countOccurrencesBetween(left: String, right: String, level: Int, maxLevel: Int, insertions: Map<String,String>, occurrences: MutableMap<String,Int>) {
    if (level > maxLevel) {
        return
    }
    val insertion = insertions[left + right]
    if (insertion == null) {
        return
    }
    occurrences[insertion] = occurrences.getOrDefault(insertion, 0) + 1
    countOccurrencesBetween(left, insertion, level + 1, maxLevel, insertions, occurrences)
    countOccurrencesBetween(insertion, right, level + 1, maxLevel, insertions, occurrences)
}

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

    var occurrences = hashMapOf<String,Int>()
    for (char in template) {
        occurrences["${char}"] = occurrences.getOrDefault("${char}", 0) + 1
    }

    for (i in 0..template.length-2) {
        countOccurrencesBetween("${template[i]}", "${template[i+1]}", 1, 10, insertions, occurrences)
    }

    println(occurrences.values.maxOrNull()!! - occurrences.values.minOrNull()!!) // 3058
}
