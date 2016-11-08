import akka.actor.{ActorSystem, Props}

/**
  * Created by gohonel on 08.11.2016.
  */
class Main {
  val system = ActorSystem("HelloSystem")
  val drone = new Drone
  val operator = new Operator(drone)
}
