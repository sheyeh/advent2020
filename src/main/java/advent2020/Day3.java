package advent2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class Day3 {
    private static final char TREE = "#".charAt(0);
    private static final List<String> lines = FileReader.readFile("src/main/resources/day3input.txt");
    private static final int L = lines.get(0).length();

    public static void main(String... args) throws IOException {
        System.out.println(trees(3, 1));
        Integer[][] values = {{1,1}, {3,1}, {5,1}, {7,1}, {1,2}};
        long prod = 1;
        for (Integer[] value : values) {
            prod *= trees(value[0], value[1]);
        }
        System.out.println(prod);
    }

    private static int trees(int X, int Y) {
        int x = 0;
        int trees = 0;
        for (int y = 0; y < lines.size(); y = y + Y) {
            if (TREE == lines.get(y).charAt(x)) {
                trees++;
            }
            x = (x + X) % L;
        }
        System.out.println(X + "," + Y + " = " + trees);
        return trees;
    }
}