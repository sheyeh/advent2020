package advent2020;

import advent2020.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day1 {
    private static List<Integer> lines;

    public static void main(String... args) throws IOException {
        lines = FileReader.readFile("src/advent2020/input/day1input.txt")
                .stream()
                .map(Integer::valueOf)
                .sorted()
                .collect(Collectors.toList());
        System.out.println("Two");
        System.out.println("* " + two(0, lines.size() - 1, 0));
        System.out.println("Three");
        System.out.println("* " + three());
    }

    private static int two(int from, int to, int base) {
        int total;
        int target = 2020 - base;
        do {
            total = lines.get(from) + lines.get(to);
            if (total < target) {
                from++;
            }
            if (total > target) {
                to--;
            }
            if (from == to) {
                return 0;
            }
        } while (total != target);
        int fromValue = lines.get(from);
        int toValue = lines.get(to);
        System.out.println(fromValue);
        System.out.println(toValue);
        return fromValue * toValue;
    }

    private static int three() {
        int bottom = 0;
        while (bottom < lines.size()) {
            int bottomValue = lines.get(bottom);
            int prod = bottomValue * two(bottom + 1, lines.size() -1, bottomValue);
            if (prod > 0) {
                System.out.println(bottomValue);
                return prod;
            }
            bottom++;
        }
        return 0;
    }
}