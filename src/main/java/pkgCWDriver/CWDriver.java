package pkgCWDriver;

import pkgCWGeometryManager.CWGeometryManager;
import pkgCWGoLArray.CWGoLArray;
import pkgCWRenderer.CWRenderer;
import pkgCWUtils.CWPingPongArray;
import pkgCWWindowsManager.CWWindowManager;

import javax.swing.*;


public class CWDriver {

    public static void main(String[] args) {


        final int numRows = 16, numCols = 16, polyLength = 50, polyOffset = 10, polyPadding = 20;
        final int winWidth = (polyLength + polyPadding) * numCols + 2 * polyOffset;
        final int winHeight = (polyLength + polyPadding) * numRows + 2 * polyOffset;
        final int winOrgX = 50, winOrgY = 80;

        CWGoLArray golArray;

        golArray = new CWGoLArray("gol_input_1.txt");

        final CWWindowManager myWM = CWWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);
        final CWRenderer myRenderer = new CWRenderer(myWM,golArray);

        myRenderer.render(polyOffset, polyPadding, polyLength, numRows, numCols);


    } // public static void main(String[] args)


}  //  public class Driver
