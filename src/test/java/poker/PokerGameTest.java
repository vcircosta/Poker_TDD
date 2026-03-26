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

    @Test
    void should_split_pot_when_board_plays_for_all_players() {
        // Example D: 5♣ 6♦ 7♥ 8♠ 9♦ / Player1: A♣ A♦ / Player2: K♣ Q♦
        // Both players' best hand is the straight 9-8-7-6-5 from the board
        List<Card> board = List.of(
                new Card(Rank.FIVE, Suit.CLUBS),
                new Card(Rank.SIX, Suit.DIAMONDS),
                new Card(Rank.SEVEN, Suit.HEARTS),
                new Card(Rank.EIGHT, Suit.SPADES),
                new Card(Rank.NINE, Suit.DIAMONDS)
        );

        Map<String, List<Card>> players = new LinkedHashMap<>();
        players.put("Player1", List.of(new Card(Rank.ACE, Suit.CLUBS), new Card(Rank.ACE, Suit.DIAMONDS)));
        players.put("Player2", List.of(new Card(Rank.KING, Suit.CLUBS), new Card(Rank.QUEEN, Suit.DIAMONDS)));

        List<String> winners = PokerGame.findWinners(players, board);

        assertEquals(2, winners.size());
        assertTrue(winners.contains("Player1"));
        assertTrue(winners.contains("Player2"));
    }
}