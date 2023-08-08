import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Enumeration;

public class puzzle8 {
    static int accumulator;
    static Hashtable<Integer, Boolean> instructionsExecuted;

    public static void main(String[] args) {
        try {
            String filename = args[0];
            File bootCodeFile = new File(filename);
            Scanner stdin = new Scanner(bootCodeFile);
            ArrayList<String> instructions = new ArrayList<>();
            instructionsExecuted = new Hashtable<>();
            int index = 0;
            while (stdin.hasNextLine()) {
                String instruction = stdin.nextLine();
                instructions.add(instruction);
                instructionsExecuted.put(index, false);
                index++;
            }
            stdin.close();
            
            System.out.println("Part One:");
            printPart1Answer(instructions);
            System.out.println("Part Two:");
            printPart2Answer(instructions);

        } catch (Exception e) {
        }
    }

    private static void printPart1Answer(ArrayList<String> instructions) {
        runBootCode(instructions);
        System.out.println("ACCUM: " + accumulator);
    }
    
    private static void printPart2Answer(ArrayList<String> instructions) {
        Hashtable<Integer, String> nopAndJmpInsts = new Hashtable<>();
        for (int i = 0; i < instructions.size(); i++) {
            String instruction = instructions.get(i);
            String opcode = instruction.substring(0,3);
            if (opcode.equals("nop") || opcode.equals("jmp"))
                nopAndJmpInsts.put(i, instruction);
        }
        boolean hasTerminated = false;
        Enumeration<Integer> instIndexes = nopAndJmpInsts.keys();
        while (instIndexes.hasMoreElements()) {
            do {
                int index = instIndexes.nextElement();
                String oldInst = nopAndJmpInsts.get(index);
                String opcode = oldInst.substring(0,3);
                if (opcode.equals("nop")) {
                    opcode = "jmp";
                } else if (opcode.equals("jmp")) {
                    opcode = "nop";
                }
                String newInst = opcode + oldInst.substring(3);
                instructions.set(index, newInst);
                for (int i = 0; i < instructions.size(); i++) {
                    if (instructionsExecuted.get(i))
                        instructionsExecuted.put(i, false);
                }
                hasTerminated = runBootCode(instructions);
                instructions.set(index, oldInst);
            } while (!hasTerminated);
            break;
        }
        System.out.println("ACCUM: " + accumulator);
    }
    
    private static boolean runBootCode(ArrayList<String> instructions) {
        accumulator = 0;
        int index = 0;
        boolean hasTerminated = false;
        do {
            String instruction = instructions.get(index);
            if (instructionsExecuted.get(index))
                break;//return false;
            instructionsExecuted.put(index, true);
            String instParts[] = instruction.split(" ");
            String operation = instParts[0];
            int argument = Integer.parseInt(instParts[1]);
            //System.out.println(operation + " " + argument);
            switch (operation) {
                case "acc":
                    accumulator += argument;
                    if (index+1 == instructions.size()) {
                        hasTerminated = true;
                    } else {
                        instruction = instructions.get(index+1);
                        index++;
                    }
                    break;
                case "jmp":
                    if (index + argument == instructions.size()) {
                        hasTerminated = true;
                    } else {
                        instruction = instructions.get(index+argument);
                        index += argument;
                    }
                    break;
                case "nop":
                    if (index+1 == instructions.size()) {
                        hasTerminated = true;
                    } else {
                        instruction = instructions.get(index+1);
                        index++;
                    }
                    break;
            }
        } while (!hasTerminated);
        return hasTerminated;//return true;
    }
}
