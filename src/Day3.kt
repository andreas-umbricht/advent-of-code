import java.io.BufferedReader
import java.io.File

fun main() {
    val input = getInput().map { it.map { it.digitToInt() } }

    var passcode = 0

    input.forEach { bank ->
        var max = 0

        //get indexes of biggest number in 0..bank.lastIndex - 12
        /*val bankMax = bank.max()
        val entryPoints = bank.mapIndexedNotNull { index, it -> if (it == bankMax) index else null }*/


        for (firstIndex in 0..<bank.lastIndex) {
            if (bank[firstIndex] > max / 10) {
                for(secondIndex in firstIndex+1..bank.lastIndex) {
                    max = maxOf(max, bank[firstIndex] * 10 + bank[secondIndex])
                }
            }
        }

        passcode += max
    }

    println("Passcode: $passcode")
}



private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-3-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n").filter { it.isNotEmpty() }
    //return listOf("5336553644444345344544134246423443634474453456455433543434354444344554344336446734443434424442135474")
}