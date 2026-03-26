package poker;

public enum HandCategory {
    HIGH_CARD(1, "Carte haute"),
    ONE_PAIR(2, "Paire"),
    TWO_PAIR(3, "Double paire"),
    THREE_OF_A_KIND(4, "Brelan"),
    STRAIGHT(5, "Quinte"),
    FLUSH(6, "Couleur"),
    FULL_HOUSE(7, "Full"),
    FOUR_OF_A_KIND(8, "Carré"),
    STRAIGHT_FLUSH(9, "Quinte flush");

    private final int priority;
    private final String label;

    HandCategory(int priority, String label) {
        this.priority = priority;
        this.label = label;
    }

    public int getPriority() { return priority; }

    @Override
    public String toString() { return label; }
}