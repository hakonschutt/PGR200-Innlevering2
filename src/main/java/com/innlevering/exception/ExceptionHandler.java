package com.innlevering.exception;

/**
 * Created by hakonschutt on 13/11/2017.
 */
public class ExceptionHandler extends Exception {

    /**
     * Possible SQLexceptions that can be thrown in the program.
     * @param exceptionCode
     */
    public static void sqlException(String exceptionCode){
        switch(exceptionCode){
            case "createDatabase":
                System.out.println("Unable to create database.");
                break;
            case "overwriteDatabase":
                System.out.println("Unable to overwrite database.");
                break;
            case "wrongDBInformation":
                System.out.println("Unable to connect with the current information");
                break;
            case "noValidation":
                System.out.println("Unable to validate database");
                break;
            case "outdatedConnection":
                System.out.println("Unable to use old connection!");
                break;
            case "upload":
                System.out.println("Unabel to upload to database.");
                break;
            case "search":
                System.out.println("Unable to search for content.");
                break;
            case "print":
                System.out.println("Unable to print content.");
                break;
            case "create":
                System.out.println("Unable to create semester plan.");
                break;
            case "semesterPrint":
                System.out.println("Unable to print semester plan.");
                break;
            default:
                System.out.println("Unknown sqlException exception");
                break;
        }
    }

    /**
     * Possible input exceptions that can be handled in this program.
     * @param exceptionCode
     */
    public static void inputException(String exceptionCode){
        switch(exceptionCode){
            case "intMismatch":
                System.out.println("The input is not av valid integer.");
                break;
            default:
                System.out.println("Unknown sqlException exception");
                break;
        }
    }

    /**
     * Possible interrupt exceptions that can be thrown.
     * @param exceptionCode
     */
    public static void interruptException(String exceptionCode){
        switch(exceptionCode){
            case "threadJoin":
                System.out.println("Unable to join threads from upload.");
                break;
            default:
                System.out.println("Unknown interrupted exception");
                break;
        }
    }

    /**
     * Possible IOExceptions that can be thrown
     * @param exceptionCode
     */
    public static void ioException(String exceptionCode){
        switch(exceptionCode){
            case "writeProperties":
                System.out.println("Unable to write to property file.");
                break;
            case "readProperties":
                System.out.println("Unable to read from property file.");
                break;
            case "readInput":
                System.out.println("Unable to read user input.");
                break;
            default:
                System.out.println("Unknown IO exception");
                break;
        }
    }

    /**
     * Possible file exceptions that can be thrown.
     * @param exceptionCode
     */
    public static void fileException(String exceptionCode){
        switch(exceptionCode){
            case "fileNotFound":
                System.out.println("Unable find chosen file.");
                break;
            case "notAbleToRead":
                System.out.println("Unable to read from given file.");
                break;
            default:
                System.out.println("Unknown File exception");
                break;
        }
    }
}