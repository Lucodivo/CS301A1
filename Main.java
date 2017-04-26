import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by Connor on 4/24/2017.
 */
public class Main {
    private static int numEquations;
    private static int [][] matrix;

    private static Scanner in;

    public static void main(String[] args) {
        in = new Scanner(System.in);

        System.out.println("How many linear equations would you like to solve?");
        numEquations = in.nextInt();
        in.nextLine();

        matrix = new int[numEquations][numEquations + 1];

        String entryString = null;
        while(entryString == null) {
            System.out.println("Would you like to enter the coefficients by hand?(Y) \nor provide a filename? (N)");
            entryString = in.nextLine();
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

        for(int i = 0; i < numEquations; ++i) {
            for(int j = 0; j <= numEquations; ++j){
                matrix[i][j] = in.nextInt();
            }
        }

        printMatrix();
    }

    private static void fileInitialization() {

        System.out.println("Please enter filename.");

        String filename = in.nextLine();
        try {
            File file = new File(filename);
            Scanner fs = new Scanner(file);

            for(int i = 0; i < numEquations; ++i) {
                for(int j = 0; j <= numEquations; ++j){
                    matrix[i][j] = fs.nextInt();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        printMatrix();
    }

    private static void printMatrix() {

        for(int i = 0; i < numEquations; ++i) {
            for(int j = 0; j <= numEquations; ++j) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }
}
