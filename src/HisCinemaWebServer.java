import java.net.*;
import java.io.*;

public class HisCinemaWebServer implements Runnable{

    //localhost:8080

    protected static ServerSocket server = null;

    public static void main(String arg[])
    {
        try {
            server = new ServerSocket(8080);
            System.out.println("Server is listening for incoming requests...");
            Thread t1 = new Thread(new HisCinemaWebServer());
            t1.start();
        }
        catch (IOException e) {
            System.out.println(e);
        }
    }

    @Override
    public void run() {

        while (true) {

            try {
                Socket client = server.accept();
                System.out.println(client.getRemoteSocketAddress() + " has established a connection.");

                Thread t2 = new Thread(new HisCinemaWebServer());
                t2.start();
                new Thread(new SocketHandler(client)).start();


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}