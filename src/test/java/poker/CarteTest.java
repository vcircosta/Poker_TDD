package poker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CarteTest {

    @Test
    void createValidCard() {
        Carte card = new Carte(Carte.Rank.ACE, Carte.Suit.HEARTS);
        assertEquals(Carte.Rank.ACE, card.getRank());
        assertEquals(Carte.Suit.HEARTS, card.getSuit());
    }

    @Test
    void cardDisplayIsCorrect() {
        Carte card = new Carte(Carte.Rank.KING, Carte.Suit.SPADES);
        assertEquals("K♠", card.toString());
    }

    @Test
    void aceHasHighestRank() {
        assertTrue(Carte.Rank.ACE.getValue() > Carte.Rank.KING.getValue());
    }

    @Test
    void rankOrderIsConsistent() {
        assertTrue(Carte.Rank.KING.getValue() > Carte.Rank.QUEEN.getValue());
        assertTrue(Carte.Rank.QUEEN.getValue() > Carte.Rank.JACK.getValue());
        assertTrue(Carte.Rank.JACK.getValue() > Carte.Rank.TEN.getValue());
        assertTrue(Carte.Rank.TEN.getValue() > Carte.Rank.TWO.getValue());
    }
}
