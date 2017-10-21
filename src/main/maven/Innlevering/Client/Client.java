package Innlevering.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Random;

/**
 * Created by hakonschutt on 21/10/2017.
 */
public class Client {

    private Random r = new Random();
    private int ID = r.nextInt(9999 - 1000) + 1000;
    private Socket socket;
    private BufferedReader inputData;
    private BufferedReader outputData;
    private PrintWriter printWriter;

    public Client() throws IOException {
        socket = new Socket("localhost", 4444);

        inputData = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outputData = new BufferedReader(new InputStreamReader(System.in));
        printWriter = new PrintWriter(socket.getOutputStream(), true);

        printWriter.println(ID);
    }

    public void initClient() throws IOException {
        System.out.println( "Your session ID is " + ID + ".");

        while(true){
            String readerInput = outputData.readLine();
            printWriter.println(readerInput);

            System.out.println(inputData.readLine());
        }
    }

    public static void main(String[] args) throws IOException {
        new Client().initClient();
    }
}
