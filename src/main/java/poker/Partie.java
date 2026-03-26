package poker;

import java.util.*;
import java.util.stream.Collectors;

public class Partie {

    private final List<Carte> deck;
    private final List<Carte> communityCards;
    private final List<String> playerNames;
    private final List<List<Carte>> playerHands;
    private List<Combinaison> bestHands;

    public Partie() {
        deck = new ArrayList<>();
        communityCards = new ArrayList<>();
        playerNames = new ArrayList<>();
        playerHands = new ArrayList<>();
        bestHands = new ArrayList<>();
        initDeck();
        shuffleDeck();
    }

    private void initDeck() {
        for (Carte.Suit suit : Carte.Suit.values()) {
            for (Carte.Rank rank : Carte.Rank.values()) {
                deck.add(new Carte(rank, suit));
            }
        }
    }

    private void shuffleDeck() {
        Collections.shuffle(deck);
    }

    private Carte draw() {
        if (deck.isEmpty()) throw new IllegalStateException("No more cards in the deck.");
        return deck.remove(0);
    }

    // -------------------------------------------------------------------------
    // Start a game
    // -------------------------------------------------------------------------

    /**
     * Starts a game with the given number of players.
     * Deals 5 community cards first, then 2 hole cards to each player.
     */
    public void start(int numberOfPlayers) {
        if (numberOfPlayers < 2) throw new IllegalArgumentException("At least 2 players are required.");
        if (numberOfPlayers > 9) throw new IllegalArgumentException("Maximum 9 players.");

        for (int i = 1; i <= numberOfPlayers; i++) {
            playerNames.add("Player " + i);
            playerHands.add(new ArrayList<>());
        }

        // 5 community cards
        System.out.println("=== Community cards ===");
        for (int i = 0; i < 5; i++) {
            communityCards.add(draw());
        }
        System.out.println(cardsToString(communityCards));
        System.out.println();

        // 2 hole cards per player
        System.out.println("=== Player hands ===");
        for (int i = 0; i < numberOfPlayers; i++) {
            playerHands.get(i).add(draw());
            playerHands.get(i).add(draw());
            System.out.println(playerNames.get(i) + ": " + cardsToString(playerHands.get(i)));
        }
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // Rankings
    // -------------------------------------------------------------------------

    /**
     * Computes and displays the best hand for each player.
     */
    public void rankings() {
        bestHands = new ArrayList<>();

        System.out.println("=== Player rankings ===");
        for (int i = 0; i < playerNames.size(); i++) {
            List<Carte> sevenCards = new ArrayList<>();
            sevenCards.addAll(communityCards);
            sevenCards.addAll(playerHands.get(i));

            Combinaison best = Combinaison.bestHand(sevenCards);
            bestHands.add(best);

            System.out.println(playerNames.get(i) + " → " + best);
        }
        System.out.println();
    }

    // -------------------------------------------------------------------------
    // Winner
    // -------------------------------------------------------------------------

    /**
     * Displays the winner(s) of the game.
     * Requires rankings() to have been called first.
     */
    public void showWinner() {
        if (bestHands.isEmpty()) {
            throw new IllegalStateException("Call rankings() before showWinner().");
        }

        List<Combinaison> winners = Combinaison.best(bestHands);

        System.out.println("=== Result ===");
        if (winners.size() == 1) {
            int idx = bestHands.indexOf(winners.get(0));
            System.out.println("Winner: " + playerNames.get(idx) + " with " + winners.get(0));
        } else {
            System.out.println("Tie between:");
            for (Combinaison c : winners) {
                int idx = bestHands.indexOf(c);
                System.out.println("  - " + playerNames.get(idx) + " with " + c);
            }
        }
    }

    // -------------------------------------------------------------------------
    // Display helpers
    // -------------------------------------------------------------------------

    private String cardsToString(List<Carte> cards) {
        return cards.stream().map(Carte::toString).collect(Collectors.joining("  "));
    }

    // -------------------------------------------------------------------------
    // Accessors (for tests)
    // -------------------------------------------------------------------------

    public List<Carte> getCommunityCards() {
        return Collections.unmodifiableList(communityCards);
    }

    public List<Carte> getPlayerHand(int index) {
        return Collections.unmodifiableList(playerHands.get(index));
    }

    public List<String> getPlayerNames() {
        return Collections.unmodifiableList(playerNames);
    }

    public List<Combinaison> getBestHands() {
        return Collections.unmodifiableList(bestHands);
    }
}
