package club.javalearn.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author king-pan
 * @date 2018/12/20
 * @Description ${DESCRIPTION}
 */
public class TcpServer {


    public static void main(String[] args) {
        ServerSocket sc = null;
        BufferedReader bufferedReader = null;
        try {
            sc = new ServerSocket(2500);
            while (true) {
                //等待客户端连接
                Socket socket = sc.accept();
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println(bufferedReader.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (sc != null) {
                    sc.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
