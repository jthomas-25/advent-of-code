import java.util.Scanner;
import java.io.File;

public class Puzzle7 {
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            String[] positions = stdin.nextLine().split(",");
            stdin.close();
            part1(positions);
            part2(positions);
        }
        catch (Exception e) {
        }
    }

    private static void part1(String[] positions) {
        calcPosAndLeastFuel(positions, true);
    }
    
    private static void part2(String[] positions) {
        calcPosAndLeastFuel(positions, false);
    }
    
    private static void calcPosAndLeastFuel(String[] positions, boolean constantFuelRate) {
        int leastFuel = -1;
        int pos = -1;
        int totalFuel = 0;
        for (int i = 0; i < positions.length; i++) {
            totalFuel = getTotalFuel(i, positions, constantFuelRate);
            if (leastFuel == -1) {
                leastFuel = totalFuel;
            }
            else if (leastFuel > totalFuel) {
                leastFuel = totalFuel;
                pos = i;
            }
        }
        System.out.println(leastFuel);
    }
    
    private static int sumFromOneToN(int n) {
        // Gauss' method
        return (int)(n * (n + 1) / 2.0);
    }

    private static int getTotalFuel(int alignPos, String[] positions, boolean constantFuelRate) {
        int total = 0;
        for (int i = 0; i < positions.length; i++) {
            int dist = Math.abs(Integer.parseInt(positions[i]) - alignPos);
            if (constantFuelRate) {
                total += dist;
            }
            else {
                total += sumFromOneToN(dist);
            }
        }
        return total;
    }

}
