package com.innlevering.exception;

import java.sql.SQLException;

/**
 * Main SQLException class for the server.
 * Created by hakonschutt on 17/11/2017.
 */
public class ServerSQLException extends SQLException {

    public ServerSQLException(String message) {
        super(message);
    }

    public static String getErrorMessage(String exceptionCode){
        switch(exceptionCode){
            case "connection":
                return "Unable to setup a connection with the given entries.";
            case "tables":
                return "Unable to query for tables for client with id ";
            case "count":
                return "Unable to query for count to construct objects for client with id ";
            case "columns":
                return "Unable to query for column names for client with id ";
            case "tableContent":
                return "Unable to query for table content for client with id ";
            default:
                return "Unknown SQLException";
        }
    }
}
