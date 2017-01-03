package com.madhukaraphatak.akka.remote

import java.io.{BufferedWriter, File, FileWriter}
import javax.print.DocFlavor.INPUT_STREAM

import akka.actor._
import com.typesafe.config.ConfigFactory

/**
 * Remote actor which listens on port 5150
 */
class CentralServer extends Actor {

var i: Int =0
  override def receive: Receive = {
    case msg: String => {
      println("remote received " + msg + " from " + sender)
      msg match {
        case "give me an id" =>
          if(ServerMain.clients(0)==null){
            ServerMain.clients(0)=sender
          }
          else{
            if(ServerMain.clients(1)==null){
              ServerMain.clients(1)=sender
            }
            else{
              if(ServerMain.clients(2)==null){
                ServerMain.clients(2)=sender
              }
              else{
                if(ServerMain.clients(3)==null){
                  ServerMain.clients(3)=sender
                }
                else{
                  if(ServerMain.clients(4)==null){
                    ServerMain.clients(4)=sender
                  }
                }
              }
            }
          }
        case "0"=>
          if(ServerMain.clients(i)==sender){
            ServerMain.clients(i+1) ! "poll"
            if(i<5) {
              i = i + 1
            }
            else{
              i = 0
            }

          }
      }
      val decomposition: Array[String]=msg.split(" ")
      if(decomposition(0)=="success"){
          //decomp(1) salvat in fisier + valori constante fara p
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

  var clients : Array[ActorRef]=Array(null,null,null,null,null)

  def main(args: Array[String]) {
    //get the configuration file from classpath
    val configFile = getClass.getClassLoader.getResource("remote_application.conf").getFile
    //parse the config
    val config = ConfigFactory.parseFile(new File(configFile))
    //create an actor system with that config
    val system = ActorSystem("RemoteSystem" , config)
    //create a remote actor from actorSystem
    var ln =""
    val remote = system.actorOf(Props[CentralServer], name="remote")
    println("remote is ready")

    var i = 0
    while (i < 5) {
      print("")
      ln=readLine()
      val decomposition: Array[String]=ln.split(" ")
      if (clients(decomposition(1).toInt)!=null){
        //println("mere")
        clients(decomposition(1).toInt) ! ln
        i=i+1
        Thread.sleep(1000)
      }
    }


  }
}

