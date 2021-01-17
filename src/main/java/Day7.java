package advent2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day7 {
    static List<Rule> rules = rules();
    static Map<String, Rule> map = rules.stream()
            .collect(Collectors.toMap(
                    rule -> rule.container, Function.identity()));

    public static void main(String... args) {
        int options = options("shiny gold");
        System.out.println(options);
        int total = total("shiny gold") - 1;
        System.out.println(total);
    }

    private static List<Rule> rules() {
        return FileReader.readFile("src/advent2020/input/day7input.txt").stream()
                .map(Rule::new)
                .collect(Collectors.toList());
    }

    private static int options(String bag) {
        Set<String> allContainers = containers(bag);
        while (true) {
            Set<String> containers = containers(allContainers);
            if (allContainers.containsAll(containers)) {
                break;
            }
            allContainers.addAll(containers);
        }
        return allContainers.size();
    }

    private static Set<String> containers(String bag) {
        Set<String> bags = new HashSet<>();
        bags.add(bag);
        return containers(bags);
    }

    private static Set<String> containers(Set<String> bags) {
        return rules.stream()
                .filter(rule -> containsAny(rule.content, bags))
                .map(rule -> rule.container)
                .collect(Collectors.toSet());
    }

    private static boolean containsAny(Collection<String> a, Collection<String> b) {
        Set<String> temp = new HashSet<>();
        temp.addAll(a);
        temp.retainAll(b);
        return !temp.isEmpty();
    }

    private static int total(String bag) {
        int total = 1;
        Rule rule = map.get(bag);
        for (int i = 0; i < rule.content.size(); i++) {
            total += (rule.counts.get(i)) * total(rule.content.get(i));
        }
        return total;
    }

    private static class Rule {
        final String container;
        final List<String> content;
        final List<Integer> counts;

        private static final Pattern PATTERN = Pattern.compile("(\\d+) ([^ ]* [^ ]*) bags{0,1}$");

        Rule(String line) {
            String trim = line.substring(0, line.length() - 1);
            String[] items = trim.split(" contain |, ");
            container = items[0].split(" bag")[0]; // could be bag or bags
            content = new ArrayList<>();
            counts = new ArrayList<>();
            if (!"no other bags".equals(items[1])) {
                for (int i = 1; i < items.length; i++) {
                    Matcher matcher = PATTERN.matcher(items[i]);
                    matcher.matches();
                    counts.add(Integer.valueOf(matcher.group(1)));
                    content.add(matcher.group(2));
                }
            }
        }

        public String toString() {
            return container + " <- " + counts + content;
        }
    }
}
