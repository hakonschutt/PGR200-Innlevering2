package Innlevering.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by hakonschutt on 21/10/2017.
 */
public class ServerThread implements Runnable {
    private Socket socket;
    private PrintWriter printWriter;
    private BufferedReader clientInput;
    private int clientID;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;

        clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        printWriter = new PrintWriter(socket.getOutputStream(), true);
        clientID = Integer.parseInt(clientInput.readLine());
    }

    public void run() {
        try {
            String message = null;
            System.out.println("New user(" + clientID + ") is connected.");
            printWriter.println("Welcome to my server");

            while ((message = clientInput.readLine()) != null){
                System.out.println("User "+ clientID + ": " + message );
                printWriter.println("Server  ==> " + message);
            }

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
