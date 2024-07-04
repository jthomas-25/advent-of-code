import java.util.Scanner;
import java.io.File;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Stack;
import java.util.ArrayList;
import java.util.Collections;

public class Puzzle10 {
    static LinkedList<String> lines;
    static Hashtable<Character, Character> matchingChars;
    static final char L_PAREN = '(', R_PAREN = ')';
    static final char L_SQUARE = '[', R_SQUARE = ']';
    static final char L_CURLY = '{', R_CURLY = '}';
    static final char L_ANGLE = '<', R_ANGLE = '>';
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            lines = new LinkedList<>();
            while (stdin.hasNextLine()) {
                lines.add(stdin.nextLine());
            }
            stdin.close();
            matchingChars = setupRules();
            printPart1Answer();
            printPart2Answer();
        }
        catch (Exception e) {
        }
    }
    
    private static void printPart1Answer() {
        Hashtable<Character, Integer> errorScoreTable = setupErrorScoreTable();
        int totalScore = 0;
        Iterator<String> itr = lines.iterator();
        while (itr.hasNext()) {
            String line = itr.next();
            Character firstIllegalChar = getFirstIllegalChar(line);
            if (firstIllegalChar != null) {
                totalScore += errorScoreTable.get(firstIllegalChar);
                itr.remove();
            }
        }
        System.out.println(totalScore);
    }
    
    private static Character getFirstIllegalChar(String line) {
        boolean corrupted = false;
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            switch (c) {
                case L_PAREN:
                case L_SQUARE:
                case L_CURLY:
                case L_ANGLE:
                    stack.push(c);
                    break;
                case R_PAREN:
                case R_SQUARE:
                case R_CURLY:
                case R_ANGLE:
                    corrupted = (stack.pop() != matchingChars.get(c));
                    break;
                    
            }
            if (corrupted) {
                return c;
            }
        }
        return null;
    }
    
    private static void printPart2Answer() {
        Hashtable<Character, Integer> scoreTable = setupCompletionScoreTable();
        ArrayList<Long> scores = new ArrayList<>();
        for (String line : lines) {
            String completionStr = getCompletionString(line);
            long totalScore = 0;
            for (int i = 0; i < completionStr.length(); i++) {
                totalScore = 5 * totalScore + scoreTable.get(completionStr.charAt(i));
            }
            scores.add(totalScore);
        }
        Collections.sort(scores);
        System.out.println(scores.get(scores.size()/2));
    }

    private static String getCompletionString(String line) {
        String completionStr = "";
        Stack<Character> stack = new Stack<>();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            switch (c) {
                case L_PAREN:
                case L_SQUARE:
                case L_CURLY:
                case L_ANGLE:
                    stack.push(c);
                    break;
                case R_PAREN:
                case R_SQUARE:
                case R_CURLY:
                case R_ANGLE:
                    stack.pop();
                    break;
            }
        }
        while (!stack.isEmpty()) {
            char openingChar = stack.pop();
            completionStr += matchingChars.get(openingChar);
        }
        return completionStr;
    }

    private static Hashtable<Character, Character> setupRules() {
        Hashtable<Character, Character> matchingChars = new Hashtable<>();
        matchingChars.put(L_PAREN, R_PAREN);
        matchingChars.put(L_SQUARE, R_SQUARE);
        matchingChars.put(L_CURLY, R_CURLY);
        matchingChars.put(L_ANGLE, R_ANGLE);
        matchingChars.put(R_PAREN, L_PAREN);
        matchingChars.put(R_SQUARE, L_SQUARE);
        matchingChars.put(R_CURLY, L_CURLY);
        matchingChars.put(R_ANGLE, L_ANGLE);
        return matchingChars;
    }

    private static Hashtable<Character, Integer> setupErrorScoreTable() {
        Hashtable<Character, Integer> scoreTable = new Hashtable<>();
        scoreTable.put(R_PAREN, 3);
        scoreTable.put(R_SQUARE, 57);
        scoreTable.put(R_CURLY, 1197);
        scoreTable.put(R_ANGLE, 25137);
        return scoreTable;
    }
    
    private static Hashtable<Character, Integer> setupCompletionScoreTable() {
        Hashtable<Character, Integer> scoreTable = new Hashtable<>();
        scoreTable.put(R_PAREN, 1);
        scoreTable.put(R_SQUARE, 2);
        scoreTable.put(R_CURLY, 3);
        scoreTable.put(R_ANGLE, 4);
        return scoreTable;
    }
}
