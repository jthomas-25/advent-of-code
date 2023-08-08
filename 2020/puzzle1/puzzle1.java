import java.util.Scanner;
import java.io.File;

public class puzzle1 {
    
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
            int[] expenses = new int[i];
            stdin = new Scanner(inputFile);
            for (i = 0; i < expenses.length; i++)
                expenses[i] = Integer.parseInt(stdin.nextLine());
            stdin.close();

            printPart1Answer(expenses);
            printPart2Answer(expenses);
            
        } catch (Exception e) {
        }
    }

    private static void printPart1Answer(int[] expenses) {
        int entry1, entry2, sum;
        for (int i = 0; i < expenses.length-1; i++) {
            entry1 = expenses[i];
            for (int j = i+1; j < expenses.length; j++) {
                entry2 = expenses[j];
                sum = entry1 + entry2;
                if (sum == 2020) {
                    System.out.println("Part One: ");
                    System.out.println(String.format("%d + %d = 2020", entry1, entry2));
                    System.out.println(String.format("%d * %d = %d", entry1, entry2, entry1 * entry2));
                    return;
                }
            }
        }
    }

    private static void printPart2Answer(int[] expenses) {
        int entry1, entry2, entry3, sum;
        for (int i = 0; i < expenses.length-2; i++) {
            entry1 = expenses[i];
            for (int j = i+1; j < expenses.length-1; j++) {
                entry2 = expenses[j];
                for (int k = i+2; k < expenses.length; k++) {
                    entry3 = expenses[k];
                    sum = entry1 + entry2 + entry3;
                    if (sum == 2020) {
                        System.out.println("Part Two: ");
                        System.out.println(String.format("%d + %d + %d = 2020", entry1, entry2, entry3));
                        System.out.println(String.format("%d * %d * %d = %d", entry1, entry2, entry3, entry1 * entry2 * entry3));
                        return;
                    }
                }
            }
        }
    }
}
