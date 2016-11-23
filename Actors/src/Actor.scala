import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props

class Drone extends Actor {
  var x = 0
  var y = 0


  def receive = {
    case input => {
      var decomposition: Array[String] = input.toString.split(" ")
      if(decomposition(0) == "#picture") {
        println("taking picture")
      }
      else if(decomposition(0) == "#fly") {
        x = decomposition(1).toInt
        y = decomposition(2).toInt
        println("flying to x=" + x + " and y=" + y)
      }
      else println("Undefined command.")
    }
  }
}