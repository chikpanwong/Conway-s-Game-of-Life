package pkgCWGoLArray;

import pkgCWUtils.CWPingPongArray;

import java.util.Random;

public class CWGoLArray extends CWPingPongArray {
    private static final int DEAD = 0;
    private static final int LIVE = 1;

    public CWGoLArray(final String myDataFile) {
        super(1,1);
        loadFile(myDataFile);
    }

    public CWGoLArray(int numRows, int numCols){
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
                setNextArray(i,j,DEAD);
                setLiveArray(i,j,DEAD);
            }
        }

        Random rand = new Random();

        int placed = 0;
        while (placed < numLiveCells) {
            // put value 1(LIVE) in random cell until numLiveCells
            int row = rand.nextInt(numRows);
            int col = rand.nextInt(numCols);
            if (getLiveArray(row,col) == DEAD) {
                setNextArray(row,col,LIVE);
                placed++;
                super.swapLiveAndNext();
            }
        }
    }
}
