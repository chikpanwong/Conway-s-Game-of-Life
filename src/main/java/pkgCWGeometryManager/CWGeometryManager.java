package pkgCWGeometryManager;

import pkgCWUtils.CWPingPongArray;

public class CWGeometryManager {
    private int NUM_COLS;
    private int NUM_ROWS;
    private int PADDING;
    private int SIZE;
    private int[] winWidthHeight;
    private int TOTAL_PRIMS;
    private int OFFSET;
    private CWPingPongArray myPPArray;

    protected CWGeometryManager(int maxRows, int maxCols, int offset, int size, int
            padding, int[] winWidthHeight) {
        this.NUM_ROWS = maxRows;
        this.NUM_COLS = maxCols;
        this.OFFSET = offset;
        this.SIZE = size;
        this.PADDING = padding;
        this.winWidthHeight = winWidthHeight;
        this.TOTAL_PRIMS = maxRows * maxCols * 2; // 2 triangles per cell

    }
}
