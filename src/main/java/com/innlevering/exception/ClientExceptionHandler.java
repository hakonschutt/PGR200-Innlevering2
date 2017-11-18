package com.innlevering.exception;

/**
 * Main exception class for client feedback. This class is only used to print error messages to the user when needed.
 * Created by hakonschutt on 14/11/2017.
 */
public class ClientExceptionHandler {
    public static String sqlExceptions(String exceptionCode){
        switch(exceptionCode){
            case "table":
                return "Unable to get content from table";
            case "print":
                return "Unable to print content";
            case "search":
                return "Unable to search for content";
            default:
                return "Unknown error!";
        }
    }
}
