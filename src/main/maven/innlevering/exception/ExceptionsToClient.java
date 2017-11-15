package innlevering.exception;

/**
 * Created by hakonschutt on 14/11/2017.
 */
public class ExceptionsToClient extends Exception {
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
