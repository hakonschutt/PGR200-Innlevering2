package Innlevering.Server;

import Innlevering.Server.database.DatabaseValidation;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by hakonschutt on 21/10/2017.
 */
public class Server {
    private static final int PORT_NUMBER = 1024;

    public static void main(String[] args) throws IOException {
        DatabaseValidation dbSetup = new DatabaseValidation();

        if (dbSetup.startDatabaseCheck()) {
            System.out.println("Checking database for tables.");
            new Server().runServer();
        } else {
            System.out.println("Unable to start server. Check you database!");
            System.out.println("If you haven't run the InitDatabase class, \nrun that and try starting again.");
        }
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
