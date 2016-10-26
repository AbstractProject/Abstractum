import java.io._
import client._
import common._
//remove if not needed
import scala.collection.JavaConversions._

object ClientDemo {

  val DEFAULT_PORT = 5555

  def main(args: Array[String]) {
    var host = ""
    val port = 0
    try {
      host = args(0)
    } catch {
      case e: ArrayIndexOutOfBoundsException => host = "localhost"
    }
    val chat = new ClientConsole(host, DEFAULT_PORT)
    chat.accept()
  }
}

class ClientConsole(host: String, port: Int) extends ChatIF {

  var client: ChatClient = _

  try {
    client = new ChatClient(host, port, this)
  } catch {
    case exception: IOException => {
      println("Error: Can't setup connection!" + " Terminating client.")
      System.exit(1)
    }
  }

  def accept() {
    try {
      val fromConsole = new BufferedReader(new InputStreamReader(System.in))
      var message: String = null
      while (true) {
        message = fromConsole.readLine()
        client.handleMessageFromClientUI(message)
      }
    } catch {
      case ex: Exception => println("Unexpected error while reading from console!")
    }
  }

  def display(message: String) {
    println("> " + message)
  }
}