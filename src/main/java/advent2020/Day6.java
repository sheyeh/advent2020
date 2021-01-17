package advent2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day6 {
    private static List<String> lines = FileReader.readFile("src/main/resources/day6input.txt");
    public static void main(String... args) {
        q1();
        q2();
    }

    private static void q1() {
        Set<String> declaration = new HashSet<>();
        int count = 0;
        for (String line : lines) {
            if (line.isEmpty()) {
                count += declaration.size();
                declaration.clear();
            } else {
                declaration.addAll(Arrays.stream(line.split("")).collect(Collectors.toList()));
            }
        }
        // Add last declartation. If it is empty, its size will be zero so it is OK.
        count += declaration.size();
        System.out.println(count);
    }

    private static void q2() {
        Set<String> declaration = new HashSet<>();
        int count = 0;
        boolean first = true;
        for (String line : lines) {
            if (line.isEmpty()) {
                count += declaration.size();
                declaration.clear();
                first = true;
            } else {
                List list = Arrays.stream(line.split("")).collect(Collectors.toList());
                if (first) {
                    declaration.addAll(list);
                    first = false;
                } else {
                    declaration.retainAll(list);
                }
            }
        }
        // Add last declartation. If it is empty, its size will be zero so it is OK.
        count += declaration.size();
        System.out.println(count);
    }
}