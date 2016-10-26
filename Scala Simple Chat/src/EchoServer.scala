import java.io._
import ocsf.server._
//remove if not needed
import scala.collection.JavaConversions._

object ServerDemo {

  val DEFAULT_PORT = 5555

  def main(args: Array[String]) {
    var port = 0
    try {
      port = java.lang.Integer.parseInt(args(0))
    } catch {
      case t: Throwable => port = DEFAULT_PORT
    }
    val sv = new EchoServer(port)
    try {
      sv.listen()
    } catch {
      case ex: Exception => println("ERROR - Could not listen for clients!")
    }
  }
}

class EchoServer(port: Int) extends AbstractServer(port) {

  def handleMessageFromClient(msg: AnyRef, client: ConnectionToClient) {
    println("Message received: " + msg + " from " + client)
    this.sendToAllClients(msg)
  }

  override protected def serverStarted() {
    println("Server listening for connections on port " + getPort)
  }

  override protected def serverStopped() {
    println("Server has stopped listening for connections.")
  }
}

