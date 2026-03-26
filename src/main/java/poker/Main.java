package poker;

public class Main {
    public static void main(String[] args) {
        int numberOfPlayers = args.length > 0 ? Integer.parseInt(args[0]) : 3;

        Partie game = new Partie();
        game.start(numberOfPlayers);
        game.rankings();
        game.showWinner();
    }
}
