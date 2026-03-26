package poker;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        int numberOfPlayers = args.length > 0 ? Integer.parseInt(args[0]) : 3;
        if (numberOfPlayers < 2 || numberOfPlayers > 9)
            throw new IllegalArgumentException("Number of players must be between 2 and 9.");

        // 1. Initialisation
        Deck deck = new Deck();
        deck.shuffle();

        // 2. Distribution (2 cartes par joueur)
        String[] names = {"Alice", "Bob", "Charlie", "Diana", "Eve", "Frank", "Grace", "Hank", "Ivy"};
        Map<String, List<Card>> players = new LinkedHashMap<>();
        for (int i = 0; i < numberOfPlayers; i++) {
            players.put(names[i], List.of(deck.draw(), deck.draw()));
        }

        // 3. Le Board (5 cartes communes)
        List<Card> board = List.of(
                deck.draw(), deck.draw(), deck.draw(),
                deck.draw(),
                deck.draw()
        );

        // 4. Affichage du contexte
        System.out.println("--- BOARD ---");
        System.out.println(board);
        System.out.println("\n--- MAINS DES JOUEURS ---");
        players.forEach((name, hand) -> System.out.println(name + " : " + hand));

        // 5. Détermination du gagnant
        List<String> winners = PokerGame.findWinners(players, board);

        System.out.println("\n--- RÉSULTAT ---");
        if (winners.size() > 1) {
            System.out.println("Égalité entre : " + String.join(", ", winners));
        } else {
            System.out.println("Le vainqueur est : " + winners.get(0));
        }

    }
}