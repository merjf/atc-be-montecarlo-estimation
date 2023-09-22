package atc.be.montecarlosimulation.model;

import lombok.Data;

import java.util.List;

@Data
public class MainPlayerHand {
    private Integer ranking;
    private String score;
    private List<Card> cards;
}
