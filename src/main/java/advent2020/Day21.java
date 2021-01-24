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
    private static final Map<String, List<Food>> allergensToFoods = new HashMap<>();
    private static final Map<String, List<Food>> ingredientsToFoods = new HashMap<>();
    private static final Set<String> allAllergens = new HashSet<>();
    private static final Set<String> allIngredients = new HashSet<>();
    private static final List<Food> allFoods = new ArrayList<>();
    private static List<String> ingredientsWithoutAllergens;

    public static void main(String... args) {
        parseLines();
        ingredientsWithoutAllergens = part1();
        part2();
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
    private static List<String> part1() {
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
        System.out.println(ingredients.size() + " ingredients can't contain any allergen : " + ingredients);
        System.out.println("Number of times they show up : " + ingredients.stream().map(ingredientsToFoods::get).mapToInt(List::size).sum());

        return ingredients;
    }

    /**
     * Now that we know which ingredients can't have allergens, ignore these ingredients.
     * Only consider ingredients that do have allergens. Each has exactly one allergen.
     * Create a map from allergen to ingredidents that can have it. The value is the
     * intersection of ingredients over all foods that have the allergen as one of its
     * allergens. For example, if we have Food 1 with allergen A and ingredients X, Y,
     * and Food 2 with allergens A, B and ingredients X, Z then this is enough to determine
     * that ingredient X has allergen A.
     */
    private static void part2() {
        // Map from allergen to possible ingredients.
        Map<String, List<String>> allergenToIngredients = new HashMap<>();
        for (Food f : allFoods) {
            Set<String> ingredients = new HashSet<>(f.ingredients);
            ingredients.removeAll(ingredientsWithoutAllergens);
            for (String a : f.allergens) {
                List<String> ing = allergenToIngredients.get(a);
                if (ing == null) {
                    allergenToIngredients.put(a, new ArrayList<>(ingredients));
                } else {
                    ing.retainAll(ingredients);
                }
            }
        }

        /*
          At this point, allergenToIngredients may have multiple possible ingredients for
          some allergens. Take the entries in the table where the list of ingredients is of
          size one. For those we know what the allergen is. If they appear in the list of
          ingredients of other allergens then remove them from those lists. Repeat until
          all the lists are of size 1.
         */
        while (allergenToIngredients.values().stream().map(List::size).anyMatch(i -> i > 1)) {
            Set<String> knownAllergens = allergenToIngredients.entrySet().stream()
                    .filter((e -> e.getValue().size() == 1))
                    .map(Entry::getKey)
                    .collect(Collectors.toSet());

            for (String a : knownAllergens) {
                String knownIngredient = allergenToIngredients.get(a).get(0);
                for (Entry<String, List<String>> entry : allergenToIngredients.entrySet()) {
                    if (!entry.getKey().equals(a)) {
                        entry.getValue().remove(knownIngredient);
                    }
                }
            }
        }
        System.out.println(allergenToIngredients);
        System.out.println("Ingredients sorted by allergens : " + allergenToIngredients.entrySet().stream()
                .sorted(Entry.comparingByKey())
                .map(e -> e.getValue().get(0))
                .collect(Collectors.joining(",")));
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

    private static class Food {
        List<String> ingredients;
        List<String> allergens;

        public Food(String line) {
            String[] parts = line.replaceAll("[(),]","").split(" contains ");
            ingredients = Arrays.asList(parts[0].split(" "));
            allergens = Arrays.asList(parts[1].split(" "));
        }

        public String toString() {
            return "ingredients : " + ingredients + " allergens : " + allergens;
        }
    }

}
