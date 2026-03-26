package poker;

import java.util.*;
import java.util.stream.Collectors;

public class Combinaison implements Comparable<Combinaison> {

    public enum Category {
        HIGH_CARD(1, "High card"),
        ONE_PAIR(2, "One pair"),
        TWO_PAIR(3, "Two pair"),
        THREE_OF_A_KIND(4, "Three of a kind"),
        STRAIGHT(5, "Straight"),
        FLUSH(6, "Flush"),
        FULL_HOUSE(7, "Full house"),
        FOUR_OF_A_KIND(8, "Four of a kind"),
        STRAIGHT_FLUSH(9, "Straight flush");

        private final int rank;
        private final String label;

        Category(int rank, String label) {
            this.rank = rank;
            this.label = label;
        }

        public int getRank() {
            return rank;
        }

        @Override
        public String toString() {
            return label;
        }
    }

    private final Category category;
    private final List<Carte> cards; // the 5 best cards
    private final int[] tiebreakers; // ordered values used to break ties within the same category

    private Combinaison(Category category, List<Carte> cards, int[] tiebreakers) {
        this.category = category;
        this.cards = Collections.unmodifiableList(cards);
        this.tiebreakers = tiebreakers;
    }

    public Category getCategory() {
        return category;
    }

    public List<Carte> getCards() {
        return cards;
    }

    // -------------------------------------------------------------------------
    // Best hand detection from 7 cards (Texas Hold'em)
    // -------------------------------------------------------------------------

    /**
     * Finds the best 5-card hand from a list of cards (minimum 5).
     */
    public static Combinaison bestHand(List<Carte> sevenCards) {
        if (sevenCards == null || sevenCards.size() < 5) {
            throw new IllegalArgumentException("At least 5 cards are required.");
        }

        List<List<Carte>> allFiveCardHands = fiveCardSubsets(sevenCards);
        Combinaison best = null;
        for (List<Carte> five : allFiveCardHands) {
            Combinaison c = evaluate(five);
            if (best == null || c.compareTo(best) > 0) {
                best = c;
            }
        }
        return best;
    }

    /**
     * Generates all C(n, 5) subsets of 5 cards from the given list.
     */
    private static List<List<Carte>> fiveCardSubsets(List<Carte> cards) {
        List<List<Carte>> result = new ArrayList<>();
        int n = cards.size();
        for (int i = 0; i < n - 4; i++) {
            for (int j = i + 1; j < n - 3; j++) {
                for (int k = j + 1; k < n - 2; k++) {
                    for (int l = k + 1; l < n - 1; l++) {
                        for (int m = l + 1; m < n; m++) {
                            result.add(Arrays.asList(
                                    cards.get(i), cards.get(j), cards.get(k),
                                    cards.get(l), cards.get(m)));
                        }
                    }
                }
            }
        }
        return result;
    }

    // -------------------------------------------------------------------------
    // 5-card hand evaluation
    // -------------------------------------------------------------------------

    public static Combinaison evaluate(List<Carte> five) {
        List<Integer> ranks = five.stream()
                .map(c -> c.getRank().getValue())
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        boolean flush = isFlush(five);
        int straightHeight = straightHeight(ranks);

        if (flush && straightHeight > 0) {
            return new Combinaison(Category.STRAIGHT_FLUSH, five, new int[]{straightHeight});
        }

        Map<Integer, Long> freq = countFrequencies(ranks);
        List<Integer> byFreqDesc = byFrequencyThenRankDesc(freq);

        // Four of a kind
        if (freq.containsValue(4L)) {
            int quadRank = byFreqDesc.get(0);
            int kicker = byFreqDesc.get(1);
            return new Combinaison(Category.FOUR_OF_A_KIND, five, new int[]{quadRank, kicker});
        }

        // Full house
        if (freq.containsValue(3L) && freq.containsValue(2L)) {
            int tripRank = byFreqDesc.get(0);
            int pairRank = byFreqDesc.get(1);
            return new Combinaison(Category.FULL_HOUSE, five, new int[]{tripRank, pairRank});
        }

        // Flush
        if (flush) {
            int[] vals = ranks.stream().mapToInt(Integer::intValue).toArray();
            return new Combinaison(Category.FLUSH, five, vals);
        }

        // Straight
        if (straightHeight > 0) {
            return new Combinaison(Category.STRAIGHT, five, new int[]{straightHeight});
        }

        // Three of a kind
        if (freq.containsValue(3L)) {
            int tripRank = byFreqDesc.get(0);
            List<Integer> kickers = byFreqDesc.subList(1, 3);
            return new Combinaison(Category.THREE_OF_A_KIND, five,
                    new int[]{tripRank, kickers.get(0), kickers.get(1)});
        }

        // Two pair
        long pairCount = freq.values().stream().filter(v -> v == 2L).count();
        if (pairCount == 2) {
            int highPair = byFreqDesc.get(0);
            int lowPair = byFreqDesc.get(1);
            int kicker = byFreqDesc.get(2);
            return new Combinaison(Category.TWO_PAIR, five,
                    new int[]{highPair, lowPair, kicker});
        }

        // One pair
        if (freq.containsValue(2L)) {
            int pairRank = byFreqDesc.get(0);
            List<Integer> kickers = byFreqDesc.subList(1, 4);
            return new Combinaison(Category.ONE_PAIR, five,
                    new int[]{pairRank, kickers.get(0), kickers.get(1), kickers.get(2)});
        }

        // High card
        int[] vals = ranks.stream().mapToInt(Integer::intValue).toArray();
        return new Combinaison(Category.HIGH_CARD, five, vals);
    }

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private static boolean isFlush(List<Carte> five) {
        Carte.Suit ref = five.get(0).getSuit();
        return five.stream().allMatch(c -> c.getSuit() == ref);
    }

