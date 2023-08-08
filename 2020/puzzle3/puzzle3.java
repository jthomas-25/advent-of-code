import java.util.Scanner;
import java.io.File;

public class puzzle3 {
    static char[][] map;
    static int mapWidth, mapHeight;
    static final char TREE = '#';
     
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File mapFile = new File(filename);
            Scanner stdin = new Scanner(mapFile);
            mapWidth = stdin.nextLine().length();
            mapHeight = 1;
            while (stdin.hasNextLine()) {
                stdin.nextLine();
                mapHeight++;
            }
            stdin.close();
            map = new char[mapHeight][mapWidth];
            stdin = new Scanner(mapFile);
            for (int row = 0; row < mapHeight; row++) {
                String mapRow = stdin.nextLine();
                for (int col = 0; col < mapWidth; col++)
                    map[row][col] = mapRow.charAt(col);
            }
            stdin.close();

            System.out.println("Part One:");
            printPart1Answer();
            System.out.println("Part Two:");
            printPart2Answer();

        } catch (Exception e) {
        }
    }

    private static void printPart1Answer() {
        int treesEncountered = traverseMap(3, 1, false);
        System.out.println("Number of trees encountered: " + treesEncountered);
    }

    private static void printPart2Answer() {
        long product = 0;
        int[] dxs = {1,3,5,7,1};
        int[] dys = {1,1,1,1,2};
        int[] results = new int[5];
        for (int i = 0; i < results.length; i++) {
            int treesEncountered = traverseMap(dxs[i], dys[i], false);
            results[i] = treesEncountered;
            product = (i == 0) ? treesEncountered : product * treesEncountered;
        }
        for (int i = 0; i < results.length; i++) {
            System.out.print(results[i]);
            if (i != results.length-1)
                System.out.print(" * ");
            else
                System.out.print(" = ");
        }
        System.out.print(product);
        System.out.println();
    }
    
    private static int traverseMap(int dx, int dy, boolean verbose) {
        int treesEncountered = 0;
        int col = 0, row = 0;
        boolean atBottom = false;
        if (verbose) {
            for (int i = 0; i < mapWidth; i++)
                System.out.print(map[row][i]);
            System.out.println();
        }
        do {
            col += dx;
            if (col > mapWidth-1)
                col %= mapWidth;    //wrap around effect (technically not on original map anymore)
            row += dy;
            if (row > mapHeight-1) {
                atBottom = true;
            } else {
                if (map[row][col] == TREE) {
                    treesEncountered++;
                    if (verbose)
                        printMapRow(row, col, 'X');
                } else {
                    if (verbose)
                        printMapRow(row, col, 'O');
                }
            }
        } while (!atBottom);
        return treesEncountered;
    }
    
    private static void printMapRow(int row, int col, char mark) {
        for (int i = 0; i < mapWidth; i++) {
            if (i == col)
                System.out.print(mark);
            else
                System.out.print(map[row][i]);
        }
        System.out.println();
    }
}
