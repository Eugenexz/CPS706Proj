import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

class HisCinemaAuthServer {


    public static void main(String argv[]) throws Exception
    {
        int hisAuthPort = 9876;

        List<Record> records = new ArrayList<>();
        records.add(new Record("video.hiscinema.com", "herCDN.com", "R"));
        records.add(new Record("herCDN.com", "www.herCDN.com", "CN"));
        records.add(new Record("www.herCDN.com", "localhost", "A"));

        DatagramSocket serverSocket = new DatagramSocket(hisAuthPort);

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        while(true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serverSocket.receive(receivePacket);
            System.out.println("hisCinema.com Packet Received \n");
            String hostname = new String( receivePacket.getData(),0, receivePacket.getLength());

            System.out.println("hisCinema.com Packet Data: " + hostname + "\n");

            InetAddress IPAddress = receivePacket.getAddress();

            int port = receivePacket.getPort();


            System.out.println("Checking if hostname exists... \n");

            for(Record record : records){

                if(hostname.contains(record.getName())){
                    sendData = record.getValue().getBytes();
                    System.out.println("Success\n");
                    break;
                }
                else {
                    System.out.println("Failure\n");
                    sendData = "No Such hostname Exists".getBytes();
                }
            }

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            serverSocket.send(sendPacket);
        }
    }
} 