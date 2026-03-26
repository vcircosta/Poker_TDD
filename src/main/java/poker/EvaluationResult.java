package poker;

import java.util.List;

public record EvaluationResult(
        HandCategory category,
        List<Card> chosen5
) implements Comparable<EvaluationResult> {

    @Override
    public int compareTo(EvaluationResult other) {
        int catComp = Integer.compare(this.category.getPriority(), other.category.getPriority());
        if (catComp != 0) return catComp;

        for (int i = 0; i < 5; i++) {
            int rankComp = Integer.compare(
                    this.chosen5.get(i).rank().getValue(),
                    other.chosen5.get(i).rank().getValue()
            );
            if (rankComp != 0) return rankComp;
        }
        return 0;
    }
}