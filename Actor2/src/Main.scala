import akka.actor.{ActorSystem, Props}

/**
  * Created by gohonel on 08.11.2016.
  */
object Main {
  def main(args: Array[String]): Unit = {
    val system = ActorSystem("HelloSystem")
    val drone = system.actorOf(Props[Drone], name = "drone")
    val operator = system.actorOf(Props(new Operator(drone)), name = "operator")
  }

}
