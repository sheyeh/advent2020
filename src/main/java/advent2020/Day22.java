package advent2020;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class Day22 {
    private static final List<String> lines = FileReader.readFile("src/main/resources/day22input.txt");
    private static List<Queue<Integer>> queues = parse(lines);
    private static int numCards = queues.stream().mapToInt(Queue::size).sum();
    private static int coef = numCards;

    public static void main(String... args) {
        Queue<Integer> winningPlayer = null;
        while (queues.stream().map(Collection::size).noneMatch(s -> s == numCards)) {
            List<Integer> cards = draw();
            int winner = winner(cards);
            winningPlayer = queues.get(winner);
            winningPlayer.addAll(cards.stream()
                    .sorted((a, b) -> - Integer.compare(a, b))
                    .collect(Collectors.toList()));
        }
        long score = winningPlayer.stream().mapToInt(card -> card * coef()).sum();
        System.out.println(score);
    }

    private static int coef() {
        return coef--;
    }
    private static int winner(List<Integer> cards) {
        int max = -1;
        int winner = -1;
        for (int i = 0; i < queues.size(); i++) {
            if (cards.get(i) > max) {
                max = cards.get(i);
                winner = i;
            }
        }
        return winner;
    }
    private static List<Integer> draw() {
        return queues.stream()
                .map(Queue::poll)
                .collect(Collectors.toList());
    }


    private static List<Queue<Integer>> parse(List<String> lines) {
        List<Queue<Integer>> queues = new ArrayList<>();
        Queue<Integer> queue = null;
        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            } else if (line.startsWith("Player")) {
                queue = new LinkedList<>();
                queues.add(queue);
            } else {
                queue.add(Integer.parseInt(line));
            }
        }
        return queues;
    }
}
