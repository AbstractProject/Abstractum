import akka.actor._
/**
  * Created by gohonel on 08.11.2016.
  */

class Operator(drone: ActorRef) extends Actor {

  val positionX: Array[Int] = Array(1,2,3,4,5,6)
  var position = 0
  val rand = scala.util.Random

  drone ! "#start"

  def receive = {
    case input =>
      val decomposition: Array[String] = input.toString.split(" ")

      if (decomposition(0) == "#picture") {
        println("OPERATOR: received picture")

        if (rand.nextInt(100) < 20) {
          println("OPERATOR: picture failed")
          Thread.sleep(500)
          println("OPERATOR: retaking picture")
          Thread.sleep(500)
          drone ! "#takePicture" // de ce da eroare aici????
        }else{
          println("OPERATOR:  picture ok")
          Thread.sleep(500)
          drone ! "#goodPicture"
        }
      }

      else if(decomposition(0) == "#sendNext") {
        val state = decomposition(0).toInt
        var nextState = -1
        Thread.sleep(500)

        state match {
          case 2 => nextState = rand.nextInt(4)
          case 5 => nextState = rand.nextInt(3)
          case 6 => nextState = rand.nextInt(3)
        }

        println("OPERATOR: sending instruction" + nextState)
      }
  }
}