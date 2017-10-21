package Innlevering.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hakonschutt on 21/10/2017.
 */
public class Server {
    private static final int PORT_NUMBER = 4444;

    public static void main(String[] args) throws IOException {
        new Server().runServer();
    }

    public void runServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
        System.out.println("Server is up & ready for connetion....");

        while(true){
            Socket socket = serverSocket.accept();
            ServerThread threadJob = new ServerThread(socket);
            new Thread(threadJob).start();
        }
    }
}
