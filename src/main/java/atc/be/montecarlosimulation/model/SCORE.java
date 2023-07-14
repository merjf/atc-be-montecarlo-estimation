package atc.be.montecarlosimulation.model;

public enum SCORE {
    ROYAL_FLUSH("RF", 10),
    STRAIGHT_FLUSH("SF", 9),
    FOUR_OF_A_KIND("FK", 8),
    FULL_HOUSE("FH", 7),
    FLUSH("F", 6),
    STRAIGHT("S", 5),
    THREE_OF_A_KIND("TK", 4),
    TWO_PAIR("TP", 3),
    PAIR("P", 2),
    HIGH_CARD("HC", 1);

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

    public int getRanking(){
        return this.ranking;
    }
}
