package com.madhukaraphatak.akka.local

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * Local actor which listens on any free port
 */
class Client extends Actor{
  @throws[Exception](classOf[Exception])
  override def preStart(): Unit = {
    /*
      Connect to remote actor. The following are the different parts of actor path

      akka.tcp : enabled-transports  of remote_application.conf

      RemoteSystem : name of the actor system used to create remote actor

      127.0.0.1:5150 : host and port

      user : The actor is user defined

      remote : name of the actor, passed as parameter to system.actorOf call

     */
    val remoteActor = context.actorSelection("akka.tcp://RemoteSystem@127.0.0.1:5150/user/remote")
    val message ="i am client 1"
    println("That 's remote:" + remoteActor)
    remoteActor ! message

  }

  //http://www.prismmodelchecker.org/casestudies/polling.php !!!!
  override def receive: Receive = {

    case msg:String => {
      if (msg == "start") {
        ClientMain.operator ! "start"
      }
      println("got message from remote " + msg)
    }
  }
}



object ClientMain {
  var operator: ActorRef = null

  def main(args: Array[String]) {

    val configFile = getClass.getClassLoader.getResource("local_application.conf").getFile
    val config = ConfigFactory.parseFile(new File(configFile))
    val system = ActorSystem("ClientSystem",config)
    val Client = system.actorOf(Props[Client], name="local")

    val system1 = ActorSystem("OperatorAndDroneSystem")
    val drone = system1.actorOf(Props[Drone], name = "drone")
    operator = system1.actorOf(Props(new Operator(drone)), name = "operator")

  }


}
