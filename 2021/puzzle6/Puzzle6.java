import java.util.Scanner;
import java.io.File;
import java.util.Hashtable;
import java.util.Enumeration;

public class Puzzle6 {
    static Hashtable<Integer, Long> numFishPerAge;
    static Hashtable<Integer, Long> growthRatePerAge;
    static final int MAX_TIMER = 8;
    static final int DAYS_TO_RUN_SIM = 80;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            String[] ages = stdin.nextLine().split(",");
            stdin.close();
            numFishPerAge = new Hashtable<>();
            growthRatePerAge = new Hashtable<>();
            for (int i = 0; i <= MAX_TIMER; i++) {
                numFishPerAge.put(i, 0L);
                growthRatePerAge.put(i, 0L);
            }
            for (String a : ages) {
                int age = Integer.parseInt(a);
                numFishPerAge.put(age, numFishPerAge.get(age)+1);
            }
            for (int day = 1; day <= DAYS_TO_RUN_SIM; day++) {
                step();
            }
            System.out.println("Total: " + calcTotal());
        }
        catch (Exception e) {
        }
    }

    private static void step() {
        calcGrowthRates();
        Hashtable<Integer, Long> copy = new Hashtable<>(numFishPerAge);
        Enumeration<Integer> ageGroups = copy.keys();
        while (ageGroups.hasMoreElements()) {
            int timer = ageGroups.nextElement();
            long numCurMembers = copy.get(timer);
            long fPrime = growthRatePerAge.get(timer);
            long numFish = numCurMembers + fPrime;
            if (numFish < 0) {
                numFish = 0;
            }
            numFishPerAge.put(timer, numFish);
        }
    }

    private static void calcGrowthRates() {
        Enumeration<Integer> ageGroups = numFishPerAge.keys();
        while (ageGroups.hasMoreElements()) {
            int timer = ageGroups.nextElement();
            long numJoining;
            if (timer == MAX_TIMER) {
                numJoining = numFishPerAge.get(0);
            }
            else if (timer == 6) {
                numJoining = numFishPerAge.get(0) + numFishPerAge.get(7);
            }
            else {
                numJoining = numFishPerAge.get(timer+1);
            }
            long numLeaving = numFishPerAge.get(timer);
            long fPrime = numJoining - numLeaving;
            growthRatePerAge.put(timer, fPrime);
        }
    }

    private static long calcTotal() {
        long total = 0;
        Enumeration<Integer> ageGroups = numFishPerAge.keys();
        while (ageGroups.hasMoreElements()) {
            int timer = ageGroups.nextElement();
            total += numFishPerAge.get(timer);
        }
        return total;
    }
}
