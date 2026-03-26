package poker;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

class PokerGameTest {
    @Test
    void should_declare_correct_winner_and_handle_split_pots() {
        List<Card> board = List.of(
                new Card(Rank.ACE, Suit.SPADES), new Card(Rank.KING, Suit.SPADES),
                new Card(Rank.QUEEN, Suit.SPADES), new Card(Rank.JACK, Suit.SPADES),
                new Card(Rank.TWO, Suit.HEARTS)
        );

        Map<String, List<Card>> players = new HashMap<>();
        players.put("Alice", List.of(new Card(Rank.TEN, Suit.SPADES), new Card(Rank.NINE, Suit.SPADES)));
        players.put("Bob", List.of(new Card(Rank.ACE, Suit.HEARTS), new Card(Rank.THREE, Suit.DIAMONDS)));

        List<String> winners = PokerGame.findWinners(players, board);

        assertEquals(1, winners.size());
        assertEquals("Alice", winners.get(0));
    }
}