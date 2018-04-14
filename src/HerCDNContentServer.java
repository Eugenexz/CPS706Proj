import java.io.*;
import java.net.*;

class HerCDNContentServer {
    public static void main(String argv[]) throws Exception {

        int herWebport = 9874;

        DataInputStream input, istream;
        PrintStream ostream;
        Socket connectionSocket;
        String path = null;

        //Creates listener socket
        ServerSocket welcomeSocket = new ServerSocket(herWebport);

        while (true) {

            try{
                connectionSocket = welcomeSocket.accept();
                ostream = new PrintStream(connectionSocket.getOutputStream());
                istream = new DataInputStream(connectionSocket.getInputStream());

                path = istream.readLine();
                if ( path != null) {
                    File file = new File(path);

                    if (file.exists()) {
                        try {
                            input = new DataInputStream(new FileInputStream(file));

                            int len = (int) file.length();
                            byte buffer[] = new byte[len];
                            input.readFully(buffer);

                            ostream.write(buffer, 0, len);
                            input.close();

                        } catch (Exception e) {
                            ostream.print("Can not read " + path);
                        }
                        ostream.flush();
                    } else
                        ostream.print(path + " Not found");

                }
                connectionSocket.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}