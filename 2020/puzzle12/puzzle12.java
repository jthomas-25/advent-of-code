import java.util.Scanner;
import java.io.File;
import java.awt.Point;

public class puzzle12 {
    static Ship ferry;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            int i = 0;
            while (stdin.hasNextLine()) {
                stdin.nextLine();
                i++;
            }
            stdin.close();
            String[] navInsts = new String[i];
            stdin = new Scanner(inputFile);
            for (i = 0; i < navInsts.length; i++)
                navInsts[i] = stdin.nextLine();
            stdin.close();

            System.out.println("Part One:");
            printPart1Answer(navInsts);
            System.out.println("Part Two:");
            printPart2Answer(navInsts);

        } catch (Exception e) {
        }
    }
    
    private static void printPart1Answer(String[] navInsts) {
        ferry = new Ship(0, 0, 'E');
        followNavInstructions(navInsts, false);
        int ferrysManhattanDistance = ferry.getManhattanDistance();
        System.out.println(ferrysManhattanDistance);
    }
    
    private static void printPart2Answer(String[] navInsts) {
        ferry.setXPos(0);
        ferry.setYPos(0);
        ferry.changeDir('E');
        Point waypoint = new Point(ferry.getXPos() + 10, ferry.getYPos() + 1);
        ferry.setWaypoint(waypoint);
        followNavInstructions(navInsts, true);
        int ferrysManhattanDistance = ferry.getManhattanDistance();
        System.out.println(ferrysManhattanDistance);
    }    

    private static void followNavInstructions(String[] navInsts, boolean useWaypoint) {
        for (String inst : navInsts) {
            char action = inst.charAt(0);
            int inputValue = Integer.parseInt(inst.substring(1));
            if (useWaypoint) {
                Point waypoint = ferry.getWaypoint();
                switch (action) {
                    case 'N':
                    case 'S':
                    case 'E':
                    case 'W':
                        moveWaypoint(waypoint, action, inputValue);
                        break;
                    case 'L':
                        rotateWaypointCounterclockwise(waypoint, inputValue);
                        break;
                    case 'R':
                        rotateWaypointClockwise(waypoint, inputValue);
                        break;
                    case 'F':
                        ferry.moveToWaypoint(inputValue);
                        break;
                }
            } else {
                switch (action) {
                    case 'N':
                    case 'S':
                    case 'E':
                    case 'W':
                        int[] velocityVector = ferry.calcVelocityVector(action, inputValue);
                        int dx = velocityVector[0];
                        int dy = velocityVector[1];
                        ferry.move(dx, dy);
                        break;
                    case 'F':
                        ferry.moveForward(inputValue);
                        break;
                    case 'L':
                    case 'R':
                        ferry.turn(action, inputValue);
                        break;
                }
            }
        }
    }
    
    private static void moveWaypoint(Point waypoint, char dir, int delta) {
        switch (dir) {
            case 'N':
                waypoint.translate(0, delta);
                break;
            case 'S':
                waypoint.translate(0, -delta);
                break;
            case 'E':
                waypoint.translate(delta, 0);
                break;
            case 'W':
                waypoint.translate(-delta, 0);
                break;
        }
    }

    private static void rotateWaypointCounterclockwise(Point waypoint, int degrees) {
        int x = (int)(waypoint.getX());
        int y = (int)(waypoint.getY());
        int ferry_x = ferry.getXPos();
        int ferry_y = ferry.getYPos();
        //rotate vector around origin first, then translate by ship's point
        if (degrees == 90)
            //rotation matrix
            //[0 1
            //-1 0]
            //(y,-x)
            waypoint.move(-(y-ferry_y)+ferry_x, (x-ferry_x)+ferry_y);
        else if (degrees == 180)
            //rotation matrix
            //[-1  0
            //  0 -1]
            //(-x,-y)
            waypoint.move(-(x-ferry_x)+ferry_x, -(y-ferry_y)+ferry_y);
        else if (degrees == 270)
            //rotate clockwise 90
            //rotation matrix
            //[0 1
            //-1 0]
            //(y,-x)
            waypoint.move((y-ferry_y)+ferry_x, -(x-ferry_x)+ferry_y);
    }

    private static void rotateWaypointClockwise(Point waypoint, int degrees) {
        int x = (int)(waypoint.getX());
        int y = (int)(waypoint.getY());
        int ferry_x = ferry.getXPos();
        int ferry_y = ferry.getYPos();
        //rotate vector around origin first, then translate by ship's point
        if (degrees == 90)
            //rotation matrix
            //[0 1
            //-1 0]
            //(y,-x)
            waypoint.move((y-ferry_y)+ferry_x, -(x-ferry_x)+ferry_y);
        else if (degrees == 180)
            //rotation matrix
            //[-1  0
            //  0 -1]
            //(-x,-y)
            waypoint.move(-(x-ferry_x)+ferry_x, -(y-ferry_y)+ferry_y);
        else if (degrees == 270)
            //rotate counterclockwise 90
            //rotation matrix
            //[0 -1
            // 1  0]
            //(-y,x)
            waypoint.move(-(y-ferry_y)+ferry_x, (x-ferry_x)+ferry_y);
    }
}

