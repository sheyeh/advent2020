package advent2020;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Day13 {
    private static final List<String> input = FileReader.readFile("src/advent2020/input/day13input.txt");
    private static final int arrival = Integer.valueOf(input.get(0));
    private static List<Integer> ids = Arrays.stream(input.get(1).split(","))
            .map(s -> s.equals("x") ? "-1" : s)
            .map(Integer::valueOf)
            .collect(Collectors.toList());

    public static void main(String... args) {
        q1();
        q2();
    }

    private static void q1() {
        int theWaitTime = Integer.MAX_VALUE;
        int theId = 0;
        for (int id : ids) {
            if (id != -1) {
                int waitTime = id - arrival % id;
                if (waitTime < theWaitTime) {
                    theWaitTime = waitTime;
                    theId = id;
                }
            }
        }
        System.out.println(theWaitTime * theId);
    }

    /**
     * Look for number N such that when divided by id, the remainder is id minus
     * the location of id in the list. For example: say id 19 is in location 32
     * in the list. 19 - 32 is -13. So we look for N such that the is n where
     * N = n * 19 - 13.
     */
    private static void q2() {
        System.out.println(multiplier(18, 32, 59, 55));
        for (int n = 0; n < 10; n++) {
            System.out.println((18 + n * 32) % 59);
        }
        System.out.println();
        long current = 0;
        long product = ids.get(0);
        // Assume first entry in the list is valid. For thast entry the
        // target remainder is 0, so a possible value is 0.
        for (int i = 1; i < ids.size(); i++) {
            if (ids.get(i) < 0) {
                continue;
            }
            int id = ids.get(i);
            int remainder = id - i;
            if (remainder < 0) {
                remainder -= Math.floorDiv(remainder, id) * id;
            }
            int n = multiplier(current, product, id, remainder);
            current += n * product;
            product *= id;
        }
        System.out.println(current);
    }

    /**
     * Look for m such that base + m x step % mod = target
     */
    private static int multiplier(long base, long step, int mod, int target) {
        if (base > mod || step > mod || target > mod) {
            return multiplier(base % mod, step % mod, mod, target % mod);
        }
        for (int m = 0; ; m++) {
            if ((base + m * step) % mod == target) {
                return m;
            }
        }
    }
}
