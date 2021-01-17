package advent2020;

import java.util.List;
import java.util.function.BiFunction;

public class Day11 {
    private static List<String> input = FileReader.readFile("src/main/resources/day11input.txt");
    private static int N = input.size();
    private static int M = input.get(0).length();
    private static final State[][] initial = seats(input);
    private static State[][] seats;

    public static void main(String... args) {
        process(Day11::numOccupied1, 4);
        process(Day11::numOccupied2, 5);
    }

    private static void process(BiFunction<Integer, Integer, Integer> count, int threshold) {
        seats = initial;
        boolean changed;
        do {
            changed = round(count, threshold);
        } while (changed);
        System.out.println(total());
    }

    public static boolean round(BiFunction<Integer, Integer, Integer> count, int threshold) {
        State[][] round = new State[N][M];
        boolean changed = false;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (seats[i][j] == State.Empty
                    && count.apply(i, j) == 0) {
                    round[i][j] = State.Occupied;
                    changed = true;
                } else if (seats[i][j] == State.Occupied
                        && count.apply(i, j) >= threshold) {
                    round[i][j] = State.Empty;
                    changed = true;
                } else {
                    round[i][j] = seats[i][j];
                }
            }
        }
        seats = round;
        return changed;
    }

    private static State[][] seats(List<String> input) {
        State[][] seats = new State[N][M];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                switch (input.get(i).substring(j, j+1)) {
                    case ".": seats[i][j] = State.Floor;
                        continue;
                    case "L": seats[i][j] = State.Empty;
                }
            }
        }

        return seats;
    }

    private static final Integer[] RANGE = new Integer[]{-1, 0 ,1};

    private static int numOccupied1(int I, int J) {
        int count = 0;
        for (int i : RANGE) {
            for (int j : RANGE) {
                int i0 = I + i;
                int j0 = J + j;
                if (!onDeck(i0, j0) || (i == 0 && j == 0)) {
                    continue;
                }
                if (seats[i0][j0] == State.Occupied) {
                    count++;
                }
            }
        }
        return count;
    }

    private static int numOccupied2(int I, int J) {
        int count = 0;
        for (int i : RANGE) {
            for (int j : RANGE) {
                if (i == 0 && j == 0) {
                    continue;
                }
                int r = 0;
                int i0;
                int j0;
                do {
                    r++;
                    i0 = I + i * r;
                    j0 = J + j * r;
                } while (onDeck(i0, j0) && seats[i0][j0] == State.Floor);
                if (onDeck(i0, j0) && seats[i0][j0] == State.Occupied) {
                    count++;
                }
            }
        }
        return count;
    }

    private static boolean onDeck(int i, int j) {
        return i >= 0 && i < N
                && j >= 0 && j < M;
    }

    private static int total() {
        int total = 0;
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                if (seats[i][j] == State.Occupied) {
                    total++;
                }
            }
        }
        return total;
    }

    private static void print() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < M; j++) {
                System.out.print(seats[i][j].toString());
            }
            System.out.println();
        }
        System.out.println();
    }

    private enum State {
        Floor("."), Empty("L"), Occupied("#");

        private String str;
        State(String s) {
            str = s;
        }
        public String toString() {
            return str;
        }
    }
}
