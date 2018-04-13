import java.io.*;
import java.net.*;

class HerCDNContentServer {
    public static void main(String argv[]) throws Exception {

        int herWebport = 9874;

        String clientSentence;

        //Creates listener socket
        ServerSocket welcomeSocket = new ServerSocket(herWebport);

        while (true) {
            Socket connectionSocket = welcomeSocket.accept();

            BufferedReader inFromClient =
                    new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

            DataOutputStream outToClient =
                    new DataOutputStream(connectionSocket.getOutputStream());

            //Reads from client
            clientSentence = inFromClient.readLine();

            //Creates file with the specified file name
            File herCDNFile = new File ("C:\\Users\\John\\IdeaProjects\\CPS706Proj\\" + clientSentence);

            //Creates new streams to send file to client
            FileInputStream fileStream = new FileInputStream(herCDNFile);
            BufferedInputStream bufferedIS = new BufferedInputStream(fileStream);

            byte [] bytearray  = new byte [(int)herCDNFile.length()];

            bufferedIS.read(bytearray,0,bytearray.length);
            outToClient.write(bytearray,0,bytearray.length);

            outToClient.flush();
        }
    }
}