
import java.net.*;
import java.io.*;

public class SocketHandler implements Runnable{

    protected Socket client;

    protected static DataInputStream input, istream;
    protected static PrintStream ostream;
    protected static String path = null;

    public SocketHandler(Socket client) {
        this.client = client;
    }

    @Override
    public void run() {

        try{
            ostream = new PrintStream(client.getOutputStream());
            istream = new DataInputStream(client.getInputStream());

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
                        writeErrorHeader(ostream, "Can not read " + path);
                    }
                    ostream.flush();
                } else
                    writeErrorHeader(ostream, path + " Not found");
            }
            client.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    static void writeErrorHeader(PrintStream o, String info)
    {
        o.print("HTTP:/1.0 404 Not Found\n");
        o.print("Content-type: text/html\n");
        o.print("Content-length: " + info.length() + "\n");
        o.print("\n");
        o.print(info + "\n");
    }


}
