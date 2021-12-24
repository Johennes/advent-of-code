import java.io.File
import java.lang.Exception
import kotlin.math.max

fun main() {
    val instructions = File("input").readLines().map { parseInstruction(it) }

    // When reading in a new digit, only the Z register from the previous block is reused, so we
    // don't have to track X or Y

    var prefixes = hashMapOf<Int,Long?>(0 to null) // Largest prefixes by z value

    for (i in instructions.indices) {
        if (instructions[i].type != InstructionType.INPUT) {
            continue
        }

        val newPrefixes = hashMapOf<Int,Long?>()

        for ((z, prefix) in prefixes) {
            for (input in 1..9) {
                val registers = arrayOf(0, 0, 0, z)
                instructions[i].eval(registers, input)

                for (j in i + 1 until instructions.count()) {
                    if (instructions[j].type == InstructionType.INPUT) {
                        break
                    }
                    instructions[j].eval(registers, input)
                }

                newPrefixes[registers[3]] = max(newPrefixes[registers[3]] ?: Long.MIN_VALUE, (prefix ?: 0) * 10 + input)
            }
        }

        prefixes = newPrefixes
        println(prefixes.count())
    }

    println(prefixes[0]) // 52926995971999
}

enum class InstructionType {
    INPUT, CALCULATION
}

data class Instruction(val type: InstructionType, val eval: (Array<Int>, Int) -> Unit)

fun parseInstruction(instruction: String): Instruction {
    val components = instruction.split(" ")
    val leftIndex = indexForRegister(components[1]) ?: throw Exception()

    if (components[0] == "inp") {
        return Instruction(InstructionType.INPUT) { registers, input -> registers[leftIndex] = input }
    }

    val rightIndex = indexForRegister(components[2])
    val rightValue = if (rightIndex == null) Integer.parseInt(components[2]) else null

    return when (components[0]) {
        "add" -> Instruction(InstructionType.CALCULATION) { registers, _ ->
            registers[leftIndex] += rightIndex?.let { registers[it] } ?: rightValue!!
        }
        "mul" -> Instruction(InstructionType.CALCULATION) { registers, _ ->
            registers[leftIndex] *= rightIndex?.let { registers[it] } ?: rightValue!!
        }
        "div" -> Instruction(InstructionType.CALCULATION) { registers, _ ->
            registers[leftIndex] /= rightIndex?.let { registers[it] } ?: rightValue!!
        }
        "mod" -> Instruction(InstructionType.CALCULATION) { registers, _ ->
            registers[leftIndex] %= rightIndex?.let { registers[it] } ?: rightValue!!
        }
        "eql" -> Instruction(InstructionType.CALCULATION) { registers, _ ->
            registers[leftIndex] = if (registers[leftIndex] == (rightIndex?.let { registers[it] } ?: rightValue!!)) 1 else 0
        }
        else -> throw Exception()
    }
}

fun indexForRegister(register: String): Int? {
    return when (register) {
        "w" -> 0
        "x" -> 1
        "y" -> 2
        "z" -> 3
        else -> null
    }
}
