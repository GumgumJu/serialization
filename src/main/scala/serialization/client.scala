package serialization

import java.net.ServerSocket
import java.net.Socket
import java.io.PrintWriter
import scala.io.Source
import scala.util.Using



object ClientMain {
  def main(args: Array[String]): Unit =
    Using(new Socket("localhost", ServerMain.port)) { server =>
      println(s">>> connected to server ${server.getInetAddress}")

      println(">>> sending request...")
      val printer = new PrintWriter(server.getOutputStream)
      printer.println("Jon")
      printer.flush()

      println(">>> waiting for a response...")
      val responseStream = Source.fromInputStream(server.getInputStream)
      for (elem <- responseStream.getLines())
        println(elem)
    }.get
}

