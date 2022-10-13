import java.net.ServerSocket
import java.net.Socket
import java.io.{BufferedReader, PrintWriter}
import scala.util.Using


object ServerMain {
  def main(args: Array[String]): Unit = {
    val serverSocket = new ServerSocket(9071);

      val socket = serverSocket.accept();
      val bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream, "utf-8"));

      val data = bufferedReader.readLine();
      println(data);

      bufferedReader.close();

      socket.close();
      }
      }
