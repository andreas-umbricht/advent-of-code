import java.io.BufferedReader
import java.io.File

fun main() {
    val input = getInput().map { it.map { it.digitToInt() } }

    println("Puzzle 1: ${solve1(input)}")
    println("Puzzle 2: ${solve2(input)}")
}

private fun solve2(input: List<List<Int>>): Long {
    var passcode = 0L

    input.forEach { bank ->
        val res = solveBank(12, bank).fold(0L) { acc, d ->
            acc * 10 + d
        }

        passcode += res
    }

    return passcode
}

private fun solveBank(neededDigits: Int, bank: List<Int>): List<Int> {
    if (bank.size == neededDigits) return bank

    if (bank.toSet().size == 1) {
        return List(neededDigits) { bank.first() }
    }

    val relevantNumbers = bank.dropLast(neededDigits - 1)

    val bankMax = relevantNumbers.max()
    relevantNumbers.forEachIndexed { index, it ->
        if (it == bankMax) {
            return if (neededDigits - 1 > 0) {
                listOf(bankMax) +  solveBank(neededDigits - 1, bank.subList(index + 1, bank.size))
            } else {
                listOf(bankMax)
            }
        }
    }

    return bank
}


private fun solve1(input: List<List<Int>>): Int {
    var passcode = 0

    input.forEach { bank ->
        var max = 0

        for (firstIndex in 0..<bank.lastIndex) {
            if (bank[firstIndex] > max / 10) {
                for(secondIndex in firstIndex+1..bank.lastIndex) {
                    max = maxOf(max, bank[firstIndex] * 10 + bank[secondIndex])
                }
            }
        }

        passcode += max
    }

    return passcode
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-3-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n").filter { it.isNotEmpty() }
}