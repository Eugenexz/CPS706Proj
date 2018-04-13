
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

            if ((path = pathOptions(istream.readLine())) != null) {
                File file = handleResource(path);
                if (file.exists()) {
                    try {
                        input = new DataInputStream(new FileInputStream(file));

                        ostream.print("HTTP:/1.0 200 OK\n");
                        ostream.print("Content-type: text/html\n");
                        ostream.print("Content-length: " + (int) file.length() + "\n");
                        ostream.print("\n");

                        int len = (int) file.length();
                        byte buffer[] = new byte[len];
                        input.readFully(buffer);
                        ostream.write(buffer, 0, len);
                        input.close();

                    } catch (Exception e) {
                        writeErrorHeader(ostream, "<h1>Can not read " + path + "</h1>");
                    }
                    ostream.flush();
                } else
                    writeErrorHeader(ostream, "<h1>" + path + " Not found </h1>");
            }
            client.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }


    //Helper functions

    static void writeErrorHeader(PrintStream o, String info)
    {
        o.print("HTTP:/1.0 404 Not Found\n");
        o.print("Content-type: text/html\n");
        o.print("Content-length: " + info.length() + "\n");
        o.print("\n");
        o.print(info + "\n");
    }

    static String pathOptions(String input)
    {
        if (input.length() == 0) return null;

        String path = input.substring(input.indexOf(' ') + 1);
        path = path.substring(0, path.indexOf(' '));

        if (path.equals("")) return "index.html";
        if (path.charAt(path.length() - 1) == '/') path += "index.html";

        return path;
    }

    static File handleResource(String filename)
    {
        File file = new File(filename);
        if (file.exists()) return file;
        if (filename.charAt(0) != '/') return file;
        return new File(filename.substring(1));
    }


}
