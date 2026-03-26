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

    private List<Card> combine(List<Card> b, List<Card> h) {
        List<Card> all = new java.util.ArrayList<>(b);
        all.addAll(h);
        return all;
    }

}