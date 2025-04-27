package pkgCWUtils;
import java.io.*;
import java.util.Arrays;
import java.util.Random;

public class CWPingPongArray {

    protected int[][] liveArray;
    protected int[][] nextArray;
    private Random random;
    protected final int ROWS;
    protected final int COLS;
    private int MIN;
    private int MAX;

    public CWPingPongArray(int numRows, int numCols) {
        this.ROWS = numRows;
        this.COLS = numCols;
        this.liveArray = new int[numRows][numCols];
        this.nextArray = new int[numRows][numCols];
    }

    public CWPingPongArray(int numRows, int numCols, int randMin, int randMax) {
        this.random = new Random();
        this.ROWS = numRows;
        this.COLS = numCols;
        this.MIN = randMin;
        this.MAX = randMax;
        this.liveArray = new int[numRows][numCols];
        this.nextArray = new int[numRows][numCols];

        //  both arrays with random values in range
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                liveArray[row][col] = random.nextInt(MIN, MAX);
                nextArray[row][col] = liveArray[row][col];
            }
        }

    }

    public void swapLiveAndNext() {
        int[][] temp = liveArray;
        liveArray = nextArray;
        nextArray = temp;
    }

    public void setCell(int row, int col, int newValue) {
        nextArray[row][col] = newValue;
    }

    public int[][] getArray() {
        return this.liveArray.clone();
    }

    public void printArray() {
        int[][] liveArrayClone = this.getArray();

        for (int row = 0; row < ROWS; row++) {
            System.out.print(row + " ");
            for (int col = 0; col < COLS; col++) {
                System.out.print(liveArrayClone[row][col] + " ");
            }
            System.out.println();
        }
    }

    // https://www.geeksforgeeks.org/how-to-clone-a-2d-array-with-different-row-sizes-in-java/
    private void copyToNextArray() {
        for (int i = 0; i < ROWS; i++) {
            System.arraycopy(liveArray[i], 0, nextArray[i], 0, COLS);
        }
    }

    // https://www.geeksforgeeks.org/shuffle-a-given-array-using-fisher-yates-shuffle-algorithm/
    private void randomize() {
        // copy live array to next array
        copyToNextArray();

        Random rand = new Random();
        for (int currRowIndex = ROWS - 1; currRowIndex > 0; currRowIndex--) {
            for (int currColIndex = COLS - 1; currColIndex > 0; currColIndex--) {

                // Pick random cell
                int randRowIndex = rand.nextInt(currRowIndex + 1);
                int randColIndex = rand.nextInt(currColIndex + 1);

                // Swap with current cell
                int temp = nextArray[currRowIndex][currColIndex];
                nextArray[currRowIndex][currColIndex] = nextArray[randRowIndex][randColIndex];
                nextArray[randRowIndex][randColIndex] = temp;
            }
        }
    }

    private void randomizeInRange() {
        copyToNextArray();

        // Create and shuffle a 1D array
        int[] randomArray = new int[ROWS * COLS];

        // Fill with randomArray
        for (int index = 0; index < ROWS * COLS; index++) {
            randomArray[index] = index % COLS; // 0,1,2,3,4,5,6,0,1,2.....
        }

        // Fisher-Yates-Knuth on 1D array
        Random rand = new Random();
        for (int currPositionIndex = ROWS * COLS - 1; currPositionIndex > 0; currPositionIndex--) {
            int randPositionIndex = rand.nextInt(currPositionIndex + 1);
            // Swap
            int temp = randomArray[currPositionIndex];
            randomArray[currPositionIndex] = randomArray[randPositionIndex];
            randomArray[randPositionIndex] = temp;
        }

        // 1D array back to 2D nextArray (from class)
        for (int index = 0; index < ROWS * COLS; index++) {
            int row = index / COLS;
            int col = index % COLS;
            nextArray[row][col] = randomArray[index];
        }
    }

    public void randomizeViaFisherYatesKnuth() {
        randomize();
    }

    // https://www.geeksforgeeks.org/read-file-into-an-array-in-java/
    // https://www.baeldung.com/java-file-to-arraylist
    public void loadFile(String dataFileName) {
        try (BufferedReader myReader = new BufferedReader(new FileReader(dataFileName))) {
            // Read default value (first line)
            String line = myReader.readLine();
            if (line == null) throw new RuntimeException("Empty file");
            int defaultVal = Integer.parseInt(line.trim());

            // Read dimensions (second line)
            line = myReader.readLine();
            if (line == null) throw new RuntimeException("Missing dimensions");
            String[] dims = line.trim().split("\\s+");
            if (dims.length != 2) throw new RuntimeException("Invalid dimensions format");
            int fileRows = Integer.parseInt(dims[0]);
            int fileCols = Integer.parseInt(dims[1]);

            // Initialize array with default values
            nextArray = new int[fileRows][fileCols];
            for (int[] row : nextArray) {
                Arrays.fill(row, defaultVal);
            }

            // Process remaining lines using your existing code
            int curRow;
            while ((line = myReader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                // Your existing processing code
                int[] readRow = Arrays.stream(line.split("\\s+"))
                        .mapToInt(Integer::parseInt)
                        .toArray();
                curRow = readRow[0];

                for (int curCol = 1; curCol < readRow.length; ++curCol) {
                    nextArray[curRow][curCol-1] = readRow[curCol];
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + dataFileName, e);
        }

    }


    public void updateToNearestNNSum() {
        int[] prevRow = new int[ROWS];
        int[] nextRow = new int[ROWS];
        int[] prevCol = new int[COLS];
        int[] nextCol = new int[COLS];

        // from class
        for (int row = 0; row < ROWS; row++) {
            prevRow[row] = (row - 1 + ROWS) % ROWS;
            nextRow[row] = (row + 1) % ROWS;
        }
        for (int col = 0; col < COLS; col++) {
            prevCol[col] = (col - 1 + COLS) % COLS;
            nextCol[col] = (col + 1) % COLS;
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int sum = liveArray[prevRow[row]][prevCol[col]]  // top-left
                        + liveArray[prevRow[row]][col]           // top
                        + liveArray[prevRow[row]][nextCol[col]]  // top-right
                        + liveArray[row][prevCol[col]]           // left
                        + liveArray[row][nextCol[col]]           // right
                        + liveArray[nextRow[row]][prevCol[col]]  // bottom-left
                        + liveArray[nextRow[row]][col]           // bottom
                        + liveArray[nextRow[row]][nextCol[col]]; // bottom-right

                nextArray[row][col] = sum;
            }

        }
    }

    // https://www.baeldung.com/java-write-to-file
    public void save(String dataFileName) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFileName))) {

            writer.println(99); // Default value
            writer.println(ROWS + " " + COLS);
            for (int row = 0; row < ROWS; row++) {
                writer.print(row);
                for (int col = 0; col < COLS; col++) {
                    writer.print(" " + liveArray[row][col]);
                }
                writer.println();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public int countLiveDegreeOneNeighbors(int row, int col){
        copyToNextArray();

        int count = 0;
        int degree = 1;
        for(int i = -degree; i <= degree; i++){
            for(int j = -degree; j <= degree; j++){
                if (i == 0 && j == 0 ) continue; // skip the center
                if (Math.abs(i) < degree && Math.abs(j) < degree) continue; // Skip all cells in the inner ring

                int newRow = (row + i + ROWS) % ROWS;
                int newCol = (col + j + COLS) % COLS;
//                System.out.print("[" + newRow + "," + newCol + "] ");
                if(liveArray[newRow][newCol] == 1){
                    count++;
                }
            }
        }

        return count;
    }

}
