package pkgCWGoLArray;

import pkgCWUtils.CWPingPongArray;

import java.util.Random;

public class CWGoLArray extends CWPingPongArray {
    private static final int DEAD = 0;
    private static final int ALIVE = 1;

    public CWGoLArray(final String myDataFile) {
        super(1,1);
        loadFile(myDataFile);
    }

    public CWGoLArray(final int numRows, final int numCols){
        super(numRows,numCols);
    }

    public CWGoLArray(int numRows, int numCols, int numLiveCells) {
        super(numRows, numCols);
        if (numLiveCells > numCols * numCols){
            throw new ArithmeticException("too many live cells, cannot contain all live cells");
        }

        // init both live and next arrays to DEAD
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                nextArray[i][j] = DEAD;
                liveArray[i][j] = DEAD;
            }
        }

        Random rand = new Random();

        int placed = 0;
        while (placed < numLiveCells) {
            // put value 1(LIVE) in random cell until numLiveCells
            int row = rand.nextInt(numRows);
            int col = rand.nextInt(numCols);
            if (liveArray[row][col] == DEAD) {
                setCell(row,col,ALIVE);
                placed++;
                super.swapLiveAndNext();
            }
        }
    }

    public void onTickUpdate() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                int liveCellCount = countLiveDegreeOneNeighbors(i, j);
                nextArray[i][j] = applyRules(liveArray[i][j], liveCellCount);
            }
        }
        swapLiveAndNext();
    }

    private int applyRules(int currentState, int liveCellCount) {
//        1. Live Neighbors < 2 --> Kill
//        2. Live Neighbors == 2 || Live Neighbors == 3 --> Retain
//        3. Live Neighbors > 3 --> Kill
//        4. Dead with Live Neighbors == 3 --> Alive again
        if (currentState == ALIVE) {
            // Rule 1,3
            if (liveCellCount < 2 || liveCellCount > 3) {
                return DEAD;
            }
            // Rule 2: Survival
            else {
                return ALIVE;
            }
        } else {
            // Rule 4
            if (liveCellCount == 3) {
                return ALIVE;
            } else {
                return DEAD;
            }
        }
    }

}
