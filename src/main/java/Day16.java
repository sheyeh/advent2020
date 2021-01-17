package advent2020;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

public class Day16 {
    private static List<String> input  = FileReader.readFile("src/advent2020/input/day16input.txt");
    private static Map<String, List<Range>> ticketFields = new HashMap<>();
    private static Map<String, Integer> fieldMapping = new HashMap<>();
    private static List<Integer[]> validTickets;
    private static Integer[] myTicket;

    public static void main(String... args) {
        validTickets = validTickets();
        computeMapping();
        long product = fieldMapping.entrySet().stream()
                .filter(e -> e.getKey().startsWith("departure"))
                .mapToLong(e -> myTicket[e.getValue()])
                .reduce(1, (a, b) -> a * b);
        System.out.println(product);
    }

    private static List<Integer[]> validTickets() {
        List<Integer[]> validTickets = new ArrayList<>();
        List<Range> ranges = new ArrayList<>();
        boolean parse = false;
        boolean parseYourTicket = false;
        long rate = 0;
        for (String line : input) {
            if (line.contains("-")) {
                String[] fields = line.split(": ");
                String key = fields[0];
                List<Range> value = ranges(fields[1]);
                ticketFields.put(key, value);
                ranges.addAll(value);
            }
            if (parseYourTicket) {
                String[] vals = line.split(",");
                myTicket = new Integer[vals.length];
                int index = 0;
                for (String val : vals) {
                    int intVal = Integer.parseInt(val);
                    myTicket[index] = intVal;
                    index++;
                }
                parseYourTicket = false;
            }
            if (parse) {
                boolean valid = true;
                String[] vals = line.split(",");
                Integer[] ints = new Integer[vals.length];
                int index = 0;
                for (String val : vals) {
                    int intVal = Integer.parseInt(val);
                    ints[index] = intVal;
                    index++;
                    if (!valid(intVal, ranges)) {
                        valid = false;
                        rate += intVal;
                    }
                }
                if (valid) {
                    validTickets.add(ints);
                }
            }
            if ("nearby tickets:".equals(line)) {
                ranges = Range.merge(ranges);
                parse = true;
            }
            if ("your ticket:".equals(line)) {
                parseYourTicket = true;
            }
        }
        System.out.println(rate);
        return validTickets;
    }

    private static final boolean valid(int val, List<Range> ranges) {
        return ranges.stream().anyMatch(r -> r.valid(val));
    }

    private static List<Range> ranges(String line) {
        List<Range> ranges = new ArrayList<>();
        String[] tokens = line.split(" ");
        for (String token : tokens) {
            if (token.contains("-")) {
                String[] vals = token. split("-");
                ranges.add(new Range(Integer.valueOf(vals[0]), Integer.valueOf(vals[1])));
            }
        }
        return ranges;
    }

    private static void computeMapping() {
        // find which row in the tickets match which field

        Map<String, List<Integer>> foo = new HashMap<>();
        for (String field : ticketFields.keySet()) {
            for (int row = 0; row < ticketFields.keySet().size(); row++) {
                List<Range> fieldRange = ticketFields.get(field);
                boolean valid = rowMatch(fieldRange, row);
                if (valid) {
                    foo.computeIfAbsent(field, k -> new ArrayList<>()).add(row);
                }
            }
        }
        for (int i = 0; i < ticketFields.size(); i++) {
            String field = foo.entrySet().stream()
                    .filter(e -> e.getValue().size() == 1)
                    .findFirst()
                    .map(Entry::getKey)
                    .get();
            int row = foo.get(field).get(0);
            fieldMapping.put(field, row);
            foo.remove(field);
            List<Integer> rowList = new ArrayList<>();
            rowList.add(row);
            for (List<Integer> vals : foo.values()) {
                vals.removeAll(rowList);
            }
        }

    }

    private static boolean rowMatch(List<Range> ranges, int row) {
        for (Integer[] vals : validTickets) {
            if (!valid(vals[row], ranges)) {
                return false;
            }
        }
        return true;
    }

    /*****************************************************************************/

    private static class Range {
        int from;
        int to;
        Range(int from, int to) {
            this.from = from;
            this.to = to;
        }

        static List<Range> merge(Range r1, Range r2) {
            List<Range> result = new ArrayList<>();
            if (r1.to < r2.from - 1 || r2.to < r1.from - 1) {
                // no overlap
                result.add(r1);
                result.add(r2);
            } else {
                // overlap
                result.add(new Range(Math.min(r1.from, r2.from), Math.max(r1.to, r2.to)));
            }
            return result;
        }

        static List<Range> merge(List<Range> list) {
            List<Range> list2 = list.stream()
                    .sorted((r1, r2) -> Integer.compare(r1.from, r2.from))
                    .collect(Collectors.toList());
            int i = 1;
            Range last = list2.get(0);
            List<Range> result = new ArrayList<>();
            while (i < list2.size()) {
                List<Range> merge = merge(last, list2.get(i));
                if (merge.size() == 1) {
                    last = merge.get(0);
                } else {
                    result.add(merge.get(0));
                    last = merge.get(1);
                }
                i++;
            }
            result.add(last);
            return result;
        }

        public String toString() {
            return "<" + from + "-" + to + ">";
        }

        public boolean valid(int val) {
            return (val >= from && val <= to);
        }
    }
}
