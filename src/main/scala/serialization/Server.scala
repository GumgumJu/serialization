package serialization

import java.net.ServerSocket
import java.net.Socket
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.Using
import scala.io.Source

object ServerMain {
  val port = 9071

  def main(args: Array[String]): Unit =
  // server-side socket creation
    Using(new ServerSocket(port)) { server =>
      println(s">>> serve on port $port")

      // loop forever, as it is a service
      while (true)
      // wait for a connection from a client
        Using(server.accept()) { client =>
          processDataFrom(client)
        }.fold( // fold acts here like an if
          // if we have an error with the client...
          error => println(s">>> client connection failure: ${error.getMessage}"),
          // otherwise, we do nothing
          _ => ()
        )
    }.get // calling "get" here might throw an exception if there is an error

  def processDataFrom(client: Socket): Unit = {
    println(s">>> connected with a client")

    val name: String = readDataFrom(client).next()
    println(s">>> got a name: $name")

    println(s">>> sending a response")
    sendDataTo(client, s"Hello $name!")
  }

  def readDataFrom(client: Socket): Iterator[String] = {
    // get data sent by the client to the server
    val source = Source.fromInputStream(client.getInputStream)

    source.getLines()
  }

  def sendDataTo(client: Socket, data: String): Unit = {
    val printer = new PrintWriter(client.getOutputStream)
    printer.println(data)
    // flush is necessary to ensure that is sent to the client
    printer.flush()
  }

}
