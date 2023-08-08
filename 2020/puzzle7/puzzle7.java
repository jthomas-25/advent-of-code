import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.HashMap;

public class puzzle7 {
    static final String MY_BAG_COLOR = "shiny gold";
    static HashMap<String, String[]> requiredContents;

    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            requiredContents = new HashMap<>();
            while (stdin.hasNextLine()) {
                String rule = stdin.nextLine();
                String[] ruleParts = rule.split(" bags contain ");
                String bagColor = ruleParts[0];
                String bagContents = ruleParts[1];
                String[] targetStrings = {" bags", " bag", "."};
                for (String s : targetStrings)
                    bagContents = bagContents.replace(s, "");
                String[] contents = bagContents.split(", ");
                if (contents[0].equals("no other"))
                    requiredContents.put(bagColor, null);
                else
                    requiredContents.put(bagColor, contents);
            }
            stdin.close();
            
            ArrayList<Bag> bags = new ArrayList<>();
            Iterator<String> bagColors = requiredContents.keySet().iterator();
            while (bagColors.hasNext()) {
                String color = bagColors.next();
                Bag bag = new Bag(color);
                String[] contents = requiredContents.get(color);
                if (contents != null)
                    bag.fill(contents);
                bags.add(bag);
            }

            System.out.println("Part One:");
            printPart1Answer(bags);
            System.out.println("Part Two:");
            printPart2Answer(bags);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printPart1Answer(ArrayList<Bag> bags) {
        int numBagsContainingShinyGoldBag = 0;
        for (Bag b : bags) {
            if (b.containsBag(b.getContents(), MY_BAG_COLOR))
                numBagsContainingShinyGoldBag++;
        }
        System.out.println(numBagsContainingShinyGoldBag);
    }

    private static void printPart2Answer(ArrayList<Bag> bags) {
        int numBagsInsideMyBag = 0;
        for (Bag b : bags) {
            if (b.getColor().equals(MY_BAG_COLOR)) {
                numBagsInsideMyBag = b.getSize(b.getContents(), MY_BAG_COLOR);
                break;
            }
        }
        System.out.println(numBagsInsideMyBag);
    }
}

abstract class Container {

    String color;
    ArrayList<Container> contents;
    
    abstract int getSize(ArrayList<Container> contents, String color);
    
    abstract String getColor();
    
    abstract ArrayList<Container> getContents();

    abstract void fill(String[] contents);

}

class Bag extends Container {

    Bag(String color) {
        this.color = color;
        this.contents = new ArrayList<>();
    }

    int getSize(ArrayList<Container> contents, String color) {
        //System.out.println("COLOR: " + this.color);
        int size = 0;
        String[] requiredContents = puzzle7.requiredContents.get(this.color);
        if (requiredContents != null) {
            for (int i = 0; i < requiredContents.length; i++) {
                int groupSize = Integer.parseInt(requiredContents[i].substring(0,1));
                String bagColor = requiredContents[i].substring(2);
                for (Container c : contents) {
                    if (c.getColor().equals(bagColor)) {
                        size += groupSize;
                        String[] innerBagContents = puzzle7.requiredContents.get(bagColor);
                        if (innerBagContents != null) {
                            int bagSize = groupSize * c.getSize(c.getContents(), bagColor);
                            //System.out.println(bagSize);
                            size += bagSize;
                        }
                    }
                }
            }
        }
        return size;
    }

    String getColor() { return this.color; }

    ArrayList<Container> getContents() { return this.contents; }

    boolean containsBag(ArrayList<Container> contents, String color) {
        boolean inThisBag = false;
        for (Container c : contents) {
            //System.out.println("BAG: " + c.getColor());
            if (c.getColor().equals(color)) {
                inThisBag = true;
                break;
            } else {
                Bag bagToCheck = (Bag)c;
                inThisBag = bagToCheck.containsBag(bagToCheck.getContents(), color);
                if (inThisBag)
                    break;
            }
        }
        return inThisBag;
    }

    void fill(String[] contents) {
        if (contents != null) {
            for (int i = 0; i < contents.length; i++) {
                int groupSize = Integer.parseInt(contents[i].substring(0,1));
                String bagColor = contents[i].substring(2);
                Bag bag = new Bag(bagColor);
                this.contents.add(bag);
                //System.out.println("\tCOLOR OF BAG IN THIS BAG: " + bagColor);
            }
            
            for (Container c : this.contents) {
                String[] bagContents = puzzle7.requiredContents.get(c.getColor());
                if (bagContents != null)
                    c.fill(bagContents);
            }
        }
    }
}
