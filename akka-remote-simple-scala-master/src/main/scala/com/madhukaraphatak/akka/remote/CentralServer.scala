package com.madhukaraphatak.akka.remote

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor._
import com.typesafe.config.ConfigFactory

/**
 * Remote actor which listens on port 5150
 */
class CentralServer extends Actor {


  override def receive: Receive = {
    case msg: String => {
      println("remote received " + msg + " from " + sender)
      msg match {
        case "i am client 1" =>
          println("match")
          ServerMain.c1 = sender
          println(ServerMain.c1)
      }
      sender ! msg

      // FileWriter
      val file = new File("C:\\Users\\Catalina\\Documents\\GitHub\\Abstractum\\akka-remote-simple-scala-master\\results.txt")
      val bw = new BufferedWriter(new FileWriter(file,true))
      bw.write(msg + '\n')
      bw.close()
    }
    case _ => println("Received unknown msg ")
  }
}

object ServerMain{

  var c1 : ActorRef = null;
  var c2 : ActorRef = null;
  var c3 : ActorRef = null;
  var c4 : ActorRef = null;
  var c5 : ActorRef = null;

  def main(args: Array[String]) {
    //get the configuration file from classpath
    val configFile = getClass.getClassLoader.getResource("remote_application.conf").getFile
    //parse the config
    val config = ConfigFactory.parseFile(new File(configFile))
    //create an actor system with that config
    val system = ActorSystem("RemoteSystem" , config)
    //create a remote actor from actorSystem
    //var ln = io.Source.stdin.toString()
    //println(ln)
    val remote = system.actorOf(Props[CentralServer], name="remote")
    println("remote is ready")

    var i = 0
    while (i == 0) {
      print("")
      if ( c1!=null){
        println("mere")
        c1 ! "start"
        i=1
        Thread.sleep(1000)
      }
    }


  }
}


