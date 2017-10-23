package Innlevering.Server.handlers;

import Innlevering.Server.database.DBHandlerData;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class ThreadHandler {
    private DBHandlerData dataHandler;

    public ThreadHandler(){
        dataHandler = new DBHandlerData();
    }

    public String[] getAllTablesFormatted() throws Exception {
        String[] tables = dataHandler.getAllTables();

        for(int i = 0; i < tables.length; i++){
            String temp = tables[i];
            String newTable = String.format("%-30s", temp);
            tables[i] = newTable;
        }

        return tables;
    }

    public String[] getAllColumnsFormatted( String tableName ) throws Exception {
        String stripTableName = tableName.replaceAll(" ", "");
        String[] columns = dataHandler.getAllColumns(stripTableName);

        for(int i = 0; i < columns.length; i++){
            String temp = columns[i];
            String newColumn = String.format("%-30s", temp);
            columns[i] = newColumn;
        }

        return columns;
    }

    public String[] getSearchStringResult(String searchString, String[] columns, String chosenColumn, String tableName) throws Exception {
        String stripcolumnName = chosenColumn.replaceAll(" ", "");
        String stripTableName = tableName.replaceAll(" ", "");

        for (int i = 0; i < columns.length; i++){
            String temp = columns[i];
            String newName = temp.replaceAll(" ", "");
            columns[i] = newName;
        }

        searchString = "%" + searchString + "%";

        return dataHandler.getSearchContent(searchString, columns, stripcolumnName, stripTableName);
    }

    public String[] getTableContent(String tableName) throws Exception {
        String stripTableName = tableName.replaceAll(" ", "");
        return dataHandler.getTableContent( stripTableName );
    }

    public String getElementFromUserInput(String[] tables, int i){
        return tables[i - 1];
    }
}
