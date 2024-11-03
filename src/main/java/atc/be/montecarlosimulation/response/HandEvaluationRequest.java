package atc.be.montecarlosimulation.response;

import atc.be.montecarlosimulation.model.Card;
import atc.be.montecarlosimulation.model.PlayerCards;
import lombok.Data;

import java.util.List;

@Data
public class HandEvaluationRequest {
    private List<Card> tableCards;
    private PlayerCards mainPlayerCards;
    private List<PlayerCards> otherPlayerCards;
    private int nSamples;
}
