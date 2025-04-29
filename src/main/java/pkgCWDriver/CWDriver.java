package pkgCWDriver;

import pkgCWGeometryManager.CWGeometryManager;
import pkgCWGoLArray.CWGoLArray;
import pkgCWRenderer.CWRenderer;
import pkgCWUtils.CWPingPongArray;
import pkgCWWindowsManager.CWWindowManager;

import javax.swing.*;


public class CWDriver {

    public static void main(String[] args) {

        int numRows, numCols;

        CWGoLArray golArray;
        if (args.length > 0) {
            // If a file is provided
            golArray = new CWGoLArray(args[0]);
            numRows = golArray.getNumRows();
            numCols = golArray.getNumCols();
        } else {
            numRows = 100; numCols = 100;
            final int numLiveCell = (int) (numRows * numCols * 0.2 + 0.5);
            golArray = new CWGoLArray(numRows, numCols, numLiveCell);
        }

        final int polyLength = 15, polyOffset = 5, polyPadding = 8;
        final int winWidth = (polyLength + polyPadding) * numCols + 2 * polyOffset;
        final int winHeight = (polyLength + polyPadding) * numRows + 2 * polyOffset;
        final int winOrgX = 50, winOrgY = 80;

        final CWWindowManager myWM = CWWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);
        final CWRenderer myRenderer = new CWRenderer(myWM,golArray);

        myRenderer.render(polyOffset, polyPadding, polyLength, numRows, numCols);


    } // public static void main(String[] args)


}  //  public class Driver
