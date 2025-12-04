import java.io.BufferedReader
import java.io.File

fun main() {
    val input = getInput()

    var result = 0

    input.forEachIndexed { rowIndex, row ->
        row.forEachIndexed { charIndex, char ->
            if(char == '@') {
                var adjacent = 0

                if (row.getOrNull(charIndex - 1)?.let { it == '@' } ?: false) adjacent++
                if (row.getOrNull(charIndex + 1)?.let { it == '@' } ?: false) adjacent++

                adjacent += checkAdjacentRow(
                    adjacentRow = input.getOrNull(rowIndex - 1),
                    charIndex = charIndex
                )

                adjacent += checkAdjacentRow(
                    adjacentRow = input.getOrNull(rowIndex + 1),
                    charIndex = charIndex
                )

                if (adjacent < 4) {
                    result++
                }
            }
        }
    }

    println("Passcode: $result")
}

private fun checkAdjacentRow(adjacentRow: String?, charIndex: Int): Int {
    var result = 0

    if (adjacentRow == null) {
        return 0
    } else {
        if (adjacentRow.getOrNull(charIndex - 1)?.let { it == '@' } ?: false) result++
        if (adjacentRow.getOrNull(charIndex)?.let { it == '@' } ?: false) result++
        if (adjacentRow.getOrNull(charIndex + 1)?.let { it == '@' } ?: false) result++
    }

    return result
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-4-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n").filter { it.isNotEmpty() }
}