import java.io.File
import java.io.BufferedReader

tailrec fun recurse(reader: BufferedReader, count: Int, prev: Int? = null): Int {
    val n = reader.readLine()?.let { it.toInt() } ?: return count
    return recurse(
        reader = reader,
        count = prev?.takeIf { it < n }?.let { count + 1 } ?: count,
        prev = n)
}

fun main() {
    println(recurse(reader = File("input").bufferedReader(), count = 0))
}
