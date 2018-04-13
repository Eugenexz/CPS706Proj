import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

class UDPClientTest {

    public static void main(String argv[]) throws Exception
    {
        int port;

        System.out.print("Do you want to connect to his(1), her(2), or DNS(3)? Enter the corresponding number: ");
        Scanner sc = new Scanner(System.in);
        int connection = sc.nextInt();
        if(connection == 1) {
            port = 9876;
            System.out.println("Connected to hisCinema Auth Server...");
        }
        else if(connection == 2){
            port = 9877;
            System.out.println("Connected to herCDN Auth Server...");
        }
        else if(connection == 3){
            port = 9878;
            System.out.println("Connected to DNS...");
        }
        else{
            port = 9878;
            System.out.println("Connected to DNS...");
        }

        while(true) {
            BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));

            DatagramSocket clientSocket = new DatagramSocket();

            InetAddress IPAddress = InetAddress.getByName("localhost");

            byte[] sendData = new byte[1024];
            byte[] receiveData = new byte[1024];

            String sentence = inFromUser.readLine();

            sendData = sentence.getBytes();

            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);

            clientSocket.send(sendPacket);

            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);

            clientSocket.receive(receivePacket);

            String servermsg = new String(receivePacket.getData());

            System.out.println("FROM DNS SERVER: " + servermsg);
            clientSocket.close();
        }

    }
}
