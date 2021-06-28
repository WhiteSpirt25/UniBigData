import io.reactivex.rxjava3.core.*
import java.io.BufferedReader
import java.io.FileReader

import primeFactorCounter
import kotlin.system.measureTimeMillis

fun main() {
    val executionTime = measureTimeMillis {
        writeListToFile(rxCounter("numbers.txt"),"primeFactorsRx.txt")
    }
    println("Work time: $executionTime ms")

    //Work time: 77034 ms
}
fun rxCounter(filename: String): List<Int> {
    val primeFactorList = mutableListOf<Int>()

    Flowable.using(
        { BufferedReader(FileReader(filename)) },
        { reader: BufferedReader ->
            Flowable.fromIterable(
                Iterable { reader.lines().iterator() })
        },
        { reader: BufferedReader -> reader.close() })
        .parallel()
        .map { x ->  primeFactorCounter(x)}
        .sequential()
        .forEach{x -> primeFactorList.add(x)}

    return primeFactorList.toList()
}