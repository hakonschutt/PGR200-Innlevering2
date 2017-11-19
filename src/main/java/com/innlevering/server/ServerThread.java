package com.innlevering.server;

import com.innlevering.exception.*;
import com.innlevering.server.handlers.ThreadHandler;
import java.io.*;
import java.net.Socket;
import java.io.BufferedReader;
import java.util.InputMismatchException;

/**
 * Main thread class for server program.
 * It runs all the necassary command the client wants to execute.
 * Created by hakonschutt on 21/10/2017.
 */
public class ServerThread implements Runnable {
    private Socket socket;
    private BufferedReader clientInput;
    private PrintWriter threadOutput;
    private ThreadHandler threadHandler;
    private int clientID;

    /**
     * Constructor that initiziates all fileds with neccassary information.
     * It also sets the client id for teh current client.
     * @param socket
     * @throws IOException
     */
    public ServerThread(Socket socket) throws IOException {
        this.socket = socket;
        threadHandler = new ThreadHandler();

        clientInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        threadOutput = new PrintWriter(socket.getOutputStream(), true);
        this.clientID = Integer.parseInt(clientInput.readLine());
    }

    /**
     * Main run method for thread.
     * It takes a user input and executes a command based of the user input.
     */
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
                    searchCommand();
                } else if (message.equals("print")){
                    printCommand();
                } else if (message.equals("table")){
                    tableCommand();
                } else {
                    threadOutput.println("Not a valid command!");
                }

                threadOutput.println("Write a command: ");
            }

            System.out.println(clientID + " - Closed the connection.");
        } catch (IOException e){
            System.err.println("Unable to read client entry for client with id " + clientID);
        } finally {
            try {
                threadOutput.println("Closing connection.");
                socket.close();
            } catch (IOException e){
                System.err.println("Error while closing socket. " + e.getMessage() + ". For client with id " + clientID);
            }
        }
    }

    /**
     * Retrives alle the tables for the user. Gets the chosen table, and prints all columns.
     * When the user has picked a column it executes the search for the string within that column.
     */
    private void searchCommand() {
        String tableName = chooseTable();
        if(tableName != null){
            try {
                String[] columns = threadHandler.getAllColumnsFormatted(tableName);
                threadOutput.println(String.format("%-20S", "Choose a column by index (1-" + columns.length + "):"));
                threadOutput.println(String.format("%-20S", "------------------------------"));
                printStringArrayWithIndex(columns);

                String chosenColumn = userChoice(columns);

                threadOutput.println(String.format("%-20S", "Search string: "));
                String searchString = clientInput.readLine();
                printStringArrayWithoutIndex(threadHandler.getSearchStringResult(searchString, columns, chosenColumn, tableName));

            } catch (NumberFormatException e){
                threadOutput.println("Invalid column entry.");
            } catch (ServerFileNotFoundException | ServerIOException | ServerSQLException e){
                threadOutput.println(ClientExceptionHandler.sqlExceptions("search"));
                System.err.println(e.getMessage() + clientID);
            } catch (IOException e){
                System.err.println("Unable to read client entry for client with id " + clientID);
            }
        }
    }

    /**
     * Prints the table content based of the choosen tablen i chooseTable method
     */
    private void printCommand() {
        String tableName = chooseTable();
        try {
            if(tableName != null){
                printStringArrayWithoutIndex( threadHandler.getTableContent( tableName ));
            }
        } catch (ServerFileNotFoundException | ServerIOException | ServerSQLException e){
            threadOutput.println(ClientExceptionHandler.sqlExceptions("print"));
            System.err.println(e.getMessage() + clientID);
        }
    }

    /**
     * Prints all the tables and returns a table that the user whants to search or print from based on the method calling it
     * @return
     */
    private String chooseTable() {
        try {
            String[] tables = threadHandler.getAllTablesFormatted();
            threadOutput.println(String.format("%-20S", "Choose a table by index (1-8):"));
            threadOutput.println(String.format("%-20S", "------------------------------"));
            printStringArrayWithIndex(tables);

            return userChoice(tables);
        } catch (ServerFileNotFoundException | ServerIOException | ServerSQLException e){
            threadOutput.println(ClientExceptionHandler.sqlExceptions("table"));
            System.err.println(e.getMessage() + clientID);
            return null;
        }
    }

    /**
     * Validates if the users choice is within the scope of the size. If it evaluates to true it returns the users choice
     * @param array
     * @return
     */
    public String userChoice(String[] array) {
        while(true){
            int asw;

            try {
                asw = Integer.parseInt(clientInput.readLine());
            } catch (InputMismatchException | NumberFormatException | IOException e){
                threadOutput.println("The entry is not valid. Try again: ");
                continue;
            }

            if (asw <= array.length && asw > 0){
                return threadHandler.getElementFromUserInput(array, asw);
            } else {
                threadOutput.println("The entry is out of range. Try again: ");
            }
        }
    }

    /**
     * Prints all the tables in the database
     */
    private void tableCommand() {
        try {
            String[] tables = threadHandler.getAllTablesFormatted();
            threadOutput.println(String.format("%-20S", "All the available tables"));
            threadOutput.println(String.format("%-20S", "----------------------------"));
            printStringArrayWithoutIndex(tables);
        } catch (ServerFileNotFoundException | ServerIOException | ServerSQLException e){
            threadOutput.println(ClientExceptionHandler.sqlExceptions("table"));
            System.err.println(e.getMessage() + clientID);
        }
    }

    /**
     * Takes a string array as a parameter, and prints all entries in a formated string to the client
     * without an index appeanded to the entries
     * @param strings
     */
    private void printStringArrayWithoutIndex(String[] strings){
        for (int i = 0; i < strings.length; i++){
            threadOutput.println(strings[i]);
        }
    }

    /**
     * Takes a string array as a parameter, and prints all entries in a formated string to the client
     * with an index appeanded to the entries
     * @param strings
     */
    private void printStringArrayWithIndex(String[] strings){
        for (int i = 0; i < strings.length; i++){
            String index = String.format("%-3s", (i + 1));
            threadOutput.println(index + strings[i]);
        }
    }

    /**
     * Prints the instruction page to the client.
     */
    private void printInstructions(){
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
