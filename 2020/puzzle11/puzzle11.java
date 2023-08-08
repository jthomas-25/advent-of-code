import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class puzzle11 {
    static char[][] initialMap;
    static char[][] map;
    static int mapWidth, mapHeight;
    static final char FLOOR = '.';
    static final char EMPTY_SEAT = 'L';
    static final char OCCUPIED_SEAT = '#';
    static enum Direction {N, S, E, W, NW, NE, SW, SE};
    
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
            initialMap = new char[mapHeight][mapWidth];
            stdin = new Scanner(mapFile);
            for (int row = 0; row < mapHeight; row++) {
                String mapRow = stdin.nextLine();
                for (int col = 0; col < mapWidth; col++)
                    initialMap[row][col] = mapRow.charAt(col);
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
        map = initialMap;
        //printMap();
        //System.out.println();
        do {
            map = changeSeatLayout(false);
            //printMap();
            //System.out.println();
        } while (canApplyRules(false));
        //printMap();
        //System.out.println();
        int numOccupiedSeats = getNumOccupiedSeats();
        System.out.println(numOccupiedSeats);
    }

    private static void printPart2Answer() {
        map = initialMap;
        //printMap();
        //System.out.println();
        do {
            map = changeSeatLayout(true);
            //printMap();
            //System.out.println();
        } while (canApplyRules(true));
        //printMap();
        //System.out.println();
        int numOccupiedSeats = getNumOccupiedSeats();
        System.out.println(numOccupiedSeats);
    }

    private static char[][] changeSeatLayout(boolean lineOfSight) {
        char[][] newMap = new char[mapHeight][mapWidth];
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                char thisPos = map[row][col];
                if (thisPos == FLOOR) {
                    newMap[row][col] = FLOOR;
                    continue;
                }
                if (!lineOfSight) {
                    ArrayList<Character> adjacentSeats = getAdjacentSeats(row, col);
                    int numAdjacentSeatsOccupied = 0;
                    for (char adjSeat : adjacentSeats) {
                        if (adjSeat == OCCUPIED_SEAT)
                            numAdjacentSeatsOccupied++;
                    }
                    if (thisPos == EMPTY_SEAT) {
                        if (numAdjacentSeatsOccupied == 0)
                            thisPos = OCCUPIED_SEAT;
                    } else if (thisPos == OCCUPIED_SEAT) {
                        if (numAdjacentSeatsOccupied >= 4)
                            thisPos = EMPTY_SEAT;
                    }
                } else {
                    ArrayList<Character> firstVisibleSeats = getFirstVisibleSeats(row, col);
                    int numVisibleSeatsOccupied = 0;
                    for (char seat : firstVisibleSeats) {
                        if (seat == OCCUPIED_SEAT)
                            numVisibleSeatsOccupied++;
                    }
                    if (thisPos == EMPTY_SEAT) {
                        if (numVisibleSeatsOccupied == 0)
                            thisPos = OCCUPIED_SEAT;
                    } else if (thisPos == OCCUPIED_SEAT) {
                        if (numVisibleSeatsOccupied >= 5)
                            thisPos = EMPTY_SEAT;
                    }
                }
                newMap[row][col] = thisPos;
            }
        }
        return newMap;
    }

    private static ArrayList<Character> getFirstVisibleSeats(int row, int col) {
        ArrayList<Character> firstVisibleSeats = new ArrayList<>();
        ArrayList<Direction> dirs = getDirectionsToSearch(row, col);
        for (Direction dir : dirs) {
            Character seat = findSeat(row, col, dir);
            if (seat != null)
                firstVisibleSeats.add(seat);
        }
        return firstVisibleSeats;
    }

    private static ArrayList<Direction> getDirectionsToSearch(int row, int col) {
        ArrayList<Direction> dirs = new ArrayList<>();
        Direction N = Direction.N;
        Direction S = Direction.S;
        Direction E = Direction.E;
        Direction W = Direction.W;
        Direction NW = Direction.NW;
        Direction NE = Direction.NE;
        Direction SW = Direction.SW;
        Direction SE = Direction.SE;
        if (isTopLeft(row, col)) {
            dirs.add(S);
            dirs.add(E);
            dirs.add(SE);
        } else if (isTopRight(row, col)) {
            dirs.add(S);
            dirs.add(W);
            dirs.add(SW);
        } else if (isBottomLeft(row, col)) {
            dirs.add(N);
            dirs.add(E);
            dirs.add(NE);
        } else if (isBottomRight(row, col)) {
            dirs.add(N);
            dirs.add(W);
            dirs.add(NW);
        } else if (isLeftEdge(col)) {
            dirs.add(N);
            dirs.add(S);
            dirs.add(E);
            dirs.add(NE);
            dirs.add(SE);
        } else if (isRightEdge(col)) {
            dirs.add(N);
            dirs.add(S);
            dirs.add(W);
            dirs.add(NW);
            dirs.add(SW);
        } else if (isTopEdge(row)) {
            dirs.add(S);
            dirs.add(E);
            dirs.add(W);
            dirs.add(SW);
            dirs.add(SE);
        } else if (isBottomEdge(row)) {
            dirs.add(N);
            dirs.add(E);
            dirs.add(W);
            dirs.add(NW);
            dirs.add(NE);
        } else {
            dirs.add(N);
            dirs.add(S);
            dirs.add(E);
            dirs.add(W);
            dirs.add(NW);
            dirs.add(NE);
            dirs.add(SW);
            dirs.add(SE);
        }
        return dirs;
    }

    private static Character findSeat(int row, int col, Direction dir) {
        Character thisPos = null;
        switch (dir) {
            case N:
                thisPos = searchNorth(row, col);
                break;
            case S:
                thisPos = searchSouth(row, col);
                break;
            case E:
                thisPos = searchEast(row, col);
                break;
            case W:
                thisPos = searchWest(row, col);
                break;
            case NW:
                thisPos = searchNorthwest(row, col);
                break;
            case SW:
                thisPos = searchSouthwest(row, col);
                break;
            case NE:
                thisPos = searchNortheast(row, col);
                break;
            case SE:
                thisPos = searchSoutheast(row, col);
                break;
        }
        return thisPos;
    }

    private static Character searchNorth(int row, int col) {
        for (int j = row-1; j >= 0; j--) {
            char thisPos = map[j][col];
            if (thisPos == EMPTY_SEAT || thisPos == OCCUPIED_SEAT)
                return thisPos;
        }
        return null;
    }

    private static Character searchSouth(int row, int col) {
        for (int j = row+1; j < mapHeight; j++) {
            char thisPos = map[j][col];
            if (thisPos == EMPTY_SEAT || thisPos == OCCUPIED_SEAT)
                return thisPos;
        }
        return null;
    }

    private static Character searchEast(int row, int col) {
        for (int i = col+1; i < mapWidth; i++) {
            char thisPos = map[row][i];
            if (thisPos == EMPTY_SEAT || thisPos == OCCUPIED_SEAT)
                return thisPos;
        }
        return null;
    }

    private static Character searchWest(int row, int col) {
        for (int i = col-1; i >= 0; i--) {
            char thisPos = map[row][i];
            if (thisPos == EMPTY_SEAT || thisPos == OCCUPIED_SEAT)
                return thisPos;
        }
        return null;
    }

    private static Character searchNorthwest(int row, int col) {
        Character thisPos = null;
        boolean outOfBounds = false;
        boolean seatFound = false;
        do {
            row--;
            if (row < 0) {
                outOfBounds = true;
            } else {
                col--;
                if (col < 0) {
                    outOfBounds = true;
                } else {
                    thisPos = map[row][col];
                    if (thisPos == EMPTY_SEAT || thisPos == OCCUPIED_SEAT)
                        seatFound = true;
                }
            }
        } while (!outOfBounds && !seatFound);
        if (seatFound)
            return thisPos;
        return null;
    }
        
    private static Character searchNortheast(int row, int col) {
        Character thisPos = null;
        boolean outOfBounds = false;
        boolean seatFound = false;
        do {
            row--;
            if (row < 0) {
                outOfBounds = true;
            } else {
                col++;
                if (col > mapWidth-1) {
                    outOfBounds = true;
                } else {
                    thisPos = map[row][col];
                    if (thisPos == EMPTY_SEAT || thisPos == OCCUPIED_SEAT)
                        seatFound = true;
                }
            }
        } while (!outOfBounds && !seatFound);
        if (seatFound)
            return thisPos;
        return null;
    }
    
    private static Character searchSouthwest(int row, int col) {
        Character thisPos = null;
        boolean outOfBounds = false;
        boolean seatFound = false;
        do {
            row++;
            if (row > mapHeight-1) {
                outOfBounds = true;
            } else {
                col--;
                if (col < 0) {
                    outOfBounds = true;
                } else {
                    thisPos = map[row][col];
                    if (thisPos == EMPTY_SEAT || thisPos == OCCUPIED_SEAT)
                        seatFound = true;
                }
            }
        } while (!outOfBounds && !seatFound);
        if (seatFound)
            return thisPos;
        return null;
    }
    
    private static Character searchSoutheast(int row, int col) {
        Character thisPos = null;
        boolean outOfBounds = false;
        boolean seatFound = false;
        do {
            row++;
            if (row > mapHeight-1) {
                outOfBounds = true;
            } else {
                col++;
                if (col > mapWidth-1) {
                    outOfBounds = true;
                } else {
                    thisPos = map[row][col];
                    if (thisPos == EMPTY_SEAT || thisPos == OCCUPIED_SEAT)
                        seatFound = true;
                }
            }
        } while (!outOfBounds && !seatFound);
        if (seatFound)
            return thisPos;
        return null;
    }

    private static boolean canApplyRules(boolean lineOfSight) {
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                char thisPos = map[row][col];
                if (thisPos == FLOOR)
                    continue;
                if (!lineOfSight) {
                    ArrayList<Character> adjacentSeats = getAdjacentSeats(row, col);
                    int numAdjacentSeatsOccupied = 0;
                    for (char seat : adjacentSeats) {
                        if (seat == OCCUPIED_SEAT)
                            numAdjacentSeatsOccupied++;
                    }
                    if (thisPos == EMPTY_SEAT) {
                        if (numAdjacentSeatsOccupied == 0)
                            return true;
                    } else if (thisPos == OCCUPIED_SEAT) {
                        if (numAdjacentSeatsOccupied >= 4)
                            return true;
                    }
                } else {
                    ArrayList<Character> firstVisibleSeats = getFirstVisibleSeats(row, col);
                    int numVisibleSeatsOccupied = 0;
                    for (char seat : firstVisibleSeats) {
                        if (seat == OCCUPIED_SEAT)
                            numVisibleSeatsOccupied++;
                    }
                    if (thisPos == EMPTY_SEAT) {
                        if (numVisibleSeatsOccupied == 0)
                            return true;
                    } else if (thisPos == OCCUPIED_SEAT) {
                        if (numVisibleSeatsOccupied >= 5)
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private static int getNumOccupiedSeats() {
        int numOccupiedSeats = 0;
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++) {
                if (map[row][col] == OCCUPIED_SEAT)
                    numOccupiedSeats++;
            }
        }
        return numOccupiedSeats;
    }

    private static ArrayList<Character> getAdjacentSeats(int row, int col) {
        ArrayList<Character> adjacentSeats = new ArrayList<>();
        if (isTopLeft(row, col)) {
            adjacentSeats.add(map[0][1]);
            adjacentSeats.add(map[1][0]);
            //diagonal
            adjacentSeats.add(map[1][1]);
        } else if (isTopRight(row, col)) {
            adjacentSeats.add(map[0][col-1]);
            adjacentSeats.add(map[row+1][col]);
            //diagonal
            adjacentSeats.add(map[row+1][col-1]);
        } else if (isBottomLeft(row, col)) {
            adjacentSeats.add(map[row][col+1]);
            adjacentSeats.add(map[row-1][0]);
            //diagonal
            adjacentSeats.add(map[row-1][col+1]);
        } else if (isBottomRight(row, col)) {
            adjacentSeats.add(map[row][col-1]);
            adjacentSeats.add(map[row-1][col]);
            //diagonal
            adjacentSeats.add(map[row-1][col-1]);
        } else if (isLeftEdge(col)) {
            adjacentSeats.add(map[row][col+1]);
            adjacentSeats.add(map[row-1][col]);
            adjacentSeats.add(map[row+1][0]);
            //diagonals
            adjacentSeats.add(map[row-1][col+1]);
            adjacentSeats.add(map[row+1][col+1]);
        } else if (isRightEdge(col)) {
            adjacentSeats.add(map[row][col-1]);
            adjacentSeats.add(map[row-1][col]);
            adjacentSeats.add(map[row+1][col]);
            //diagonals
            adjacentSeats.add(map[row-1][col-1]);
            adjacentSeats.add(map[row+1][col-1]);
        } else if (isTopEdge(row)) {
            adjacentSeats.add(map[row][col-1]);
            adjacentSeats.add(map[row][col+1]);
            adjacentSeats.add(map[row+1][col]);
            //diagonals
            adjacentSeats.add(map[row+1][col-1]);
            adjacentSeats.add(map[row+1][col+1]);
        } else if (isBottomEdge(row)) {
            adjacentSeats.add(map[row][col-1]);
            adjacentSeats.add(map[row][col+1]);
            adjacentSeats.add(map[row-1][col]);
            //diagonals
            adjacentSeats.add(map[row-1][col-1]);
            adjacentSeats.add(map[row-1][col+1]);
        } else {
            adjacentSeats.add(map[row][col-1]);
            adjacentSeats.add(map[row][col+1]);
            adjacentSeats.add(map[row-1][col]);
            adjacentSeats.add(map[row+1][col]);
            //diagonals
            adjacentSeats.add(map[row-1][col-1]);
            adjacentSeats.add(map[row-1][col+1]);
            adjacentSeats.add(map[row+1][col-1]);
            adjacentSeats.add(map[row+1][col+1]);
        }
        return adjacentSeats;
    }

    private static boolean isTopLeft(int row, int col) { return row == 0 && col == 0; }
    private static boolean isTopRight(int row, int col) { return row == 0 && col == mapWidth - 1; }
    private static boolean isBottomLeft(int row, int col) { return row == mapHeight - 1 && col == 0; }
    private static boolean isBottomRight(int row, int col) { return row == mapHeight - 1 && col == mapWidth - 1; }
    private static boolean isLeftEdge(int col) { return col == 0; }
    private static boolean isRightEdge(int col) { return col == mapWidth - 1; }
    private static boolean isTopEdge(int row) { return row == 0; }
    private static boolean isBottomEdge(int row) { return row == mapHeight - 1; }
    
    private static void printMap() {
        for (int row = 0; row < mapHeight; row++) {
            for (int col = 0; col < mapWidth; col++)
                System.out.print(map[row][col]);
            System.out.println();
        }
    }
}
