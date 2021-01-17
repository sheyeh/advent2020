package advent2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day4 {
    public static void main(String... args) {
        List<String> lines = FileReader.readFile("src/main/resources/day4input.txt");
        List<Passport> infos = infos(lines);
//        for (Passport info : infos) {
//            System.out.println(info + " : " + info.valid());
//        }
        System.out.println(infos.stream().filter(Passport::valid).count());
        System.out.println(infos.stream().filter(Passport::strict).count());
    }

    private static List<Passport> infos(List<String> lines) {
        List<Passport> infos = new ArrayList<>();
        String info = "";
        for (String line : lines) {
            if (line.isEmpty()) {
                infos.add(new Passport(info));
                info = "";
            } else {
                info += line + " ";
            }
        }
        // Last line might not be empty
        if (!info.isEmpty()) {
            infos.add(new Passport(info));
        }
        return infos;
    }

    private static final List<String> REQUIRED_FILEDS =
            Arrays.asList("byr", "iyr", "eyr", "hgt", "hcl", "ecl", "pid");

    private static final List<String> VALID_ECLS =
            Arrays.asList("amb", "blu", "brn", "gry", "grn", "hzl", "oth");

    private static class Passport {
        Map<String, String> map;
        Passport(String info) {
            map = passport(info);
        }

        private static Map<String, String> passport(String info) {
            return Arrays.stream(info.split(" "))
                    .map(s -> s.split(":"))
                    .collect(Collectors.toMap(s -> s[0], s -> s[1]));
        }

        public boolean valid() {
            return map.keySet().containsAll(REQUIRED_FILEDS);
        }

        public boolean strict() {
            return validByr()
                    && validIyr()
                    && validEyr()
                    && validEcl()
                    && validHcl()
                    && validHgt()
                    && validPid()
                    ;
        }

        private boolean validByr() {
            return validYear("byr", 1920, 2002);
        }

        private boolean validIyr() {
            return validYear("iyr", 2010, 2020);
        }

        private boolean validEyr() {
            return validYear("eyr", 2020, 2030);
        }

        private static final Pattern HGT_PATTERN = Pattern.compile("(\\d*)(cm|in)");

        private boolean validHgt() {
            String hgt = map.getOrDefault("hgt", "");
            Matcher match = HGT_PATTERN.matcher(hgt);
            if (match.matches()) {
                int height = Integer.parseInt(match.group(1));
                switch (match.group(2)) {
                    case "cm": return height >= 150 && height <= 193;
                    case "in": return height >= 59 && height <= 76;
                }
            }
            return false;
        }

        private boolean validHcl() {
            return map.getOrDefault("hcl", "").matches("#[0-9a-f]{6}");
        }

        private boolean validEcl() {
            return VALID_ECLS.contains(map.get("ecl"));
        }

        private boolean validPid() {
            return map.getOrDefault("pid", "").matches("[0-9]{9}");
        }

        private boolean validYear(String key, int min, int max) {
            int val = Integer.parseInt(map.getOrDefault(key, "-1"));
            return val >= min && val <= max;
        }

        public String toString() {
            return map.toString();
        }
    }
}