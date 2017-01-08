package systemEngineeringProject.remote

import java.io.{BufferedWriter, File, FileWriter}

import akka.actor._
import com.typesafe.config.ConfigFactory
import org.sameersingh.scalaplot.Implicits._

class CentralServer extends Actor {

var i: Int =0
  override def receive: Receive = {
    var id : Int = 0
    case msg: String => {
      println("Server received: " + msg + "  from " + sender)
      msg match {
        case "ID request" =>
          println("New client is trying to connect.")
          if(ServerMain.clients(0)==null){
            ServerMain.clients(0)=sender
          }
          else{
            if(ServerMain.clients(1)==null){
              ServerMain.clients(1)=sender
              id = 1;
            }
            else{
              if(ServerMain.clients(2)==null){
                ServerMain.clients(2)=sender
                id = 2;
              }
              else{
                if(ServerMain.clients(3)==null){
                  ServerMain.clients(3)=sender
                  id = 3;
                }
                else{
                  if(ServerMain.clients(4)==null){
                    ServerMain.clients(4)=sender
                    id = 4;
                  }
                }
              }
            }
          }
          println("New client with id "+ id+ " is connected.")

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
        // FileWriter
        val file = new File("C:\\Users\\Catalina\\Documents\\GitHub\\Abstractum\\akka-remote-simple-scala-master\\results.txt")
        val bw = new BufferedWriter(new FileWriter(file,true))
        bw.write(decomposition(1) + '\n')
        bw.close()
      }
      sender ! msg
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
    val remote = system.actorOf(Props[CentralServer], name="server")
    println("Server started ")



   // val x = 0.0 until 2.0 * math.Pi by 0.1
    //output(PNG("docs/","test"), xyChart(x ->(math.sin(_), math.cos(_))))



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


