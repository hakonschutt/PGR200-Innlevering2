package Innlevering.Client;

import java.io.*;
import java.net.Socket;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Created by hakonschutt on 21/10/2017.
 */
public class Client {

    private Random r = new Random();
    private int ID = r.nextInt(9999 - 1000) + 1000;
    private Socket socket;
    private BufferedReader readInputFromCommandPromp;
    private BufferedReader inputDataFromServer;
    private PrintWriter outputDataFromClient;

    public Client() throws IOException {
        socket = new Socket("localhost", 1024);

        readInputFromCommandPromp = new BufferedReader(new InputStreamReader(System.in));
        outputDataFromClient = new PrintWriter(socket.getOutputStream(), true);
        inputDataFromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputDataFromClient.println(String.valueOf(ID));

        System.out.println("Your session ID is " + ID + ".");
    }

    public void initClient() throws IOException {
        while(true){

            if (inputDataFromServer.ready()){
                String serverInput = inputDataFromServer.readLine();
                System.out.println(serverInput);
            }

            if (readInputFromCommandPromp.ready()){
                String clientInput = readInputFromCommandPromp.readLine();
                outputDataFromClient.println(clientInput);
                if(clientInput.equals("quit")) {
                    socket.close();
                    break;
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new Client().initClient();
    }
}
