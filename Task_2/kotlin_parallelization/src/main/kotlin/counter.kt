import java.io.File
import java.io.InputStream
import java.lang.Math.sqrt
import kotlin.system.measureTimeMillis

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

    var count = 0
    while (num%2 == 0L){
        count++
        num/=2
    }
    val squareRoot = kotlin.math.sqrt(num.toDouble()).toInt()

    // Run loop from 3 to square root of n. Check for divisibility by i. Add i in arr till it is divisible by i.
    for (i in 3..squareRoot step 2) {
        while (num % i == 0L) {
            count++
            num /= i
        }
    }

    // If n is a prime number greater than 2.
    if (num > 2) {
        count++
    }
    //println(count)
    return count
}

fun consequentCounter(filename:String): List<Int> {
    val inputStream: InputStream = File(filename).inputStream()
    val primeFactorList = mutableListOf<Int>()

    inputStream.bufferedReader().forEachLine { primeFactorList.add(primeFactorCounter(it)) }
    return primeFactorList.toList()
}
fun writeListToFile(lst:List<Int>,filename: String){
    val file = File(filename)
    file.writeText(lst.toString())
}
fun main(){

    val executionTime = measureTimeMillis {
        writeListToFile(consequentCounter("numbers.txt"),"primeFactorsConsequent.txt")
    }
    println("Work time: $executionTime ms")

    //Work time: 76373 ms
}