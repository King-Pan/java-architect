package club.javalearn.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * @author king-pan
 * @date 2018/12/20
 * @Description ${DESCRIPTION}
 */
public class UdpClient {

    public static void main(String[] args) {
        try {
            InetAddress address = InetAddress.getByName("localhost");
            byte[] sendData = "Hello udp".getBytes();
            DatagramPacket datagramPacket = new DatagramPacket(sendData, sendData.length, address, 2600);
            DatagramSocket datagramSocket = new DatagramSocket();
            datagramSocket.send(datagramPacket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
