/**
 * Created by Han Yu on 2018-04-10.
 */

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HerCDNAuthServer {

    public static void main(String argv[]) throws Exception
    {
        int herAuthport = 9877;
        Record rec = new Record("herCDN.com", "www.herCDN.com", "CN");
        Record rec2 = new Record("www.herCDN.com", "localhost", "A");
        Record test[] = new Record[2];
        test[0] = rec;
        test[1] = rec2;

        DatagramSocket serverSocket = new DatagramSocket(herAuthport);

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            System.out.println("herCDN.com Packet Received \n");
            String hostname = new String( receivePacket.getData(),0, receivePacket.getLength());

            System.out.println("herCDN.com Packet Data: " + hostname + "\n");

            InetAddress IPAddress = receivePacket.getAddress();

            int port = receivePacket.getPort();


            for(Record r : test){
                String name = hostname;
                if(hostname.contains(r.getName()) && !(r.getType().equals("A"))){
                    hostname = r.getValue();
                }
                else if(hostname.contains(r.getName()) && (r.getType().equals("A"))){
                    hostname = r.getValue();
                    System.out.println("Found IP Address for " + name + "\n");
                    sendData = hostname.getBytes();
                    break;
                }
                else if(!hostname.contains(r.getName())){
                    sendData = "No Such hostname Exists".getBytes();
                }
            }

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            serverSocket.send(sendPacket);

        }
    }
}
