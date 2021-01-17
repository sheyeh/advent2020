package advent2020;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day20 {
    private static final List<String> MONSTER =
            Arrays.asList("                  # ", "#    ##    ##    ###", " #  #  #  #  #  #   ");
    private static final List<String> lines = FileReader.readFile("src/main/resources/day20input.txt");

    /**
     * The solution is based on the fact that there are exactly four tiles where each of
     * them has 2 edges that no other tile has, and those have to be in the corners.
     * Also, each other edge appears in exactly two tiles. This means that given a tile, we
     * know for sure who are its neighbors.
     */
    public static void main(String... args) {
        List<Tile> tiles = parse();
        // Mop from edge to tiles that have this edge
        Map<Edge, List<Tile>> edgeToTiles = new HashMap<>();
        for (Tile tile : tiles) {
            for (Edge edge : tile.edges()) {
                edgeToTiles.computeIfAbsent(edge, k -> new ArrayList<>()).add(tile);
            }
        }
        // Edges that appear in exactly one tile (unique edges)
        List<Edge> uniqueEdges = edgeToTiles.entrySet().stream()
                .filter(e -> e.getValue().size() == 1)
                .map(Entry::getKey)
                .collect(Collectors.toList());
        // Tiles that have 2 unique edges.
        long prod = 1;
        List<Tile> corners = new ArrayList<>();
        for (Tile tile : tiles) {
            List<Edge> dest = new ArrayList<>(uniqueEdges);
            dest.retainAll(tile.edges());
            if (dest.size() == 2) {
                prod *= tile.tileNum;
                corners.add(tile);
            }
        }
        System.out.println("Product of corners: " + prod);

        int N = 12;
        Tile[][] image = new Tile[N][N];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                Tile t;
                if (row == 0 && col == 0) {
                    // Place a corner edge on the north-west corner
                    t = corners.get(1);
                    // Orient it so that its unique edges are facing outside
                    orient(t, uniqueEdges, uniqueEdges);
                } else if (col == 0) {
                    Tile above = image[row - 1][col];
                    Edge north = above.south;
                    List<Tile> two = edgeToTiles.get(north);
                    t = two.get(0);
                    if (t == above) {
                        t = two.get(1);
                    }
                    // For the first column (other than the corner) the north edge needs to
                    // match the edge above, and the west image is unique
                    orient(t, uniqueEdges, Collections.singletonList(north));
                } else {
                        Tile left = image[row][col - 1];
                        Edge west = left.east;
                        List<Tile> two = edgeToTiles.get(west);
                        t = two.get(0);
                        if (t == left) {
                            t = two.get(1);
                        }
                        List<Edge> toNorth;
                        if (row == 0) {
                            toNorth = uniqueEdges;
                        } else {
                            toNorth = Collections.singletonList(image[row - 1][col].south);
                        }
                        orient(t, Collections.singletonList(west), toNorth);
                }
                image[row][col] = t;
            }
        }
        int NN = 12 * (10 -2); // 12 tiles per row/col, each one has 10 digits, and remove the edges.
        String[][] actualImage = new String[NN][];
        int l = 0;
        for (int row = 0; row < N; row++) {
            for (int lineNum = 1; lineNum < 9; lineNum++) {
                String line = "";
                for (int col = 0; col < N; col++) {
                    line += image[row][col].lines.get(lineNum).substring(1, 9);
                }
                actualImage[l++] = line.split("");
            }
        }

        // x, y are the coordinates of the MONSTER digits
        int M = MONSTER.stream().map(s -> s.replaceAll(" ", "")).mapToInt(String::length).sum();
        int[] x = new int[M];
        int[] y = new int[M];
        int k = 0;
        for (int i = 0; i < MONSTER.size(); i++) {
            String m = MONSTER.get(i);
            for (int j = 0; j < m.length(); j++) {
                if (m.charAt(j) == "#".charAt(0)) {
                    x[k] = i;
                    y[k] = j;
                    k++;
                }
            }
        }

        // When starting with corner #1, no need to rotate or flip the actual image in order to find
        // monsters (which is not the case when starting with any of the other corners).
        for (int I = 0; I < NN - MONSTER.size(); I++) {
            for (int J = 0; J < NN - MONSTER.get(0).length(); J++) {
                int II = I;
                int JJ = J;
                boolean match = IntStream.range(0, x.length)
                        .noneMatch(i -> ".".equals(actualImage[II + x[i]][JJ + y[i]]));
                if (match) {
                    IntStream.range(0, x.length).forEach(i -> actualImage[II + x[i]][JJ + y[i]] = "O");
                }
            }
        }

        int roughness = 0;
        for (int i = 0; i < NN; i++) {
            for (int j = 0; j < NN; j++) {
                if ("#".equals(actualImage[i][j])) {
                    roughness++;
                }
            }
        }
        System.out.println("Roughness: " + roughness);
    }

    /**
     * Rotate and flip a tile until its west edge is a member of toWest and its north
     * edge is a member of toNorth.
     *
     * @param tile the tile to rotate
     * @param toWest list of possible west edges
     * @param toNorth list of possible north edges
     */
    private static void orient(Tile tile, List<Edge> toWest, List<Edge> toNorth) {
        int r = 0;
        while (!toWest.contains(tile.west) || !toNorth.contains(tile.north)) {
            tile.rotate();
            r++;
            if (r == 4) {
                tile.flip();
            }
            if (r == 8) {
                throw new RuntimeException("Could not find orientation");
            }
        }
    }

    private static List<Tile> parse() {
        List<Tile> tiles = new ArrayList<>();
        List<String> tile = new ArrayList<>();
        int tileNumber = -1;
        for (String next : Day20.lines) {
            if (next.isEmpty()) {
                tiles.add(new Tile(tileNumber, tile));
                tile = new ArrayList<>();
            } else if (next.contains("Tile")) {
                tileNumber = Integer.parseInt(next.split("[ :]")[1]);
            } else {
                tile.add(next);
            }
        }
        return tiles;
    }

    private static class Tile {
        int tileNum;
        Edge south;
        Edge north;
        Edge east;
        Edge west;
        List<String> lines = new ArrayList<>();

        Tile(int tileNum, List<String> lines) {
            this.tileNum = tileNum;
            this.lines.addAll(lines);
            north = new Edge(lines.get(0));
            south = new Edge(lines.get(lines.size() - 1));
            west = new Edge(lines.stream().map(l -> l.substring(0, 1)).collect(Collectors.joining()));
            east = new Edge(lines.stream().map(l -> l.substring(l.length() - 1)).collect(Collectors.joining()));
        }

        List<Edge> edges() {
            List<Edge> edges = new ArrayList<>();
            edges.add(south);
            edges.add(north);
            edges.add(east);
            edges.add(west);
            return edges;
        }
        /**
         * Flip north - south
         */
        void flip() {
            west = west.reverse();
            east = east.reverse();
            Edge temp = north;
            north = south;
            south = temp;
            flipLines();
        }

        /**
         * Rotate 90 degrees clockwise
         */
        void rotate() {
            Edge temp = north;
            north = west.reverse();
            west = south;
            south = east.reverse();
            east = temp;
            rotateLines();
        }

        private void rotateLines() {
            List<String> newLines = new ArrayList<>();
            int N = lines.size();
            for (int i = 0; i < N; i++) {
                String newLine = "";
                for (int j = 0; j < N; j++) {
                    newLine += lines.get(N - j - 1).substring(i, i + 1);
                }
                newLines.add(newLine);
            }
            lines = newLines;
        }

        private void flipLines() {
            Collections.reverse(lines);
        }

        public String toString() {
            return "{n:" + north
                    + " s:" + south
                    + " e:" + east
                    + " w:" + west
                    +"}";
        }
    }

    private static class Edge {
        final String value;

        Edge(String str) {
            value = str;
        }

        public Edge reverse() {
            return new Edge(Edge.reverse(value));
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Edge)) {
                return false;
            }
            Edge e = (Edge)o;
            return value.equals(e.value) || value.equals(Edge.reverse(e.value));
        }

        @Override
        public int hashCode() {
            return Math.min(value.hashCode(), Edge.reverse(value).hashCode());
        }

        static String reverse(String str) {
            return new StringBuffer().append(str).reverse().toString();
        }

        public String toString() {
            return value;
        }
    }
}
