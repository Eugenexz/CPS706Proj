import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

class ApplicationClient {

    public static void main(String argv[]) throws Exception
    {
        int port;
        byte[] sendData = new byte[1024];
        byte[] receiveData = new byte[1024];

        System.out.println("Please choose one of the options below: \n");
        System.out.println("1: HisCinema WebServer ");
        System.out.println("2: HisCinema authoritative DNS ");
        System.out.println("3: HerCDN authoritative DNS ");
        System.out.println("4: Local DNS ");

        Scanner sc = new Scanner(System.in);
        int connection = sc.nextInt();

        if (connection == 1) port = 9875;

        else if(connection == 2) {
            port = 9876;
            System.out.println("Connected to HisCinema Authoritative DNS...");
        }
        else if(connection == 3){
            port = 9877;
            System.out.println("Connected to HerCDN Authoritative DNS...");
        }
        else if(connection == 4){
            port = 9878;
            System.out.println("Connected to local DNS...");
        }
        else{
            port = 9878;
            System.out.println("Connected to local DNS...");
        }


        //TCP Connection with webservers
        if (connection == 1) {

            Socket clientSocket = new Socket("localhost", port);

            DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
            BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            outToServer.writeBytes("index.html" + '\n');

            String response;
            while ((response = inFromServer.readLine()) != null){
                System.out.println(response);
            }

            clientSocket.close();

        }
        //UDP Connection with DNS servers
        else {

            BufferedReader request = new BufferedReader(new InputStreamReader(System.in));
            DatagramSocket clientSocket = new DatagramSocket();
            InetAddress IPAddress = InetAddress.getByName("localhost");

            String sentence = request.readLine();
            sendData = sentence.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            clientSocket.receive(receivePacket);

            String response = new String(receivePacket.getData());

            System.out.println("Response: " + response);
            clientSocket.close();

        }



    }
}
