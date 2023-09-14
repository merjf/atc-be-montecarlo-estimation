package atc.be.montecarlosimulation.costant;

import lombok.Getter;

@Getter
public enum RANKINGS {
    ACE("A", 13),
    KING("K", 12),
    QUEEN("Q", 11),
    JACK("J", 10),
    TEN("10", 9),
    NINE("9", 8),
    EIGHT("8", 7),
    SEVEN("7", 6),
    SIX("6", 5),
    FIVE("5", 4),
    FOURTH("4", 3),
    THREE("3", 2),
    TWO("2", 1);

    public final String card;
    public final int ranking;
    private RANKINGS(String card, int ranking) {
        this.card = card;
        this.ranking = ranking;
    }

    public static RANKINGS valueOfLabel(String card) {
        for (RANKINGS ranking : values()) {
            if (ranking.card.equals(card.toUpperCase())) {
                return ranking;
            }
        }
        return null;
    }

    public static RANKINGS valueOfValue(int value){
        for (RANKINGS ranking : values()) {
            if (ranking.getRanking() == value) {
                return ranking;
            }
        }
        return null;
    }

}
