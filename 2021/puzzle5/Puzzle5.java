import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.awt.Point;

public class Puzzle5 {
    static int width, height;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            ArrayList<Line> lines = new ArrayList<>();
            while (stdin.hasNextLine()) {
                String[] endpoints = stdin.nextLine().split(" -> ");
                String[] p1Coords = endpoints[0].split(",");
                String[] p2Coords = endpoints[1].split(",");
                Point p1 = getPoint(p1Coords);
                Point p2 = getPoint(p2Coords);
                if (width < (int)(Math.max(p1.getX(), p2.getX()))) {
                    width = (int)(Math.max(p1.getX(), p2.getX()));
                }
                if (height < (int)(Math.max(p1.getY(), p2.getY()))) {
                    height = (int)(Math.max(p1.getY(), p2.getY()));
                }
                Line line = new Line(p1, p2);
                lines.add(line);
            }
            stdin.close();
            width++;
            height++;
            part1(lines);
            part2(lines);
        }
        catch (Exception e) {
        }
    }

    private static void part1(ArrayList<Line> lines) {
        System.out.println(checkLines(lines, true));
    }
    
    private static void part2(ArrayList<Line> lines) {
        System.out.println(checkLines(lines, false));
    }

    private static int checkLines(ArrayList<Line> lines, boolean ignoreDiag) {
        int[][] diagram = new int[width][height];
        int sum = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                for (Line line : lines) {
                    if (ignoreDiag && line.isDiagonal()) {
                        continue;
                    }
                    if (line.containsPoint(row, col)) {
                        diagram[row][col]++;
                    }
                }
                if (diagram[row][col] >= 2) {
                    sum++;
                }
            }
        }
        //printDiagram(diagram);
        return sum;
    }

    private static void printDiagram(int[][] diagram) {
        // coords are col, row
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                // transpose
                System.out.print(diagram[col][row] + " ");
            }
            System.out.println();
        }
    }

    private static Point getPoint(String[] coords) {
        int x = Integer.parseInt(coords[0]);
        int y = Integer.parseInt(coords[1]);
        return new Point(x, y);
    }
}
