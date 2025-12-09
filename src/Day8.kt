import java.io.BufferedReader
import java.io.File
import kotlin.math.sqrt
import kotlin.text.toInt

fun main() {
    val input = getInput()

    val passCode1 = solve1(input)

    println("Passcode1: $passCode1")
}

private fun solve1(input: List<String>): Int {
    val boxes = input.map {
        it.split(",").let {
            JunctionBox(
                x = it[0].toInt(),
                y = it[1].toInt(),
                z = it[2].toInt()
            )
        }
    }.toMutableList()

    var connections = mutableListOf<Connection>()

    boxes.forEachIndexed { index1, box ->
        for (i in 0..<index1) {
            val checkBox = boxes[i]
            connections.add(Connection(calculateDistance(box, checkBox), box, checkBox))
        }
    }

    val circuits = mutableListOf<Circuit>()

    connections = connections.sortedBy { it.distance }.toMutableList()

    connections.forEach {
        if (it.to.circuit == null && it.from.circuit == null) {
            val newCircuit = Circuit(mutableSetOf(it.from, it.to))
            it.from.circuit = newCircuit
            it.to.circuit = newCircuit
            circuits.add(newCircuit)
        } else if (it.to.circuit != null && it.from.circuit == null) {
            it.from.circuit = it.to.circuit
            it.to.circuit!!.boxes.add(it.from)
        } else if (it.from.circuit != null && it.to.circuit == null) {
            it.to.circuit = it.from.circuit
            it.from.circuit!!.boxes.add(it.to)
        }
    }

    return circuits.sortedByDescending { it.boxes.size }.take(3).fold(1) { acc, circuit -> acc * circuit.boxes.size }
}

private data class Connection(
    val distance: Double,
    val from: JunctionBox,
    val to: JunctionBox
)

private data class Circuit(
    val boxes: MutableSet<JunctionBox>
)

private fun calculateDistance(p1: JunctionBox, p2: JunctionBox): Double {
    val dx = p2.x - p1.x
    val dy = p2.y - p1.y
    val dz = p2.z - p1.z
    return sqrt((dx * dx + dy * dy + dz * dz).toDouble())
}

private data class JunctionBox(
    val x: Int,
    val y: Int,
    val z: Int
) {
    var circuit: Circuit? = null
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-8-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n")
}
