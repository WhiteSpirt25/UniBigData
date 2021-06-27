import java.io.File
import java.io.InputStream
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.thread
import kotlin.concurrent.withLock


fun findPrime(num:Long, start_divider:Long):Long{

    for (i in start_divider..num) {
        if (num % i == 0L){
            return i
        }
    }
    return 0L
}
fun primeFactorCounter(num_str:String):Int{
    var num = num_str.toLong()
    
    var count:Int = 0
    var prime = findPrime(num,2L)
    while( num > 1 && prime != 0L){
        count++
        //println(div)
        num /= prime
        //println(num)
        prime = findPrime(num,prime)
        
    }
    //println(count)
    return count
}

fun consequentCounter(filename:String){
    val inputStream: InputStream = File(filename).inputStream()
    val primeFactorList = mutableListOf<Int>()

    inputStream.bufferedReader().forEachLine { primeFactorList.add(primeFactorCounter(it)) }
    val file = File("primeFactors.txt")
    file.appendText(primeFactorList.toString())
}

fun sortListPair(list: List<Pair<Int, Int>>): List<Int> {
    val sortedPairList = list.sortedWith(compareBy({ it.first }))
    val result = sortedPairList.map { it.second }
    return result
}
fun parallelCounter(filename:String,numOfThreads:Int) {
    val inputStream = File(filename).inputStream()
    val primeFactorList = mutableListOf<Pair<Int,Int>>()
    val lock = ReentrantLock()

    val executor = Executors.newFixedThreadPool(numOfThreads)

    var counter:Int = 0

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
    while (!executor.isTerminated) {
    }
    println("Finished all threads")

    val file = File("primeFactorsParallel.txt")
    file.writeText(sortListPair(primeFactorList).toString())
}
fun main(){
    //print(primeFactorCounter("238739043893001"))
    //consequentCounter("test.txt")
    parallelCounter("test.txt",4)
    //test("test.txt")
}