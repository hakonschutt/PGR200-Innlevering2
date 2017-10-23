package Innlevering.Server;

import Innlevering.Server.handlers.TableHandler;

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
    private TableHandler tableHandler;
    private int clientID;

    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        tableHandler = new TableHandler();

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

                    //TODO: Implement search function.

                    threadOutput.println("Server! This is a search function");
                    threadOutput.println("Server! Hello user. Type teacher to test!");
                    if(clientInput.readLine().equals("teacher")){
                        threadOutput.println("Server! WORKED!");
                    } else {
                        threadOutput.println("Server! DIDNT WORK!");
                    }
                } else if (message.equals("print")){
                    printCommand();
                } else if (message.equals("table")){
                    tableCommand();
                } else {
                    threadOutput.println("Not a valid command!");
                }

                threadOutput.println("Write a command: ");
            }

            System.out.println(clientID + "- Closed the connection.");
            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printCommand() throws Exception {
        String[] tables = tableHandler.getAllTablesFormatted();
        threadOutput.println(String.format("%-20S", "Choose a table by index (1-8):"));
        threadOutput.println(String.format("%-20S", "------------------------------"));
        printStringArrayWithIndex(tables);

        int chosenTable = Integer.parseInt(clientInput.readLine());
        if(chosenTable < 9 && chosenTable > 0 ){
            String tableName = tableHandler.getChoicenTable(tables, chosenTable);
            printStringArrayWithoutIndex( tableHandler.getTableContent( tableName ));
        } else {
            threadOutput.println(String.format("%-20S", "Invalid table entry."));
        }
    }

    public void tableCommand() throws Exception {
        String[] tables = tableHandler.getAllTablesFormatted();
        threadOutput.println(String.format("%-20S", "All the available tables"));
        threadOutput.println(String.format("%-20S", "----------------------------"));
        printStringArrayWithoutIndex(tables);
    }

    public void printStringArrayWithoutIndex(String[] strings){
        for (int i = 0; i < strings.length; i++){
            threadOutput.println(strings[i]);
        }
        threadOutput.println(" ");
    }

    public void printStringArrayWithIndex(String[] strings){
        for (int i = 0; i < strings.length; i++){
            String index = String.format("%-3s", (i + 1));
            threadOutput.println(index + strings[i]);
        }
        threadOutput.println(" ");
    }

    public void printInstructions(){
        String line = String.format("%-25S", "------------------------------------------");
        threadOutput.println(line);
        threadOutput.println(String.format("%-25S", "Option commands"));
        threadOutput.println(line);
        threadOutput.println(String.format("%-10s %-15s", "info", "Prints the command page"));
        threadOutput.println(String.format("%-10s %-15s", "search", "Search in a chosen table"));
        threadOutput.println(String.format("%-10s %-15s", "print", "Prints content from table"));
        threadOutput.println(String.format("%-10s %-15s", "table", "Prints tables from the database"));
        threadOutput.println(String.format("%-10s %-15s", "quit", "Quits the program"));
        threadOutput.println(line);
    }
}
