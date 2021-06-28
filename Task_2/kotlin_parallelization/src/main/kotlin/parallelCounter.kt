import java.io.File
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock
import kotlin.system.measureTimeMillis

fun sortListPair(list: List<Pair<Int, Int>>): List<Int> {
    val sortedPairList = list.sortedWith(compareBy({ it.first }))
    val result = sortedPairList.map { it.second }
    return result
}
fun parallelCounter(filename:String): List<Int> {
    val numOfThreads = Runtime.getRuntime().availableProcessors()

    val inputStream = File(filename).inputStream()
    val primeFactorList = mutableListOf<Pair<Int,Int>>()
    val lock = ReentrantLock()

    val executor = Executors.newFixedThreadPool(numOfThreads)

    var counter = 0

    inputStream.bufferedReader().forEachLine {
        val count_copy = counter
        counter++
        val worker = Runnable() {
            val result = primeFactorCounter(it)
            //println(result)
            lock.withLock {
                primeFactorList.add(Pair(count_copy,result))
            }
        }
        executor.execute(worker)
    }
    executor.shutdown()
    while (!executor.isTerminated) {}
    println("Finished all threads")

    return sortListPair(primeFactorList)
}
fun main(){
    val executionTime = measureTimeMillis {
        writeListToFile(parallelCounter("numbers.txt"),"primeFactorsParallel.txt")
    }
    println("Work time: $executionTime ms")

    //Work time: 25054 ms
}