package club.javalearn.udp;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * @author king-pan
 * @date 2018/12/20
 * @Description ${DESCRIPTION}
 */
public class UdpServer {

    public static void main(String[] args) {
        try {
            DatagramSocket ds = new DatagramSocket(2600);
            byte[] receiveData = new byte[1024];
            DatagramPacket dp = new DatagramPacket(receiveData,receiveData.length);
            ds.receive(dp);
            System.out.println(new String(receiveData));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
