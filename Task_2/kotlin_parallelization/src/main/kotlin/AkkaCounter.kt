
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.UntypedAbstractActor
import akka.routing.Broadcast
import akka.routing.RoundRobinPool

import primeFactorCounter
import scala.concurrent.Await
import sortListPair

import java.io.File
import java.io.InputStream
import java.lang.System.currentTimeMillis
import kotlin.system.exitProcess


data class EnumNumberString(val idx: Int,val numStr: String)
data class EnumPrimes(val idx_primePair:Pair<Int,Int>)
class End
class Terminated
class Start



fun main() {
    akkaCounter("numbers.txt")

    //Work time: 20338 ms
}
fun akkaCounter(filename:String){
    val inputStream: InputStream = File(filename).inputStream()

    val system = ActorSystem.create("MySystem")
    val masterActor = system.actorOf(Props.create(MasterActor::class.java), "master")
    var counter = 0
    masterActor.tell(Start(),null)
    inputStream.bufferedReader().forEachLine {
        masterActor.tell(EnumNumberString(counter,it), null)
        counter++
    }
    masterActor.tell(End(),null)
}

class CounterActor: UntypedAbstractActor() {

    override fun onReceive(message: Any?) {
        when(message) {
            is EnumNumberString -> {

                val prime = primeFactorCounter(message.numStr)

                sender.tell(EnumPrimes(Pair(message.idx,prime)), self)
            }
            is End -> {
                sender.tell(Terminated(),self)
            }
        }
    }
}

class MasterActor: UntypedAbstractActor() {
    val numberOfThreads = Runtime.getRuntime().availableProcessors()
    var finishedCounter = 0
    val worker = context.actorOf(RoundRobinPool(numberOfThreads).props(Props.create(CounterActor::class.java)), "counter")
    //val worker = context.actorOf(Props.create(DownloaderActor::class.java), "downloader1")
    val primeFactorList = mutableListOf<Pair<Int,Int>>()
    var startTime:Long? = null

    override fun onReceive(message: Any?) {
        when(message) {
            is Start -> {
                startTime = currentTimeMillis()
            }
            is EnumNumberString -> {
                worker.tell(message,self)
            }
            is EnumPrimes -> {
                //println(message.idx_primePair)
                primeFactorList.add(message.idx_primePair)
            }
            is End -> {
                worker.tell(Broadcast(End()),self)
            }
            is Terminated -> {
                finishedCounter++
                if (finishedCounter == numberOfThreads) {

                    val file = File("primeFactorsAkka.txt")
                    file.writeText(sortListPair(primeFactorList).toString())
                    println("Work time: ${currentTimeMillis()- startTime!!} ms")
                    exitProcess(0)
                }
            }
        }
    }
}

