package advent2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Day21 {
    private static final List<String> lines = FileReader.readFile("src/main/resources/day21input.txt");
    private static Map<String, List<Food>> allergensToFoods = new HashMap<>();
    private static Map<String, List<Food>> ingredientsToFoods = new HashMap<>();
    private static Set<String> allAllergens = new HashSet<>();
    private static Set<String> allIngredients = new HashSet<>();
    private static List<Food> allFoods = new ArrayList<>();

    public static void main(String... args) {
        parseLines();
        part1();
    }

    /**
     * When a food contains an allergen, it means the allergen is in one of the food's ingredients.
     * Therefore it cannot be in any of the ingredients not in that food. This is true for all foods the
     * allergen is contained in.
     * Given an allergen, iterate over all the foods the allergen is contained in and create a list that
     * is the union over these foods that contains the ingredient not in the food (the complements).
     * This list is the ingredients that can't contain the allergen. Then do the intersection of all
     * these lists to get ingredients that can't be in any allergen.
     */
    private static void part1() {
        Map<String, Set<String>> perAllergenIngredients = new HashMap<>();
        for (Entry<String, List<Food>> entry : allergensToFoods.entrySet()) {
            String allergen = entry.getKey();
            List<Food> foods = entry.getValue();
            for (Food f : foods) {
                List<String> complement = new ArrayList<>(allIngredients);
                complement.removeAll(f.ingredients);
                perAllergenIngredients.computeIfAbsent(allergen, a -> new HashSet<>()).addAll(complement);
            }
        }
        List<String> ingredients = new ArrayList<>(allIngredients);
        perAllergenIngredients.entrySet().stream().map(e -> "Ingredients that can't contain " + e).forEach(System.out::println);
        perAllergenIngredients.values().forEach(ingredients::retainAll);
        System.out.println(ingredients);
        System.out.println(ingredients.size() + " ingredients can't contain any allergen : " + ingredients);
        System.out.println("Number of times they show up : " + ingredients.stream().map(ingredientsToFoods::get).mapToInt(List::size).sum());
    }

    private static void parseLines() {
        for (String line : lines) {
            Food food = new Food(line);
            allFoods.add(food);
            allAllergens.addAll(food.allergens);
            allIngredients.addAll(food.ingredients);
            for (String ingredient : food.ingredients) {
                ingredientsToFoods.computeIfAbsent(ingredient, k -> new ArrayList<>()).add(food);
            }
            for (String allergen : food.allergens) {
                allergensToFoods.computeIfAbsent(allergen, k -> new ArrayList<>()).add(food);
            }
        }
        System.out.println(lines.size() + " foods");
        System.out.println(allIngredients.size() + " ingredients");
        System.out.println(allAllergens.size() + " allergens");
    }

    private static int INDEX = 0;

    private static class Food {
        int index;
        List<String> ingredients;
        List<String> allergens;

        public Food(String line) {
            this.index = ++INDEX;
            String[] parts = line.replaceAll("[(),]","").split(" contains ");
            ingredients = Arrays.asList(parts[0].split(" "));
            allergens = Arrays.asList(parts[1].split(" "));
        }

        public String toString() {
            return "Ingredients : " + ingredients + " allergens : " + allergens;
        }
    }

}
