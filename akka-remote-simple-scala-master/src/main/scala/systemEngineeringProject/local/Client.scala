/**
  * Created by Catalina on 2016. 12. 10..
  */
package systemEngineeringProject.local

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor.{Actor, ActorRef, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

class Client extends Actor{

  var remoteActor : ActorSelection = null
  var totalMissionTime: Int = 0

  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {

    remoteActor = context.actorSelection("akka.tcp://RemoteSystem@192.168.8.100:5150/user/remote")
    val message ="ID request"
    println("ID request sent to the server.")
    remoteActor ! message
  }

  override def receive: Receive = {

    case msg:String => {
      val decomposition: Array[String] = msg.toString.split(" ")
      if (decomposition(0) == "start") {
        totalMissionTime = 0
        ClientMain.operator ! "start"
        remoteActor ! "Operator started"
      } else if (decomposition(0) == "success") {
        totalMissionTime = decomposition(1).toInt
      }
      else if (decomposition(0) == "poll"){
        println("[CLIENT] Server polled the client.")
        remoteActor ! totalMissionTime
        if (totalMissionTime != 0) totalMissionTime = 0;
    }
      //println("got message from remote " + msg)
    }
  }
}

object ClientMain {
  var operator: ActorRef = null

  def main(args: Array[String]) {
    val configFile = getClass.getClassLoader.getResource("local_application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("ClientSystem",config)
    val client = system.actorOf(Props[Client], name="local")

    val system1 = ActorSystem("OperatorAndDroneSystem")
    val drone = system1.actorOf(Props[Drone], name = "drone")
    operator = system1.actorOf(Props(new Operator(drone,client)), name = "operator")
  }
}
