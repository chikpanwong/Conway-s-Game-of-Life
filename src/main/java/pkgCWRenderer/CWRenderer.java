package pkgCWRenderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import pkgCWWindowsManager.CWWindowManager;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwWaitEvents;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetUniformLocation;

public class CWRenderer {

    private int shader_program;
    private int NUM_COLS;
    private Matrix4f viewProjMatrix = new Matrix4f();
    private final int VPT = 4;
    private CWWindowManager WM;
    private int[] winWidthHeight;
    private final static int OGL_MATRIX_SIZE = 16;
    private FloatBuffer myFloatBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
    private int NUM_ROWS;
    private int PADDING;
    private final int EPT = 6;
    private int SIZE;
    private int OFFSET;
    private final int FPV = 2;
    private int vpMatLocation = 0;
    private int renderColorLocation = 0;

    public CWRenderer(CWWindowManager windowManager) {
        this.WM = windowManager;

    }

    public void render(final int offset, final int padding,
                       final int size, final int numRows, final int numCols) {
        this.NUM_ROWS = numRows;
        this.NUM_COLS = numCols;
        this.PADDING = padding;
        this.OFFSET = offset;
        this.SIZE = size;

        this.WM.updateContextToThis();
        renderLoop();
        this.WM.destroyGlfwWindow();

    }

    private void renderLoop() {
        glfwPollEvents();
        initOpenGL();
        renderObjects();
        /* Process window messages in the main thread */
        while (!this.WM.isGlfwWindowClosed()) {
            glfwWaitEvents();
        }
    }

    private void initOpenGL() {
        winWidthHeight = this.WM.getWindowSize();
        GL.createCapabilities();
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_CULL_FACE);
        glViewport(0, 0, winWidthHeight[0], winWidthHeight[1]);
        glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        this.shader_program = glCreateProgram();
        int vs = glCreateShader(GL_VERTEX_SHADER);
        glShaderSource(vs,
                "uniform mat4 viewProjMatrix;" +
                        "void main(void) {" +
                        " gl_Position = viewProjMatrix * gl_Vertex;" +
                        "}");
        glCompileShader(vs);
        glAttachShader(shader_program, vs);
        int fs = glCreateShader(GL_FRAGMENT_SHADER);
        glShaderSource(fs,
                "uniform vec3 color;" +
                        "void main(void) {" +
                        " gl_FragColor = vec4(0.6f, 0.7f, 0.1f, 1.0f);" +
                        "}");
        glCompileShader(fs);
        glAttachShader(shader_program, fs);
        glLinkProgram(shader_program);
        glUseProgram(shader_program);
        vpMatLocation = glGetUniformLocation(shader_program, "viewProjMatrix");

    }

    private void renderObjects() {
        winWidthHeight = this.WM.getWindowSize();
//        float xmin = OFFSET + NUM_COLS * (SIZE + PADDING);
//        float ymin = winWidthHeight[1] - (OFFSET + SIZE + MUM_ROWS * (SIZE + PADDING));

        float xmin = OFFSET;
        float ymin = winWidthHeight[1] - (OFFSET + SIZE);

        while (!this.WM.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            int vbo = glGenBuffers();
            int ibo = glGenBuffers();
//            float[] vertices = {-20f, -20f, 20f, -20f, 20f, 20f, -20f, 20f};
//            float[] vertices = {-(OFFSET + NUM_COLS * (SIZE + PADDING)), -20f, 20f, -20f, 20f, 20f, -20f, 20f};
//            float[] vertices = {xmin, ymin, (xmin + SIZE), ymin, (xmin + SIZE), (ymin + SIZE), xmin, (ymin + SIZE)};
//            int[] indices = {0, 1, 2, 0, 2, 3};
            float[] vertices = generateTilesVertices(NUM_ROWS,NUM_COLS);
            int[] indices = generateTileIndices(NUM_ROWS,NUM_COLS);
            glBindBuffer(GL_ARRAY_BUFFER, vbo);
            glBufferData(GL_ARRAY_BUFFER, (FloatBuffer) BufferUtils.
                    createFloatBuffer(vertices.length).
                    put(vertices).flip(), GL_STATIC_DRAW);
            glEnableClientState(GL_VERTEX_ARRAY);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, (IntBuffer) BufferUtils.
                    createIntBuffer(indices.length).
                    put(indices).flip(), GL_STATIC_DRAW);
            glVertexPointer(2, GL_FLOAT, 0, 0L);
            // set here for the suqare in bottom left
            viewProjMatrix.setOrtho(0, winWidthHeight[0], 0, winWidthHeight[1], 0, 10);
            glUniformMatrix4fv(vpMatLocation, false,
                    viewProjMatrix.get(myFloatBuffer));
            glUniform3f(renderColorLocation, 1.0f, 0.498f, 0.153f);
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
//            final int VTD = 6; // need to process 6 Vertices To Draw 2 triangles
            final int totalIndices = NUM_ROWS * NUM_COLS * EPT; // Draw all tiles
            glDrawElements(GL_TRIANGLES, totalIndices, GL_UNSIGNED_INT, 0L);
            this.WM.swapBuffers();
        }

    }

    private float[] generateTilesVertices(final int rowTiles,
                                          final int columnTiles) {

        int totalVertices = rowTiles * columnTiles * VPT * FPV;
        float[] vertices = new float[totalVertices];

        for (int row = 0; row < rowTiles; row++) {
            for (int col = 0; col < columnTiles; col++) {

                int index = (row * columnTiles + col) * VPT * FPV;

                // first top-left corner of the tile
                float xmin = OFFSET + col * (SIZE + PADDING);
                float ymin = winWidthHeight[1] - (OFFSET + SIZE + row * (SIZE + PADDING));

                vertices[index] = xmin;              // Vertex 1
                vertices[index + 1] = ymin;
                vertices[index + 2] = xmin + SIZE;   // Vertex 2
                vertices[index + 3] = ymin;
                vertices[index + 4] = xmin + SIZE;   // Vertex 3
                vertices[index + 5] = ymin + SIZE;
                vertices[index + 6] = xmin;         // Vertex 4
                vertices[index + 7] = ymin + SIZE;
            }
        }

        return vertices;

    }

    private int[] generateTileIndices(final int rows, final int cols) {

        int totalIndices = rows * cols * EPT;
        int[] indices = new int[totalIndices];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int tileNum = row * cols + col;
                int startIndex = tileNum * EPT;
                int startVertex = tileNum * VPT;

                indices[startIndex] = startVertex;         // Triangle 1: Vertex 1
                indices[startIndex + 1] = startVertex + 1; // Triangle 1: Vertex 2
                indices[startIndex + 2] = startVertex + 2; // Triangle 1: Vertex 3
                indices[startIndex + 3] = startVertex;     // Triangle 2: Vertex 1
                indices[startIndex + 4] = startVertex + 2; // Triangle 2: Vertex 3
                indices[startIndex + 5] = startVertex + 3; // Triangle 2: Vertex 4
            }
        }

        return indices;
    }


}
