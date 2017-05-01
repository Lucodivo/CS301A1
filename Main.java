import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Connor on 4/24/2017.
 */
public class Main {
    private static int numEquations;
    private static float [][] matrix;
    private static float [] pivots;
    private static float [] solution;
    private static Stack<Integer> eliminationStack;

    private static Scanner in;

    public static void main(String[] args) {
        in = new Scanner(System.in);

        System.out.println("How many linear equations would you like to solve?");
        numEquations = 3;//in.nextInt();
        //in.nextLine();

        matrix = new float[numEquations][numEquations + 1];
        pivots = new float[numEquations];
        solution = new float[numEquations];
        eliminationStack = new Stack<Integer>();

        String entryString = null;
        while(entryString == null) {
            System.out.println("Would you like to enter the coefficients by hand?(Y) \nor provide a filename? (N)");
            entryString = "hello";//in.nextLine();
            char entryChoice = 'n';//entryString.toCharArray()[0];
            if(entryChoice == 'Y' || entryChoice == 'y') {
                manualInitialization();
            } else if(entryChoice == 'N' || entryChoice == 'n'){
                fileInitialization();
            } else {
                System.out.println("Sorry, that input was incorrect.");
                entryString = null;
            }
        }
        calcPivots();
        GaussianESPP();
        printMatrix();
        forwardElimination();
        printSolutions();
    }

    private static void forwardElimination() {
        for(int i = 0; i < numEquations; ++i) {
            int currRow = eliminationStack.pop();
            float rhs = matrix[currRow][numEquations];
            for(int j = 0; j < numEquations; ++j) {
                rhs -= solution[j] * matrix[currRow][j];
            }
            solution[numEquations - 1 - i] = rhs / matrix[currRow][numEquations - 1 - i];
        }
    }

    private static void GaussianESPP() {
        int numIterations = numEquations - 1;

        for(int i = 0; i < numIterations; ++i) {
            float rmax = 0;
            int pivotIndex = -1;
            for(int j = 0; j < numEquations; ++j) {
                if(pivots[j] != 0) {
                    float r = Math.abs(matrix[j][i]/pivots[j]);
                    if(r > rmax) {
                        rmax = r;
                        pivotIndex = j;
                    }
                }
            }
            pivots[pivotIndex] = 0;
            eliminationStack.push(pivotIndex);
            for(int k = 0; k < numEquations; ++k) {
                if(pivots[k] != 0) {
                    float xmult = matrix[k][i] / matrix[pivotIndex][i];
                    for(int l = i; l < numEquations + 1; ++l) {
                        matrix[k][l] -= xmult * matrix[pivotIndex][l];
                    }
                }
            }
        }

        for(int i = 0; i < numEquations; ++i) {
            if(pivots[i] != 0) {
                eliminationStack.push(i);
                break;
            }
        }
    }

    private static void calcPivots() {

        for(int i = 0; i < numEquations; ++i) {
            pivots[i] = Math.abs(matrix[i][0]);
            for(int j = 1; j < numEquations; ++j) {
                if(Math.abs(matrix[i][j]) > pivots[i]) {
                    pivots[i] = Math.abs(matrix[i][j]);
                }
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

        String filename = "input.txt";//in.nextLine();
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

    private static void printPivots() {

        for(int i = 0; i < numEquations; ++i) {
            System.out.print(pivots[i]);
        }

    }

    private static void printSolutions() {
        for(int i = 0; i < numEquations; i++) {
            System.out.println("X" + i + "=" + solution[i]);
        }
    }
}
