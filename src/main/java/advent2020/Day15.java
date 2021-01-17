package advent2020;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day15 {
    private static final List<Integer> input = Arrays.asList(0,13,16,17,1,10,6);

    public static void main(String... args) {
        q1(2020);
        q1(30000000);
    }

    private static void q1(int turns) {
        Map<Integer, Integer> lastTurn = new HashMap<>();
        int lastCall = 0;
        int diff = 0;
        boolean firstTime = true;
        for (int turn = 0; turn < turns; turn++) {
            if (turn < input.size()) {
                lastCall = input.get(turn);
            } else {
                if (firstTime) {
                    lastCall = 0;
                } else {
                    lastCall = diff;
                }
            }
            firstTime = !lastTurn.containsKey(lastCall);
            if (lastTurn.containsKey(lastCall)) {
                diff = turn - lastTurn.get(lastCall);
            }
            lastTurn.put(lastCall, turn);
        }
        System.out.println(lastCall);
    }
}
