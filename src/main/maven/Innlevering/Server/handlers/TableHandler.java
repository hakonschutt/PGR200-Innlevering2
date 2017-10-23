package Innlevering.Server.handlers;

import Innlevering.Server.database.DBHandler;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class TableHandler {
    private DBHandler handler;

    public TableHandler(){
        handler = new DBHandler();
    }

    public String[] getAllTablesFormatted() throws Exception {
        String[] tables = handler.getAllTables();

        for(int i = 0; i < tables.length; i++){
            String temp = tables[i];
            String newTable = String.format("%-30s", temp);
            tables[i] = newTable;
        }

        return tables;
    }

    public String getChoicenTable(String[] tables, int i){
        return tables[i - 1];
    }
}
