package poker;

import java.util.List;

public record EvaluationResult(
        HandCategory category,
        List<Card> chosen5
) implements Comparable<EvaluationResult> {

    @Override
    public int compareTo(EvaluationResult other) {
        // 1. Comparer les catégories (ex: Flush > Straight) [cite: 23, 35, 36]
        int catComp = Integer.compare(this.category.getPriority(), other.category.getPriority());
        if (catComp != 0) return catComp;

        // 2. Si même catégorie, comparer les cartes une par une (Tie-break) [cite: 36, 37]
        for (int i = 0; i < 5; i++) {
            int rankComp = Integer.compare(
                    this.chosen5.get(i).rank().getValue(),
                    other.chosen5.get(i).rank().getValue()
            );
            if (rankComp != 0) return rankComp;
        }
        return 0; // Égalité parfaite [cite: 37]
    }
}