package Innlevering.Server;

/**
 * Created by hakonschutt on 23/10/2017.
 */
public class testDiff {
    public static void main(String[] args){
        String formatString = "%-10S %-15S %-15s";
        String[] string = {"Hello", "heieh", "longer text"};
        String returnString = String.format(formatString, (Object[])string);
        System.out.println(returnString);
    }
}
