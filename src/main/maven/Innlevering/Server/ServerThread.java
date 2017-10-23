package Innlevering.Server;

import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;

/**
 * Created by hakonschutt on 21/10/2017.
 */
public class ServerThread implements Runnable {
    private Socket socket;
    private BufferedReader clientInput;
    private PrintWriter threadOutput;
    private int clientID;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;

        clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        threadOutput = new PrintWriter(socket.getOutputStream(), true);
        this.clientID = Integer.parseInt(clientInput.readLine());
    }

    public void run() {
        try {
            String message = null;
            System.out.println("New user(" + clientID + ") is connected.");
            printInstructions();
            threadOutput.println("Write a command: ");

            while ((message = clientInput.readLine()) != null){
                System.out.println("User "+ clientID + ": " + message );

                if(message.equals("quit")){
                    threadOutput.println("Closing connection.");
                    break;
                } else if(message.equals("info")) {
                    printInstructions();
                } else if (message.equals("search")){
                    threadOutput.println("Server! This is a search function");
                    threadOutput.println("Server! Hello user. Type teacher to test!");
                    if(clientInput.readLine().equals("teacher")){
                        threadOutput.println("Server! WORKED!");
                    } else {
                        threadOutput.println("Server! DIDNT WORK!");
                    }
                } else if (message.equals("print")){
                    threadOutput.println("Server! This is a print function");
                } else {
                    threadOutput.println("Not a valid command!");
                }

                threadOutput.println("Write a command: ");
            }

            System.out.println(clientID + "- Closed the connection.");
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void printStringArray(String[] strings){
        for (int i = 0; i < strings.length; i++){
            threadOutput.println(strings[i]);
        }
    }


    public void printInstructions(){
        String line = String.format("%-25S", "-----------------------------------------");
        threadOutput.println(line);
        String intro = String.format("%-25S", "Option commands");
        threadOutput.println(intro);
        threadOutput.println(line);
        String infoCommand = String.format("%-10s", "info");
        String infoInfo = String.format("%-15s", "Prints the command page");
        threadOutput.println(infoCommand + infoInfo);
        String searchCommand = String.format("%-10s", "search");
        String searchInfo = String.format("%-15s", "Search in a chosen table");
        threadOutput.println(searchCommand + searchInfo);
        String printCommand = String.format("%-10s", "print");
        String printInfo = String.format("%-15s", "Print table from the database");
        threadOutput.println(printCommand + printInfo);
        String quitCommand = String.format("%-10s", "quit");
        String quitInfo = String.format("%-15s", "Quits the program");
        threadOutput.println(quitCommand + quitInfo);
        threadOutput.println(line);
    }
}
