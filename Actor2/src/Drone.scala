import akka.actor.Actor

/**
  * Created by gohonel on 08.11.2016.
  */

class Drone extends Actor {
  var x = 0
  var y = 0

  def receive = {
    case input =>
      val decomposition: Array[String] = input.toString.split(" ")
      if (decomposition(0) == "picture") {
        println("DRONE:    taking picture")
        Thread.sleep(500);
        sender ! "picture"
      } else if (decomposition(0) == "fly") {
        x = decomposition(1).toInt
        y = decomposition(2).toInt
        println("DRONE:    flying to x=" + x + " and y=" + y)
        Thread.sleep(500);
        sender ! "flying to x=" + x + " and y=" + y
      }
  }
}
