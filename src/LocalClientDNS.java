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
        records.add(new Record("NShiscinema.com", "127.0.0.1", "A"));

        int portHisCinema = 9876;
        int portHerCDN = 9877;

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
                }
                else if(!hostname.contains(record.getName())){
                    //sendData = "No Such hostname Exists".getBytes();
                    System.out.println("Contacting hisCinemaDNS for Type R or V record... hisCinemaDNS port: " + portHisCinema);
                    sendData = hostname.getBytes();
                    byte[] hisIpAddr = new byte[]{127, 0, 0, 1};
                    InetAddress hisAddr = InetAddress.getByAddress(hisIpAddr);
                    DatagramPacket hisPacket = new DatagramPacket(sendData, sendData.length, hisAddr, portHisCinema);
                    localDNSSocket.send(hisPacket);

                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    localDNSSocket.receive(receivePacket);
                    hostname = new String(receivePacket.getData(),0, receivePacket.getLength());
                    System.out.println("Received Type NS Record: \"" + hostname + "\" from hisCinemaDNS port: " + portHisCinema);
                    System.out.println("hisPacket: " + hostname + "\n");

                    System.out.println("Being redirected...");

                    System.out.println("Contacting herCDNDNS for Type A... herCDNDNS port: " + portHerCDN);
                    byte[] herIpAddr = new byte[]{127, 0, 0, 1};
                    InetAddress herAddr = InetAddress.getByAddress(herIpAddr);
                    DatagramPacket herPacket = new DatagramPacket(receivePacket.getData(), sendData.length, herAddr, portHerCDN);
                    localDNSSocket.send(herPacket);

                    receivePacket = new DatagramPacket(receiveData, receiveData.length);
                    localDNSSocket.receive(receivePacket);
                    hostname = new String(receivePacket.getData(),0, receivePacket.getLength());
                    System.out.println("Received Type A Record: \"" + hostname + "\" from herCDNDNS port: " + portHerCDN);
                    System.out.println("herPacket: " + hostname + "\n");

                    sendData = hostname.getBytes();
                    break;
                }
            }

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            localDNSSocket.send(sendPacket);
        }

    }

}

