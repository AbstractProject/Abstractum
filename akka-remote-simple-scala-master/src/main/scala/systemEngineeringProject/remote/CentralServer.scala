package systemEngineeringProject.remote

import java.io._

//import _root_.org.jfree._
import akka.actor._
import com.typesafe.config.ConfigFactory
import org.jfree.chart.plot.PlotOrientation
import org.jfree.chart.{ChartFactory, ChartUtilities, JFreeChart}
import org.jfree.data.xy
import org.jfree.data.xy.{XYSeries, XYSeriesCollection}
import org.sameersingh.scalaplot.Implicits._

class CentralServer extends Actor {

var i: Int =0
  var end = false
  override def receive: Receive = {
    case msg: Int=>{
      end = true
      for (index <- 0 to 4) {
        if (ServerMain.clientsDone(index) == false) {
          end = false
        }
      }


      if (msg == 0) {
        for (index <- 0 to 4) {
          if (ServerMain.clients(index) == sender) {
            //println("0 got from client " + index + " polling " + ((index + 1) % 5))
            if(!end)
              ServerMain.clients((index + 1) % 5) ! "poll"
          }
        }
      }
      else {
        // FileWriter


        for (index <- 0 to 4) {
          if (ServerMain.clients(index) == sender) {
            val bw = new BufferedWriter(new FileWriter(ServerMain.file,true))
            if (ServerMain.clientsDone(index)==false)
              bw.write(msg.toString + '\n')

            bw.close()
            ServerMain.drawGraph()
            ServerMain.clientsDone(index)=true
            println("result: " + msg + "from client" + index)
            for(b <- 0 to 4) {
              println(ServerMain.clientsDone(b))
            }

            if(end == false){
              ServerMain.clients((index + 1) % 5) ! "poll"
            }
          }
        }
      }
    }

    case msg: String => {
      var id : Int = 0
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

        case "Operator started" => println("hue")
      }
      println(msg)

      sender ! msg
    }
    //case _ => {println("Received unknown msg ")}

      if(end) {
        println("end")
        ServerMain.drawGraph()
        ServerMain.startClients()
      }
  }
}

object ServerMain{

  var clients : Array[ActorRef]=Array(null,null,null,null,null)
  var clientsDone: Array[Boolean] = Array(false, false, false, false, false)
  val file = new File("C:\\Users\\Catalina\\Documents\\GitHub\\Abstractum\\akka-remote-simple-scala-master\\results.txt")

  def drawGraph(): Unit = {
    val br = new BufferedReader(new FileReader(ServerMain.file))
    var sCurrentLine: String = br.readLine();
    var dataset :XYSeriesCollection = new xy.XYSeriesCollection()
    var series :XYSeries = new XYSeries("Series")
    var average :XYSeries = new XYSeries("Average")
    var clientNumber : Int = 0
    var sum : Double = 0;

    while (sCurrentLine != null) {
      series.add( clientNumber,sCurrentLine.toDouble);
      clientNumber = clientNumber+1
      sum = sum +sCurrentLine.toDouble
      //System.out.println(sCurrentLine);
      sCurrentLine= br.readLine();
    }

    var avg : Double = sum/(clientNumber+1)
    for(index <- -1 to clientNumber) {
      average.add(index, avg)
    }

    dataset.addSeries(series);
    dataset.addSeries(average);

    var chart:JFreeChart=null
    chart=ChartFactory.createXYBarChart("Mission Time per Client","Client",false,"Time",dataset,PlotOrientation.VERTICAL,false,false,false)
    //chart=ChartFactory.createXYLineChart("Mission Time per Client","Client","Time",dataset,PlotOrientation.VERTICAL,false,false,false)
    ChartUtilities.saveChartAsJPEG(new File("C:\\Users\\Catalina\\Documents\\GitHub\\Abstractum\\akka-remote-simple-scala-master\\poza" + clientNumber % 5 + ".jpeg"),1.0f,chart,640,400)

  }

  def startClients(): Unit ={
    println("newstart")
    for(a <-0 to 4){
      clientsDone(a)=false
    }

    var ln =""
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
    clients(0) ! "poll"
    readLine()
  }


  def main(args: Array[String]) {
    //get the configuration file from classpath
    val configFile = getClass.getClassLoader.getResource("remote_application.conf").getFile
    //parse the config
    val config = ConfigFactory.parseFile(new File(configFile))
    //create an actor system with that config
    val system = ActorSystem("RemoteSystem" , config)
    //create a remote actor from actorSystem
    drawGraph()
    val remote = system.actorOf(Props[CentralServer], name="remote")
    println("Server started ")

    startClients()

  }
}


