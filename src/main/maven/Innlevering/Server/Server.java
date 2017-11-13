package innlevering.server;

import innlevering.server.database.DBValidator;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * server class for server - client program.
 * Creates a socketServer with the necassary port.
 * Sends the client into a uniq thread when a clients tries to connect
 * Created by hakonschutt on 21/10/2017.
 */
public class Server {
    private static final int PORT_NUMBER = 1024;

    /**
     * Main method to server that makes a check for the necassary tables.
     * If all tables are present it starts the server.
     * If not it quits and prints out a message to the user.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        DBValidator dbSetup = new DBValidator();

        if (dbSetup.startDatabaseCheck()) {
            System.out.println("Checking database for tables.");
            new Server().runServer();
        } else {
            System.out.println("Unable to start server. Check you database!");
            System.out.println("If you haven't run the initDatabase class, \nrun that and try starting again.");
        }
    }

    /**
     * RunServer method starts a new socket server.
     * It pushes the users into a uniq thread when they connect.
     * @throws IOException
     */
    public void runServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
        System.out.println("server is up & ready for connetion....");

        while(true){
            Socket socket = serverSocket.accept();
            ServerThread threadJob = new ServerThread(socket);
            new Thread(threadJob).start();
        }
    }
}
