package pkgCWRenderer;

import pkgCWGoLArray.CWGoLArray;
import pkgCWUtils.CWPingPongArray;

class CWGeometryManager {
    private int NUM_COLS;
    private int NUM_ROWS;
    private int PADDING;
    private int SIZE;
    private int[] winWidthHeight;
    private int TOTAL_PRIMS;
    private int OFFSET;

    private CWPingPongArray myPPArray;
    private CWGoLArray myGolArray;
    private CWGeometryManager GM;

    private final int FPV = 2;
    private final int VPT = 4;
    private final int EPT = 6;

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

    protected boolean fillArrayWithTileVertices(float[] vertices, int startIndex, float xmin, float ymin) {
        vertices[startIndex] = xmin;
        vertices[startIndex + 1] = ymin;
        vertices[startIndex + 2] = xmin + SIZE;
        vertices[startIndex + 3] = ymin;
        vertices[startIndex + 4] = xmin;
        vertices[startIndex + 5] = ymin + SIZE;
        vertices[startIndex + 6] = xmin + SIZE;
        vertices[startIndex + 7] = ymin + SIZE;
        return true;
    }

    protected float[] generateTilesVertices(final int rowTiles, final int columnTiles) {

        int totalVertices = rowTiles * columnTiles * VPT * FPV;
        float[] vertices = new float[totalVertices];

        int index = 0;

        for (int row = 0; row < rowTiles; row++) {
            for (int col = 0; col < columnTiles; col++) {

//                int index = (row * columnTiles + col) * VPT * FPV;

                // first top-left corner of the tile
                float xmin = OFFSET + col * (SIZE + PADDING);
                float ymin = winWidthHeight[1] - (OFFSET + SIZE + row * (SIZE + PADDING));

                if (!fillArrayWithTileVertices(vertices, index, xmin, ymin)) {
                    return null; // If failed, return null
                }
                index += VPT * FPV;
            }
        }

        return vertices;

    }

    protected int[] generateTileIndices(final int totalTiles) {

        int[] indices = new int[totalTiles * EPT];

//        for (int row = 0; row < rows; row++) {
//            for (int col = 0; col < cols; col++) {
//                int tileNum = row * cols + col;
        for (int i = 0; i < totalTiles; i++) {
            int startIndex = i * EPT;
            // 4 vertices per cell
            int startVertex = i * VPT;

            // First triangle (bottom-left, bottom-right, top-left)
            indices[startIndex] = startVertex;
            indices[startIndex + 1] = startVertex + 1;
            indices[startIndex + 2] = startVertex + 2;

            // Second triangle (top-left, bottom-right, top-right)
            indices[startIndex + 3] = startVertex + 2;
            indices[startIndex + 4] = startVertex + 1;
            indices[startIndex + 5] = startVertex + 3;

        }

        return indices;
    }

    protected boolean generateTilesVertices(final CWGoLArray myGoLA, float[] vertices) {
        if (myGoLA == null || vertices == null) {
            return false;
        }

        int index = 0;
        for (int r = 0; r < NUM_ROWS; r++) {
            for (int c = 0; c < NUM_COLS; c++) {
                if (myGoLA.isCellAlive(r,c)) { // alive
                    float xmin = OFFSET + c * (SIZE + PADDING);
                    float ymin = winWidthHeight[1] - (OFFSET + SIZE + r * (SIZE + PADDING));

                    if (!fillArrayWithTileVertices(vertices, index, xmin, ymin)) {
                        return false;
                    }

                    index += VPT * FPV; // Only increment when alive
                }
                // If dead: skip! (do not touch the index or vertices)
            }
        }

        return true;
    }

}
