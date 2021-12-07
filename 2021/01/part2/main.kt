import java.io.File

fun main() {
    var count = 0
    var nums = arrayOf(0, 0, 0)
    var sum = 0
    var idx = 0
    var prev: Int? = null
    File("input").forEachLine {
        val n = it.toInt()
        sum += n - nums[idx % 3]
        nums[idx % 3] = n
        if (idx >= 2) {
            prev?.takeIf { it < sum }?.let { ++count }
            prev = sum
        }
        ++idx
    }
    println(count)
}
