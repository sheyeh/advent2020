package advent2020;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Day20 {
    private static List<String> lines = FileReader.readFile("src/main/resources/day20input.txt");

    /**
     * The solution is based on the fact that there are exactly four tiles where each of
     * them has 2 edges that no other tile has, and those have to be in the corners.
     * Also, each other edge appears in exactly two tiles. This means that given a tile, we
     * know for sure who are its neighbors.
     */
    public static void main(String... args) {
        Map<Integer, Tile> tiles = parse(lines);
        System.out.println("Done reading");
        // Mop from edge to tiles that have this edge
        Map<Edge, List<Integer>> edgeToTileNums = new HashMap<>();
        Map<Edge, List<Tile>> edgeToTiles = new HashMap<>();
        for (Map.Entry<Integer, Tile> entry : tiles.entrySet()) {
            int tileNum  = entry.getKey();
            Tile tile = entry.getValue();
            for (Edge edge : tile.edges()) {
                edgeToTileNums.computeIfAbsent(edge, k -> new ArrayList<>()).add(tileNum);
                edgeToTiles.computeIfAbsent(edge, k -> new ArrayList<>()).add(tile);
            }
        }
        // Edges that appear in exactly one tile (unique edges)
        List<Edge> uniqueEdges = edgeToTileNums.entrySet().stream()
                .filter(e -> e.getValue().size() == 1)
                .map(e -> e.getKey())
                .collect(Collectors.toList());
        // Tiles that have 2 unique edges.
        long prod = 1;
        List<Tile> corners = new ArrayList<>();
        for (Map.Entry<Integer, Tile> entry : tiles.entrySet()) {
            List<Edge> dest = new ArrayList<>();
            dest.addAll(uniqueEdges);
            dest.retainAll(entry.getValue().edges());
            if (dest.size() == 2) {
                prod *= entry.getKey();
                corners.add(entry.getValue());
            }
        }
        System.out.println(prod);

        int N = 12;

        Tile[][] image = new Tile[N][N];
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                Tile t;
                if (row == 0 && col == 0) {
                    t = corners.get(0);
                    orient(t, uniqueEdges, uniqueEdges);
                } else if (col == 0) {
                    Tile above = image[row - 1][col];
                    Edge north = above.south;
                    List<Tile> two = edgeToTiles.get(north);
                    t = two.get(0);
                    if (t == above) {
                        t = two.get(1);
                    }
                    orient(t, uniqueEdges, Collections.singletonList(north));
                } else {
                        Tile left = image[row][col - 1];
                        Edge west = left.east;
                        List<Tile> two = edgeToTiles.get(west);
                        t = two.get(0);
                        if (t == left) {
                            t = two.get(1);
                        }
                        List toNorth;
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
        System.out.println("Done placing tiles");

    }

    /**
     * Rotate and flip a tile until its west edge is a member of toWest and its north
     * edge is a member of toNorth.
     *
     * @param tile the tile to rotate
     * @param toWest list of possible west edges
     * @param toNorth list of possible north edges
     * @return the rotated tile
     */
    private static Tile orient(Tile tile, List<Edge> toWest, List<Edge> toNorth) {
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
        return tile;
    }

    private static Map<Integer, Tile> parse(List<String> lines) {
        Map<Integer, Tile> tiles = new HashMap<>();
        List<String> tile = new ArrayList<>();
        int tileNumber = -1;
        for (String next : lines) {
            if (next.isEmpty()) {
                tiles.put(tileNumber, new Tile(tile));
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
        Edge south;
        Edge north;
        Edge east;
        Edge west;
        List<String> lines = new ArrayList<>();

        Tile(List<String> lines) {
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
        Tile flip() {
            west = west.reverse();
            east = east.reverse();
            Edge temp = north;
            north = south;
            south = temp;
            flipLines();
            return this;
        }

        /**
         * Rotate 90 degrees clockwise
         */
        Tile rotate() {
            Edge temp = north;
            north = west.reverse();
            west = south;
            south = east.reverse();
            east = temp;
            rotateLines();
            return this;
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
