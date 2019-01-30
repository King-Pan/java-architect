package club.javalearn.socket.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author king-pan
 * @date 2019/1/30
 * @Description BIO服务端
 */
public class Server {
    public static void main(String[] args) {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(8000);
            System.out.println("服务端启动成功");
            //阻塞，等待客户端连接
            Socket socket = serverSocket.accept();
            System.out.println("客户端连接成功.");
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line = null;
            while (true){
                line = in.readLine();
                if(line!=null){
                    System.out.println(line);
                }else{
                    break;
                }

            }
            PrintWriter out = new PrintWriter(socket.getOutputStream());
            out.println("服务器: 客户端连接成功，发送数据");

            //
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
