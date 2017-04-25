import java.util.Scanner;

/**
 * Created by Connor on 4/24/2017.
 */
public class Main {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("How many linear equations would you like to solve?");
        //int numEquations = in.nextInt();

        String entryString = null;
        while(entryString == null) {
            System.out.println("Would you like to enter the coefficients by hand?(Y) \nor provide a filename? (N)");
            entryString = in.next();
            char entryChoice = entryString.toCharArray()[0];
            if(entryChoice == 'Y' || entryChoice == 'y') {
                manualInitialization();
            } else if(entryChoice == 'N' || entryChoice == 'n'){
                fileInitialization();
            } else {
                System.out.println("Sorry, that input was incorrect.");
                entryString = null;
            }
        }
    }

    private static void manualInitialization() {
        System.out.println("Manual init.");
    }

    private static void fileInitialization() {
        System.out.println("Init from file.");
    }
}
