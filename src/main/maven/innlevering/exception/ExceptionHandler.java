package innlevering.exception;

/**
 * Created by hakonschutt on 13/11/2017.
 */
public class ExceptionHandler {

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
