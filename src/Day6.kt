import java.io.BufferedReader
import java.io.File

fun main() {
    val input = getInput()

    val passCode1 = solve1(input)

    println("Passcode1: $passCode1")
}

private fun solve1(input: List<String>): String {
    val buff = List(
        input.first().split(" ").filter { it.isNotEmpty() }.size
    ) { mutableListOf<String>() }

    val operations = mutableListOf<Operation>()

    input.forEachIndexed { index, it ->
        it.split(" ").filter { it.isNotEmpty() }.forEachIndexed { rowIndex, it ->
            if (index == input.lastIndex) {
                operations.add(Operation(sign = it, numbers = buff[rowIndex]))
            } else {
                buff[rowIndex].add(it)
            }
        }
    }

    val operationResults = operations.map {
        if (it.sign == "+") {
            it.numbers.sum()
        } else {
            it.numbers.fold("1") { prev, it -> multiplyTwoString(prev, it) }
        }
    }

    return operationResults.sum()
}

private fun List<String>.sum() = fold("0") { prev, it -> addTwoString(prev, it) }

private fun multiplyTwoString(a: String, b: String): String {
    val numbersA = a.reversed().map { it.digitToInt() }
    val numbersB = b.reversed().map { it.digitToInt() }

    val multiplicationAdditions = numbersB.mapIndexed { indexB, numB ->
        numbersA.mapIndexed { indexA, numA ->
            (numB * numA).toString() + "0".repeat(indexA + indexB)
        }
    }.flatten()

    return multiplicationAdditions.sum()
}

private fun addTwoString(a: String, b: String): String {
    val numbersA = a.reversed()
    val numbersB = b.reversed()

    var carry = 0
    val result = mutableListOf<Char>()

    for (i in 0..<maxOf(a.length, b.length)) {
        val aInt = numbersA.getOrNull(i)?.digitToInt() ?: 0
        val bInt = numbersB.getOrNull(i)?.digitToInt() ?: 0

        val res = (aInt + bInt + carry).toString()

        result.add(0, res.last())
        carry = 0

        if (res.length >= 2) {
            carry++
        }
    }

    if (carry != 0) {
        result.add(0, carry.digitToChar())
    }

    return result.joinToString(separator = "")
}

data class Operation(
    val sign: String,
    val numbers: List<String>
)

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-6-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n")
}
