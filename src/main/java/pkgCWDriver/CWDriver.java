package pkgCWDriver;

import pkgCWGeometryManager.CWGeometryManager;
import pkgCWGoLArray.CWGoLArray;
import pkgCWRenderer.CWRenderer;
import pkgCWUtils.CWPingPongArray;
import pkgCWWindowsManager.CWWindowManager;

import javax.swing.*;


public class CWDriver {

    public static void main(String[] args) {


//        // Default size for GoL grid
//        final int DEFAULT_ROWS = 14;
//        final int DEFAULT_COLS = 14;
//        int numLiveCells;
//        numLiveCells = (int) (DEFAULT_ROWS * DEFAULT_COLS * 0.2f * 0.5);
//
//        // Declare the SlGoLArray
//        CWGoLArray golArray = null;
//
//        // Check if a filename was provided
//        if (args.length == 0) {
//            // No filename provided, create a default 100x100 grid with random values
//            System.out.println("No input file provided. Using default grid size (100x100) with random live cells.");
//            golArray = new CWGoLArray(DEFAULT_ROWS, DEFAULT_COLS, numLiveCells);
//        } else {
//            golArray = new CWGoLArray("gol_input_1.txt");
//        }

        CWGoLArray golArray = null;

        golArray = new CWGoLArray("gol_input_1.txt");

        // Now run the Game of Life simulation
        runSimulation(golArray);



    } // public static void main(String[] args)

    public static void runSimulation(CWGoLArray golArray) {
        int generationCount = 0;
        while (true) {
            // Render the current state of the grid
            System.out.println("Generation " + generationCount);
            System.out.println("liveArray:");
            golArray.render();
//            System.out.println("======================");
//            System.out.println("nextArray:");
//            golArray.renderTest();

            // Update to the next generation based on the GoL rules
            golArray.onTickUpdate();

//            System.out.println("after update:");
//            System.out.println("liveArray:");
//            golArray.render();
//            System.out.println("======================");
//            System.out.println("nextArray:");
//            golArray.renderTest();


            // Sleep for a short duration to simulate the game at a reasonable pace
            try {
                Thread.sleep(500); // 500 ms between generations (adjust as needed)
            } catch (InterruptedException e) {
                System.out.println("Simulation interrupted.");
                break;
            }

            // Increment the generation counter
            generationCount++;
        }
    }

}  //  public class Driver
