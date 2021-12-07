import java.io.File

fun computeRate(lines: List<String>, isDiscriminator: (Int, Int) -> Boolean): Int {
    var remainder = lines
    var i = 0
    while (remainder.count() > 1) {
        val sum = remainder.fold(0) { sum, line -> sum + Character.getNumericValue(line[i]) }
        val discriminator = if (isDiscriminator(sum, remainder.count())) '1' else '0'
        remainder = remainder.filter { it[i] == discriminator }
        ++i
    }
    return Integer.parseInt(remainder[0], 2)
}

fun main() {
    val lines = File("input").bufferedReader().readLines()
    val ogr = computeRate(lines, { sum, count -> 2 * sum >= count })
    val csr = computeRate(lines, { sum, count -> 2 * sum < count })
    println(ogr * csr) // 2817661
}
