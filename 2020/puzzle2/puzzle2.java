import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class puzzle2 {
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            ArrayList<String> policies = new ArrayList<>();
            ArrayList<String> passwords = new ArrayList<>();
            while (stdin.hasNextLine()) {
                String[] parts = stdin.nextLine().split(": ");
                String policy = parts[0];
                policies.add(policy);
                String password = parts[1];
                passwords.add(password);
            }
            stdin.close();
            
            System.out.println("Part One:");
            printPart1Answer(policies, passwords);
            System.out.println("Part Two:");
            printPart2Answer(policies, passwords);

        } catch (Exception e) {
        }
    }

    private static void printPart1Answer(ArrayList<String> policies, ArrayList<String> passwords) {
        int numValidPasswords = 0;
        for (int i = 0; i < policies.size(); i++) {
            String policy = policies.get(i);
            String[] parts = policy.split(" ");
            String[] instanceBounds = parts[0].split("-");
            int minInstances = Integer.parseInt(instanceBounds[0]);
            int maxInstances = Integer.parseInt(instanceBounds[1]);
            String letterToFind = parts[1];
            String password = passwords.get(i);
            int numInstances = 0;
            for (int j = 0; j < password.length(); j++) {
                String letter = password.substring(j, j+1);
                if (letter.equals(letterToFind))
                    numInstances++;
            }
            if (numInstances >= minInstances && numInstances <= maxInstances) {
                numValidPasswords++;
                //System.out.println(String.format("%s: %s", policy, password));
            }
        }
        System.out.println("Number of valid passwords: " + numValidPasswords);
    }

    private static void printPart2Answer(ArrayList<String> policies, ArrayList<String> passwords) {
        int numValidPasswords = 0;
        for (int i = 0; i < policies.size(); i++) {
            String policy = policies.get(i);
            String[] parts = policy.split(" ");
            String[] positions = parts[0].split("-");
            int pos1 = Integer.parseInt(positions[0]);
            int pos2 = Integer.parseInt(positions[1]);
            String letterToFind = parts[1];
            String password = passwords.get(i);
            int numInstances = 0;
            for (int j = 1; j <= password.length(); j++) {
                if (j > pos2)
                    break;
                String letter = password.substring(j-1, j);
                if (letter.equals(letterToFind) && (j == pos1 || j == pos2))
                    numInstances++;
            }
            if (numInstances == 1) {
                numValidPasswords++;
                //System.out.println(String.format("%s: %s", policy, password));
            }
        }
        System.out.println("Number of valid passwords: " + numValidPasswords);
    }
}
