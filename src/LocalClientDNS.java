import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class LocalClientDNS {

    public static void main(String argv[]) throws Exception
    {
        List<Record> records = new ArrayList<>();
        //Differenciate the two localhosts with different port numbers
        records.add(new Record("herCDN.com", "NSherCDN.com", "NS"));
        records.add(new Record("NSherCDN.com", "localhost", "A"));
        records.add(new Record("hiscinema.com", "NShiscinema.com", "NS"));
        records.add(new Record("NShiscinema.com", "localhost", "A"));

        int portHisCinema = 9876;
        int portHerCinema = 9877;

        byte[] receiveData = new byte[1024];
        byte[] sendData = new byte[1024];

        DatagramSocket localDNSSocket = new DatagramSocket(9878);


        while(true) {

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            localDNSSocket.receive(receivePacket);

            System.out.println("Client packet received \n");
            String hostname = new String(receivePacket.getData());

            System.out.println("Requested hostname: " + hostname + "\n");
            InetAddress IPAddress = receivePacket.getAddress();

            int port = receivePacket.getPort();


            //Will fix below
            for(Record record : records){

                if(hostname.contains(record.getName()) && !(record.getType().equals("A"))){
                    hostname = record.getValue();
                }
                else if(hostname.contains(record.getName()) && (record.getType().equals("A"))){
                    hostname = record.getValue();
                    System.out.println("Found IP Address for " + hostname + "\n");
                    sendData = hostname.getBytes();
                    break;
                }
                else if(!hostname.contains(record.getName())){
                    sendData = "No Such hostname Exists".getBytes();
                }
            }

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            localDNSSocket.send(sendPacket);

        }


    }



}

