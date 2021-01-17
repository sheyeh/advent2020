package advent2020;

import java.util.List;

public class Day18 {
    private static boolean plusPresedence;
    public static void main(String... args) {
        List<String> lines = FileReader.readFile("src/main/resources/day18input.txt");
        for (boolean p : new boolean[]{false, true}) {
            plusPresedence = p;
            System.out.println(lines.stream().map(Day18::parse).mapToLong(Expression::apply).sum());
        }
    }

    private static Expression parse(String line) {
        if (line.matches("\\d+")) {
            return new Constant(Long.parseLong(line));
        } else if (line.contains("(")) {
            int open = line.lastIndexOf("(");
            int close = line.substring(open).indexOf(")") + open;
            long value = parse(line.substring(open + 1, close)).apply();
            String left = open > 0 ? line.substring(0, open) : "";
            String right = close < line.length() - 1 ? line.substring(close + 1) : "";
            return parse(left + value + right);
        } else if (plusPresedence && line.contains("*")) {
            int mult = line.indexOf(" * ");
            return new Multiplication(parse(line.substring(0, mult)), parse(line.substring(mult + 3)));
        } else {
            int space = line.lastIndexOf(" ");
            String p1 = line.substring(0, space - 2);
            char operation = line.charAt(space - 1);
            String p2 = line.substring(space + 1);
            Expression e1 = parse(p1);
            Expression e2 = parse(p2);
            return operation == "+".charAt(0)
                    ? new Addition(e1, e2)
                    : new Multiplication(e1, e2);
        }
    }

    private interface Expression {
        long apply();
    }

    private static class Operation implements Expression {
        final Expression arg1;
        final Expression arg2;
        Operation(Expression e1, Expression e2) {
            arg1 = e1;
            arg2 = e2;
        }

        public long apply() {
            return 0;
        }
    }

    private static class Addition extends Operation {
        Addition(Expression e1, Expression e2) {
            super(e1, e2);
        }

        @Override
        public long apply() {
            return arg1.apply() + arg2.apply();
        }
    }

    private static class Multiplication extends Operation {
        Multiplication(Expression e1, Expression e2) {
            super(e1, e2);
        }

        @Override
        public long apply() {
            return arg1.apply() * arg2.apply();
        }
    }

    private static class Constant implements Expression {
        final long constant;
        Constant(long constant) {
            this.constant = constant;
        }

        @Override
        public long apply() {
            return constant;
        }
    }
}
