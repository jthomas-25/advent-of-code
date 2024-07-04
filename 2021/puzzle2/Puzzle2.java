import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;

public class Puzzle2 {
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            ArrayList<String> commands = new ArrayList<>();
            while (stdin.hasNextLine()) {
                commands.add(stdin.nextLine());
            }
            stdin.close();
            Submarine sub = new Submarine();
            part1(commands, sub);
            sub.setXPos(0);
            sub.setDepth(0);
            part2(commands, sub);
        }
        catch (Exception e) {
        }
    }

    private static void part1(ArrayList<String> commands, Submarine sub) {
        for (String command : commands) {
            String[] parts = command.split(" ");
            String dir = parts[0];
            int delta = Integer.parseInt(parts[1]);
            switch (dir) {
                case "forward":
                    sub.moveForward(delta);
                    break;
                case "down":
                    sub.moveDown(delta);
                    break;
                case "up":
                    sub.moveUp(delta);
                    break;
            }
        }
        System.out.println(sub.getXPos() * sub.getDepth());
    }
    
    private static void part2(ArrayList<String> commands, Submarine sub) {
        for (String command : commands) {
            String[] parts = command.split(" ");
            String dir = parts[0];
            int delta = Integer.parseInt(parts[1]);
            switch (dir) {
                case "forward":
                    sub.moveForward(delta);
                    sub.moveDown(sub.getAim() * delta);
                    break;
                case "down":
                    sub.increaseAim(delta);
                    break;
                case "up":
                    sub.decreaseAim(delta);
                    break;
            }
        }
        System.out.println(sub.getXPos() * sub.getDepth());
    }
}
