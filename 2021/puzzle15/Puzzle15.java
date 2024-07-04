import java.util.Scanner;
import java.io.File;

public class Puzzle15 {
    static Point[][] riskmap;
    static int width, height;
    static final int SCALE = 5;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            printPart1Answer(filename);
            printPart2Answer();
        }
        catch (Exception e) {
        }
    }

    private static void printPart1Answer(String filename) throws Exception {
        readMap(filename);
        findLowestRiskPath();
    }

    private static void printPart2Answer() {
        getFullMap();
        findLowestRiskPath();
    }

    private static void findLowestRiskPath() {
        Graph graph = loadGraph();
        Point start = riskmap[0][0];
        Point end = riskmap[height-1][width-1];
        int lowestTotalRisk = Dijkstras.shortestPath(graph, start, end);
        System.out.println(lowestTotalRisk);
    }

    private static Graph loadGraph() {
        Graph graph = new Graph(height * width);
        setEdges(graph);
        return graph;
    }
    
    private static void setEdges(Graph graph) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Point point = riskmap[row][col];
                if (point.isBottomRight()) {
                    continue;
                }
                for (Point neighbor : point.getNeighbors()) {
                    int from = point.getIndex();
                    int to = neighbor.getIndex();
                    graph.insertEdge(from, to, neighbor.getRiskLevel());
                    graph.insertEdge(to, from, point.getRiskLevel());
                }
            }
        }
    }

    private static void getFullMap() {
        Point[][] newRiskmap = new Point[height * SCALE][width * SCALE];
        for (int tileRow = 0; tileRow < SCALE; tileRow++) {
            for (int tileCol = 0; tileCol < SCALE; tileCol++) {
                fillTile(tileRow, tileCol, newRiskmap);
            }
        }
        riskmap = newRiskmap;
        height = riskmap.length;
        width = height;
    }

    private static void fillTile(int tileRow, int tileCol, Point[][] newRiskmap) {
        int tileIndex = tileRow * SCALE + tileCol;
        // to fill in this tile, loop through the original map
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Point p;
                if (tileIndex == 0) {
                    p = riskmap[row][col];
                    p.setGrid(newRiskmap);
                }
                else {
                    int curRiskLevel = riskmap[row][col].getRiskLevel();
                    int numRepeats = tileRow + tileCol;
                    int newRiskLevel;
                    if ((curRiskLevel + numRepeats) > 9) {
                        // get a value between 1 and 9, inclusive
                        newRiskLevel = 1 + (curRiskLevel + numRepeats) % 10;
                    }
                    else {
                        newRiskLevel = curRiskLevel + numRepeats;
                    }
                    p = new Point(row + height*tileRow, col + width*tileCol, newRiskLevel, newRiskmap);
                }
                newRiskmap[p.getRow()][p.getCol()] = p;
            }
        }
    }
    
    private static void printMap() {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                Point point = riskmap[row][col];
                System.out.print(point.getRiskLevel());
            }
            System.out.println();
        }
    }
    
    private static void readMap(String filename) throws Exception {
        File file = new File(filename);
        Scanner stdin = new Scanner(file);
        String mapRow = stdin.nextLine();
        height = mapRow.length();
        width = height;
        riskmap = new Point[height][width];
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                int riskLevel = mapRow.charAt(col) - '0';
                Point point = new Point(row, col, riskLevel, riskmap);
                riskmap[row][col] = point;
            }
            if (stdin.hasNextLine()) {
                mapRow = stdin.nextLine();
            }
        }
        stdin.close();
    }
}

