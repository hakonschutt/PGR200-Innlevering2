package Innlevering.Server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.ServerSocket;

/**
 * Created by hakonschutt on 21/10/2017.
 */
public class ServerThread implements Runnable {
    private Socket socket;
    private int number;

    public ServerThread(Socket socket, int number){
        this.socket = socket;
        this.number = number;
    }

    @Override
    public void run() {
        try {
            String msg = null;
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            while ((msg = bufferedReader.readLine()) != null){
                System.out.println("Inncomming message from " + msg );
            }

            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
