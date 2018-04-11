/**
 * Created by Han Yu on 2018-04-10.
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HerCDNAuthServer {

    public static void main(String argv[]) throws Exception
    {
        Records rec = new Records("herCDN.com", "www.herCDN.com", "CN");
        Records rec2 = new Records("www.herCDN.com", "IPwww", "A");

        DatagramSocket serverSocket = new DatagramSocket(9877);

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);

            String hostname = new String(receivePacket.getData());

            InetAddress IPAddress = receivePacket.getAddress();

            int port = receivePacket.getPort();


            System.out.println("Checking if hostname exists...");
            if(hostname.contains(rec.getName())){
                sendData = rec.getValue().getBytes();
                System.out.println("Success");
            }
            else sendData = "No Such hostname Exists".getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            serverSocket.send(sendPacket);

        }
    }
}
