import akka.actor.{ActorSystem, Props}

object Main extends App {
  val system = ActorSystem("HelloSystem")
  // default Actor constructor
  val drone = system.actorOf(Props[Drone], name = "drone")

  while (true){
    drone ! scala.io.StdIn.readLine()
  }
}