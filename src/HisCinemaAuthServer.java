import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

class HisCinemaAuthServer {


    public static void main(String argv[]) throws Exception
    {
        Records rec = new Records("video.netcinema.com", "herCDN.com", "R");

        DatagramSocket serverSocket = new DatagramSocket(9876);

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            System.out.println("hisCinema.com Packet Received \n");
            String hostname = new String(receivePacket.getData());

            System.out.println("hisCinema.com Packet Data: " + hostname + "\n");

            InetAddress IPAddress = receivePacket.getAddress();

            int port = receivePacket.getPort();


            System.out.println("Checking if hostname exists... \n");
            if(hostname.contains(rec.getName())){
                sendData = rec.getValue().getBytes();
                System.out.println("Success\n");
            }
            else {
                System.out.println("Failure\n");
                sendData = "No Such hostname Exists".getBytes();
            }

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            serverSocket.send(sendPacket);

        }
    }
} 