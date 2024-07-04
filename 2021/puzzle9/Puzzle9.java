import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Queue;
import java.util.ArrayDeque;
import java.util.Collections;

public class Puzzle9 {
    static int[][] heightmap;
    static int width, height;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            width = stdin.nextLine().length();
            height = 1;
            while (stdin.hasNextLine()) {
                stdin.nextLine();
                height++;
            }
            stdin.close();
            heightmap = new int[height][width];
            stdin = new Scanner(file);
            for (int row = 0; row < height; row++) {
                String mapRow = stdin.nextLine();
                for (int col = 0; col < width; col++) {
                    heightmap[row][col] = mapRow.charAt(col) - '0';
                }
            }
            stdin.close();
            ArrayList<Point> lowPoints = new ArrayList<>();
            part1(lowPoints);
            part2(lowPoints);
        }
        catch (Exception e) {
        }
    }

    private static void part1(ArrayList<Point> lowPoints) {
        int sum = 0;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (isLowPoint(row, col)) {
                    lowPoints.add(new Point(row, col));
                    int riskLevel = 1 + heightmap[row][col];
                    //System.out.println(riskLevel);
                    sum += riskLevel;
                }
            }
        }
        System.out.println(sum);
    }

    private static void part2(ArrayList<Point> lowPoints) {
        ArrayList<Integer> basinSizes = new ArrayList<>();
        for (Point p : lowPoints) {
            basinSizes.add(getBasinSize(p));
        }
        Collections.sort(basinSizes);
        int size = basinSizes.size();
        System.out.println(basinSizes.get(size-1) * basinSizes.get(size-2) * basinSizes.get(size-3));
    }
    
    private static boolean inBasin(int row, int col) {
        return heightmap[row][col] != 9;
    }

    // uses breadth-first search to explore the basin, starting at its low point
    private static int getBasinSize(Point lowPoint) {
        int basinSize = 0;
        boolean[][] wasHere = new boolean[height][width];
        Queue<Point> queue = new ArrayDeque<>();
        Point current = lowPoint;
        boolean done = false;
        while (!done) {
            // mark this spot as visited
            if (!wasHere[current.getRow()][current.getCol()]) {
                wasHere[current.getRow()][current.getCol()] = true;
                basinSize++;
            }
            // if we haven't gone left and we can go left, enqueue left
            if (current.getCol()-1 >= 0 && inBasin(current.getRow(), current.getCol()-1) && !wasHere[current.getRow()][current.getCol()-1]) {
                queue.add(new Point(current.getRow(), current.getCol()-1));
            }
            // enqueue right
            if (current.getCol()+1 <= width-1 && inBasin(current.getRow(), current.getCol()+1) && !wasHere[current.getRow()][current.getCol()+1]) {
                queue.add(new Point(current.getRow(), current.getCol()+1));
            }
            // enqueue top
            if (current.getRow()-1 >= 0 && inBasin(current.getRow()-1, current.getCol()) && !wasHere[current.getRow()-1][current.getCol()]) {
                queue.add(new Point(current.getRow()-1, current.getCol()));
            }
            // enqueue bottom
            if (current.getRow()+1 <= height-1 && inBasin(current.getRow()+1, current.getCol()) && !wasHere[current.getRow()+1][current.getCol()]) {
                queue.add(new Point(current.getRow()+1, current.getCol()));
            }
            // if the queue is empty, we've fully explored the basin!
            if (queue.isEmpty()) {
                done = true;
            }
            else {
                current = queue.remove();
            }
            /*
            for (int row = 0; row < height; row++) {
                for (int col = 0; col < width; col++) {
                    if (wasHere[row][col]) {
                        System.out.print("1 ");
                    }
                    else {
                        System.out.print("0 ");
                    }
                }
                System.out.println();
            }
            System.out.println();
            */
        } 
        //System.out.println(basinSize);
        return basinSize;
    }

    private static boolean isLowPoint(int row, int col) {
        int loc = heightmap[row][col];
        if (isCorner(row, col)) {
            if (isTopLeft(row, col)) {
                return loc < heightmap[row][col+1] && loc < heightmap[row+1][col];
            }
            if (isTopRight(row, col)) {
                return loc < heightmap[row][col-1] && loc < heightmap[row+1][col];
            }
            if (isBottomLeft(row, col)) {
                return loc < heightmap[row][col+1] && loc < heightmap[row-1][col];
            }
            if (isBottomRight(row, col)) {
                return loc < heightmap[row][col-1] && loc < heightmap[row-1][col];
            }
        }
        else if (isEdge(row, col)) {
            if (isTopEdge(row, col)) {
                return loc < heightmap[row][col-1] && loc < heightmap[row][col+1] && loc < heightmap[row+1][col];
            }
            if (isBottomEdge(row, col)) {
                return loc < heightmap[row][col-1] && loc < heightmap[row][col+1] && loc < heightmap[row-1][col];
            }
            if (isLeftEdge(row, col)) {
                return loc < heightmap[row][col+1] && loc < heightmap[row-1][col] && loc < heightmap[row+1][col];
            }
            if (isRightEdge(row, col)) {
                return loc < heightmap[row][col-1] && loc < heightmap[row-1][col] && loc < heightmap[row+1][col];
            }
        }
        return (loc < heightmap[row][col-1] &&
                loc < heightmap[row][col+1] &&
                loc < heightmap[row-1][col] &&
                loc < heightmap[row+1][col]);
    }
    
    private static boolean isCorner(int row, int col) {
        return (isTopLeft(row, col) ||
                isTopRight(row, col) ||
                isBottomLeft(row, col) ||
                isBottomRight(row, col));
    }
    
    private static boolean isEdge(int row, int col) {
        return (isTopEdge(row, col) ||
                isBottomEdge(row, col) ||
                isLeftEdge(row, col) ||
                isRightEdge(row, col));
    }

    private static boolean isTopLeft(int row, int col) {
        return row == 0 && col == 0;
    }
    
    private static boolean isTopRight(int row, int col) {
        return row == 0 && col == width-1;
    }
    
    private static boolean isBottomLeft(int row, int col) {
        return row == height-1 && col == 0;
    }
    
    private static boolean isBottomRight(int row, int col) {
        return row == height-1 && col == width-1;
    }

    private static boolean isTopEdge(int row, int col) {
        return row == 0 && (col > 0 && col < width-1);
    }

    private static boolean isBottomEdge(int row, int col) {
        return row == height-1 && (col > 0 && col < width-1);
    }

    private static boolean isLeftEdge(int row, int col) {
        return col == 0 && (row > 0 && row < height-1);
    }

    private static boolean isRightEdge(int row, int col) {
        return col == width-1 && (row > 0 && row < height-1);
    }

    private static void printMap() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                System.out.print(heightmap[row][col] + " ");
            }
            System.out.println();
        }
    }
}

class Point {
    private int y;
    private int x;

    Point(int y, int x) {
        this.y = y;
        this.x = x;
    }
    
    int getRow() { return y; }

    int getCol() { return x; }
}
