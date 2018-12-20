package club.javalearn.tcp;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author king-pan
 * @date 2018/12/20
 * @Description TCP客户端
 */
public class TcpClient {
    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 2500);
            PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("Hello");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
