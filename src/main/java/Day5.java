package advent2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day5 {
    public static void main(String... args) {
        List<String> boardings = FileReader.readFile("src/advent2020/input/day5input.txt");
        System.out.println(boardings.stream().map(Day5::id).max(Integer::compareTo).get());
        int minRow = boardings.stream().map(Day5::id).min(Integer::compareTo).get();
        int maxRow = boardings.stream().map(Day5::id).max(Integer::compareTo).get();

        List<Integer> allIds = boardings.stream()
                //.filter(s -> row(s) > minRow && row(s) < maxRow)
                .map(Day5::id)
                .sorted()
                .collect(Collectors.toList());
        for (int i = 0; i < allIds.size() - 1; i++) {
            if (allIds.get(i + 1) - allIds.get(i) > 1) {
                System.out.println(allIds.get(i) + 1);
            }
        }
    }

    private static int id(String s) {
        return row(s.substring(0, 7)) * 8 + col(s.substring(7, 10));
    }
    private static int row(String s) {
        return IntStream.range(0, 7)
                .map(i -> (int)Math.pow(2, 6-i) * ((s.charAt(i) == "B".charAt(0)) ? 1 : 0))
                .sum();
    }

    private static int col(String s) {
        return IntStream.range(0, 3)
                .map(i -> (int)Math.pow(2, 2-i) * ((s.charAt(i) == "R".charAt(0)) ? 1 : 0))
                .sum();
    }
}