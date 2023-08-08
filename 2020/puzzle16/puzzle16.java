import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Collections;

public class puzzle16 {
    static ArrayList<String> allFields;
    static Hashtable<String, String[]> validRangesForEachTicketField;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            ArrayList<String> rules = readRules(stdin);
            stdin.nextLine();   //read past 'your ticket:' line
            String[] myTicket = stdin.nextLine().split(",");
            stdin.nextLine();   //skip blank line
            stdin.nextLine();   //read past 'nearby tickets:' line
            ArrayList<String[]> nearbyTickets = readNearbyTickets(stdin);
            stdin.close();

            parseRules(rules);
            printPart1Answer(nearbyTickets);
            printPart2Answer(nearbyTickets, myTicket);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private static void printPart1Answer(ArrayList<String[]> nearbyTickets) {
        int errorRate = 0;
        for (String[] ticket : nearbyTickets) {
            for (String valueStr : ticket) {
                int value = Integer.parseInt(valueStr);
                if (!valueIsValid(value)) {
                    errorRate += value;
                }
            }
        }
        System.out.println("Error rate = " + errorRate);
    }

    private static void printPart2Answer(ArrayList<String[]> nearbyTickets, String[] myTicket) {
        ArrayList<String[]> validTickets = new ArrayList<>();
        for (String[] ticket : nearbyTickets) {
            if (ticketIsValid(ticket)) {
                validTickets.add(ticket);
            }
        }

        String[] orderedFields = getOrderOfFields(validTickets);
        /*
        System.out.println("Order of fields:");
        for (int i = 0; i < orderedFields.length; i++) {
            System.out.println(orderedFields[i]);
        }
        */

        long product = 1;
        for (int i = 0; i < orderedFields.length; i++) {
            String field = orderedFields[i];
            if (field.startsWith("departure")) {
                int value = Integer.parseInt(myTicket[i]);
                product *= value;
            }
        }
        System.out.println("Product = " + product);
    }

    private static boolean ticketIsValid(String[] ticket) {
        for (String valueStr : ticket) {
            int value = Integer.parseInt(valueStr);
            if (!valueIsValid(value)) {
                return false;
            }
        }
        return true;
    }

    private static boolean valueIsValid(int value) {
        for (String field : allFields) {
            if (valueIsValidForThisField(value, field)) {
                return true;
            }
        }
        return false;
    }

    private static boolean valueIsValidForThisField(int value, String field) {
        String[] ranges = validRangesForEachTicketField.get(field);
        for (String range : ranges) {
            String[] rangeBounds = range.split("-");
            int lowerBound = Integer.parseInt(rangeBounds[0]);
            int upperBound = Integer.parseInt(rangeBounds[1]);
            if (value >= lowerBound && value <= upperBound) {
                return true;
            }
        }
        return false;
    }

    private static String[] getOrderOfFields(ArrayList<String[]> tickets) {
        String[] orderedFields = new String[allFields.size()];
        Hashtable<Integer, ArrayList<String>> possibleFieldsForEachPosition = new Hashtable<>();
        int pos = 0;
        ArrayList<String> fields = new ArrayList<>(allFields);
        while (!fields.isEmpty()) {
            ArrayList<String> choices = null;
            // don't recalculate choices
            if (possibleFieldsForEachPosition.containsKey(pos)) {
                choices = possibleFieldsForEachPosition.get(pos);
            }
            else {
                choices = getPossibleFieldsForThisPosition(pos, tickets, fields);
                possibleFieldsForEachPosition.put(pos, choices);
            }
            if (choices.size() == 1) {
                String field = choices.get(0);
                orderedFields[pos] = field;
                fields.remove(field);
                // narrow down the choices for the other positions
                for (int i = 0; i < orderedFields.length; i++) {
                    if (i != pos && possibleFieldsForEachPosition.containsKey(i)) {
                        possibleFieldsForEachPosition.get(i).remove(field);
                    }
                }
            }
            pos++;
            if (pos > orderedFields.length - 1) {
                pos = 0;
            }
        }
        return orderedFields;
    }

    private static ArrayList<String> getPossibleFieldsForThisPosition(int pos, ArrayList<String[]> tickets, ArrayList<String> fields) {
        ArrayList<String> possibleFields = new ArrayList<>();
        for (String field : fields) {
            if (allTicketsValidForThisField(pos, field, tickets)) {
                possibleFields.add(field);
            }
        }
        return possibleFields;
    }

    private static boolean allTicketsValidForThisField(int pos, String field, ArrayList<String[]> tickets) {
        for (String[] ticket : tickets) {
            int value = Integer.parseInt(ticket[pos]);
            if (!valueIsValidForThisField(value, field)) {
                return false;
            }
        }
        return true;
    }
    
    private static ArrayList<String> readRules(Scanner stdin) {
        ArrayList<String> rules = new ArrayList<>();
        String line = stdin.nextLine();
        while (!line.isEmpty()) {
            rules.add(line);
            line = stdin.nextLine();
        }
        return rules;
    }

    private static ArrayList<String[]> readNearbyTickets(Scanner stdin) {
        ArrayList<String[]> nearbyTickets = new ArrayList<>();
        while (stdin.hasNextLine()) {
            String[] ticket = stdin.nextLine().split(",");
            nearbyTickets.add(ticket);
        }
        return nearbyTickets;
    }

    private static void parseRules(ArrayList<String> rules) {
        allFields = new ArrayList<>();
        validRangesForEachTicketField = new Hashtable<>();
        for (String rule : rules) {
            String[] parts = rule.split(": ");
            String field = parts[0];
            allFields.add(field);
            String[] ranges = parts[1].split(" or ");
            validRangesForEachTicketField.put(field, ranges);
        }
        //allFields = Collections.list(validRangesForEachTicketField.keys());
    }
}
