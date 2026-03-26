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
        System.out.println("=== Cartes communes ===");
        board.forEach(c -> System.out.print(c + "  "));
        System.out.println("\n");

        System.out.println("=== Mains des joueurs ===");
        players.forEach((name, hand) -> System.out.println(name + " : " + hand.get(0) + "  " + hand.get(1)));
        System.out.println();

        // 5. Détermination du gagnant
        Map<String, EvaluationResult> results = new LinkedHashMap<>();
        players.forEach((name, hand) -> {
            List<Card> fullHand = new ArrayList<>(board);
            fullHand.addAll(hand);
            results.put(name, HandEvaluator.evaluate(fullHand));
        });

        System.out.println("=== Classement des joueurs ===");
        results.forEach((name, res) -> System.out.println(name + " → " + res.category() + " " + res.chosen5()));
        System.out.println();

        List<String> winners = PokerGame.findWinners(players, board);

        System.out.println("=== Résultat ===");
        if (winners.size() > 1) {
            System.out.println("Égalité entre : " + String.join(", ", winners));
        } else {
            String winner = winners.get(0);
            System.out.println("Gagnant : " + winner + " avec " + results.get(winner).category() + " " + results.get(winner).chosen5());
        }

    }
}