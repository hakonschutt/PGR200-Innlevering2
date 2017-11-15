package innlevering.client;

import java.io.*;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.Random;

/**
 * Main client class.
 * This connects to the server and listes for input, and execute client output.
 * Created by hakonschutt on 21/10/2017.
 */
public class Client {

    private Random r = new Random();
    private int ID = r.nextInt(9999 - 1000) + 1000;
    private BufferedReader readInputFromCommandPromp;
    private BufferedReader inputDataFromServer;
    private PrintWriter outputDataFromClient;

    /**
     * Connects to the socket and sets the fields for the class
     */
    public Client() {
        try (Socket socket = new Socket("localhost", 1024)) {

            readInputFromCommandPromp = new BufferedReader(new InputStreamReader(System.in));
            outputDataFromClient = new PrintWriter(socket.getOutputStream(), true);
            inputDataFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            outputDataFromClient.println(String.valueOf(ID));

            System.out.println("Your session ID is " + ID + ".");

            socket.setSoTimeout(5000);
            String clientInput;

            do {
                clientInput = runClient();
            } while (!clientInput.equals("quit"));

        } catch (SocketTimeoutException e){
            System.out.println("The socket timed out.");
        } catch (IOException e){
            System.out.println("Client error " + e.getMessage());
        }
    }

    /**
     * Listens for input from the server and prints everything.
     * When the server has nothing to send, the user can execute commands
     * @throws IOException
     */
    public String runClient() throws IOException {
        if (inputDataFromServer.ready()){
            String serverInput = inputDataFromServer.readLine();
            System.out.println(serverInput);
        }

        if (readInputFromCommandPromp.ready()){
            String clientInput = readInputFromCommandPromp.readLine();
            outputDataFromClient.println(clientInput);
            return clientInput;
        }

        return "";
    }

    /**
     * Runs the client class.
     * @param args
     */
    public static void main(String[] args) {
        new Client();
    }
}
