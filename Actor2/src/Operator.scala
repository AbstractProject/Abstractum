import akka.actor._
/**
  * Created by gohonel on 08.11.2016.
  */

class Operator(drone: ActorRef) extends Actor {

  val positionX: Array[Int] = Array(4, 8, 3, 7, 9, 6, 2, 7, 9, 5)
  val positionY: Array[Int] = Array(3, 7, 0, 3, 6, 0, 7, 2, 4, 8)
  var position = 0

  println("OPERATOR: fly to" + "(" + positionX(position) + ", " + positionY(position) + ") and take picture")
  Thread.sleep(500)
  drone ! "fly " + positionX(position) + " " + positionY(position)
  drone ! "picture"

  def receive = {
    case input =>
      val decomposition: Array[String] = input.toString.split(" ")
      if (decomposition(0) == "picture") {
        println("OPERATOR: received picture")

        val rand = scala.util.Random

        if (rand.nextInt(100) < 20) {
          println("OPERATOR: picture failed")
          Thread.sleep(500);
          println("OPERATOR: retaking picture")
          Thread.sleep(500);
          drone ! "picture" // de ce da eroare aici????
        } else {
          println("OPERATOR: picture valid")
          position += 1
          if (position < positionX.length) {
            println("OPERATOR: fly to (" + positionX(position) + ", " + positionY(position) + ") and take picture")
            Thread.sleep(500);
            drone ! "fly " + positionX(position) + " " + positionY(position)
            drone ! "picture"
          }
        }
      }
  }
}