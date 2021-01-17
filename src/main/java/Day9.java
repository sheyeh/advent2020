package advent2020;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Day9 {
    private static List<Long> vals = FileReader.readFile("src/advent2020/input/day9input.txt").stream()
            .map(Long::valueOf).collect(Collectors.toList());
    private static int LEN = 25;

    public static void main(String... args) {
        long violation = violation();
        System.out.println(violation);
        List<Long> match = match(violation);
        System.out.println(match);
        System.out.println(
                match.stream().max(Long::compare).get()
                + match.stream().min(Long::compare).get());
    }

    private static long violation() {
        for (int i = LEN; i < vals.size(); i++) {
            List<Long> prev = vals.subList(i - LEN, i);
            long sum = vals.get(i);
            if (!valid(sum, prev))  {
                return sum;
            }
        }
       return -1;
    }

    private static List<Long> match(long sum) {
        for (int i = 0; i < vals.size(); i++) {
            long s = 0;
            for (int j = i; j < vals.size() && s <= sum; j++) {
                s+= vals.get(j);
                if (s == sum) {
                    return vals.subList(i, j + 1);
                }
            }
        }
        return Collections.emptyList();
    }
    /**
     * Check that there is a pair of different numbers which sum is
     * the argument sum
     *
     * @param list list of numbers to check
     * @return whether such a pair exists
     */
    private static boolean valid(long sum, List<Long> list) {
        Set<Long> set = list.stream()
                .map(val -> sum - val)
                .collect(Collectors.toSet());
        return list.stream()
                .anyMatch(l -> set.contains(l) && sum != l * 2);
    }
}
