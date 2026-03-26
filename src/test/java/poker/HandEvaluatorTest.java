package poker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

class HandEvaluatorTest {

    @Test
    void should_identify_high_card() {
        List<Card> cards = List.of(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.NINE, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.CLUBS),
                new Card(Rank.TWO, Suit.SPADES)
        );

        var result = HandEvaluator.evaluate(cards);
        assertEquals(HandCategory.HIGH_CARD, result.category());
    }

    @Test
    void should_identify_one_pair() {
        List<Card> cards = List.of(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.FOUR, Suit.SPADES)
        );

        var result = HandEvaluator.evaluate(cards);
        assertEquals(HandCategory.ONE_PAIR, result.category());
        assertEquals(Rank.ACE, result.chosen5().get(0).rank());
    }

    @Test
    void should_identify_two_pair() {
        List<Card> cards = List.of(
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.NINE, Suit.DIAMONDS),
                new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.ACE, Suit.SPADES)
        );

        var result = HandEvaluator.evaluate(cards);
        assertEquals(HandCategory.TWO_PAIR, result.category());
    }

    @Test
    void should_identify_three_of_a_kind() {
        List<Card> cards = List.of(
                new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.DIAMONDS),
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.TWO, Suit.SPADES)
        );

        var result = HandEvaluator.evaluate(cards);
        assertEquals(HandCategory.THREE_OF_A_KIND, result.category());
    }

    @Test
    void should_identify_straight() {
        List<Card> cards = List.of(
                new Card(Rank.SIX, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.DIAMONDS),
                new Card(Rank.NINE, Suit.CLUBS),
                new Card(Rank.TEN, Suit.SPADES)
        );

        var result = HandEvaluator.evaluate(cards);
        assertEquals(HandCategory.STRAIGHT, result.category());
        assertEquals(Rank.TEN, result.chosen5().get(0).rank());
        assertEquals(Rank.SIX, result.chosen5().get(4).rank());
    }

    @Test
    void should_identify_straight_flush() {
        List<Card> cards = List.of(
                new Card(Rank.FIVE, Suit.HEARTS),
                new Card(Rank.SIX, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.HEARTS),
                new Card(Rank.NINE, Suit.HEARTS)
        );

        var result = HandEvaluator.evaluate(cards);
        assertEquals(HandCategory.STRAIGHT_FLUSH, result.category());
        assertEquals(Rank.NINE, result.chosen5().get(0).rank());
        assertEquals(Rank.FIVE, result.chosen5().get(4).rank());
    }

    @Test
    void should_identify_flush() {
        List<Card> cards = List.of(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.FOUR, Suit.HEARTS),
                new Card(Rank.TWO, Suit.HEARTS)
        );

        var result = HandEvaluator.evaluate(cards);
        assertEquals(HandCategory.FLUSH, result.category());
    }

    @Test
    void should_select_best_five_from_seven_cards() {
        List<Card> sevenCards = List.of(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.TWO, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.CLUBS),
                new Card(Rank.SIX, Suit.SPADES),
                new Card(Rank.EIGHT, Suit.HEARTS),
                new Card(Rank.TEN, Suit.DIAMONDS)
        );

        var result = HandEvaluator.evaluate(sevenCards);
        assertEquals(5, result.chosen5().size());
        assertEquals(Rank.ACE, result.chosen5().get(0).rank());
    }
    @Test
    void should_find_winner_among_multiple_players() {
        List<Card> board = List.of(
                new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.KING, Suit.DIAMONDS),
                new Card(Rank.TEN, Suit.DIAMONDS), new Card(Rank.EIGHT, Suit.SPADES),
                new Card(Rank.TWO, Suit.CLUBS)
        );

        List<Card> p1Hole = List.of(new Card(Rank.QUEEN, Suit.DIAMONDS), new Card(Rank.JACK, Suit.DIAMONDS)); // Royal Flush
        List<Card> p2Hole = List.of(new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.CLUBS));        // Brelan d'As

        var p1Best = HandEvaluator.evaluate(combine(board, p1Hole));
        var p2Best = HandEvaluator.evaluate(combine(board, p2Hole));

        assertTrue(p1Best.compareTo(p2Best) > 0);
    }

    @Test
    void should_identify_full_house_and_sort_by_triplet_then_pair() {
        List<Card> cards = List.of(
                new Card(Rank.TEN, Suit.SPADES), new Card(Rank.TEN, Suit.HEARTS), new Card(Rank.TEN, Suit.DIAMONDS),
                new Card(Rank.TWO, Suit.CLUBS), new Card(Rank.TWO, Suit.SPADES)
        );

        var result = HandEvaluator.evaluate(cards);

        assertEquals(HandCategory.FULL_HOUSE, result.category());
        assertEquals(Rank.TEN, result.chosen5().get(0).rank());
        assertEquals(Rank.TWO, result.chosen5().get(3).rank());
    }

    @Test
    void should_identify_ace_low_straight_as_five_high() {
        List<Card> cards = List.of(
                new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.THREE, Suit.DIAMONDS),
                new Card(Rank.FOUR, Suit.CLUBS),
                new Card(Rank.FIVE, Suit.SPADES)
        );

        var result = HandEvaluator.evaluate(cards);

        assertEquals(HandCategory.STRAIGHT, result.category());
        assertEquals(Rank.FIVE, result.chosen5().get(0).rank());
        assertEquals(Rank.ACE, result.chosen5().get(4).rank());
    }

    @Test
    void should_decide_winner_with_kicker_when_quads_on_board() {
        List<Card> board = List.of(
                new Card(Rank.SEVEN, Suit.SPADES), new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.DIAMONDS), new Card(Rank.SEVEN, Suit.CLUBS),
                new Card(Rank.TWO, Suit.HEARTS)
        );

        List<Card> p1Hole = List.of(new Card(Rank.ACE, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS));
        List<Card> p2Hole = List.of(new Card(Rank.QUEEN, Suit.SPADES), new Card(Rank.JACK, Suit.HEARTS));

        var p1Best = HandEvaluator.evaluate(combine(board, p1Hole));
        var p2Best = HandEvaluator.evaluate(combine(board, p2Hole));

        assertTrue(p1Best.compareTo(p2Best) > 0);
    }

    @Test
    void deck_should_contain_52_unique_cards() {
        Deck deck = new Deck();
        Set<Card> drawnCards = new HashSet<>();

        for (int i = 0; i < 52; i++) {
            drawnCards.add(deck.draw());
        }

        assertEquals(52, drawnCards.size());
        assertThrows(IllegalStateException.class, deck::draw); // Vérifie l'erreur quand vide
    }

    @Test
    void main_method_should_run_without_errors() {
        assertDoesNotThrow(() -> Main.main(new String[]{}));
    }

    @Test
    void should_identify_ace_high_straight() {
        // Example B: 10♣ J♦ Q♥ K♠ 2♦ / A♣ 3♦ → Straight A-high
        List<Card> sevenCards = List.of(
                new Card(Rank.TEN, Suit.CLUBS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.TWO, Suit.DIAMONDS),
                new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.THREE, Suit.DIAMONDS)
        );

        var result = HandEvaluator.evaluate(sevenCards);

        assertEquals(HandCategory.STRAIGHT, result.category());
        assertEquals(Rank.ACE, result.chosen5().get(0).rank());
        assertEquals(Rank.TEN, result.chosen5().get(4).rank());
    }

    @Test
    void should_identify_four_of_a_kind() {
        List<Card> cards = List.of(
                new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.THREE, Suit.SPADES)
        );

        var result = HandEvaluator.evaluate(cards);
        assertEquals(HandCategory.FOUR_OF_A_KIND, result.category());
        assertEquals(Rank.JACK, result.chosen5().get(0).rank());
        assertEquals(Rank.THREE, result.chosen5().get(4).rank());
    }

    @Test
    void wrap_around_straight_is_invalid() {
        // Q K A 2 3 must NOT be detected as a straight (no wrap-around)
        List<Card> cards = List.of(
                new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.ACE, Suit.DIAMONDS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.THREE, Suit.SPADES)
        );

        var result = HandEvaluator.evaluate(cards);
        assertNotEquals(HandCategory.STRAIGHT, result.category());
    }

    @Test
    void should_identify_wheel_straight_flush() {
        // A♥ 2♥ 3♥ 4♥ 5♥ → straight flush 5-high
        List<Card> cards = List.of(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.TWO, Suit.HEARTS),
                new Card(Rank.THREE, Suit.HEARTS),
                new Card(Rank.FOUR, Suit.HEARTS),
                new Card(Rank.FIVE, Suit.HEARTS)
        );

        var result = HandEvaluator.evaluate(cards);
        assertEquals(HandCategory.STRAIGHT_FLUSH, result.category());
        assertEquals(Rank.FIVE, result.chosen5().get(0).rank());
        assertEquals(Rank.ACE, result.chosen5().get(4).rank());
    }

    @Test
    void straight_flush_tiebreak_higher_wins() {
        // 9♥ 8♥ 7♥ 6♥ 5♥  vs  8♠ 7♠ 6♠ 5♠ 4♠ → first wins (9-high > 8-high)
        var high = HandEvaluator.evaluate(List.of(
                new Card(Rank.NINE, Suit.HEARTS), new Card(Rank.EIGHT, Suit.HEARTS),
                new Card(Rank.SEVEN, Suit.HEARTS), new Card(Rank.SIX, Suit.HEARTS),
                new Card(Rank.FIVE, Suit.HEARTS)));
        var low = HandEvaluator.evaluate(List.of(
                new Card(Rank.EIGHT, Suit.SPADES), new Card(Rank.SEVEN, Suit.SPADES),
                new Card(Rank.SIX, Suit.SPADES), new Card(Rank.FIVE, Suit.SPADES),
                new Card(Rank.FOUR, Suit.SPADES)));

        assertEquals(HandCategory.STRAIGHT_FLUSH, high.category());
        assertEquals(HandCategory.STRAIGHT_FLUSH, low.category());
        assertTrue(high.compareTo(low) > 0);
    }

    @Test
    void should_pick_best_five_flush_cards_when_six_suited_available() {
        // Example C: A♥ J♥ 9♥ 4♥ 2♣ / 6♥ K♦ → chosen5 = A♥ J♥ 9♥ 6♥ 4♥ (2♥ dropped)
        List<Card> sevenCards = List.of(
                new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.NINE, Suit.HEARTS),
                new Card(Rank.FOUR, Suit.HEARTS),
                new Card(Rank.TWO, Suit.CLUBS),
                new Card(Rank.SIX, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS)
        );

        var result = HandEvaluator.evaluate(sevenCards);

        assertEquals(HandCategory.FLUSH, result.category());
        assertEquals(5, result.chosen5().size());
        assertEquals(Rank.ACE, result.chosen5().get(0).rank());
        assertEquals(Rank.JACK, result.chosen5().get(1).rank());
        assertEquals(Rank.NINE, result.chosen5().get(2).rank());
        assertEquals(Rank.SIX, result.chosen5().get(3).rank());
        assertEquals(Rank.FOUR, result.chosen5().get(4).rank());
    }

    // -------------------------------------------------------------------------
    // Tiebreakers
    // -------------------------------------------------------------------------

    @Test
    void two_pair_tiebreak_kicker_decides_when_pairs_equal() {
        // A A K K Q  vs  A A K K J → first wins (Q kicker > J)
        var high = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.SPADES)));
        var low = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.KING, Suit.DIAMONDS), new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.JACK, Suit.DIAMONDS)));

        assertEquals(HandCategory.TWO_PAIR, high.category());
        assertEquals(HandCategory.TWO_PAIR, low.category());
        assertTrue(high.compareTo(low) > 0);
    }

    @Test
    void one_pair_tiebreak_pair_rank_decides() {
        // A A 9 7 5  vs  K K 9 7 5 → first wins (pair A > pair K)
        var high = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.NINE, Suit.SPADES), new Card(Rank.SEVEN, Suit.SPADES),
                new Card(Rank.FIVE, Suit.CLUBS)));
        var low = HandEvaluator.evaluate(List.of(
                new Card(Rank.KING, Suit.DIAMONDS), new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.NINE, Suit.DIAMONDS), new Card(Rank.SEVEN, Suit.DIAMONDS),
                new Card(Rank.FIVE, Suit.DIAMONDS)));

        assertEquals(HandCategory.ONE_PAIR, high.category());
        assertEquals(HandCategory.ONE_PAIR, low.category());
        assertTrue(high.compareTo(low) > 0);
    }

    @Test
    void four_of_a_kind_tiebreak_quad_rank_decides() {
        // A A A A 2  vs  K K K K A → first wins (quad A > quad K)
        var high = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.TWO, Suit.SPADES)));
        var low = HandEvaluator.evaluate(List.of(
                new Card(Rank.KING, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS), new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.ACE, Suit.HEARTS)));

        assertEquals(HandCategory.FOUR_OF_A_KIND, high.category());
        assertEquals(HandCategory.FOUR_OF_A_KIND, low.category());
        assertTrue(high.compareTo(low) > 0);
    }

    @Test
    void flush_tiebreak_higher_card_wins() {
        // A♥ J♥ 9♥ 4♥ 2♥  vs  K♠ Q♠ 9♠ 4♠ 2♠ → first flush wins (A > K)
        var high = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.NINE, Suit.HEARTS), new Card(Rank.FOUR, Suit.HEARTS),
                new Card(Rank.TWO, Suit.HEARTS)));
        var low = HandEvaluator.evaluate(List.of(
                new Card(Rank.KING, Suit.SPADES), new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.NINE, Suit.SPADES), new Card(Rank.FOUR, Suit.SPADES),
                new Card(Rank.TWO, Suit.SPADES)));

        assertEquals(HandCategory.FLUSH, high.category());
        assertEquals(HandCategory.FLUSH, low.category());
        assertTrue(high.compareTo(low) > 0);
    }

    @Test
    void two_pair_tiebreak_high_pair_then_low_pair_then_kicker() {
        // A A K K Q  vs  A A Q Q K → first wins (K pair > Q pair)
        var highLowPair = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.SPADES), new Card(Rank.ACE, Suit.HEARTS),
                new Card(Rank.KING, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.SPADES)));
        var lowLowPair = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.QUEEN, Suit.DIAMONDS), new Card(Rank.QUEEN, Suit.CLUBS),
                new Card(Rank.KING, Suit.DIAMONDS)));

        assertEquals(HandCategory.TWO_PAIR, highLowPair.category());
        assertEquals(HandCategory.TWO_PAIR, lowLowPair.category());
        assertTrue(highLowPair.compareTo(lowLowPair) > 0);
    }

    @Test
    void one_pair_tiebreak_pair_rank_then_kickers() {
        // K K A 9 7  vs  K K A 9 5 → first wins (7 kicker > 5)
        var high = HandEvaluator.evaluate(List.of(
                new Card(Rank.KING, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.ACE, Suit.SPADES), new Card(Rank.NINE, Suit.SPADES),
                new Card(Rank.SEVEN, Suit.CLUBS)));
        var low = HandEvaluator.evaluate(List.of(
                new Card(Rank.KING, Suit.DIAMONDS), new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.ACE, Suit.DIAMONDS), new Card(Rank.NINE, Suit.DIAMONDS),
                new Card(Rank.FIVE, Suit.CLUBS)));

        assertEquals(HandCategory.ONE_PAIR, high.category());
        assertEquals(HandCategory.ONE_PAIR, low.category());
        assertTrue(high.compareTo(low) > 0);
    }

    @Test
    void three_of_a_kind_tiebreak_triplet_then_kickers() {
        // Q Q Q A K  vs  Q Q Q A J → first wins (K kicker > J)
        var high = HandEvaluator.evaluate(List.of(
                new Card(Rank.QUEEN, Suit.SPADES), new Card(Rank.QUEEN, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.DIAMONDS), new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.KING, Suit.SPADES)));
        var low = HandEvaluator.evaluate(List.of(
                new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.HEARTS), new Card(Rank.ACE, Suit.CLUBS),
                new Card(Rank.JACK, Suit.CLUBS)));

        assertEquals(HandCategory.THREE_OF_A_KIND, high.category());
        assertEquals(HandCategory.THREE_OF_A_KIND, low.category());
        assertTrue(high.compareTo(low) > 0);
    }

    @Test
    void high_card_tiebreak_descending_ranks() {
        // A K Q J 9  vs  A K Q J 8 → first wins (9 > 8)
        var high = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.DIAMONDS), new Card(Rank.JACK, Suit.CLUBS),
                new Card(Rank.NINE, Suit.SPADES)));
        var low = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.CLUBS), new Card(Rank.JACK, Suit.DIAMONDS),
                new Card(Rank.EIGHT, Suit.HEARTS)));

        assertEquals(HandCategory.HIGH_CARD, high.category());
        assertEquals(HandCategory.HIGH_CARD, low.category());
        assertTrue(high.compareTo(low) > 0);
    }

    @Test
    void full_house_tiebreak_same_triplet_pair_rank_decides() {
        // K K K A A  vs  K K K Q Q → first wins (A pair > Q pair)
        var highPair = HandEvaluator.evaluate(List.of(
                new Card(Rank.KING, Suit.SPADES), new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.KING, Suit.DIAMONDS), new Card(Rank.ACE, Suit.SPADES),
                new Card(Rank.ACE, Suit.HEARTS)));
        var lowPair = HandEvaluator.evaluate(List.of(
                new Card(Rank.KING, Suit.SPADES), new Card(Rank.KING, Suit.CLUBS),
                new Card(Rank.KING, Suit.HEARTS), new Card(Rank.QUEEN, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.HEARTS)));

        assertEquals(HandCategory.FULL_HOUSE, highPair.category());
        assertEquals(HandCategory.FULL_HOUSE, lowPair.category());
        assertTrue(highPair.compareTo(lowPair) > 0);
    }

    @Test
    void identical_rank_hands_with_different_suits_are_a_tie() {
        // A♠ K♠ Q♠ J♠ 9♠  vs  A♥ K♥ Q♥ J♥ 9♥ → compareTo == 0
        var hand1 = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.SPADES), new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.SPADES), new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.NINE, Suit.SPADES)));
        var hand2 = HandEvaluator.evaluate(List.of(
                new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.KING, Suit.HEARTS),
                new Card(Rank.QUEEN, Suit.HEARTS), new Card(Rank.JACK, Suit.HEARTS),
                new Card(Rank.NINE, Suit.HEARTS)));

        assertEquals(HandCategory.FLUSH, hand1.category());
        assertEquals(HandCategory.FLUSH, hand2.category());
        assertEquals(0, hand1.compareTo(hand2));
    }

    private List<Card> combine(List<Card> b, List<Card> h) {
        List<Card> all = new java.util.ArrayList<>(b);
        all.addAll(h);
        return all;
    }

}