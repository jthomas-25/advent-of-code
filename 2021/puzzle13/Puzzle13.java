import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Puzzle13 {
    static char[][] originalPaper;
    static char[][] paper;
    static int width, height;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            ArrayList<String> foldLines = new ArrayList<>();
            setupPaper(stdin, foldLines);
            part1(foldLines);
            resetPaper();
            part2(foldLines);
        }
        catch (Exception e) {
        }
    }

    private static void part1(ArrayList<String> foldLines) {
        fold(1, foldLines);
        System.out.println(countDots());
    }
    
    private static void part2(ArrayList<String> foldLines) {
        fold(foldLines.size(), foldLines);
        printPaper();
    }

    private static void fold(int n, ArrayList<String> foldLines) {
        for (int i = 0; i < n; i++) {
            String line = foldLines.get(i);
            if (line.charAt(0) == 'y') {
                foldPaperUp(line);
            }
            else {
                foldPaperLeft(line);
            }
        }
    }

    private static int countDots() {
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (paper[y][x] == '#') {
                    count++;
                }
            }
        }
        return count;
    }

    private static void foldPaperUp(String foldLine) {
        int lineY = Integer.parseInt(foldLine.split("=")[1]);
        char[][] foldedPaper = new char[lineY][width];
        // get top half first
        for (int y = 0; y < lineY; y++) {
            for (int x = 0; x < width; x++) {
                foldedPaper[y][x] = paper[y][x];
            }
        }
        // now for the bottom half
        int row = 0, col;
        for (int y = height-1; y > lineY; y--) {
            for (int x = 0; x < width; x++) {
                col = x;
                // if there's a dot here, don't overwrite it -- the paper is transparent
                if (foldedPaper[row][col] != '#') {
                    foldedPaper[row][col] = paper[y][x];
                }
            }
            if (row < lineY) {
                row++;
            }
        }
        paper = foldedPaper;
        height = paper.length;
    }

    private static void foldPaperLeft(String foldLine) {
        int lineX = Integer.parseInt(foldLine.split("=")[1]);
        char[][] foldedPaper = new char[height][lineX];
        // get left half first
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < lineX; x++) {
                foldedPaper[y][x] = paper[y][x];
            }
        }
        // now for the right half
        int row, col = 0;
        for (int y = 0; y < height; y++) {
            row = y;
            for (int x = width-1; x > lineX; x--) {
                // if there's a dot here, don't overwrite it -- the paper is transparent
                if (foldedPaper[row][col] != '#') {
                    foldedPaper[row][col] = paper[y][x];
                }
                col = (col < lineX-1) ? (col+1) : 0;
            }
        }
        paper = foldedPaper;
        width = paper[0].length;
    }
    
    private static void setupPaper(Scanner stdin, ArrayList<String> foldLines) {
        width = -1;
        height = -1;
        ArrayList<Dot> dots = new ArrayList<>();
        boolean done = false;
        while (!done) {
            String line = stdin.nextLine();
            if (line.isEmpty()) {
                done = true;
            }
            else {
                String[] coords = line.split(",");
                int x = Integer.parseInt(coords[0]);
                int y = Integer.parseInt(coords[1]);
                if (width < x) {
                    width = x;
                }
                if (height < y) {
                    height = y;
                }
                dots.add(new Dot(x, y));
            }
        }
        while (stdin.hasNextLine()) {
            foldLines.add(stdin.nextLine().split(" ")[2]);
        }
        stdin.close();
        width++;
        height++;
        originalPaper = new char[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                originalPaper[y][x] = '.';
                for (Dot d : dots) {
                    if (y == d.getY() && x == d.getX()) {
                        originalPaper[y][x] = '#';
                        break;
                    }
                }
            }
        }
        paper = originalPaper;
    }

    private static void resetPaper() {
        paper = originalPaper;
        height = paper.length;
        width = paper[0].length;
    }

    private static void printPaper() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                System.out.print(paper[y][x] + " ");
            }
            System.out.println();
        }
    }
}

class Dot {
    private int x;
    private int y;

    Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    int getX() { return x; }
    
    int getY() { return y; }
}
