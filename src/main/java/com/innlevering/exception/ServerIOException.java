package com.innlevering.exception;

import java.io.IOException;

/**
 * Created by hakonschutt on 17/11/2017.
 */
public class ServerIOException extends IOException {
    public ServerIOException(String message) {
        super(message);
    }

    public static String getErrorMessage(String exceptionCode){
        switch(exceptionCode){
            case "readProperties":
                return "Unable to read property file. Make sure its formatted correctly.";
            default:
                return "Unknown IOException";
        }
    }
}
