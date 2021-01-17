package advent2020;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class Day8 {
    private static List<String> ops = FileReader.readFile("src/main/resources/day8input.txt");
    private static Set<Integer> executed = new HashSet<>();
    private static int lineNumber;
    private static boolean done;

    public static void main(String... args) {
        System.out.println(execute(ops));
        for (int i = 0; i < ops.size(); i++) {
            if (ops.get(i).startsWith("nop") || ops.get(i).startsWith("jmp")) {
                executed.clear();
                lineNumber = 0;
                long cum = execute(newOps(i));
                if (executed.contains(ops.size() - 1)) {
                    System.out.println(cum);
                    break;
                }
            }
        }
    }

    private static List<String> newOps(int i) {
        List<String> newOps = new ArrayList<>();
        newOps.addAll(ops);
        String newOp = null;
        String op = ops.get(i);
        if (op.startsWith("jmp")) {
            newOp = "nop " + op.substring(4);
        } else if (op.startsWith("nop")) {
            newOp = "jmp " + op.substring(4);
        }
        newOps.set(i, newOp);
        return newOps;
    }

    private static long execute(List<String> ops) {
        lineNumber = 0;
        long cum = 0;
        while (!executed.contains(lineNumber)) {
            executed.add(lineNumber);
            String op = ops.get(lineNumber);
            if (op.startsWith("nop")) {
                lineNumber++;
            } else if (op.startsWith("acc")) {
                cum += Integer.valueOf(op.substring(4));
                lineNumber++;
            } else if (op.startsWith("jmp")) {
                lineNumber += Integer.valueOf(op.substring(4));
            }
            lineNumber = lineNumber % ops.size();
        }
        return cum;
    }
}
