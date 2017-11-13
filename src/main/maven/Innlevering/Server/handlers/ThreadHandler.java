package innlevering.server.handlers;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import innlevering.server.database.DBDataHandler;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Main thread assists class
 * Created by hakonschutt on 23/10/2017.
 */
public class ThreadHandler {
    private DBDataHandler dataHandler;

    /**
     * sets the field element
     */
    public ThreadHandler(){
        dataHandler = new DBDataHandler();
    }

    /**
     * Returns all tables formatted to the thread
     * @return
     * @throws Exception
     */
    public String[] getAllTablesFormatted() throws IOException, SQLException {
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
     * @throws Exception
     */
    public String[] getAllColumnsFormatted( String tableName ) throws IOException, SQLException {
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
     * @throws Exception
     */
    public String[] getSearchStringResult(String searchString, String[] columns, String chosenColumn, String tableName) throws IOException, SQLException {
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

    /**
     * Returns all table content to the thread
     * @param tableName
     * @return
     * @throws Exception
     */
    public String[] getTableContent(String tableName) throws IOException, SQLException {
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
