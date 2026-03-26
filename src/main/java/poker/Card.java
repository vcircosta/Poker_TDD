package poker;

public record Card(Rank rank, Suit suit) {
    @Override
    public String toString() {
        return rank.getSymbol() + suit.getSymbol();
    }
}