package atc.be.montecarlosimulation.costant;

import lombok.Data;
import lombok.Getter;

@Getter
public enum SUITS{
    HEARTH("H", 4),
    DIAMONDS("D", 3),
    CLUBS("C", 2),
    SPADES("S", 1);

    private final String suit;
    private final int ranking;

    SUITS(String suit, int ranking) {
        this.suit = suit;
        this.ranking = ranking;
    }

    public static SUITS valueOfLabel(String value) {
        for (SUITS suit : values()) {
            if (suit.suit.equals(value.toUpperCase())) {
                return suit;
            }
        }
        return null;
    }
}