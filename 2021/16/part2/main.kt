import java.io.File

interface Evaluable {
    fun evaluate(values: List<Long>): Long
}

enum class OperatorTypeId(val id: Int): Evaluable {
    TYPE_ID_SUM(0) {
        override fun evaluate(values: List<Long>): Long {
            return values.reduce { acc, next -> acc + next }
        }
    },
    TYPE_ID_PRODUCT(1) {
        override fun evaluate(values: List<Long>): Long {
            return values.reduce { acc, next -> acc * next }
        }
    },
    TYPE_ID_MINIMUM(2) {
        override fun evaluate(values: List<Long>): Long {
            return values.reduce { min, next -> if (min < next) min else next }
        }
    },
    TYPE_ID_MAXIMUM(3) {
        override fun evaluate(values: List<Long>): Long {
            return values.reduce { max, next -> if (max > next) max else next }
        }
    },
    TYPE_ID_GREATER_THAN(5) {
        override fun evaluate(values: List<Long>): Long {
            return if (values[0] > values[1]) 1 else 0
        }
    },
    TYPE_ID_LESS_THAN(6) {
        override fun evaluate(values: List<Long>): Long {
            return if (values[0] < values[1]) 1 else 0
        }
    },
    TYPE_ID_EQUAL_TO(7) {
        override fun evaluate(values: List<Long>): Long {
            return if (values[0] == values[1]) 1 else 0
        }
    }
}

final class PacketReader(private val input: String) {
    private val TYPE_ID_SUM = 0
    private val TYPE_ID_PRODUCT = 1
    private val TYPE_ID_MINIMUM = 2
    private val TYPE_ID_MAXIMUM = 3
    private val TYPE_ID_LITERAL = 4
    private val TYPE_ID_GREATER_THAN = 5
    private val TYPE_ID_LESS_THAN = 6
    private val TYPE_ID_EQUAL_TO = 7

    private var index = 0

    fun readPacket(): Long {
        skipBits(3) // version
        val typeId = readInteger(3)

        if (typeId == TYPE_ID_LITERAL) {
            return readLiteralValuePacketContents()
        }

        return readOperatorPacketContents(typeId)
    }

    private fun skipBits(num: Int) {
        index += num
    }

    private fun readInteger(numBits: Int): Int {
        return readBits(numBits).toInt(2)
    }

    private fun readBits(num: Int): String {
        return input.substring(index, index + num).also { index += num }
    }

    private fun readLiteralValuePacketContents(): Long {
        return readLiteralValue()
    }

    private fun readOperatorPacketContents(typeId: Int): Long {
        var values = mutableListOf<Long>()
        val lengthTypeId = readBit()

        if (lengthTypeId == '0') {
            val totalLength = readInteger(15)
            val cutoff = index + totalLength
            while (index < cutoff) {
                values.add(readPacket())
            }
        } else {
            val numSubpackets = readInteger(11)
            for (i in 1..numSubpackets) {
                values.add(readPacket())
            }
        }

        return OperatorTypeId.values().first { it.id == typeId }?.evaluate(values) ?: throw Exception()
    }

    private fun readLiteralValue(): Long {
        var bits = ""
        var hasMore = true
        while (hasMore) {
            hasMore = (readBit() != '0')
            bits += readBits(4)
        }
        return bits.toLong(2)
    }

    private fun readBit(): Char {
        return input[index].also { index += 1}
    }
}

fun main() {
    val input = File("input").bufferedReader().readLine().map { char ->
        Integer.toBinaryString(Integer.parseInt(Character.toString(char), 16)).padStart(4, '0')
    }.joinToString("")

    println(PacketReader(input).readPacket()) // 18234816469452
}
