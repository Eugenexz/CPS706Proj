import java.net.*;
import java.io.*;

public class HisCinemaWebServer implements Runnable{

    //localhost:8080

    protected static ServerSocket server = null;

    public static void main(String arg[])
    {
        try {
            server = new ServerSocket(9875);
            System.out.println("HisCinema is listening for incoming requests...");
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
                System.out.println("Client has established a connection with www.hiscinema.com");

                Thread t2 = new Thread(new HisCinemaWebServer());
                t2.start();

                new Thread(new SocketHandler(client)).start();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}