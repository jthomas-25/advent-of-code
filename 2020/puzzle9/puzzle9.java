import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class puzzle9 {
    static final int PREAMBLE_LENGTH = 25;
    static long firstInvalidNum;

    public static void main(String[] args) {
        try {
            String filename = args[0];
            File xmasDataFile = new File(filename);
            Scanner stdin = new Scanner(xmasDataFile);
            int i = 0;
            while (stdin.hasNextLine()) {
                stdin.nextLine();
                i++;
            }
            stdin.close();
            long[] xmasData = new long[i];
            stdin = new Scanner(xmasDataFile);
            for (i = 0; i < xmasData.length; i++)
                xmasData[i] = stdin.nextLong();
            stdin.close();
            
            System.out.println("Part One:");
            printPart1Answer(xmasData);
            System.out.println("Part Two:");
            printPart2Answer(xmasData);

        } catch (Exception e) {
        }
    }
    
    private static void printPart1Answer(long[] xmasData) {
        firstInvalidNum = findFirstInvalidNumber(xmasData);
        System.out.println(firstInvalidNum);
    }
    
    private static void printPart2Answer(long[] xmasData) {
        long encryptionWeakness = findEncryptionWeakness(xmasData);
        System.out.println(encryptionWeakness);
    }
    
    private static long findFirstInvalidNumber(long[] xmasData) {
        for (int i = PREAMBLE_LENGTH; i < xmasData.length; i++) {
            long nextNum = xmasData[i];
            boolean isValid = false;
            for (int j = i-PREAMBLE_LENGTH; j < i-1; j++) {
                long num1 = xmasData[j];
                for (int k = j+1; k < i; k++) {
                    long num2 = xmasData[k];
                    if (nextNum == num1 + num2) {
                        isValid = true;
                        break;
                    }
                }
            }
            if (!isValid)
                return nextNum;
        }
        return -1;
    }
    
    private static long findEncryptionWeakness(long[] xmasData) {
        ArrayList<Long> summands = new ArrayList<>();
        boolean weaknessFound = false;
        for (int i = 0; i < xmasData.length-2; i++) {
            long num1 = xmasData[i];
            long num2 = xmasData[i+1];
            summands.add(num1);
            summands.add(num2);
            long sum = num1 + num2;
            if (sum == firstInvalidNum) {
                weaknessFound = true;
                break;
            } else if (sum > firstInvalidNum) {
                continue;
            }
            for (int j = 2; j < xmasData.length-1; j++) {
                long nextNum = xmasData[i+j];
                summands.add(nextNum);
                sum += nextNum;
                if (sum == firstInvalidNum) {
                    weaknessFound = true;
                    break;
                } else if (sum > firstInvalidNum) {
                    break;
                }
            }
            if (weaknessFound)
                break;
            summands = new ArrayList<>();
        }
        Collections.sort(summands);
        long min = summands.get(0);
        long max = summands.get(summands.size()-1);
        return min + max;
    }
}
