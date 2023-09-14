package atc.be.montecarlosimulation.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@Data
public class MontecarloRequest{
    private TableCards tableCards;
    private PlayerCards mainPlayerCards;
    private List<PlayerCards> otherPlayerCards;
    private int nSamples;
}
