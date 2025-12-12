import java.io.BufferedReader
import java.io.File

fun main() {
    val input = getInput()

    val passCode1 = solve1(input)

    println("Passcode1: $passCode1")
}

private fun solve1(inputData: List<String>): Int {
    val parts = inputData.joinToString("\n").split("\n\n")

    val presentDefinitions = parts.subList(0, parts.size - 1)
    val regionDefinitions = parts.last()

    val sizes = mutableMapOf<Int, Int>()

    for (presentBlock in presentDefinitions) {
        val lines = presentBlock.split("\n").filter { it.isNotBlank() }
        if (lines.isEmpty()) continue

        val name = lines.first().trim().removeSuffix(":").toIntOrNull() ?: continue

        val gridLines = lines.subList(1, lines.size)

        sizes[name] = gridLines.sumOf { row -> row.count { it == '#' } }
    }

    var ans = 0

    for (regionLine in regionDefinitions.split("\n").filter { it.isNotBlank() }) {
        val (szStr, nsStr) = regionLine.split(": ")

        val (R, C) = szStr.split('x').mapNotNull { it.toIntOrNull() }
        val totalGridSize = R * C

        val presentCounts = nsStr.split(" ").mapNotNull { it.toIntOrNull() }
        val totalPresentSize = presentCounts.mapIndexed { index, count -> count * (sizes[index] ?: 0) }.sum()

        //WHY THE F*** 1.3?
        if (totalPresentSize * 1.3 < totalGridSize) {
            // EASY to fit (enough safety margin)
            ans++
        } else if (totalPresentSize < totalGridSize) {
            println("HARD totalGridSize=$totalGridSize totalPresentSize=$totalPresentSize")
        }
    }

    return ans
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-12-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n")
}
