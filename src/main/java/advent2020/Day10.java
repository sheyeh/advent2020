package advent2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day10 {
    private static List<Integer> joltages = FileReader.readFile("src/main/resources/day10input.txt").stream()
            .map(Integer::valueOf)
            .sorted()
            .collect(Collectors.toList());

    public static void main(String... args) {
        joltages.add(0, 0);
        int size = joltages.size();
        joltages.add(joltages.get(size - 1) + 3);
        System.out.println(joltages);
        Map<Integer, Integer> histogram = histogram();
        System.out.println(histogram.get(1) * histogram.get(3));

        System.out.println(combinations());
    }

    private static Map<Integer, Integer> histogram() {
        Map<Integer, Integer> histogram = new HashMap<>();
        histogram.put(1, 0);
        histogram.put(2, 0);
        histogram.put(3, 0);
        for (int i = 1; i < joltages.size(); i++) {
            int diff = joltages.get(i) - joltages.get(i - 1);
            histogram.put(diff, histogram.get(diff) + 1);
        }
        return histogram;
    }

    private static long combinations() {
        // Assume only differences of 1 and 3
        long combinations = 1;
        int loc = 0;
        for (int i = 1; i < joltages.size(); i++) {
            if (joltages.get(i) - joltages.get(i - 1) == 3) {
                int N = i - loc - 1;
                combinations *= options(N);
                loc = i;
            }
        }
        return combinations;
    }

    /**
     * Number of combinations for a sequence of N differences of 1.
     *
     * @param N number of diff-1 ion the sequence
     * @return number of options to use them.
     */
    private static int options(int N) {
        switch (N) {
            case 0: return 1;
            case 1: return 1;
            case 2: return 2;
            case 3: return 4;
            default: return options(N - 3) + options(N - 2) + options(N - 1);
        }
    }
}


/*
Find all gaps of 3and anchor them
find 2s followed by 2 or 3 and leave them

4 / 4 / 3 / 2 / 4 / 1 / 4



*/