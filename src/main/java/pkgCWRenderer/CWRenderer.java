package pkgCWRenderer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL;
import pkgCWGeometryManager.CWGeometryManager;
import pkgCWGoLArray.CWGoLArray;
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
    private int[] winWidthHeight;
    private final static int OGL_MATRIX_SIZE = 16;
    private FloatBuffer myFloatBuffer = BufferUtils.createFloatBuffer(OGL_MATRIX_SIZE);
    private int NUM_ROWS;
    private int PADDING;
    private int SIZE;
    private int OFFSET;
    private final int FPV = 2;
    private final int VPT = 4;
    private final int EPT = 6;

    private int vpMatLocation = 0;
    private int renderColorLocation = 0;

    private final CWWindowManager WM;
    private final CWGoLArray GOL;
    private CWGeometryManager GM;


    public CWRenderer(CWWindowManager windowManager, CWGoLArray golArray) {
        this.WM = windowManager;
        this.GOL = golArray;
    }

    public void render(final int offset, final int padding,
                       final int size, final int numRows, final int numCols) {
        this.NUM_ROWS = numRows;
        this.NUM_COLS = numCols;
        this.PADDING = padding;
        this.OFFSET = offset;
        this.SIZE = size;
        winWidthHeight = this.WM.getWindowSize();
        this.GM = new CWGeometryManager(NUM_ROWS, NUM_COLS, OFFSET, SIZE, PADDING, winWidthHeight);
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
        while (!this.WM.isGlfwWindowClosed()) {
            glfwPollEvents();
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            int vbo = glGenBuffers();
            int ibo = glGenBuffers();

            float[] vertices = GM.generateTilesVertices(NUM_ROWS,NUM_COLS);
            GM.generateTilesVertices(GOL,vertices);

            // Count actual alive cells to determine how many indices we need
            int aliveCount = 0;
            for (int r = 0; r < NUM_ROWS; r++) {
                for (int c = 0; c < NUM_COLS; c++) {
                    if (GOL.isCellAlive(r, c)) {
                        aliveCount++;
                    }
                }
            }

            int[] indices = GM.generateTileIndices(aliveCount);

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
            final int totalIndices = aliveCount * EPT;
            glDrawElements(GL_TRIANGLES, totalIndices, GL_UNSIGNED_INT, 0L);
            this.WM.swapBuffers();

        }


    }


}
