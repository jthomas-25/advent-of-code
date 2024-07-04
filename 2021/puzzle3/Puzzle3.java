import java.util.Scanner;
import java.io.File;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;

public class Puzzle3 {
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            ArrayList<String> binaryNums = new ArrayList<>();
            while (stdin.hasNextLine()) {
                binaryNums.add(stdin.nextLine());
            }
            stdin.close();
            part1(binaryNums);
            part2(binaryNums);
        }
        catch (Exception e) {
        }
    }
    
    private static void part1(AbstractList<String> binaryNums) {
        System.out.println(calcGammaRate(binaryNums) * calcEpsilonRate(binaryNums));
    }
    
    private static void part2(AbstractList<String> binaryNums) {
        System.out.println(calcOxygenGeneratorRating(binaryNums) * calcCO2ScrubberRating(binaryNums));
    }

    private static Integer calcOxygenGeneratorRating(AbstractList<String> binaryNums) {
        LinkedList<String> candidates = new LinkedList<>();
        for (String binString : binaryNums) {
            candidates.add(binString);
        }
        for (int i = 0; i < candidates.get(0).length(); i++) {
            Character bit = findMostCommonBit(i, candidates);
            Iterator<String> itr = candidates.iterator();
            while (itr.hasNext()) {
                String c = itr.next();
                // 0 and 1 are equally common
                // keep numbers with a '1' in the current bit position
                if (bit == null) {
                    if (c.charAt(i) == '0') {
                        itr.remove();
                    }
                }
                // 0 is more common
                else if (bit == '0' && c.charAt(i) == '1') {
                    itr.remove();
                }
                // 1 is more common
                else if (bit == '1' && c.charAt(i) == '0') {
                    itr.remove();
                }
                if (candidates.size() == 1) {
                    return Integer.parseInt(candidates.get(0), 2);
                }
            }
        }
        return null;
    }
    
    private static Integer calcCO2ScrubberRating(AbstractList<String> binaryNums) {
        LinkedList<String> candidates = new LinkedList<>();
        for (String binString : binaryNums) {
            candidates.add(binString);
        }
        for (int i = 0; i < candidates.get(0).length(); i++) {
            Character bit = findLeastCommonBit(i, candidates);
            Iterator<String> itr = candidates.iterator();
            while (itr.hasNext()) {
                String c = itr.next();
                // 0 and 1 are equally common
                // keep numbers with a '0' in the current bit position
                if (bit == null) {
                    if (c.charAt(i) == '1') {
                        itr.remove();
                    }
                }
                // 0 is less common
                else if (bit == '0' && c.charAt(i) == '1') {
                    itr.remove();
                }
                // 1 is less common
                else if (bit == '1' && c.charAt(i) == '0') {
                    itr.remove();
                }
                if (candidates.size() == 1) {
                    return Integer.parseInt(candidates.get(0), 2);
                }
            }
        }
        return null;
    }

    private static int calcGammaRate(AbstractList<String> binaryNums) {
        String gammaRate = "";
        for (int i = 0; i < binaryNums.get(0).length(); i++) {
            Character bit = findMostCommonBit(i, binaryNums);
            if (bit != null) {
                gammaRate += findMostCommonBit(i, binaryNums);
            }
        }
        return Integer.parseInt(gammaRate, 2);
    }

    private static int calcEpsilonRate(AbstractList<String> binaryNums) {
        String epsilonRate = "";
        for (int i = 0; i < binaryNums.get(0).length(); i++) {
            Character bit = findLeastCommonBit(i, binaryNums);
            if (bit != null) {
                epsilonRate += findLeastCommonBit(i, binaryNums);
            }
        }
        return Integer.parseInt(epsilonRate, 2);
    }

    private static Character findMostCommonBit(int pos, AbstractList<String> binaryNums) {
        int numOnes = 0;
        int numZeroes = 0;
        for (String binString : binaryNums) {
            if (binString.charAt(pos) == '1') {
                numOnes++;
            }
            else {
                numZeroes++;
            }
        }
        if (numOnes == numZeroes) {
            return null;
        }
        if (numOnes > numZeroes) {
            return '1';
        }
        return '0';
    }
    
    private static Character findLeastCommonBit(int pos, AbstractList<String> binaryNums) {
        int numOnes = 0;
        int numZeroes = 0;
        for (String binString : binaryNums) {
            if (binString.charAt(pos) == '1') {
                numOnes++;
            }
            else {
                numZeroes++;
            }
        }
        if (numOnes == numZeroes) {
            return null;
        }
        if (numOnes < numZeroes) {
            return '1';
        }
        return '0';
    }
}
