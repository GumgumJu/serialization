package serialization

import java.net.ServerSocket
import java.net.Socket
import java.io.{BufferedReader, InputStreamReader, PrintWriter}
import scala.util.Using
import scala.io.Source

object TcpServer {
    @throws[IOException]
    def main(args: Array[String]): Unit = {
      val port = 9071
      val connectionSocket = new ServerSocket(port)
      // 监听端口，accept为阻塞方法
      System.out.println("Get socket successfully, wait for request...")
      val communicationSocket = connectionSocket.accept
      // 获取输入流，读取数据（此处有问题）
      val inputStream = communicationSocket.getInputStream
      val inputStreamReader = new InputStreamReader(inputStream)
      val bufferedReader = new BufferedReader(inputStreamReader)
      var message = null
        while ( {
                 (message = bufferedReader.readLine) != null
           }) System.out.printf("get message from client : %s", message)
      communicationSocket.shutdownInput()
      // 获取输出流,返回结果
      val outputStream = communicationSocket.getOutputStream
      val outputStreamWriter = new OutputStreamWriter(outputStream)
      val bufferedWriter = new BufferedWriter(outputStreamWriter)
      bufferedWriter.write("收到，结束！")
      bufferedWriter.flush()
      communicationSocket.shutdownOutput()
      // close
      bufferedWriter.close()
      bufferedReader.close()
    }
  }
