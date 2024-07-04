import java.util.Scanner;
import java.io.File;

public class Puzzle11 {
    static int[][] initialState;
    static Octopus[][] grid;
    static final int WIDTH = 10;
    static final int HEIGHT = 10;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            initialState = new int[HEIGHT][WIDTH];
            grid = new Octopus[HEIGHT][WIDTH];
            for (int row = 0; row < HEIGHT; row++) {
                String gridRow = stdin.nextLine();
                for (int col = 0; col < WIDTH; col++) {
                    int energyLevel = gridRow.charAt(col) - '0';
                    initialState[row][col] = energyLevel;
                    grid[row][col] = new Octopus(row, col, energyLevel, grid);
                }
            }
            stdin.close();
            part1();
            resetGrid();
            part2();
        }
        catch (Exception e) {
        }
    }

    private static void part1() {
        simulateSteps(100);
        int totalFlashes = 0;
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Octopus octopus = grid[row][col];
                totalFlashes += octopus.getNumFlashes();
            }
        }
        System.out.println(totalFlashes);
    }

    private static void part2() {
        System.out.println("First synchronized flash at step " + simulateStepsUntilFirstSyncedFlash());
    }

    private static void step() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Octopus octopus = grid[row][col];
                octopus.step();
            }
        }
        //printGrid();
        //System.out.println();
    }
    
    private static void simulateSteps(int n) {
        for (int step = 1; step <= n; step++) {
            //System.out.println("STEP " + step);
            step();
            prepareForNextStep();
        }
    }

    private static void prepareForNextStep() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Octopus octopus = grid[row][col];
                octopus.reset();
            }
        }
    }

    private static int simulateStepsUntilFirstSyncedFlash() {
        int step = 0;
        boolean allFlashed;
        do {
            step++;
            //System.out.println("STEP " + step);
            step();
            allFlashed = true;
            for (int row = 0; row < HEIGHT; row++) {
                for (int col = 0; col < WIDTH; col++) {
                    Octopus octopus = grid[row][col];
                    if (octopus.getEnergyLevel() != 0) {
                        allFlashed = false;
                    }
                }
            }
            prepareForNextStep();
        } while (!allFlashed);
        return step;
    }

    private static void resetGrid() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Octopus octopus = grid[row][col];
                octopus.setEnergyLevel(initialState[row][col]);
                octopus.setNumFlashes(0);
            }
        }
    }

    private static void printGrid() {
        for (int row = 0; row < HEIGHT; row++) {
            for (int col = 0; col < WIDTH; col++) {
                Octopus octopus = grid[row][col];
                System.out.print(octopus.getEnergyLevel() + " ");
            }
            System.out.println();
        }
    }
}
