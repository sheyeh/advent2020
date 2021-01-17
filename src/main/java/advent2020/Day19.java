package advent2020;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day19 {

    private static List<String> lines = FileReader.readFile("src/main/resources/day19input.txt");
    private static Map<Boolean, List<String>> input = lines.stream()
            .filter(l -> !l.isEmpty())
            .collect(Collectors.groupingBy(l -> l.contains(":")));
    // Map from key to String representation of the rule
    private static Map<String, String> raw = input.get(true).stream()
            .collect(Collectors.toMap(
                    s -> s.split(": ")[0], s -> s.split(": ")[1]));
    // Map from key to Rule
    private static Map<String, Rule> rules = new HashMap<>();

    public static void main(String... args) {
        part1();
        part2();
    }

    private static void part1() {
        Rule r0 = parse(input.get(true));
        long matches = input.get(false).stream().filter(r0::match).count();
        System.out.println(matches);
    }

    private static void part2() {
        String p2 = ruleStr("0");
        String p3 = r8(p2);
        String p4 = r11(p3);
        long matches = input.get(false).stream().filter(l -> l.matches(p4)).count();
        System.out.println(matches);
    }

    // Handle 8: 42 | 42 8
    private static String r8(String p) {
        String r8 = ruleStr("8");
        int i8 = p.indexOf(r8);
        return p.substring(0 , i8 + r8.length()) + "+" + p.substring(i8 + r8.length());
    }

    // Handle 11: 42 31 | 42 11 31
    private static String r11(String p) {
        String r42 = ruleStr("42");
        String r31 = ruleStr("31");
        int DEPTH = 5;
        String r11 = "";
        for (int i = 1; i <= DEPTH; i++) {
            String r = "";
            for (int j = 0; j < i; j++) {
                r = r42 + r + r31;
            }
            r11 = r11 + "(" + r + ")" + (i < DEPTH ? "|" : "");
        }
        int i42_31 = p.indexOf(r42+r31);
        return p.substring(0, i42_31) + "(" + r11 + ")" + p.substring(i42_31 + (r42 + r31).length());
    }

    private static Rule parse(List<String> lines) {
        for (String key : raw.keySet()) {
            if (!rules.containsKey(key)) {
                Rule r = parse(key);
            }
        }
        return rules.get("0");
    }

    private static Rule parse(String key) {
        if (rules.containsKey(key)) {
            return rules.get(key);
        }
        String ruleString = raw.get(key);
        Rule r;
        if (ruleString.matches("\".\"")) {
            r = new SimpleRule(ruleString.substring(1, 2));
        } else if (ruleString.contains("|")) {
            String[] parts = ruleString.split(" \\| ");
            Rule[] rs = new Rule[2];
            for (int i = 0; i < 2; i++) {
                rs[i] = concatOrParse(parts[i]);
            }
            r = new OrRule(rs[0], rs[1]);
        } else {
            r = concatOrParse(ruleString);
        }
        rules.put(key, r);
        return r;
    }

    private static Rule concat(String str) {
        String[] nums = str.split(" ");
        Rule r1 = parse(nums[0]);
        Rule r2 = parse(nums[1]);
        return new ConcatRule(r1, r2);
    }

    private static Rule concatOrParse(String str) {
        if (str.contains(" ")) {
            return concat(str);
        } else {
            return parse(str);
        }
    }

    private static String ruleStr(String key) {
        return rules.get(key).toString();
    }

    private interface Rule {
        default boolean match(String s) {
            return s.matches(this.toString());
        }
    }

    private static class SimpleRule implements Rule {
        String c;
        SimpleRule(String c) {
            this.c = c;
        }

        public String toString() {
            return c;
        }
    }

    private static class OrRule implements Rule {
        Rule a;
        Rule b;
        OrRule(Rule a, Rule b) {
            this.a = a;
            this.b = b;
        }

        public String toString() {
            return "(" + a + "|" + b + ")";
        }
    }

    private static class ConcatRule implements Rule {
        Rule a;
        Rule b;
        ConcatRule(Rule a, Rule b) {
            this.a = a;
            this.b = b;
        }

        public String toString() {
            return "" + a + b;
        }
    }
}
