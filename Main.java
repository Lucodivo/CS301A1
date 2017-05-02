import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

/**
 * Created by Connor on 4/24/2017.
 */
public class Main {
    private static int numEquations; // number of equations (and variables)
    private static float [][] matrix; // matrix to hold equations
    private static float [] pivots; // the max abs value of any constant in each row
    private static float [] solution; // the solutions to each variable
    private static Stack<Integer> eliminationStack; // stack to keep track of Gaus Elim order

    private static Scanner in; // to read user input

    public static void main(String[] args) {
        in = new Scanner(System.in);

        // get how many equations from user
        System.out.println("How many linear equations would you like to solve?");
        numEquations = in.nextInt();
        in.nextLine();

        // init global vars
        matrix = new float[numEquations][numEquations + 1];
        pivots = new float[numEquations];
        solution = new float[numEquations];
        eliminationStack = new Stack<Integer>();

        String entryString = null; // user input
        // get user input until it is correct
        while(entryString == null) {
            // ask if user wants to manually input equations or read from file
            System.out.println("Would you like to enter the coefficients by hand?(Y) \nor provide a filename? (N)");
            entryString = in.nextLine();
            char entryChoice = entryString.toCharArray()[0];
            if(entryChoice == 'Y' || entryChoice == 'y') {
                // user will manually input equations
                manualInitialization();
            } else if(entryChoice == 'N' || entryChoice == 'n'){
                // read equations from file
                fileInitialization();
            } else {
                // inform the user their input was incorrect and make another attempt
                System.out.println("Sorry, that input was incorrect.");
                entryString = null;
            }
        }

        printMatrix();
        calcPivots();
        GaussianESPP();
        printMatrix();
        forwardElimination();
        printSolutions();
    }

    /**
     * performs forward elimination on a matrix that has already undergone Gaussian elimination
     * Stores solution in global solution array
     */
    private static void forwardElimination() {
        // for every equation
        for(int i = 0; i < numEquations; ++i) {
            // determine equation from original order of elimination
            int currRow = eliminationStack.pop();
            // right hand side originally equals the original rhs of
            float rhs = matrix[currRow][numEquations];
            for(int j = 0; j < numEquations; ++j) {
                // multiply the solution value by the coefficient and subtract it from the rhs
                rhs -= solution[j] * matrix[currRow][j];
            }
            // solution for the current variable is rhs divided by the coefficient of variable
            solution[numEquations - 1 - i] = rhs / matrix[currRow][numEquations - 1 - i];
        }
    }

    /**
     * Performs gaussian elimination with scaled partial pivot on matrix
     */
    private static void GaussianESPP() {
        // For the number of columns that need to be eliminated
        int numIterations = numEquations - 1;
        for(int i = 0; i < numIterations; ++i) {
            // keep track of the current max partial pivot
            float rmax = 0;
            int pivotIndex = -1;
            for(int j = 0; j < numEquations; ++j) {
                if(pivots[j] != 0) {
                    // calculate partial pivot
                    float r = Math.abs(matrix[j][i]/pivots[j]);
                    // if it is greater than previous greatest, replace with new greatest partial pivot
                    if(r > rmax) {
                        rmax = r;
                        pivotIndex = j;
                    }
                }
            }
            pivots[pivotIndex] = 0;
            // Push current equation index onto the stack for forward elimination
            eliminationStack.push(pivotIndex);
            for(int k = 0; k < numEquations; ++k) {
                if(pivots[k] != 0) {
                    // get the xmult by determining what mult of the equation will eliminate the ith variable
                    float xmult = matrix[k][i] / matrix[pivotIndex][i];
                    // User the multiplier to modify the entire equation
                    for(int l = i; l < numEquations + 1; ++l) {
                        matrix[k][l] -= xmult * matrix[pivotIndex][l];
                    }
                }
            }
        }

        // push the index of the equation that was never used as a pivot on the stack
        // it will be the first to be used in forward elimination
        for(int i = 0; i < numEquations; ++i) {
            if(pivots[i] != 0) {
                eliminationStack.push(i);
                break;
            }
        }
    }

    /**
     * Acquires pivots by scanning through each equation and finding the max coefficient
     */
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

    /**
     * Reads and stores equation information from user inputs
     */
    private static void manualInitialization() {

        for(int i = 0; i < numEquations; ++i) {
            for(int j = 0; j <= numEquations; ++j){
                matrix[i][j] = in.nextInt();
            }
        }
    }

    /**
     * Reads and stores equation information from a space separated file
     */
    private static void fileInitialization() {

        // get filename from user
        System.out.println("Please enter filename. (include file extension)");
        String filename = in.nextLine();

        try {
            // open file
            File file = new File(filename);
            Scanner fs = new Scanner(file);

            // fill matrix with values from file
            for(int i = 0; i < numEquations; ++i) {
                for(int j = 0; j <= numEquations; ++j){
                    matrix[i][j] = fs.nextInt();
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("File cannot be opened.");
        }
    }

    /**
     * Prints Matrix
     */
    private static void printMatrix() {

        for(int i = 0; i < numEquations; ++i) {
            for(int j = 0; j <= numEquations; ++j) {
                System.out.print(matrix[i][j] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Prints pivots
     */
    private static void printPivots() {

        for(int i = 0; i < numEquations; ++i) {
            System.out.print(pivots[i]);
        }

    }

    /**
     * Prints solutions
     */
    private static void printSolutions() {
        for(int i = 0; i < numEquations; i++) {
            System.out.println("X" + i + "=" + solution[i]);
        }
    }
}
