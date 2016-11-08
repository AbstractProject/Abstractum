import akka.actor.Actor

/**
  * Created by gohonel on 08.11.2016.
  */

class Operator(drone: Drone) extends Actor {
  val positionX: Array[Int] = Array(4, 8, 3, 7, 9, 6, 2, 7, 9, 5)
  val positionY: Array[Int] = Array(3, 7, 0, 3, 6, 0, 7, 2, 4, 8)
  var position = 0

  drone ! "fly " + positionX(position) + " " + positionY(position)

  def receive = {
    case input =>
      val decomposition: Array[String] = input.toString.split(" ")
      if (decomposition(0) == "picture") {
        println("received picture")

        val rand = scala.util.Random

        if (rand.nextInt(100) < 20) {
          println("picture failed")
          println("retaking picture")
          drone ! "picture" // de ce da eroare aici????
        } else {
          println("picture valid")
          position += 1
          if (position < positionX.length) {
            drone ! "fly " + positionX(position) + " " + positionY(position)
          }
        }
      }
  }
}