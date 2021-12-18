import java.io.File
import kotlin.math.ceil
import kotlin.math.floor

fun main() {
    var result: Node? = null

    File("input").forEachLine { line ->
        val summand = createLinkedList(line)

        if (result == null) {
            result = summand
            return@forEachLine
        }

        result = add(result!!, summand!!)

        var onceMore = true
        while (onceMore) {
            explode(result!!)
            onceMore = splitFirst(result!!)
        }
    }

    val (_, magnitude) = computeMagnitude(result!!)
    println(magnitude) // 3725
}

data class Node(var value: Int, var next: Node?) {}

fun createLinkedList(line: String): Node? {
    var head: Node? = null
    var previous: Node? = null

    fun addNode(value: Int) {
        val node = Node(value, null)
        if (head == null) {
            head = node
        }
        previous?.let { it.next = node }
        previous = node
    }

    var digits = mutableListOf<Char>()

    fun digitsToInt(): Int? {
        if (digits.isEmpty()) {
            return null
        }
        return Integer.parseInt(digits.joinToString(""))
    }

    for (char in line) {
        if (char == '[') {
            addNode(Int.MIN_VALUE)
            continue
        }
        if (char == ',') {
            digitsToInt()?.let { addNode(it) }?.also { digits.clear() }
            continue
        }
        if (char == ']') {
            digitsToInt()?.let { addNode(it) }?.also { digits.clear() }
            addNode(Int.MAX_VALUE)
            continue
        }
        digits.add(char)
    }

    return head
}

fun add(head1: Node, head2: Node): Node {
    val leftBrace = Node(Int.MIN_VALUE, head1)
    val rightBrace = Node(Int.MAX_VALUE, null)

    var last1 = head1
    while (last1.next != null) {
        last1 = last1.next!!
    }
    last1.next = head2

    var last2 = head2
    while (last2.next != null) {
        last2 = last2.next!!
    }
    last2.next = rightBrace

    return leftBrace
}

fun explode(head: Node) {
    var depth = 0
    var node = head
    var previous: Node? = null
    var lastNumber: Node? = null
    var toPassOn: Int? = null

    while (node.next != null) {
        if (node.value == Int.MIN_VALUE) {
            if (depth < 4) {
                depth += 1
            } else {
                // First pass on value from explosion to the left, if any
                toPassOn?.let { node.next!!.value += it }?.also { toPassOn = null }

                lastNumber?.let { it.value += node.next!!.value }
                toPassOn = node.next!!.next!!.value

                val next = node.next!!.next!!.next!!.next!!

                val new = Node(0, next)
                previous?.next = new

                node = new
                lastNumber = node
            }
        } else if (node.value == Int.MAX_VALUE) {
            depth -= 1
        } else {
            toPassOn?.let { node.value += it }?.also { toPassOn = null }
            lastNumber = node
        }

        previous = node
        node = node.next!!
    }
}

fun splitFirst(head: Node): Boolean {
    var node = head
    var previous: Node? = null

    while (node.next != null) {
        if (node.value != Int.MIN_VALUE && node.value != Int.MAX_VALUE && node.value >= 10) {
            val rightBrace = Node(Int.MAX_VALUE, node.next)
            val rightNode = Node(ceil(node.value / 2.0).toInt(), rightBrace)
            val leftNode = Node(floor(node.value / 2.0).toInt(), rightNode)
            previous?.next = Node(Int.MIN_VALUE, leftNode)
            return true
        }

        previous = node
        node = node.next!!
    }

    return false
}

fun computeMagnitude(node: Node): Pair<Node?, Int> {
    if (node.value != Int.MIN_VALUE && node.value != Int.MAX_VALUE) {
        return Pair(node.next, node.value)
    }
    val (next, leftMagnitude) = computeMagnitude(node.next!!)
    val (nextNext, rightMagnitude) = computeMagnitude(next!!)
    return Pair(nextNext!!.next, 3 * leftMagnitude + 2 * rightMagnitude)
}
