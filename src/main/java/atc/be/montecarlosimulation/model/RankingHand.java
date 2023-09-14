package atc.be.montecarlosimulation.model;

import lombok.Data;

import java.util.List;

@Data
public class RankingHand {
    private Integer rankingMainPlayer;
    private String score;
    private List<Card> mainPlayerCards;
}
