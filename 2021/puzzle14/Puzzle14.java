import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.Collection;
import java.util.Collections;

public class Puzzle14 {
    static Hashtable<String, ArrayList<String>> productions;
    static ArrayList<String> allPairTypes;
    static Hashtable<String, Long> pairCounts;
    static Hashtable<String, Long> growthRates;
    static Hashtable<Character, Long> elementCounts;
    static final int NUM_PAIR_INSERTIONS = 10;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            readManual(filename);
            for (int step = 1; step <= NUM_PAIR_INSERTIONS; step++) {
                step();
            }
            System.out.println(calcRangeOfOccurrences());
        }
        catch (Exception e) {
        }
    }

    private static void step() {
        calcPrimes();
        Hashtable<String, Long> copy = new Hashtable<>(pairCounts);
        for (String pair : allPairTypes) {
            long count = copy.get(pair);    // pairs
            long prime = growthRates.get(pair);    // pairs/step
            pairCounts.put(pair, count + prime);
        }
    }
    
    private static void calcPrimes() {
        for (String pair : allPairTypes) {
            // count the existing pairs that will make this pair
            long inFlow = 0;
            for (String type : allPairTypes) {
                long count = pairCounts.get(type);
                if (count > 0) {
                    ArrayList<String> results = productions.get(type);
                    if (results.contains(pair)) {
                        inFlow += count;
                    }
                }
            }
            long outFlow = pairCounts.get(pair);
            long prime = inFlow - outFlow;
            growthRates.put(pair, prime);
        }
    }
    
    private static long calcRangeOfOccurrences() {
        countElements();
        Collection<Long> counts = elementCounts.values();
        return Collections.max(counts) - Collections.min(counts);
    }

    private static void countElements() {
        for (String pair : allPairTypes) {
            long pairCount = pairCounts.get(pair);
            for (int i = 0; i < 2; i++) {
                Character element = pair.charAt(i);
                elementCounts.put(element, elementCounts.get(element) + pairCount);
            }
        }
        // because the pairs overlap, we have to divide the counts by 2
        Enumeration<Character> elements = elementCounts.keys();
        while (elements.hasMoreElements()) {
            Character e = elements.nextElement();
            long count = elementCounts.get(e);
            // divide by 2 if count is even
            if (count % 2 == 0) {
                elementCounts.put(e, count/2);
            }
            // divide by 2 and add 1 if count is odd
            else {
                elementCounts.put(e, count/2 + 1);
            }
        }
    }

    private static void readManual(String filename) throws Exception {
        File file = new File(filename);
        Scanner stdin = new Scanner(file);
        String polymerTemplate = stdin.nextLine();
        stdin.nextLine();
        productions = new Hashtable<>();
        elementCounts = new Hashtable<>();
        while (stdin.hasNextLine()) {
            String[] parts = stdin.nextLine().split(" -> ");
            String pair = parts[0];
            Character element = parts[1].charAt(0);
            if (!elementCounts.containsKey(element)) {
                elementCounts.put(element, 0L);
            }
            String newPair1 = pair.charAt(0) + element.toString();
            String newPair2 = element.toString() + pair.charAt(1);
            ArrayList<String> results = new ArrayList<>();
            results.add(newPair1);
            results.add(newPair2);
            productions.put(pair, results);
        }
        stdin.close();
        pairCounts = new Hashtable<>();
        growthRates = new Hashtable<>();
        allPairTypes = Collections.list(productions.keys());
        for (String pair : allPairTypes) {
            pairCounts.put(pair, 0L);
            growthRates.put(pair, 0L);
        }
        for (int i = 0; i < polymerTemplate.length()-1; i++) {
            String pair = polymerTemplate.substring(i, i+2);
            pairCounts.put(pair, pairCounts.get(pair)+1);
        }
    }
}
