package Innlevering.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

/**
 * Created by hakonschutt on 21/10/2017.
 */
public class Client {

    private static Random r = new Random();
    private static int ID = r.nextInt(9999 - 1000) + 1000;

    public static void main(String[] args) throws UnknownHostException, IOException {

        Socket socket = new Socket("localhost", 4444);
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        while(true){
            String readerInput = bufferedReader.readLine();
            printWriter.println(ID + ": " + readerInput);
        }
    }
}
