package pkgCWRenderer;

import pkgCWGoLArray.CWGoLArray;

class CWGeometryManager {
    private int NUM_COLS;
    private int NUM_ROWS;
    private int PADDING;
    private int SIZE;
    private int[] winWidthHeight;
    private int TOTAL_PRIMS;
    private int OFFSET;

    private final int FPV = 2;
    private final int VPT = 4;
    private final int EPT = 6;

    private final CWGoLArray GOL;

    protected CWGeometryManager(int maxRows, int maxCols, int offset, int size, int
            padding, int[] winWidthHeight, CWGoLArray gol) {
        this.NUM_ROWS = maxRows;
        this.NUM_COLS = maxCols;
        this.OFFSET = offset;
        this.SIZE = size;
        this.PADDING = padding;
        this.winWidthHeight = winWidthHeight;
        GOL = gol;
        this.TOTAL_PRIMS = maxRows * maxCols * 2; // 2 triangles per cell
    }

    protected boolean fillArrayWithTileVertices(float[] vertices, int startIndex, float xmin, float ymin) {
//       3 ---- 2
//       |    / |
//       |  /   |
//       |/     |
//       0 ---- 1

        vertices[startIndex] = xmin; vertices[startIndex + 1] = ymin; // vertices 0

        vertices[startIndex + 2] = xmin + SIZE; vertices[startIndex + 3] = ymin; // vertices 1

        vertices[startIndex + 4] = xmin + SIZE; vertices[startIndex + 5] = ymin + SIZE; // vertices 2

        vertices[startIndex + 6] = xmin; vertices[startIndex + 7] = ymin + SIZE; // vertices 3

        return true;
    }

    protected float[] generateTilesVertices(final int rowTiles, final int columnTiles) {

        int totalVertices = rowTiles * columnTiles * VPT * FPV;
        float[] vertices = new float[totalVertices];

        // only fills it for alive cells
        if (!generateTilesVertices(GOL,vertices)) {
            System.out.println("generateTilesVertices error");
        }

        return vertices;
    }

    protected int[] generateTileIndices(final int totalTiles) {

        int[] indices = new int[totalTiles * EPT];

        for (int i = 0; i < totalTiles; i++) {

            // 6 indices per cell
            // Tile 0 starts at 0 = 0 * EPT
            // Tile 1 starts at 6 = 1 * EPT
            // Tile 2 starts at 12 = 2 * EPT
            int startIndex = i * EPT;  // Elements Per Tile

            // 4 vertices per cell
            // Tile 0 starts at vertex 0 = 0 * VPT
            // Tile 1 starts at vertex 4 = 1 * VPT
            // Tile 2 starts at vertex 8 = 2 * VPT
            int startVertex = i * VPT; // Vertices Per Tile

            // First triangle (bottom-left, bottom-right, top-left)
            indices[startIndex] = startVertex;
            indices[startIndex + 1] = startVertex + 1;
            indices[startIndex + 2] = startVertex + 2;

            // Second triangle (bottom-left, top-right, bottom-right)
            indices[startIndex + 3] = startVertex;
            indices[startIndex + 4] = startVertex + 2;
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

                    // fill the vertices
                    if (!fillArrayWithTileVertices(vertices, index, xmin, ymin)) {
                        System.out.println("fillArrayWithTileVertices error");
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
