package poker;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static poker.Carte.Suit.*;
import static poker.Carte.Rank.*;

class CombinaisonTest {

    // -------------------------------------------------------------------------
    // Helpers
    // -------------------------------------------------------------------------

    private Carte c(Carte.Rank rank, Carte.Suit suit) {
        return new Carte(rank, suit);
    }

    private Combinaison eval(Carte... cards) {
        return Combinaison.evaluate(Arrays.asList(cards));
    }

    // -------------------------------------------------------------------------
    // Category detection
    // -------------------------------------------------------------------------

    @Test
    void detectStraightFlush() {
        Combinaison c = eval(c(TEN, HEARTS), c(JACK, HEARTS), c(QUEEN, HEARTS), c(KING, HEARTS), c(ACE, HEARTS));
        assertEquals(Combinaison.Category.STRAIGHT_FLUSH, c.getCategory());
    }

    @Test
    void detectFourOfAKind() {
        Combinaison c = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS), c(KING, SPADES), c(ACE, HEARTS));
        assertEquals(Combinaison.Category.FOUR_OF_A_KIND, c.getCategory());
    }

    @Test
    void detectFullHouse() {
        Combinaison c = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS), c(ACE, SPADES), c(ACE, HEARTS));
        assertEquals(Combinaison.Category.FULL_HOUSE, c.getCategory());
    }

    @Test
    void detectFlush() {
        Combinaison c = eval(c(TWO, HEARTS), c(FIVE, HEARTS), c(SEVEN, HEARTS), c(NINE, HEARTS), c(JACK, HEARTS));
        assertEquals(Combinaison.Category.FLUSH, c.getCategory());
    }

    @Test
    void detectStraight() {
        Combinaison c = eval(c(FIVE, HEARTS), c(SIX, SPADES), c(SEVEN, DIAMONDS), c(EIGHT, CLUBS), c(NINE, HEARTS));
        assertEquals(Combinaison.Category.STRAIGHT, c.getCategory());
    }

    @Test
    void detectThreeOfAKind() {
        Combinaison c = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS), c(ACE, SPADES), c(TWO, HEARTS));
        assertEquals(Combinaison.Category.THREE_OF_A_KIND, c.getCategory());
    }

    @Test
    void detectTwoPair() {
        Combinaison c = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(ACE, CLUBS), c(ACE, SPADES), c(TWO, HEARTS));
        assertEquals(Combinaison.Category.TWO_PAIR, c.getCategory());
    }

    @Test
    void detectOnePair() {
        Combinaison c = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(ACE, CLUBS), c(EIGHT, SPADES), c(TWO, HEARTS));
        assertEquals(Combinaison.Category.ONE_PAIR, c.getCategory());
    }

    @Test
    void detectHighCard() {
        Combinaison c = eval(c(TWO, HEARTS), c(FIVE, SPADES), c(SEVEN, DIAMONDS), c(NINE, CLUBS), c(JACK, HEARTS));
        assertEquals(Combinaison.Category.HIGH_CARD, c.getCategory());
    }

    // -------------------------------------------------------------------------
    // Straight edge cases
    // -------------------------------------------------------------------------

    @Test
    void wheelStraightWithAceLow() {
        // A-2-3-4-5: straight with height 5
        Combinaison c = eval(c(ACE, HEARTS), c(TWO, SPADES), c(THREE, DIAMONDS), c(FOUR, CLUBS), c(FIVE, HEARTS));
        assertEquals(Combinaison.Category.STRAIGHT, c.getCategory());
    }

    @Test
    void wheelStraightFlushWithAceLow() {
        Combinaison c = eval(c(ACE, HEARTS), c(TWO, HEARTS), c(THREE, HEARTS), c(FOUR, HEARTS), c(FIVE, HEARTS));
        assertEquals(Combinaison.Category.STRAIGHT_FLUSH, c.getCategory());
    }

    // -------------------------------------------------------------------------
    // Cross-category comparisons
    // -------------------------------------------------------------------------

    @Test
    void straightFlushBeatsFullHouse() {
        Combinaison sf = eval(c(TEN, HEARTS), c(JACK, HEARTS), c(QUEEN, HEARTS), c(KING, HEARTS), c(ACE, HEARTS));
        Combinaison quads = eval(c(ACE, HEARTS), c(ACE, DIAMONDS), c(ACE, CLUBS), c(ACE, SPADES), c(KING, HEARTS));
        assertTrue(sf.compareTo(quads) > 0);
    }

    @Test
    void fourOfAKindBeatsFullHouse() {
        Combinaison quads = eval(c(ACE, HEARTS), c(ACE, DIAMONDS), c(ACE, CLUBS), c(ACE, SPADES), c(KING, HEARTS));
        Combinaison full = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS), c(ACE, SPADES), c(ACE, HEARTS));
        assertTrue(quads.compareTo(full) > 0);
    }

    @Test
    void fullHouseBeatsFlush() {
        Combinaison full = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS), c(ACE, SPADES), c(ACE, HEARTS));
        Combinaison flush = eval(c(TWO, HEARTS), c(FIVE, HEARTS), c(SEVEN, HEARTS), c(NINE, HEARTS), c(JACK, HEARTS));
        assertTrue(full.compareTo(flush) > 0);
    }

    @Test
    void flushBeatsStraight() {
        Combinaison flush = eval(c(TWO, HEARTS), c(FIVE, HEARTS), c(SEVEN, HEARTS), c(NINE, HEARTS), c(JACK, HEARTS));
        Combinaison straight = eval(c(FIVE, HEARTS), c(SIX, SPADES), c(SEVEN, DIAMONDS), c(EIGHT, CLUBS), c(NINE, HEARTS));
        assertTrue(flush.compareTo(straight) > 0);
    }

    // -------------------------------------------------------------------------
    // Tiebreakers within the same category
    // -------------------------------------------------------------------------

    @Test
    void higherStraightWins() {
        Combinaison high = eval(c(SIX, HEARTS), c(SEVEN, SPADES), c(EIGHT, DIAMONDS), c(NINE, CLUBS), c(TEN, HEARTS));
        Combinaison low = eval(c(FIVE, HEARTS), c(SIX, SPADES), c(SEVEN, DIAMONDS), c(EIGHT, CLUBS), c(NINE, HEARTS));
        assertTrue(high.compareTo(low) > 0);
    }

    @Test
    void fourAcesBeatsKings() {
        Combinaison fourAces = eval(c(ACE, HEARTS), c(ACE, DIAMONDS), c(ACE, CLUBS), c(ACE, SPADES), c(TWO, HEARTS));
        Combinaison fourKings = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS), c(KING, SPADES), c(ACE, HEARTS));
        assertTrue(fourAces.compareTo(fourKings) > 0);
    }

    @Test
    void fourOfAKindKickerBreaksTie() {
        Combinaison withKing = eval(c(ACE, HEARTS), c(ACE, DIAMONDS), c(ACE, CLUBS), c(ACE, SPADES), c(KING, HEARTS));
        Combinaison withQueen = eval(c(ACE, HEARTS), c(ACE, DIAMONDS), c(ACE, CLUBS), c(ACE, SPADES), c(QUEEN, HEARTS));
        assertTrue(withKing.compareTo(withQueen) > 0);
    }

    @Test
    void fullHouseTripBreaksTie() {
        Combinaison kingsOverAces = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS), c(ACE, SPADES), c(ACE, HEARTS));
        Combinaison queensOverAces = eval(c(QUEEN, HEARTS), c(QUEEN, DIAMONDS), c(QUEEN, CLUBS), c(ACE, SPADES), c(ACE, HEARTS));
        assertTrue(kingsOverAces.compareTo(queensOverAces) > 0);
    }

    @Test
    void twoPairHighPairBreaksTie() {
        Combinaison acesAndKings = eval(c(ACE, HEARTS), c(ACE, DIAMONDS), c(KING, CLUBS), c(KING, SPADES), c(TWO, HEARTS));
        Combinaison queensAndKings = eval(c(QUEEN, HEARTS), c(QUEEN, DIAMONDS), c(KING, CLUBS), c(KING, SPADES), c(TWO, HEARTS));
        assertTrue(acesAndKings.compareTo(queensAndKings) > 0);
    }

    @Test
    void perfectTie() {
        Combinaison a = eval(c(ACE, HEARTS), c(KING, HEARTS), c(QUEEN, HEARTS), c(JACK, HEARTS), c(TEN, SPADES));
        Combinaison b = eval(c(ACE, SPADES), c(KING, SPADES), c(QUEEN, SPADES), c(JACK, SPADES), c(TEN, HEARTS));
        assertEquals(0, a.compareTo(b));
    }

    // -------------------------------------------------------------------------
    // Best hand from 7 cards
    // -------------------------------------------------------------------------

    @Test
    void bestHandFrom7CardsFindsQuads() {
        List<Carte> seven = Arrays.asList(
                c(ACE, HEARTS), c(ACE, DIAMONDS), c(ACE, CLUBS), c(ACE, SPADES),
                c(KING, HEARTS), c(QUEEN, SPADES), c(JACK, CLUBS));
        Combinaison best = Combinaison.bestHand(seven);
        assertEquals(Combinaison.Category.FOUR_OF_A_KIND, best.getCategory());
    }

    @Test
    void bestHandFrom7CardsFindsFullHouse() {
        List<Carte> seven = Arrays.asList(
                c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS),
                c(ACE, SPADES), c(ACE, HEARTS),
                c(TWO, HEARTS), c(THREE, SPADES));
        Combinaison best = Combinaison.bestHand(seven);
        assertEquals(Combinaison.Category.FULL_HOUSE, best.getCategory());
    }

    // -------------------------------------------------------------------------
    // best() method
    // -------------------------------------------------------------------------

    @Test
    void bestReturnsWinner() {
        Combinaison quads = eval(c(ACE, HEARTS), c(ACE, DIAMONDS), c(ACE, CLUBS), c(ACE, SPADES), c(KING, HEARTS));
        Combinaison full = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS), c(ACE, SPADES), c(ACE, HEARTS));
        List<Combinaison> results = Combinaison.best(Arrays.asList(full, quads));
        assertEquals(1, results.size());
        assertEquals(Combinaison.Category.FOUR_OF_A_KIND, results.get(0).getCategory());
    }

    @Test
    void bestReturnsBothOnTie() {
        Combinaison a = eval(c(ACE, HEARTS), c(KING, HEARTS), c(QUEEN, HEARTS), c(JACK, HEARTS), c(TEN, SPADES));
        Combinaison b = eval(c(ACE, SPADES), c(KING, SPADES), c(QUEEN, SPADES), c(JACK, SPADES), c(TEN, HEARTS));
        List<Combinaison> results = Combinaison.best(Arrays.asList(a, b));
        assertEquals(2, results.size());
    }

    // -------------------------------------------------------------------------
    // compareSameCategory()
    // -------------------------------------------------------------------------

    @Test
    void compareSameCategoryThrowsOnDifferentCategories() {
        Combinaison quads = eval(c(ACE, HEARTS), c(ACE, DIAMONDS), c(ACE, CLUBS), c(ACE, SPADES), c(KING, HEARTS));
        Combinaison full = eval(c(KING, HEARTS), c(KING, DIAMONDS), c(KING, CLUBS), c(ACE, SPADES), c(ACE, HEARTS));
        assertThrows(IllegalArgumentException.class,
                () -> Combinaison.compareSameCategory(quads, full));
    }
}
