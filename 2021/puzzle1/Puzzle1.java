import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Puzzle1 {
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            ArrayList<Integer> depths = new ArrayList<>();
            while (stdin.hasNextLine()) {
                depths.add(Integer.parseInt(stdin.nextLine()));
            }
            stdin.close();
            part1(depths);
            part2(depths);
        }
        catch (Exception e) {
        }
    }

    private static void part1(ArrayList<Integer> depths) {
        int numDepthIncreases = 0;
        for (int i = 1; i < depths.size(); i++) {
            if (depths.get(i) > depths.get(i-1)) {
                numDepthIncreases++;
            }
        }
        System.out.println(numDepthIncreases);
    }

    private static void part2(ArrayList<Integer> depths) {
        int windowSize = 3;
        Integer prevSum = null;
        int numSumIncreases = 0;
        for (int i = 0; i <= depths.size()-windowSize; i++) {
            int sum = depths.get(i) + depths.get(i+1) + depths.get(i+2);
            if (prevSum != null && sum > prevSum) {
                numSumIncreases++;
            }
            prevSum = sum;
        }
        System.out.println(numSumIncreases);
    }
}
