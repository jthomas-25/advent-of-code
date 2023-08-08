import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Hashtable;
import java.util.Collections;

public class puzzle21 {
    static ArrayList<Food> foods;
    static ArrayList<String> allIngredients;
    static ArrayList<String> allAllergens;
    
    public static void main(String[] args) {
        try {
            String filename = args[0];
            File inputFile = new File(filename);
            Scanner stdin = new Scanner(inputFile);
            readFoodInfo(stdin);
            stdin.close();
            Hashtable<String, ArrayList<String>> possibleAllergensForEachDangerousIngredient = new Hashtable<>();
            printPart1Answer(possibleAllergensForEachDangerousIngredient);
            printPart2Answer(possibleAllergensForEachDangerousIngredient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printPart1Answer(Hashtable<String, ArrayList<String>> possibleAllergensForEachDangerousIngredient) {
        int totalAppearances = 0;
        Hashtable<String, Integer> counts = getCounts();
        for (String ingredient : allIngredients) {
            ArrayList<String> choices = getPossibleAllergensForThisIngredient(ingredient, counts);
            if (choices.isEmpty()) {
                totalAppearances += countAppearances(ingredient);
            }
            else {
                possibleAllergensForEachDangerousIngredient.put(ingredient, choices);
            }
        }
        System.out.println("Total appearances: " + totalAppearances);
    }

    private static void printPart2Answer(Hashtable<String, ArrayList<String>> possibleAllergensForEachDangerousIngredient) {
        Hashtable<String, String> allergensToIngredients = mapAllergensToIngredients(possibleAllergensForEachDangerousIngredient);
        Collections.sort(allAllergens);
        ArrayList<String> canonicalList = new ArrayList<>();
        for (String allergen : allAllergens) {
            canonicalList.add(allergensToIngredients.get(allergen));
        }
        String canonicalListStr = "";
        int i = 0;
        String ingredient = null;
        while (i < canonicalList.size() - 1) {
            ingredient = canonicalList.get(i);
            canonicalListStr += (ingredient + ",");
            i++;
        }
        ingredient = canonicalList.get(i);
        canonicalListStr += ingredient;
        System.out.println("Canonical dangerous ingredient list: " + canonicalListStr);
    }

    private static Hashtable<String, Integer> getCounts() {
        // given an ingredient and an allergen, return the number of times
        // the ingredient appears in foods that list the allergen
        Hashtable<String, Integer> counts = new Hashtable<>();
        // cartesian product of all ingredients and all allergens
        for (String ingredient : allIngredients) {
            for (String allergen : allAllergens) {
                String key = ingredient + "," + allergen;
                counts.put(key, 0);
            }
        }
        for (String ingredient : allIngredients) {
            for (String allergen : allAllergens) {
                for (Food food : foods) {
                    if (food.getIngredients().contains(ingredient) && food.getListedAllergens().contains(allergen)) {
                        String key = ingredient + "," + allergen;
                        counts.put(key, counts.get(key) + 1);
                    }
                }
            }
        }
        return counts;
    }
    
    private static ArrayList<String> getPossibleAllergensForThisIngredient(String ingredient, Hashtable<String, Integer> counts) {
        ArrayList<String> possibleAllergens = new ArrayList<>();
        for (String allergen : allAllergens) {
            if (ingredientPossiblyContainsAllergen(ingredient, allergen, counts)) {
                possibleAllergens.add(allergen);
            }
        }
        return possibleAllergens;
    }
    
    private static boolean ingredientPossiblyContainsAllergen(String ingredient, String allergen, Hashtable<String, Integer> counts) {
        String key = ingredient + "," + allergen;
        int numAppearancesInFoodsThatListAllergen = counts.get(key);
        int numFoodsThatListAllergen = countFoodsThatListAllergen(allergen);
        return numAppearancesInFoodsThatListAllergen == numFoodsThatListAllergen;
    }
    
    private static int countFoodsThatListAllergen(String allergen) {
        int count = 0;
        for (Food food : foods) {
            if (food.getListedAllergens().contains(allergen)) {
                count++;
            }
        }
        return count;
    }

    private static int countAppearances(String ingredient) {
        int total = 0;
        for (Food food : foods) {
            total += Collections.frequency(food.getIngredients(), ingredient);
        }
        return total;
    }
    
    private static Hashtable<String, String> mapAllergensToIngredients(Hashtable<String, ArrayList<String>> possibleAllergensForEachDangerousIngredient) {
        Hashtable<String, String> allergensToIngredients = new Hashtable<>();
        ArrayList<String> dangerousIngredients = Collections.list(possibleAllergensForEachDangerousIngredient.keys());
        LinkedList<String> queue = new LinkedList<>(dangerousIngredients);
        while (!queue.isEmpty()) {
            String ingredient = queue.removeFirst();
            ArrayList<String> choices = possibleAllergensForEachDangerousIngredient.get(ingredient);
            if (choices.size() == 1) {
                String allergen = choices.get(0);
                allergensToIngredients.put(allergen, ingredient);
                // narrow down the choices for the other ingredients
                for (String i : dangerousIngredients) {
                    if (!i.equals(ingredient)) {
                        possibleAllergensForEachDangerousIngredient.get(i).remove(allergen);
                    }
                }
            }
            else {
                queue.addLast(ingredient);
            }
        }
        return allergensToIngredients;
    }
    
    private static void readFoodInfo(Scanner stdin) {
        foods = new ArrayList<>();
        allIngredients = new ArrayList<>();
        allAllergens = new ArrayList<>();
        while (stdin.hasNextLine()) {
            String[] parts = stdin.nextLine().replace("(","").replace(")","").split(" contains ");
            String[] ingredients = parts[0].split(" ");
            for (String ingredient : ingredients) {
                if (!allIngredients.contains(ingredient)) {
                    allIngredients.add(ingredient);
                }
            }
            String[] listedAllergens = parts[1].split(", ");
            for (String allergen : listedAllergens) {
                if (!allAllergens.contains(allergen)) {
                    allAllergens.add(allergen);
                }
            }
            Food food = new Food(ingredients, listedAllergens);
            foods.add(food);
        }
    }
}

class Food {
    private List<String> ingredients;
    private List<String> listedAllergens;

    Food(String[] ingredients, String[] listedAllergens) {
        this.ingredients = Arrays.asList(ingredients);
        this.listedAllergens = Arrays.asList(listedAllergens);
    }

    List<String> getIngredients() { return this.ingredients; }

    List<String> getListedAllergens() { return this.listedAllergens; }
}
