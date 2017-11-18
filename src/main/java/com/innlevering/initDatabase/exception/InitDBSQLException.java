package com.innlevering.initDatabase.exception;

import java.sql.SQLException;

/**
 * Created by hakonschutt on 18/11/2017.
 */
public class InitDBSQLException extends SQLException {

    public InitDBSQLException(String message) {
        super(message);
    }

    public static String getErrorMessage(String exceptionCode){
        switch(exceptionCode){
            case "connection":
                return "Unable to setup a connection with the given entries.";
            case "executeUpdateFK":
                return "Unable to execute update of foreign keys";
            case "noValidation":
                return "Unable to validate if database exists";
            case "createDatabase":
                return "Unable to create the given database";
            case "overwriteDatabase":
                return "Unable to overwrite the current database";
            case "queryTables":
                return "Unable to query for tables";
            case "insertDataToTableQuery":
                return "Unable to insert data into table in database";
            case "createTableQuery":
                return "Unable to create table for database";
            default:
                return "Unknown SQLException";
        }
    }
}
