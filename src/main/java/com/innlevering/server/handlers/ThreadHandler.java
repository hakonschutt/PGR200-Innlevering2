package com.innlevering.server.handlers;

import com.innlevering.exception.ServerFileNotFoundException;
import com.innlevering.exception.ServerIOException;
import com.innlevering.exception.ServerSQLException;
import com.innlevering.server.database.DBContentHandler;

/**
 * Main thread assists class
 * Created by hakonschutt on 23/10/2017.
 */
public class ThreadHandler {
    private DBContentHandler dataHandler;

    /**
     * sets the field element
     */
    public ThreadHandler(){
        dataHandler = new DBContentHandler();
    }

    /**
     * Returns all tables formatted to the thread
     * @return
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    public String[] getAllTablesFormatted() throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String[] tables = dataHandler.getAllTables();

        for(int i = 0; i < tables.length; i++){
            String temp = tables[i];
            String newTable = String.format("%-30s", temp);
            tables[i] = newTable;
        }

        return tables;
    }

    /**
     * Returns all the columns formatted to the thread
     * @param tableName
     * @return
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    public String[] getAllColumnsFormatted( String tableName ) throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String stripTableName = tableName.replaceAll(" ", "");
        String[] columns = dataHandler.getAllColumns(stripTableName);

        for(int i = 0; i < columns.length; i++){
            String temp = columns[i];
            String newColumn = String.format("%-30s", temp);
            columns[i] = newColumn;
        }

        return columns;
    }

    /**
     * Returns the search content based of the search string
     * @param searchString
     * @param columns
     * @param chosenColumn
     * @param tableName
     * @return
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    public String[] getSearchStringResult(String searchString, String[] columns, String chosenColumn, String tableName) throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String stripColumnName = chosenColumn.replaceAll(" ", "");
        String stripTableName = tableName.replaceAll(" ", "");

        for (int i = 0; i < columns.length; i++){
            String temp = columns[i];
            String newName = temp.replaceAll(" ", "");
            columns[i] = newName;
        }

        searchString = "%" + searchString + "%";

        return dataHandler.getSearchContent(searchString, columns, stripColumnName, stripTableName);
    }

    /**
     * Returns all table content to the thread
     * @param tableName
     * @return
     * @throws ServerFileNotFoundException
     * @throws ServerIOException
     * @throws ServerSQLException
     */
    public String[] getTableContent(String tableName) throws ServerFileNotFoundException, ServerIOException, ServerSQLException {
        String stripTableName = tableName.replaceAll(" ", "");
        return dataHandler.getTableContent( stripTableName );
    }

    /**
     * Returns the column or table based of user input
     * @param tables
     * @param i
     * @return
     */
    public String getElementFromUserInput(String[] tables, int i){
        return tables[i - 1];
    }
}
