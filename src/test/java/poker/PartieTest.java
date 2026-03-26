package poker;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class PartieTest {

    @Test
    void startDeals5CommunityCards() {
        Partie game = new Partie();
        game.start(2);
        assertEquals(5, game.getCommunityCards().size());
    }

    @Test
    void startDeals2CardsPerPlayer() {
        Partie game = new Partie();
        game.start(3);
        for (int i = 0; i < 3; i++) {
            assertEquals(2, game.getPlayerHand(i).size());
        }
    }

    @Test
    void noCardIsDealtTwice() {
        Partie game = new Partie();
        game.start(4);

        Set<String> seen = new HashSet<>();
        for (Carte c : game.getCommunityCards()) {
            assertTrue(seen.add(c.toString()), "Duplicate: " + c);
        }
        for (int i = 0; i < 4; i++) {
            for (Carte c : game.getPlayerHand(i)) {
                assertTrue(seen.add(c.toString()), "Duplicate: " + c);
            }
        }
    }

    @Test
    void rankingsProducesOneHandPerPlayer() {
        Partie game = new Partie();
        game.start(3);
        game.rankings();
        assertEquals(3, game.getBestHands().size());
    }

    @Test
    void showWinnerWithoutRankingsThrows() {
        Partie game = new Partie();
        game.start(2);
        assertThrows(IllegalStateException.class, game::showWinner);
    }

    @Test
    void tooFewPlayersThrows() {
        Partie game = new Partie();
        assertThrows(IllegalArgumentException.class, () -> game.start(1));
    }

    @Test
    void tooManyPlayersThrows() {
        Partie game = new Partie();
        assertThrows(IllegalArgumentException.class, () -> game.start(10));
    }

    @Test
    void fullGameWith2PlayersRunsWithoutError() {
        Partie game = new Partie();
        game.start(2);
        game.rankings();
        game.showWinner();
        assertNotNull(game.getBestHands());
        assertEquals(2, game.getBestHands().size());
    }
}
