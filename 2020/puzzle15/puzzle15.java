import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class puzzle15 {
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            String[] startingNums = stdin.nextLine().split(",");
            stdin.close();
            
            System.out.println("Part One:");
            printPart1Answer(startingNums);
            System.out.println("Part Two:");
            printPart2Answer(startingNums);

        } catch (Exception e) {
        }
    }

    private static void printPart1Answer(String[] startingNums) {
        int numSpoken = playGame(startingNums, 2020);
        System.out.println(numSpoken);
    }
    
    private static void printPart2Answer(String[] startingNums) {
        int numSpoken = playGame(startingNums, 30000000);
        System.out.println(numSpoken);
    }

    private static int playGame(String[] startingNums, int numTurns) {
        Hashtable<Integer, ArrayList<Integer>> turnNumsForEachNumSpoken = new Hashtable<>();
        Integer numSpoken = null, lastNumSpoken = null;
        ArrayList<Integer> lastTwoTurnsNumWasSpoken;
        for (int turnNum = 1; turnNum <= numTurns; turnNum++) {
            if (turnNum <= startingNums.length) {
                numSpoken = Integer.parseInt(startingNums[turnNum-1]);
                lastTwoTurnsNumWasSpoken = new ArrayList<>();
                lastTwoTurnsNumWasSpoken.add(turnNum);
                turnNumsForEachNumSpoken.put(numSpoken, lastTwoTurnsNumWasSpoken);
            } else {
                ArrayList<Integer> lastTwoTurnsLastNumWasSpoken = turnNumsForEachNumSpoken.get(lastNumSpoken);
                if (lastTwoTurnsLastNumWasSpoken.size() == 1)
                    numSpoken = 0;
                else
                    numSpoken = lastTwoTurnsLastNumWasSpoken.get(1) - lastTwoTurnsLastNumWasSpoken.get(0);
                lastTwoTurnsNumWasSpoken = turnNumsForEachNumSpoken.get(numSpoken);
                if (lastTwoTurnsNumWasSpoken == null) {
                    lastTwoTurnsNumWasSpoken = new ArrayList<>();
                    lastTwoTurnsNumWasSpoken.add(turnNum);
                } else {
                    if (lastTwoTurnsNumWasSpoken.size() < 2) {
                        lastTwoTurnsNumWasSpoken.add(turnNum);
                    } else {
                        lastTwoTurnsNumWasSpoken.remove(0);
                        lastTwoTurnsNumWasSpoken.add(turnNum);
                    }
                }
                turnNumsForEachNumSpoken.put(numSpoken, lastTwoTurnsNumWasSpoken);
            }
            lastNumSpoken = numSpoken;
        }
        return numSpoken;
    }
}
