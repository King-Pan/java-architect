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
        Socket socket = null;
        PrintWriter printWriter = null;
        try {
            socket = new Socket("127.0.0.1", 2500);
            printWriter = new PrintWriter(socket.getOutputStream(), true);
            printWriter.println("Hello");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (printWriter != null) {
                    printWriter.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
