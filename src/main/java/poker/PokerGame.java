package poker;

import java.util.*;

public class PokerGame {
    public record PlayerResult(String name, EvaluationResult result) {}

    public static List<String> findWinners(Map<String, List<Card>> playersHoleCards, List<Card> board) {
        List<PlayerResult> results = new ArrayList<>();

        for (var entry : playersHoleCards.entrySet()) {
            List<Card> fullHand = new ArrayList<>(board);
            fullHand.addAll(entry.getValue());
            results.add(new PlayerResult(entry.getKey(), HandEvaluator.evaluate(fullHand)));
        }

        EvaluationResult bestHand = results.stream()
                .map(PlayerResult::result)
                .max(EvaluationResult::compareTo)
                .orElseThrow();

        return results.stream()
                .filter(pr -> pr.result().compareTo(bestHand) == 0)
                .map(PlayerResult::name)
                .toList();
    }
}