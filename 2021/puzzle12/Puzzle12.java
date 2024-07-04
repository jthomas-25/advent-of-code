import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Stack;

public class Puzzle12 {
    static Graph<String> graph;
    static ArrayList<String> caves;
    static final String START = "start", END = "end";
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            loadGraph(filename);
            printPart1Answer();
            printPart2Answer();
        }
        catch (Exception e) {
        }
    }

    private static void printPart1Answer() {
        long numPaths = countPaths(START, END, 1);
        System.out.println(numPaths);
    }
    
    private static void printPart2Answer() {
        long numPaths = countPaths(START, END, 2);
        System.out.println(numPaths);
    }
    
    private static long countPaths(String start, String end, int rule) {
        Hashtable<String, Integer> visited = new Hashtable<>();
        for (String c : caves) {
            visited.put(c, 0);
        }
        Stack<String> path = new Stack<>();
        return countPaths(visited, start, end, path, rule);
    }
    
    private static long countPaths(Hashtable<String, Integer> visited, String start, String end, Stack<String> path, int rule) {
        visited.put(start, visited.get(start)+1);
        path.push(start);
        if (start.equals(end)) {
            //System.out.println(String.join(",", path));
            visited.put(start, visited.get(start)-1);
            path.pop();
            return 1;
        }
        long total = 0;
        ArrayList<String> neighbors = graph.getAdjacentNodes(start);
        switch (rule) {
            // visit small caves at most once
            case 1:
                boolean currentlyInSmallCave = isSmallCave(start);
                for (String node : neighbors) {
                    if (node.equals(START)) {
                        continue;
                    }
                    if (isSmallCave(node)) {
                        int numVisits = visited.get(node);
                        if (numVisits == 1) {
                            continue;
                        }
                        if (currentlyInSmallCave && graph.getAdjacentNodes(node).size() == 1) {
                            continue;
                        }
                    }
                    total += countPaths(visited, node, end, path, 1);
                }
                break;
            // visit a single small cave at most twice and all other small caves at most once
            case 2:
                boolean smallCaveVisitedTwice = false;
                for (String cave : caves) {
                    if (isSmallCave(cave) && visited.get(cave) == 2) {
                        smallCaveVisitedTwice = true;
                        break;
                    }
                }
                for (String node : neighbors) {
                    if (node.equals(START)) {
                        continue;
                    }
                    if (isSmallCave(node)) {
                        int numVisits = visited.get(node);
                        if (numVisits == 2) {
                            continue;
                        }
                        if (numVisits == 1 && smallCaveVisitedTwice) {
                            continue;
                        }
                    }
                    total += countPaths(visited, node, end, path, 2);
                }
                break;
        }
        visited.put(start, visited.get(start)-1);
        path.pop();
        return total;
    }
    
    private static boolean isSmallCave(String cave) {
        return Character.isLowerCase(cave.charAt(0));
    }

    private static void loadGraph(String filename) throws Exception {
        File file = new File(filename);
        Scanner stdin = new Scanner(file);
        caves = new ArrayList<>();
        while (stdin.hasNextLine()) {
            String[] cavePair = stdin.nextLine().split("\\-");
            for (String cave : cavePair) {
                if (!caves.contains(cave)) {
                    caves.add(cave);
                }
            }
        }
        stdin.close();
        graph = new Graph<>(caves.size());
        // setup nodes
        for (int node = 0; node < graph.getSize(); node++) {
            graph.setValue(node, caves.get(node));
        }
        // setup edges
        stdin = new Scanner(file);
        while (stdin.hasNextLine()) {
            String[] cavePair = stdin.nextLine().split("\\-");
            graph.insertEdge(cavePair[0], cavePair[1]);
        }
        stdin.close();
    }
}
