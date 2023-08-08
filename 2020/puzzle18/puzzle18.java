import java.util.Scanner;
import java.io.File;
import java.util.Hashtable;
import java.util.ArrayList;
import java.util.Stack;
import java.util.EmptyStackException;

public class puzzle18 {
    static Hashtable<String, Integer> precedences;
    static final String ADD = "+";
    static final String MUL = "*";
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File file = new File(filename);
            Scanner stdin = new Scanner(file);
            precedences = new Hashtable<>();
            part1(stdin);
            stdin = new Scanner(file);
            part2(stdin);
        }
        catch (Exception e) {
        }
    }

    private static void part1(Scanner stdin) {
        precedences.put(ADD, 1);
        precedences.put(MUL, 1);
        System.out.println(sumResults(stdin));
    }
    
    private static void part2(Scanner stdin) {
        precedences.put(ADD, 2);
        precedences.put(MUL, 1);
        System.out.println(sumResults(stdin));
    }
    
    private static long sumResults(Scanner stdin) {
        long sum = 0;
        while (stdin.hasNextLine()) {
            String expression = stdin.nextLine();
            //System.out.println(expression);
            ArrayList<String> parts = new ArrayList<>();
            convertToRPN(expression, parts);
            sum += evaluateExpression(parts);
        }
        stdin.close();
        return sum;
    }

    private static long evaluateExpression(ArrayList<String> parts) {
        Stack<Long> stack = new Stack<>();
        for (int i = 0; i < parts.size(); i++) {
            try {
                long value = Long.parseLong(parts.get(i));
                stack.push(value);
            }
            catch (NumberFormatException e) {
                if (parts.get(i).equals(ADD)) {
                    long a = stack.pop();
                    long b = stack.pop();
                    stack.push(a + b);
                }
                else if (parts.get(i).equals(MUL)) {
                    long a = stack.pop();
                    long b = stack.pop();
                    stack.push(a * b);
                }
            }
        }
        long result = stack.pop();
        //System.out.println(result);
        return result;
    }

    private static void convertToRPN(String expression, ArrayList<String> parts) {
        // Convert the expression to Reverse Polish Notation using the shunting-yard algorithm
        Stack<String> opstack = new Stack<>();
        int i = 0;
        while (i < expression.length()) {
            String c = expression.substring(i,i+1);
            try {
                long value = Long.parseLong(c);
                parts.add(c);
                i++;
            }
            catch (NumberFormatException e1) {
                if (c.equals(" ")) {
                    i++;
                    continue;
                }
                if (c.equals(ADD) || c.equals(MUL)) {
                    String op1 = c;
                    try {
                        String op2 = opstack.peek();
                        while (!op2.equals("(") && precedences.get(op2) >= precedences.get(op1)) {
                            parts.add(opstack.pop());
                            op2 = opstack.peek();
                        }
                    }
                    catch (EmptyStackException e2) {
                    }
                    opstack.push(op1);
                    i++;
                }
                else if (c.equals("(")) {
                    opstack.push(c);
                    i++;
                }
                else if (c.equals(")")) {
                    while (!opstack.peek().equals("(")) {
                        parts.add(opstack.pop());
                    }
                    opstack.pop();
                    i++;
                }
            }
        }
        while (!opstack.empty()) {
            String operator = opstack.pop();
            parts.add(operator);
        }
        //printExpr(parts);
    }

    private static void printExpr(ArrayList<String> parts) {
        for (String part : parts) {
            System.out.print(part + " ");
        }
        System.out.println();
    }
}
