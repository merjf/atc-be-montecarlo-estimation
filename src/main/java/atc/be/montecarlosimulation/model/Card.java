package atc.be.montecarlosimulation.model;

import atc.be.montecarlosimulation.costant.RANKINGS;
import atc.be.montecarlosimulation.costant.SUITS;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Card{
    private Suit suit;
    private Ranking ranking;
}