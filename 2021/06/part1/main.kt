import java.io.File

fun evolve(fish: MutableList<Int>) {
    var children = mutableListOf<Int>()
    for (i in fish.indices) {
        if (fish[i] == 0) {
            fish[i] = 6
            children.add(8)
        } else {
            --fish[i]
        }
    }
    fish.addAll(children)
}

fun main() {
    var fish = File("input").bufferedReader().readLine().split(",").map { it.toInt() }.toMutableList()
    (0..79).forEach { evolve(fish) }
    println(fish.count()) // 385391
}
