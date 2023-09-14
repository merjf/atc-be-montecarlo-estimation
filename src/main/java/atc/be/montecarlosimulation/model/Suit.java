package atc.be.montecarlosimulation.model;

import atc.be.montecarlosimulation.costant.SUITS;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Suit {
    private int value;
    private String label;
}