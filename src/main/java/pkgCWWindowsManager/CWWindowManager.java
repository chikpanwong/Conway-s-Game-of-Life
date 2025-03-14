package pkgCWWindowsManager;


import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.opengl.GL11C.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

public class CWWindowManager {

    private static GLFWFramebufferSizeCallback resizeWindow =
            new GLFWFramebufferSizeCallback(){
                @Override
                public void invoke(long window, int width, int height){
                    glViewport(0,0,width, height);
                }
            };

    private static CWWindowManager my_window;
    private static long glfwWindow;
    private static int win_height;
    private static int win_width;

    private CWWindowManager() {
        my_window = this;
        // Default size from asm 1
//        setWinWidth(1800,1200);
        initGlfwWindow();
    }

    private CWWindowManager(int width, int height) {
        this.win_width = width;
        this.win_height = height;
        my_window = this;
        initGlfwWindow();
    }

    public void updateContextToThis(){
        glfwMakeContextCurrent(glfwWindow);
    }

    public void destroyGlfwWindow(){
        glfwDestroyWindow(glfwWindow);
    }

    public boolean isGlfwWindowClosed(){
        return glfwWindowShouldClose(glfwWindow);
    }

    public static CWWindowManager get(){
        if (my_window == null) {
            // Default size from asm 1
            my_window = new CWWindowManager();
        }
        return my_window;
    }

    public static CWWindowManager get(int width, int height){
        if (my_window == null) {
            my_window = new CWWindowManager(width, height);
        }
        return my_window;
    }

    public static void setWindowPosition(int orgX, int orgY) {
        if (glfwWindow > 0) {
            glfwSetWindowPos(glfwWindow, orgX, orgY);
        }  //  if (glfwWindow > 0)
    }  //  public void setWindowPosition(...)

    public static CWWindowManager get(int width, int height, int orgX, int orgY) {
        my_window = get(width, height);
        setWindowPosition(orgX, orgY);
        return my_window;
    }  //  public SlWindowManager get(...)


    protected static void setWinWidth(int width, int height){
        win_height = height;
        win_width = width;
    }

    public void enableResizeWindowCallback(){
        glfwSetFramebufferSizeCallback(glfwWindow, resizeWindow);
    }  //  public void enableResizeWindowCallback(...)

    public void swapBuffers(){
        glfwSwapBuffers(glfwWindow);
    }

    // from asm 1
    private static void initGlfwWindow(){

        if (!glfwInit())
            throw new IllegalStateException("Unable to initialize GLFW");

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 8);

        glfwWindow = glfwCreateWindow(win_width, win_height, "CSC 133", NULL, NULL);

        if (glfwWindow == NULL)
            throw new RuntimeException("Failed to create the GLFW window");

        my_window.updateContextToThis();
        my_window.enableResizeWindowCallback();

        // Default position 30, 90 from asm 1 driver
        setWindowPosition(30,90);
        int VSYNC_INTERVAL = 1;
        glfwSwapInterval(VSYNC_INTERVAL);
        glfwShowWindow(glfwWindow);

    }

    public int[] getWindowSize(){
        return new int[]{win_width,win_height};
    }


}
