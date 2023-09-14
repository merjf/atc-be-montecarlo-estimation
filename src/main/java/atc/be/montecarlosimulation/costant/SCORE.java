package atc.be.montecarlosimulation.costant;

import lombok.Getter;

import java.util.List;

@Getter
public enum SCORE {
    ROYAL_FLUSH("RF", 900),
    STRAIGHT_FLUSH("SF", 800),
    FOUR_OF_A_KIND("FK", 700),
    FULL_HOUSE("FH", 600),
    FLUSH("F", 500),
    STRAIGHT("S", 400),
    THREE_OF_A_KIND("TK", 300),
    TWO_PAIR("TP", 200),
    PAIR("P", 100),
    HIGH_CARD("HC", 1),
    NONE("ERROR", -1);

    private static final List<String> ROYALFLUSHCARDS = List.of("A", "K", "Q", "J", "10");

    private final String hand;
    private final int ranking;
    SCORE(String hand, int ranking) {
        this.hand = hand;
        this.ranking = ranking;
    }

    public static SCORE valueOfLabel(String hand) {
        for (SCORE score : values()) {
            if (score.hand.equals(hand.toUpperCase())) {
                return score;
            }
        }
        return null;
    }

    public static List<String> getRoyalflushcards(){
        return ROYALFLUSHCARDS;
    }
}
