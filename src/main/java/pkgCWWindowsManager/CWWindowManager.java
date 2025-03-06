package pkgCWWindowsManager;


import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.opengl.GL11C.glViewport;

public class CWWindowManager {

    private static GLFWFramebufferSizeCallback resizeWindow =
            new GLFWFramebufferSizeCallback(){
                @Override
                public void invoke(long window, int width, int height){
                    glViewport(0,0,width, height);
                }
            };

    private static CWWindowManager my_window = new CWWindowManager();
    private static long glfwWindow;
    private static int win_height;
    private static int win_width;

    private CWWindowManager(){

    }

    public void updateContextToThis(){

    }

    public void  destroyGlfwWindow(){

    }

    public boolean isGlfwWindowClosed(){

    }

    public static CWWindowManager get(int OrgX, int OrgY){

    }

    public static CWWindowManager get(){

    }

    protected static void setWinWidth(int height, int width){
        win_height = height;
        win_width = width;
    }

    public void enableResizeWindowCallback(){
        glfwSetFramebufferSizeCallback(glfwWindow, resizeWindow);
    }  //  public void enableResizeWindowCallback(...)

    public void swapBuffers(){

    }

    public void initGlfwWindow(){

    }

    public int[] getWindowSize(){
        return
    }


}
