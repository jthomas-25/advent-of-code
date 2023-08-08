import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class puzzle23 {
    static ArrayList<Cup> initialCupConfig;
    static ArrayList<Cup> cups;
    static final int LOWEST_LABEL = 1;
    static int highestLabel;

    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            initialCupConfig = new ArrayList<>();
            String cupLabelsStartingOrder = stdin.nextLine();
            for (int i = 0; i < cupLabelsStartingOrder.length(); i++) {
                int label = Integer.parseInt(cupLabelsStartingOrder.substring(i,i+1));
                Cup cup = new Cup(label);
                initialCupConfig.add(cup);
            }
            stdin.close();

            System.out.println("Part One: ");
            printPart1Answer();
            System.out.println("Part Two: ");
            printPart2AnswerQuickVersion(); //Quick as in "already played the game and calculated the labels"

        } catch (Exception e) {
        }
    }

    private static void printPart1Answer() {
        highestLabel = 9;
        cups = new ArrayList<>(initialCupConfig);
        playGame(100);
        String endingOrderOfCupLabels = getEndingOrderOfCupLabels();
        System.out.println(endingOrderOfCupLabels);
    }

    private static void printPart2AnswerFullVersion() {
        //WARNING: Takes a looooooooong time
        highestLabel = 1000000;
        cups = new ArrayList<>(initialCupConfig);
        for (int i = 10; i <= highestLabel; i++) {
            int label = i;
            Cup cup = new Cup(label);
            cups.add(cup);
        }
        playGame(10000000);
        int indexOfCupLabel1 = getIndexOfCupLabel(1);
        Cup c1 = cups.get((indexOfCupLabel1 + 1) % cups.size());
        Cup c2 = cups.get((indexOfCupLabel1 + 2) % cups.size());
        int label1 = c1.getLabel();
        int label2 = c2.getLabel();
        long product = (long)label1 * (long)label2;
        System.out.println("1st cup clockwise of cup 1: " + label1);
        System.out.println("2nd cup clockwise of cup 1: " + label2);
        System.out.println("Label of 1st cup * Label of 2nd cup = " + product);
    }
    
    private static void printPart2AnswerQuickVersion() {
        int label1 = 257760;
        int label2 = 163997;
        long product = (long)label1 * (long)label2;
        System.out.println("1st cup clockwise of cup 1: " + label1);
        System.out.println("2nd cup clockwise of cup 1: " + label2);
        System.out.println("Label of 1st cup * Label of 2nd cup = " + product);
    }

    private static void playGame(int numMoves) {
        Cup currentCup = cups.get(0);
        for (int moveNum = 1; moveNum <= numMoves; moveNum++) {
            Cup[] cupsClockwiseOfCurrentCup = new Cup[3];
            Integer currentCupPos = null;
            for (int i = 1; i <= cupsClockwiseOfCurrentCup.length; i++) {
                currentCupPos = cups.indexOf(currentCup);
                cupsClockwiseOfCurrentCup[i-1] = cups.remove((currentCupPos + 1) % cups.size());
            }
            Cup destCup = null;
            int destCupLabel = currentCup.getLabel()-1;
            if (destCupLabel < LOWEST_LABEL)
                destCupLabel = highestLabel;
            boolean cupNotPickedUp = false;
            while (!cupNotPickedUp) {
                boolean destCupAlreadyPickedUp = false;
                for (Cup cupJustPickedUp : cupsClockwiseOfCurrentCup) {
                   if (cupJustPickedUp.getLabel() == destCupLabel) {
                      destCupLabel--;
                      if (destCupLabel < LOWEST_LABEL)
                          destCupLabel = highestLabel;
                      destCupAlreadyPickedUp = true;
                      break;
                   }
                }
                if (!destCupAlreadyPickedUp)
                    cupNotPickedUp = true;
            }
            for (Cup c : cups) {
                if (c.getLabel() == destCupLabel) {
                    destCup = c;
                    break;
                }
            }
            int destCupPos = cups.indexOf(destCup);
            int[] positionsToPlaceCups = new int[cupsClockwiseOfCurrentCup.length];
            for (int i = 1; i <= cupsClockwiseOfCurrentCup.length; i++) {
                Cup cupJustPickedUp = cupsClockwiseOfCurrentCup[i-1];
                int posToPlace = destCupPos + i;
                positionsToPlaceCups[i-1] = posToPlace;
            }
            for (int i = 1; i <= cupsClockwiseOfCurrentCup.length; i++) {
                int posToPlace = positionsToPlaceCups[i-1];
                Cup cupJustPickedUp = cupsClockwiseOfCurrentCup[i-1];
                cups.add(posToPlace, cupJustPickedUp);
            }
            currentCupPos = cups.indexOf(currentCup);
            currentCup = cups.get((currentCupPos + 1) % cups.size());
        }
    }

    private static int getIndexOfCupLabel(int cupLabel) {
        for (int i = 0; i < cups.size(); i++) {
            Cup cup = cups.get(i);
            if (cup.getLabel() == 1)
                return i;
        }
        return -1;
    }

    private static String getEndingOrderOfCupLabels() {
        String endingOrder = "";
        int indexOfCupLabel1 = getIndexOfCupLabel(1);
        for (int i = 1; i <= cups.size()-1; i++) {
            Cup c = cups.get((indexOfCupLabel1 + i) % cups.size());
            endingOrder += c.getLabel();
        }
        return endingOrder;
    }
}

class Cup {
    private int label;

    Cup(int label) {
        this.label = label;
    }

    int getLabel() { return this.label; }
}
