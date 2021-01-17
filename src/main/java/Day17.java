package advent2020;

import java.util.List;

public class Day17 {
    private static final Integer[] RANGE = new Integer[]{-1, 0 ,1};
    private static char DASH = "#".charAt(0);

    public static void main(String... args) {
        part1();
        part2();
    }

    private static void part1() {
        State state = initialize();
//        state.print();
        for (int g = 1; g < 7; g++) {
            state = state.next();
//            state.print();
        }
        System.out.println(state.total());
    }

    private static void part2() {
        State4D state = initialize4D();
        for (int g = 1; g < 7; g++) {
            state = state.next();
        }
        System.out.println(state.total());
    }

    private static State initialize() {
        List<String> input = FileReader.readFile("src/advent2020/input/day17input.txt");
        return new State(input);
    }

    private static State4D initialize4D() {
        List<String> input = FileReader.readFile("src/advent2020/input/day17input.txt");
        return new State4D(input);
    }

    private static class State {
        boolean[][][] values;
        int N;
        int Z;

        public State(List<String> lines) {
            N = lines.size();
            Z = 1;
            values = new boolean[Z][N][N];
            for (int i = 0; i < N; i++) {
                String line = lines.get(i);
                for (int j = 0; j < N; j++) {
                    values[0][i][j] = line.charAt(j) == DASH;
                }
            }
        }

        public State(int Z, int N) {
            this.N = N;
            this.Z = Z;
            values = new boolean[Z][N][N];
        }

        public State next() {
            int ZZ = values.length + 2;
            int NN = values[0].length + 2;
            State newState = new State(ZZ, NN);
            for (int K = 0; K < ZZ; K++) {
                for (int I = 0; I < NN; I++) {
                    for (int J = 0; J < NN; J++) {
                        int count = count(I - 1, J - 1, K - 1);
                        boolean currentState = K > 0 && K < ZZ - 1
                                && I > 0 && I <= N
                                && J > 0 && J <= N
                                && values[K - 1][I - 1][J - 1];
                        boolean newValue = (currentState && (count == 2 || count == 3))
                                || (!currentState && count == 3);
//                        System.out.println(I + ", " + J + ", " + K + " : " + currentState + " / " + count + " -> " + newValue);
                        newState.values[K][I][J] = newValue;
                    }
                }
            }
            return newState;
        }

        private int count(int I, int J, int K) {
            int count = 0;
            for (int i : RANGE) {
                for (int j : RANGE) {
                    for (int k : RANGE) {
                        if (i != 0 || j != 0 || k != 0) {
                            count += get(I + i, J + j, K + k);
                        }
                    }
                }
            }
            return count;
        }

        private int get(int i, int j, int k) {
            if (i >=0 && i < N && j >=0 && j < N && k >=0 && k < Z) {
                return values[k][i][j] ? 1 : 0;
            } else {
                return 0;
            }
        }

        public int total() {
            int total = 0;
            for (int k = 0; k < Z; k++) {
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        total += values[k][i][j] ? 1 : 0;
                    }
                }
            }
            return total;
        }

        public void print() {
            int Z = values.length;
            int Z2 = Z / 2;
            int N = values[0].length;
            for (int z = 0; z < Z; z++) {
                System.out.println("z=" + (z - Z2));
                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        System.out.print(values[z][i][j] ? "#" : ".");
                    }
                    System.out.println();
                }
            }
            System.out.println();
        }
    }

    private static class State4D {
        boolean[][][][] values;
        int N;
        int Z;
        int W;

        public State4D(List<String> lines) {
            N = lines.size();
            Z = 1;
            W = 1;
            values = new boolean[Z][W][N][N];
            for (int i = 0; i < N; i++) {
                String line = lines.get(i);
                for (int j = 0; j < N; j++) {
                    values[0][0][i][j] = line.charAt(j) == DASH;
                }
            }
        }
        public State4D(int Z, int W, int N) {
            this.N = N;
            this.Z = Z;
            this.W = W;
            values = new boolean[Z][W][N][N];
        }

        public State4D next() {
            int ZZ = values.length + 2;
            int WW = values[0].length + 2;
            int NN = values[0][0].length + 2;

            State4D newState = new State4D(ZZ, WW, NN);
            for (int K = 0; K < ZZ; K++) {
                for (int L = 0; L < WW; L++) {
                    for (int I = 0; I < NN; I++) {
                        for (int J = 0; J < NN; J++) {
                            int count = count(I - 1, J - 1, K - 1, L - 1);
                            boolean currentState =
                                    K > 0 && K < ZZ - 1 && L > 0 && L < WW - 1
                                    && I > 0 && I <= N && J > 0 && J <= N
                                    && values[K - 1][L - 1][I - 1][J - 1];
                            boolean newValue = (currentState && (count == 2 || count == 3)) || (!currentState
                                    && count == 3);
                            newState.values[K][L][I][J] = newValue;
                        }
                    }
                }
            }
            return newState;
        }

        private int count(int I, int J, int K, int L) {
            int count = 0;
            for (int i : RANGE) {
                for (int j : RANGE) {
                    for (int k : RANGE) {
                        for (int l : RANGE) {
                            if (i != 0 || j != 0 || k != 0 || l != 0) {
                                count += get(I + i, J + j, K + k, L + l);
                            }
                        }
                    }
                }
            }
            return count;
        }

        private int get(int i, int j, int k, int l) {
            if (i >=0 && i < N && j >=0 && j < N && k >=0 && k < Z && l >=0 && l < W) {
                return values[k][l][i][j] ? 1 : 0;
            } else {
                return 0;
            }
        }

        public int total() {
            int total = 0;
            for (int k = 0; k < Z; k++) {
                for (int l = 0; l < W; l++) {
                    for (int i = 0; i < N; i++) {
                        for (int j = 0; j < N; j++) {
                            total += values[k][l][i][j] ? 1 : 0;
                        }
                    }
                }
            }
            return total;
        }

    }
}
