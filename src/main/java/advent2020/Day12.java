package advent2020;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day12 {
    private static final List<Instruction> instructions =
            FileReader.readFile("src/main/resources/day12input.txt").stream()
            .map(Instruction::new).collect(Collectors.toList());

    public static void main(String... args) {
        q1();
        q2();
    }

    private static void q1() {
        State state = new State(Action.E, 0, 0);
        instructions.stream().forEach(state::move);
        System.out.println(state.manhattanCoordinates());
    }

    private static void q2() {
        StateWithWaypoint state = new StateWithWaypoint(0, 0, 1, 10);
        instructions.stream().forEach(state::move);
        System.out.println(state.manhattanCoordinates());
    }

    private static class Instruction {
        final Action direction;
        final int value;

        Instruction(String instruction) {
            direction = Action.valueOf(instruction.substring(0, 1));
            value = Integer.valueOf(instruction.substring(1));
        }

        public String toString() {
            return direction + ":" + value;
        }
    }

    private static class State {
        Action facing; // One of N, E, S, E
        Position coordinates;

        private State(Action face, int x, int y) {
            facing = face;
            coordinates = new Position(x, y);
        }

        private void move(Instruction move) {
            switch (move.direction) {
                case R: rotate(move.value); break;
                case L: rotate(-move.value); break;
                case F: forward(move.value); break;
                case N: coordinates.moveNorth(move.value); break;
                case E: coordinates.moveEast(move.value); break;
                case S: coordinates.moveNorth(-move.value); break;
                case W: coordinates.moveEast(-move.value); break;
            }
        }

        private void rotate(int degrees) {
            int steps = degrees / 90;
            facing = Action.values()[((facing.ordinal() + steps) + 4) %4];
        }

        private void forward(int step) {
            coordinates.moveNorth(facing.northStep * step);
            coordinates.moveEast(facing.eastStep * step);
        }

        private int manhattanCoordinates() {
            return coordinates.manhattanCoordinates();
        }

        public String toString() {
            return "Facing " + facing + " at " + coordinates;
        }
    }

    private static class StateWithWaypoint {
        Position coordinates;
        Position waypoint;

        private StateWithWaypoint(int x, int y, int wpx, int wpy) {
            coordinates = new Position(x, y);
            waypoint = new Position(wpx, wpy);
        }

        private void move(Instruction move) {
            switch (move.direction) {
                case R: waypoint.rotate(move.value / 90); break;
                case L: waypoint.rotate(-move.value / 90); break;
                case F: forward(move.value); break;
                case N: waypoint.moveNorth(move.value); break;
                case E: waypoint.moveEast(move.value); break;
                case S: waypoint.moveNorth(-move.value); break;
                case W: waypoint.moveEast(-move.value); break;
            }
        }

        /**
         * Move in the direction of the waypoint.
         * @param step number of steps to move
         */
        private void forward(int step) {
            coordinates.moveNorth(step * waypoint.north);
            coordinates.moveEast(step * waypoint.east);
        }

        public String toString() {
            return "Coordinates: " + coordinates + " Waypoint: " + waypoint;
        }

        private int manhattanCoordinates() {
            return coordinates.manhattanCoordinates();
        }
    }

    private static class Position {
        int north;
        int east;

        Position(int x, int y) {
            north = x;
            east = y;
        }

        void moveNorth(int val) {
            north += val;
        }

        void moveEast(int val) {
            east += val;
        }

        /**
         * Rotate left 90 degrees.
         */
        void rotate() {
            int temp = north;
            north = -east;
            east = temp;
        }

        /**
         * Rotate left the number of steps, each one is 90 degrees.
         * To rotate right call this with negative argument.
         * @param steps
         */
        void rotate(int steps) {
            while (steps < 0) {
                steps += 4;
            }
            IntStream.range(0, steps).forEach(i -> this.rotate());
        }

        public String toString() {
            return "[" + east + "E, " + north + "N]";
        }

        public int manhattanCoordinates() {
            return Math.abs(east) + Math.abs(north);
        }
    }

    private enum Action {
        N(1, 0), E(0, 1), S(-1, 0), W(0, -1), // the order is from north clockwise
        F(), R(), L();

        // Used to determine the steps in each direction when instructed to move "Forward".
        int northStep;
        int eastStep;

        Action(int x, int y) {
            northStep = x;
            eastStep = y;
        }

        // Steps are not relevant for F, R, L.
        Action() {
            northStep = 0;
            eastStep = 0;
        }
    }
}