    /**
     * Returns the height of the straight (rank of the highest card),
     * or 0 if the hand is not a straight.
     * Special case: A-2-3-4-5 (wheel) is a valid straight with height 5.
     */
    private static int straightHeight(List<Integer> ranksDesc) {
        // ranksDesc is already sorted descending
        boolean normal = true;
        for (int i = 0; i < 4; i++) {
            if (ranksDesc.get(i) - ranksDesc.get(i + 1) != 1) {
                normal = false;
                break;
            }
        }
        if (normal) return ranksDesc.get(0);

        // Wheel: A-2-3-4-5 (ranks: 14, 5, 4, 3, 2)
        List<Integer> wheel = Arrays.asList(14, 5, 4, 3, 2);
        if (new HashSet<>(ranksDesc).equals(new HashSet<>(wheel))) return 5;

        return 0;
    }

    private static Map<Integer, Long> countFrequencies(List<Integer> ranks) {
        return ranks.stream().collect(Collectors.groupingBy(r -> r, Collectors.counting()));
    }

    /**
     * Returns ranks sorted by descending frequency, then by descending rank value.
     */
    private static List<Integer> byFrequencyThenRankDesc(Map<Integer, Long> freq) {
        return freq.entrySet().stream()
                .sorted(Map.Entry.<Integer, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.<Integer, Long>comparingByKey(Comparator.reverseOrder())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // -------------------------------------------------------------------------
    // Comparison
    // -------------------------------------------------------------------------

    /**
     * Compares two hands. Returns > 0 if this hand is better, < 0 if worse, 0 if tied.
     */
    @Override
    public int compareTo(Combinaison other) {
        int cmp = Integer.compare(this.category.getRank(), other.category.getRank());
        if (cmp != 0) return cmp;

        int len = Math.min(this.tiebreakers.length, other.tiebreakers.length);
        for (int i = 0; i < len; i++) {
            cmp = Integer.compare(this.tiebreakers[i], other.tiebreakers[i]);
            if (cmp != 0) return cmp;
        }
        return 0;
    }

    /**
     * Returns the best hand(s) from a list (may return multiple in case of a tie).
     */
    public static List<Combinaison> best(List<Combinaison> hands) {
        if (hands == null || hands.isEmpty()) return Collections.emptyList();

        Combinaison max = Collections.max(hands);
        return hands.stream()
                .filter(c -> c.compareTo(max) == 0)
                .collect(Collectors.toList());
    }

    /**
     * Compares two hands of the same category.
     * Returns 1 if a wins, -1 if b wins, 0 if tied.
     * Throws IllegalArgumentException if categories differ.
     */
    public static int compareSameCategory(Combinaison a, Combinaison b) {
        if (a.category != b.category) {
            throw new IllegalArgumentException(
                    "Both hands must have the same category.");
        }
        return a.compareTo(b);
    }

    // -------------------------------------------------------------------------
    // Display
    // -------------------------------------------------------------------------

    @Override
    public String toString() {
        String cardsStr = cards.stream()
                .map(Carte::toString)
                .collect(Collectors.joining(", "));
        return category + " [" + cardsStr + "]";
    }
}
