import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;

public class puzzle6 {
    static final String QUESTIONS = "abcdefghijklmnopqrstuvwxyz";
    static final int NUM_QUESTIONS = 26;

    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            ArrayList<ArrayList<Person>> groups = new ArrayList<>();
            ArrayList<Person> group = null;
            String line = "";
            boolean done = false;
            do {
                if (!stdin.hasNextLine()) {
                    done = true;
                } else {
                    line = stdin.nextLine();
                    group = readGroupAnswers(stdin, line);
                    groups.add(group);
                }
            } while (!done);
            stdin.close();

            System.out.println("Part One:");
            printPart1Answer(groups);
            System.out.println("Part Two:");
            printPart2Answer(groups);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printPart1Answer(ArrayList<ArrayList<Person>> groups) {
        int sum = 0;
        for (ArrayList<Person> group : groups) {
            int groupCount = 0;
            for (int i = 0; i < NUM_QUESTIONS; i++) {
                String question = QUESTIONS.substring(i,i+1);
                boolean questionAnswered = false;
                for (Person person : group) {
                    if (person.getAnswer(question) != null) {
                        questionAnswered = true;
                        break;
                    }
                }
                if (questionAnswered)
                    groupCount++;
            }
            //System.out.println(groupCount);
            sum += groupCount;
        }
        System.out.println(sum);
    }
    
    private static void printPart2Answer(ArrayList<ArrayList<Person>> groups) {
        int sum = 0;
        for (ArrayList<Person> group : groups) {
            int groupCount = 0;
            for (int i = 0; i < NUM_QUESTIONS; i++) {
                String question = QUESTIONS.substring(i,i+1);
                boolean everyoneAnswered = true;
                for (Person person : group) {
                    if (person.getAnswer(question) == null) {
                        everyoneAnswered = false;
                        break;
                    }
                }
                if (everyoneAnswered)
                    groupCount++;
            }
            //System.out.println(groupCount);
            sum += groupCount;
        }
        System.out.println(sum);
    }

    private static ArrayList<Person> readGroupAnswers(Scanner stdin, String line) {
        ArrayList<Person> group = new ArrayList<>();
        do {
            Person person = parsePersonAnswers(line);
            group.add(person);
            if (!stdin.hasNextLine())
                break;
            line = stdin.nextLine();
        } while (!line.equals(""));
        return group;
    }

    private static Person parsePersonAnswers(String line) {
        Person p = new Person();
        for (int i = 0; i < line.length(); i++) {
            String question = line.substring(i,i+1);
            p.setAnswer(question, "yes");
        }
        return p;
    }
}

class Person {
    private Hashtable<String, String> answers;

    Person() {
        this.answers = new Hashtable<>();
    }
    
    String getAnswer(String question) { return this.answers.get(question); }

    void setAnswer(String question, String answer) { this.answers.put(question, answer); }
}
