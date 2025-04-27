package pkgCWDriver;

import pkgCWGoLArray.CWGoLArray;
import pkgCWUtils.CWPingPongArray;


public class CWDriver {

    public static void main(String[] args) {
//        final int numRows = 6, numCols = 7, polyLength = 50, polyOffset = 10, polyPadding = 20;
//        final int winWidth = (polyLength + polyPadding) * numCols + 2 * polyOffset;
//        final int winHeight = (polyLength + polyPadding) * numRows + 2 * polyOffset;
//        final int winOrgX = 50, winOrgY = 80;
//        final CWWindowManager myWM = CWWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);
//        final CWRenderer myRenderer = new CWRenderer(myWM);
//        myRenderer.render(polyOffset, polyPadding, polyLength, numRows, numCols);


        final int ROWS = 7, COLS = 7;
        int myMin = 10, myMax = 20;
        CWPingPongArray myBoard = new CWPingPongArray(ROWS, COLS, myMin, myMax);
        myBoard.swapLiveAndNext();
        System.out.println("[10, 20) Board:");
        myBoard.printArray();

        for (int row = 0; row < ROWS; ++row) {
            for (int col = 0; col < COLS; ++col) {
                myBoard.setCell(row, col, col);
            }  //  for(int col = 0; col < COLS; ++col)
        }  //  for(int row = 0; row < ROWS; ++row)
        myBoard.swapLiveAndNext();
        System.out.println("\n[0, COLS) Board:");
        myBoard.printArray();

        myBoard.randomizeViaFisherYatesKnuth();
        myBoard.swapLiveAndNext();
        System.out.println("\n[0, COLS) Board Randomized via FYK algorithm:");
        myBoard.printArray();

        myBoard.loadFile("neighbors_test.txt");
        System.out.println("\n[0, 1] data file array:");
        myBoard.swapLiveAndNext();
        myBoard.printArray();

        myBoard.updateToNearestNNSum();
        myBoard.swapLiveAndNext();
        System.out.println("\nNearest Neighbor sum array:");
        myBoard.printArray();
        myBoard.save("test_sum.txt");

        CWPingPongArray myBoardLive = new CWGoLArray(ROWS, COLS, 10);
        myBoardLive.swapLiveAndNext();
        System.out.println("\nmyBoardLive array:");
        myBoardLive.printArray();
        int countLive = myBoardLive.countLiveDegreeOneNeighbors(2,2);
        System.out.println("\nThere are " + countLive + " lives in cell 2,2:");

        CWPingPongArray CWGoLArray = new CWGoLArray("neighbors_test.txt");
        System.out.println("\n[0, 1] data CWGoLArray file array:");
        myBoard.swapLiveAndNext();
        myBoard.printArray();

    } // public static void main(String[] args)

}  //  public class Driver
