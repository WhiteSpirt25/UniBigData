
import java.math.BigInteger
import java.util.Random
import java.io.File

fun main(){
    val num2generate:Int = 2000

    val random = Random()
    val file = File("numbers.txt")
    file.writeText("")

    for (num in 1..num2generate){
        //print(num)
        var f = BigInteger(48, random).toString()
        file.appendText(f+'\n')
    }


}