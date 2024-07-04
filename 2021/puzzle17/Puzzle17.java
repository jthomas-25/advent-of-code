import java.util.Scanner;
import java.io.File;
import java.util.regex.MatchResult;

public class Puzzle17 {
    static int targetMinX, targetMaxX;
    static int targetMinY, targetMaxY;
    static Probe probe;
    static final int STARTX = 0, STARTY = 0;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            readTargetArea(filename);
            probe = new Probe(STARTX, STARTY);
            int minXVel = solveForNAtLeast(targetMinX);
            int maxYVel = Math.abs(targetMinY+1);
            printPart1Answer(minXVel, maxYVel);
            printPart2Answer(minXVel, maxYVel);
        }
        catch (Exception e) {
        }
    }
    
    private static void printPart1Answer(int minXVel, int maxYVel) {
        fire(minXVel, maxYVel);
        System.out.println("Max y: " + probe.getMaxY());
    }

    private static void printPart2Answer(int minXVel, int maxYVel) {
        int total = 0;
        int maxXVel = targetMaxX;
        int minYVel = targetMinY;
        for (int initdx = minXVel; initdx <= maxXVel; initdx++) {
            for (int initdy = minYVel; initdy <= maxYVel; initdy++) {
                boolean hitTarget = fire(initdx, initdy);
                if (hitTarget) {
                    total++;
                }
            }
        }
        System.out.println(total);
    }

    private static boolean fire(int initdx, int initdy) {
        probe.setX(STARTX);
        probe.setY(STARTY);
        probe.setXVel(initdx);
        probe.setYVel(initdy);
        probe.setMaxY(STARTY);
        boolean hitTarget = false;
        boolean done = false;
        while (!done) {
            probe.move();
            if ((probe.getX() >= targetMinX && probe.getX() <= targetMaxX) &&
                (probe.getY() >= targetMinY && probe.getY() <= targetMaxY)) {
                hitTarget = true;
                done = true;
            }
            else if (probe.getX() < targetMinX && probe.getXVel() == 0) {
                done = true;
            }
            else if (probe.getX() > targetMaxX || probe.getY() < targetMinY) {
                done = true;
            }
        }
        return hitTarget;
    }

    private static int solveForNAtLeast(int sum) {
        int i = 1;
        while (sumFromOneToN(i) < sum) {
            i++;
        }
        return i;
    }
    
    private static int sumFromOneToN(int n) {
        return (int)(n * (n + 1) / 2.0);    // Gauss' method
    }

    private static void readTargetArea(String filename) throws Exception {
        File file = new File(filename);
        Scanner stdin = new Scanner(file);
        String line = stdin.nextLine();
        stdin.close();
        stdin = new Scanner(line);
        stdin.findInLine("x=(-?\\d+)\\.\\.(-?\\d+), y=(-?\\d+)\\.\\.(-?\\d+)");
        MatchResult result = stdin.match();
        targetMinX = Integer.parseInt(result.group(1));
        targetMaxX = Integer.parseInt(result.group(2));
        targetMinY = Integer.parseInt(result.group(3));
        targetMaxY = Integer.parseInt(result.group(4));
        stdin.close();
    }
}

class Probe {
    private int x, y;
    private int dx, dy;
    private int maxY;

    Probe(int x, int y) {
        this.x = x;
        this.y = y;
        maxY = y;
    }

    int getX() { return x; }

    int getY() { return y; }

    int getXVel() { return dx; }

    int getYVel() { return dy; }

    int getMaxY() { return maxY; }

    void setX(int x) { this.x = x; }

    void setY(int y) { this.y = y; }

    void setXVel(int dx) { this.dx = dx; }

    void setYVel(int dy) { this.dy = dy; }

    void setMaxY(int y) { maxY = y; }

    void move() {
        x += dx;
        y += dy;
        if (dx > 0) {
            dx--;
        }
        dy--;
        if (dy == 0) {
            maxY = y;
        }
    }
}
