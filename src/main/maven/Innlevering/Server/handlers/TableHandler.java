package Innlevering.Server.handlers;

import Innlevering.Server.database.DBHandlerColumnData;
import Innlevering.Server.database.DBHandlerTableData;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class TableHandler {
    private DBHandlerTableData tableHandler;
    private DBHandlerColumnData columnHandler;

    public TableHandler(){
        tableHandler = new DBHandlerTableData();
        columnHandler = new DBHandlerColumnData();
    }

    public String[] getAllTablesFormatted() throws Exception {
        String[] tables = tableHandler.getAllTables();

        for(int i = 0; i < tables.length; i++){
            String temp = tables[i];
            String newTable = String.format("%-30s", temp);
            tables[i] = newTable;
        }

        return tables;
    }

    public String[] getTableContent(String tableName) throws Exception {
        String stripTableName = tableName.replaceAll(" ", "");
        return columnHandler.getTableContent( stripTableName );
    }

    public String getChoicenTable(String[] tables, int i){
        return tables[i - 1];
    }
}
