package serialization

import java.net.ServerSocket
import java.net.Socket
import java.io.PrintWriter
import scala.io.Source
import scala.util.Using
import scala.io._

object Client {
  
  def main(args: Array[String]): Unit =
    Using(new Socket("localhost", Server.port)) { server =>
      println(s">>> connected to server ${server.getInetAddress}")

      println(">>> sending request...")
      println(">>> serialization...")
      val line = StdIn.readLine()
      println("you input is: " + line)
      println(">>> deserialization...")
      val deline = StdIn.readLine()
      println("you input is: " + deline)
      val printer = new PrintWriter(server.getOutputStream)
     printer.println(line)
      printer.flush()
      println(">>> waiting for a response...")
      val responseStream = Source.fromInputStream(server.getInputStream)
      for (elem <- responseStream.getLines())
        println(elem)
    }.get
}
