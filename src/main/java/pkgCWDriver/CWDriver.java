package pkgCWDriver;

import pkgCWRenderer.CWRenderer;
import pkgCWWindowsManager.CWWindowManager;


public class CWDriver {

    public static void main(String[] args) {
        final int numRows = 6, numCols = 7, polyLength = 50, polyOffset = 10, polyPadding = 20;
        final int winWidth = (polyLength + polyPadding) * numCols + 2 * polyOffset;
        final int winHeight = (polyLength + polyPadding) * numRows + 2 * polyOffset;
        final int winOrgX = 50, winOrgY = 80;
        final CWWindowManager myWM = CWWindowManager.get(winWidth, winHeight, winOrgX, winOrgY);
        final CWRenderer myRenderer = new CWRenderer(myWM);
        myRenderer.render(polyOffset, polyPadding, polyLength, numRows, numCols);
    } // public static void main(String[] args)

}  //  public class Driver
