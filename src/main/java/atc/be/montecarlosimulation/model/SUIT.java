package atc.be.montecarlosimulation.model;

public enum SUIT {
    HEARTH("H", 4),
    DIAMONDS("D", 3),
    CLUBS("C", 2),
    SPADES("S", 1);

    private final String suit;
    private final int ranking;

    SUIT(String suit, int ranking) {
        this.suit = suit;
        this.ranking = ranking;
    }

    public static SUIT valueOfLabel(String value) {
        for (SUIT suit : values()) {
            if (suit.suit.equals(value.toUpperCase())) {
                return suit;
            }
        }
        return null;
    }

    public String getSuit(){
        return this.suit;
    }
}