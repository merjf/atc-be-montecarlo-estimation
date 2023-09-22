package atc.be.montecarlosimulation.model;

import lombok.Data;

import java.util.List;

@Data
public class HandEvaluationRequest {
    private List<Card> tableCards;
    private PlayerCards mainPlayerCards;
    private List<PlayerCards> otherPlayerCards;
    private int nSamples;
}
