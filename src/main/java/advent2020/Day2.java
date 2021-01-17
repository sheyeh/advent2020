package advent2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day2 {

    public static void main(String... args) throws IOException {
        List<Line> lines = FileReader.readFile("src/main/resources/day2input.txt")
                .stream()
                .map(Line::new)
                .collect(Collectors.toList());
        System.out.println(lines.stream().filter(Line::valid).count());
        System.out.println(lines.stream().filter(Line::valid2).count());
    }

    private static List<Line> readFile() throws IOException {
        return Files.readAllLines(Paths.get("src/advent/2020/input/day2input.txt"))
                .stream()
                .map(Line::new)
                .collect(Collectors.toList());
    }
    private static class Line {
        private static final Pattern PATTERN = Pattern.compile("(\\d+)-(\\d+) ([a-z]): (.*)");

        int min;
        int max;
        String letter;
        String password;

        private Line(String s) {
            Matcher m = PATTERN.matcher(s);
            m.matches();
            min = Integer.parseInt(m.group(1));
            max = Integer.parseInt(m.group(2));
            letter = m.group(3);
            password = m.group(4);
        }

        public boolean valid() {
            int count = password.replaceAll("[^"+letter+"]","").length();
            return count >= min && count <= max;
        }

        public boolean valid2() {
            char c = letter.charAt(0);
            boolean p1 = c == password.charAt(min - 1);
            boolean p2 = c == password.charAt(max - 1);
            return (p1 && !p2) || (p2 && !p1);
        }
    }
}