/**
 * Created by Han Yu on 2018-04-12.
 */
import java.io.*;
import java.net.*;

public class TCPHisCinemaWebServer {

    public static void main(String argv[]) throws Exception {
        String clientSentence;
        String capitalizedSentence;


        ServerSocket welcomeSocket = new ServerSocket(6789);

        while (true) {
            System.out.println("Waiting for connection...");

            Socket connectionSocket = welcomeSocket.accept();

            System.out.println("Connected\n");
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));


            DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());


            clientSentence = inFromClient.readLine();

            capitalizedSentence = clientSentence.toUpperCase() + '\n';


            outToClient.writeBytes(capitalizedSentence);
        }
    }
}
