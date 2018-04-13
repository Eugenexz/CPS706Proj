import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

class ApplicationClient {

    public static void main(String argv[]) throws Exception
    {
        String url = "";
        String dnsResponse = "";
        String fileName = "";
        String pathName ="C:\\Users\\John\\Desktop\\CPS706\\";

        int port;
        int ddnsport = 9878;
        int hisAuthport = 9876;
        int herAuthport = 9877;
        int herCDNWebPort = 9874;
        int hisCinWebPort = 9875;

        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        System.out.println("Please choose one of the options below: \n");
        System.out.println("1: HisCinema WebServer ");
        System.out.println("2: HisCinema authoritative DNS ");
        System.out.println("3: HerCDN authoritative DNS ");
        System.out.println("4: Local DNS ");
        System.out.println("5: HerCDN ContentServer ");

        Scanner sc = new Scanner(System.in);
        int connection = sc.nextInt();

        if (connection == 1) port = hisCinWebPort;

        else if(connection == 2) {
            port = hisAuthport;
            System.out.println("Connected to HisCinema Authoritative DNS...");
        }
        else if(connection == 3){
            port = herAuthport;
            System.out.println("Connected to HerCDN Authoritative DNS...");
        }
        else if(connection == 4){
            port = ddnsport;
            System.out.println("Connected to local DNS...");
        }
        else if(connection == 5){
            port = herCDNWebPort;
        }
        else{
            port = ddnsport;
            System.out.println("Connected to local DNS...");
        }

        while(true) {
            //TCP Connection with www.hisCinema.com
            if (connection == 1) {

                Socket clientSocket = new Socket("localhost", port);

                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                System.out.println("Connected to www.hiscinema.com. Please select a file below.");
                outToServer.writeBytes("index.html" + '\n');

                String response;
                while ((response = inFromServer.readLine()) != null) {
                    System.out.println(response);
                }
                Scanner scn = new Scanner(System.in);
                int file = scn.nextInt();
                if(file == 1){
                    System.out.println("Selected F1");
                    url = "https://video.hiscinema.com/F1.mp4";
                    fileName = "F1.mp4";
                }
                else if (file == 2){
                    System.out.println("Selected F2");
                    url = "https://video.hiscinema.com/F2.mp4";
                    fileName = "F2.mp4";
                }
                else if (file == 3){
                    System.out.println("Selected F3");
                    url = "https://video.hiscinema.com/F3.mp4";
                    fileName = "F3.mp4";
                }
                else if (file == 4){
                    System.out.println("Selected F4");
                    url = "https://video.hiscinema.com/F4.mp4";
                    fileName = "F4.mp4";
                }

                clientSocket.close();

                System.out.println("Connecting to local DNS...");

                //Switching to Local dns port
                port = ddnsport;
                connection = 4;
            }

            //TCP connection with content server www.herCDN.com
            else if (connection == 5) {
                System.out.println("Connecting to www.herCDN.com to retrieve video...");
                int current = 0;
                int bytesRead = 0;

                //Switches to herCDNWebServer port
                port = herCDNWebPort;

                Socket clientSocket = new Socket(dnsResponse, port);
                DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
                FileOutputStream fileStream = new FileOutputStream(pathName + fileName);
                InputStream inFromServer = clientSocket.getInputStream();
                BufferedOutputStream outToClient = new BufferedOutputStream(fileStream);
                byte [] byteArray  = new byte [99999];

                System.out.println("Connected to www.herCDN.com");

                System.out.println("Retrieving file from www.herCDN.com...");

                //Not sure if this works. Suppose to transfer the bytes
                while(bytesRead < inFromServer.available()){
                    bytesRead =
                            inFromServer.read(byteArray, current, (byteArray.length-current));
                    if(bytesRead >= 0) current += bytesRead;
                }

                outToClient.write(byteArray, 0 , current);
                outToClient.flush();
                outToServer.writeBytes(fileName + '\n');
                System.out.println("File retrieved");
                clientSocket.close();
                break;
            }

            //UDP Connection with DNS servers
            else {

                DatagramSocket clientSocket = new DatagramSocket();
                InetAddress IPAddress = InetAddress.getByName("localhost");

                sendData = url.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                clientSocket.send(sendPacket);

                DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
                clientSocket.receive(receivePacket);

                dnsResponse = new String(receivePacket.getData());

                System.out.println("DNS Response: " + dnsResponse);
                System.out.println("Finished contacting local DNS");
                clientSocket.close();
                connection = 5;
            }
        }
    }
}
