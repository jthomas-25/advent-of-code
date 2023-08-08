import java.util.Scanner;
import java.io.File;
import java.util.Hashtable;

public class puzzle5 {
    static Hashtable<Integer, String> passesById;
    static int largestId;

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
            String[] boardingPasses = new String[i];
            stdin = new Scanner(inputFile);
            for (i = 0; i < boardingPasses.length; i++)
                boardingPasses[i] = stdin.nextLine();
            stdin.close();

            System.out.println("Part One:");
            printPart1Answer(boardingPasses);
            System.out.println("Part Two:");
            printPart2Answer();
                
        } catch (Exception e) {
        }
    }

    private static void printPart1Answer(String[] boardingPasses) {
        largestId = findLargestId(boardingPasses);
        System.out.println("Highest seat ID: " + largestId);
    }

    private static void printPart2Answer() {
        for (int id = 9; id < largestId; id++) {
            String pass = passesById.get(id);
            if (pass == null) {
                if (passesById.get(id-1) != null && passesById.get(id+1) != null) {
                    System.out.println("My seat ID: " + id);
                    break;
                }
            }
        }
    }

    private static int findLargestId(String[] boardingPasses) {
        passesById = new Hashtable<>();
        String pass = boardingPasses[0];
        int row = calcRow(pass);
        int col = calcColumn(pass);
        int seatId = row * 8 + col;
        int largestId = seatId;
        passesById.put(seatId, pass);
        for (int i = 1; i < boardingPasses.length; i++) {
            pass = boardingPasses[i];
            row = calcRow(pass);
            col = calcColumn(pass);
            seatId = row * 8 + col;
            passesById.put(seatId, pass);
            if (seatId > largestId)
                largestId = seatId;
            //System.out.println(String.format("%s: row %d, column %d, seat ID %d", pass, row, col, seatId));
        }
        return largestId;
    }

    private static int calcRow(String pass) {
        int minRow = 0, maxRow = 127, row = 0;
        for (int i = 0; i < 7; i++) {
            char letter = pass.charAt(i);
            if (letter == 'F') {
                maxRow = (minRow + maxRow) / 2; //take lower half
                if (i == 6)
                    row = minRow;
            } else if (letter == 'B') {
                minRow = (minRow + maxRow)/2 + 1;   //take upper half
                if (i == 6)
                    row = maxRow;
            }
        }
        return row;
    }
    
    private static int calcColumn(String pass) {
        int minCol = 0, maxCol = 7, col = 0;
        for (int i = 7; i < 10; i++) {
            char letter = pass.charAt(i);
            if (letter == 'L') {
                maxCol = (minCol + maxCol) / 2; //take lower half
                if (i == 9)
                    col = minCol;
            } else if (letter == 'R') { //take upper half
                minCol = (minCol + maxCol)/2 + 1;
                if (i == 9)
                    col = maxCol;
            }
        }
        return col;
    }
}
