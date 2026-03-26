package poker;

import java.util.*;
import java.util.stream.Collectors;

public class HandEvaluator {

    public static EvaluationResult evaluate(List<Card> cards) {
        // Génère les 21 combinaisons possibles (5 parmi 7) [cite: 20]
        return generateCombinations(cards, 5).stream()
                .map(HandEvaluator::evaluateFiveCards)
                .max(EvaluationResult::compareTo)
                .orElseThrow();
    }

    private static EvaluationResult evaluateFiveCards(List<Card> fiveCards) {
        // 1. Calcul des fréquences (Evite les doublons de calcul)
        Map<Rank, Long> counts = fiveCards.stream()
                .collect(Collectors.groupingBy(Card::rank, Collectors.counting()));

        // 2. Tri par importance (Fréquence, puis Valeur)
        List<Card> sorted = fiveCards.stream()
                .sorted((c1, c2) -> {
                    long count1 = counts.get(c1.rank());
                    long count2 = counts.get(c2.rank());
                    if (count1 != count2) return Long.compare(count2, count1);
                    return Integer.compare(c2.rank().getValue(), c1.rank().getValue());
                }).toList();

        // 3. Détection des catégories (Ordre décroissant obligatoire) [cite: 24]
        boolean isFlush = fiveCards.stream().map(Card::suit).distinct().count() == 1;
        Optional<List<Card>> straightCards = getStraightCards(fiveCards);

        if (isFlush && straightCards.isPresent()) return new EvaluationResult(HandCategory.STRAIGHT_FLUSH, straightCards.get());
        if (counts.containsValue(4L)) return new EvaluationResult(HandCategory.FOUR_OF_A_KIND, sorted);
        if (counts.containsValue(3L) && counts.containsValue(2L)) return new EvaluationResult(HandCategory.FULL_HOUSE, sorted);
        if (isFlush) return new EvaluationResult(HandCategory.FLUSH, sorted);
        if (straightCards.isPresent()) return new EvaluationResult(HandCategory.STRAIGHT, straightCards.get());
        if (counts.containsValue(3L)) return new EvaluationResult(HandCategory.THREE_OF_A_KIND, sorted);

        long pairCount = counts.values().stream().filter(v -> v == 2).count();
        if (pairCount == 2) return new EvaluationResult(HandCategory.TWO_PAIR, sorted);
        if (pairCount == 1) return new EvaluationResult(HandCategory.ONE_PAIR, sorted);

        return new EvaluationResult(HandCategory.HIGH_CARD, sorted);
    }

    // Gestion de la Quinte incluant l'As-bas (Wheel)
    private static Optional<List<Card>> getStraightCards(List<Card> cards) {
        List<Card> distinctSorted = cards.stream()
                .sorted(Comparator.comparingInt((Card c) -> c.rank().getValue()).reversed())
                .toList();

        // Cas spécial de l'As-bas (A, 2, 3, 4, 5) [cite: 40, 74, 133]
        boolean hasAce = distinctSorted.get(0).rank() == Rank.ACE;
        boolean isWheel = hasAce && distinctSorted.get(1).rank() == Rank.FIVE && distinctSorted.get(4).rank() == Rank.TWO;

        if (isWheel) {
            List<Card> wheel = new ArrayList<>(distinctSorted.subList(1, 5));
            wheel.add(distinctSorted.get(0)); // L'As à la fin [cite: 74]
            return Optional.of(wheel);
        }

        for (int i = 0; i < distinctSorted.size() - 4; i++) {
            if (distinctSorted.get(i).rank().getValue() - distinctSorted.get(i + 4).rank().getValue() == 4) {
                return Optional.of(distinctSorted.subList(i, i + 5));
            }
        }
        return Optional.empty();
    }

    private static List<List<Card>> generateCombinations(List<Card> cards, int k) {
        List<List<Card>> result = new ArrayList<>();
        helper(cards, k, 0, new ArrayList<>(), result);
        return result;
    }

    private static void helper(List<Card> cards, int k, int start, List<Card> current, List<List<Card>> result) {
        if (current.size() == k) {
            result.add(new ArrayList<>(current));
            return;
        }
        for (int i = start; i < cards.size(); i++) {
            current.add(cards.get(i));
            helper(cards, k, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }
}