class Ship {
    private int xPos, yPos;
    private char dir;
    private Point waypoint;

    Ship(int startXPos, int startYPos, char startDir) {
        this.xPos = startXPos;
        this.yPos = startYPos;
        this.dir = startDir;
    }

    int getXPos() { return this.xPos; }

    int getYPos() { return this.yPos; }
    
    char getDir() { return this.dir; }

    Point getWaypoint() { return this.waypoint; }

    int getXDistFromWaypoint() { return Math.abs(this.xPos - (int)(waypoint.getX())); }
    
    int getYDistFromWaypoint() { return Math.abs(this.yPos - (int)(waypoint.getY())); }

    void setXPos(int xPos) { this.xPos = xPos; }

    void setYPos(int yPos) { this.yPos = yPos; }

    void changeDir(char dir) { this.dir = dir; }
    
    void setWaypoint(Point waypoint) { this.waypoint = waypoint; }

    void turn(char turnDir, int degrees) {
        switch (this.dir) {
            case 'N':
                if (turnDir == 'R') {
                    if (degrees == 90)
                        changeDir('E');
                    else if (degrees == 180)
                        changeDir('S');
                    else if (degrees == 270)
                        changeDir('W');
                } else if (turnDir == 'L') {
                    if (degrees == 90)
                        changeDir('W');
                    else if (degrees == 180)
                        changeDir('S');
                    else if (degrees == 270)
                        changeDir('E');
                }
                break;
            case 'S':
                if (turnDir == 'R') {
                    if (degrees == 90)
                        changeDir('W');
                    else if (degrees == 180)
                        changeDir('N');
                    else if (degrees == 270)
                        changeDir('E');
                } else if (turnDir == 'L') {
                    if (degrees == 90)
                        changeDir('E');
                    else if (degrees == 180)
                        changeDir('N');
                    else if (degrees == 270)
                        changeDir('W');
                }
                break;
            case 'E':
                if (turnDir == 'R') {
                    if (degrees == 90)
                        changeDir('S');
                    else if (degrees == 180)
                        changeDir('W');
                    else if (degrees == 270)
                        changeDir('N');
                } else if (turnDir == 'L') {
                    if (degrees == 90)
                        changeDir('N');
                    else if (degrees == 180)
                        changeDir('W');
                    else if (degrees == 270)
                        changeDir('S');
                }
                break;
            case 'W':
                if (turnDir == 'R') {
                    if (degrees == 90)
                        changeDir('N');
                    else if (degrees == 180)
                        changeDir('E');
                    else if (degrees == 270)
                        changeDir('S');
                } else if (turnDir == 'L') {
                    if (degrees == 90)
                        changeDir('S');
                    else if (degrees == 180)
                        changeDir('E');
                    else if (degrees == 270)
                        changeDir('N');
                }
                break;
        }
    }
    
    int[] calcVelocityVector(char moveDir, int speed) {
        int[] velocityVector = new int[2];
        int dx = 0, dy = 0;
        switch (moveDir) {
            case 'N':
                dx = 0;
                dy = speed;
                break;
            case 'S':
                dx = 0;
                dy = -speed;
                break;
            case 'E':
                dx = speed;
                dy = 0;
                break;
            case 'W':
                dx = -speed;
                dy = 0;
                break;
        }
        velocityVector[0] = dx;
        velocityVector[1] = dy;
        return velocityVector;
    }

    void move(int dx, int dy) {
        this.xPos += dx;
        this.yPos += dy;
    }

    void moveForward(int delta) {
        switch (this.dir) {
            case 'N':
                this.yPos += delta;
                break;
            case 'S':
                this.yPos -= delta;
                break;
            case 'E':
                this.xPos += delta;
                break;
            case 'W':
                this.xPos -= delta;
                break;
        }
    }

    void moveToWaypoint(int numTimesToMove) {
        int waypointX = (int)(waypoint.getX());
        int waypointY = (int)(waypoint.getY());
        int xDist = this.getXDistFromWaypoint();
        int yDist = this.getYDistFromWaypoint();
        int dx = (waypointX >= this.xPos) ? xDist : -xDist;
        int dy = (waypointY >= this.yPos) ? yDist : -yDist;
        for (int i = 0; i < numTimesToMove; i++) {
            this.move(dx, dy);
            this.waypoint.translate(dx, dy);
        }
    }

    int getManhattanDistance() { return Math.abs(this.xPos) + Math.abs(this.yPos); }
}
