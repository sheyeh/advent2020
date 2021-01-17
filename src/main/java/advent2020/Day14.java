package advent2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14 {
    private static final List<String> input = FileReader.readFile("src/main/resources/day14input.txt");
    private static final Pattern mem = Pattern.compile("mem\\[(\\d*)\\] = (\\d*)");

    public static void main(String... args) {
        q1();
        q2();
    }

    private static void q1() {
        Map<Integer, Long> values = new HashMap<>();
        long zeroMask = 0;
        long onesMask = 0;
        for (String line : input) {
            if (line.startsWith("mask")) {
                String mask = line.substring(7);
                zeroMask = mask(mask, "1");
                onesMask = mask(mask, "0");
            } else {
                Matcher matcher = mem.matcher(line);
                matcher.matches();
                int key = Integer.parseInt(matcher.group(1));
                int val = Integer.parseInt(matcher.group(2));
                long val2 = val & zeroMask | onesMask;
                values.put(key, val2);
            }
        }
        System.out.println(values.values().stream().mapToLong(Long::longValue).sum());
    }

    private static void q2() {
        Map<Long, Long> values = new HashMap<>();
        String mask = null;
        for (String line : input) {
            if (line.startsWith("mask")) {
                mask = line.substring(7);
            } else {
                Matcher matcher = mem.matcher(line);
                matcher.matches();
                long key = Integer.parseInt(matcher.group(1));
                long val = Integer.parseInt(matcher.group(2));
                List<Long> addresses = addresses(mask, key);
                addresses.forEach(k -> values.put(k, val));
            }
        }
        System.out.println(values.values().stream().mapToLong(Long::longValue).sum());
    }

    private static long mask(String mask, String x) {
        return Long.parseLong(mask.replaceAll("X", x), 2);
    }

    private static List<Long> addresses(String mask, long baseAddress) {
        // If the mask is 1 then overwrite wtih 1
        // If the mask if 0 then unchanged
        // Xs are replcaed with all combinations

        // take the base address and OR it with the mask where Xs are zeros
        long base1 = baseAddress | mask(mask, "0");
        // Now set to zero all the bits that are X in the mask.
        final long base2 = base1 & mask(mask.replaceAll("0", "1"), "0");
        return masks(mask).stream()
                .map(m -> m | base2)
                .collect(Collectors.toList());
    }

    /**
     * Given a mask of zeros, ones and Xs, return a list for all combinations
     * where Xs are replaced with zeros and ones.
     */
    private static List<Long> masks(String mask) {
        List<Long> result = new ArrayList<>();
        int loc = mask.indexOf("X");
        if (loc == -1) {
            result.add(Long.parseLong(mask, 2));
        } else {
            result.addAll(masks(mask.replaceFirst("X", "0")));
            result.addAll(masks(mask.replaceFirst("X", "1")));
        }
        return result;
    }
}
