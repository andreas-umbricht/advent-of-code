import java.io.BufferedReader
import java.io.File
import kotlin.text.getOrNull

fun main() {
    val input = getInput()

    val passcode1 = puzzle1(input)
    val passcode2 = puzzle2(input)

    println("Passcode1: $passcode1")
    println("Paccode2: $passcode2")
}

private fun puzzle2(input: List<String>): Int {
    val places = input.map { it -> it.map { Place(char = it) } }

    places.forEachIndexed { index, row ->
        row.forEachIndexed { rowIndex, place ->
            place.addNeighbour(row.getOrNull(rowIndex + 1))
            place.addNeighbour(row.getOrNull(rowIndex - 1))
            place.addNeighbour(places.getOrNull(index - 1)?.getOrNull(rowIndex - 1))
            place.addNeighbour(places.getOrNull(index - 1)?.getOrNull(rowIndex))
            place.addNeighbour(places.getOrNull(index - 1)?.getOrNull(rowIndex + 1))
            place.addNeighbour(places.getOrNull(index + 1)?.getOrNull(rowIndex - 1))
            place.addNeighbour(places.getOrNull(index + 1)?.getOrNull(rowIndex))
            place.addNeighbour(places.getOrNull(index + 1)?.getOrNull(rowIndex + 1))
        }
    }

    return places.flatten().sumOf { it.check() }
}

private class Place(
    val char: Char,
) {
    private var neighbours = mutableListOf<Place>()

    var destroyed = false
        private set

    fun addNeighbour(n: Place?) {
        n?.let { if (n.char == '@') neighbours.add(n) }
    }

    fun check(): Int {
        return if (neighbours.size < 4 && !destroyed && char == '@') {
            destroyed = true
            neighbours.sumOf { it.removeNeighbour(this) } + 1
        } else {
            0
        }
    }

    fun removeNeighbour(place: Place): Int {
        if (destroyed) return 0

        neighbours.remove(place)
        return check()
    }
}

private fun puzzle1(input: List<String>): Int {
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

    return result
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