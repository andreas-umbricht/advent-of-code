import java.io.BufferedReader
import java.io.File
import javax.swing.Box
import kotlin.math.sqrt
import kotlin.text.toInt

fun main() {
    val input = getInput()

    val passCode1 = solve1(input)
    val passCode2 = solve2(input)

    println("Passcode1: $passCode1")
    println("Passcode2: $passCode2")
}

private fun solve2(input: List<String>): Long {
    val boxes = getBoxes(input)
    val connections = getClosestConnections(boxes)

    val circuits = boxes.associateWith { Circuit(boxes = listOf(it)) }.toMutableMap()

    var lastConnection: Connection? = null

    var i = 0

    while(lastConnection == null) {
        val connection = connections[i]

        if (circuits[connection.to] != circuits[connection.from]) {
            val newCircuit = Circuit(
                boxes = (circuits[connection.to]!!.boxes + circuits[connection.from]!!.boxes).distinct()
            )

            if (newCircuit.boxes.size == boxes.size) lastConnection = connection

            newCircuit.boxes.forEach {
                circuits[it] = newCircuit
            }
        }

        i++
    }

    return lastConnection.from.x * lastConnection.to.x
}

private fun solve1(input: List<String>): Long {
    val boxes = getBoxes(input)
    val connections = getClosestConnections(boxes)

    val circuits = boxes.associateWith { Circuit(boxes = listOf(it)) }.toMutableMap()

    repeat(1000) {
        val connection = connections[it]

        if (circuits[connection.to] != circuits[connection.from]) {
            val newCircuit = Circuit(
                boxes = (circuits[connection.to]!!.boxes + circuits[connection.from]!!.boxes).distinct()
            )

            newCircuit.boxes.forEach {
                circuits[it] = newCircuit
            }
        }
    }

    return circuits.values.distinct().sortedByDescending { it.boxes.size }.take(3).fold(1) { acc, circuit -> acc * circuit.boxes.size }
}

private fun getClosestConnections(boxes: List<JunctionBox>): MutableList<Connection> {
    val connections = mutableListOf<Connection>()

    boxes.forEachIndexed { index1, box ->
        for (i in index1 + 1..boxes.lastIndex) {
            val checkBox = boxes[i]
            connections.add(Connection(calculateDistance(box, checkBox), box, checkBox))
        }
    }

    return connections.sortedBy { it.distance }.toMutableList()
}

private fun getBoxes(input: List<String>) = input.map {
    it.split(",").let {
        JunctionBox(
            x = it[0].toLong(),
            y = it[1].toLong(),
            z = it[2].toLong()
        )
    }
}

private data class Connection(
    val distance: Double,
    val from: JunctionBox,
    val to: JunctionBox
)

private data class Circuit(
    val boxes: List<JunctionBox>
)

private fun calculateDistance(p1: JunctionBox, p2: JunctionBox): Double {
    val dx = p2.x - p1.x
    val dy = p2.y - p1.y
    val dz = p2.z - p1.z
    return sqrt((dx * dx + dy * dy + dz * dz).toDouble())
}

private data class JunctionBox(
    val x: Long,
    val y: Long,
    val z: Long
)

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-8-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n")
}
