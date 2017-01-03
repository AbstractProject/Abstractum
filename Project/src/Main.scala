import akka.actor.{ActorSystem, Props}

/**
  * Created by Gellért on 2016. 12. 11..
  */
object Main {

  def main(args: Array[String]): Unit = {
    val system = ActorSystem("OperatorAndDroneSystem")
    val drone = system.actorOf(Props[Drone], name = "drone")
    val operator = system.actorOf(Props(new Operator(drone)), name = "operator")
    operator ! "start"
  }

}
