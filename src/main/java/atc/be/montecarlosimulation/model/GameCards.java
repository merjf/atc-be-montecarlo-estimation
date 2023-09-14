package atc.be.montecarlosimulation.model;

import lombok.Data;

import java.util.List;

@Data
public class GameCards {
    private TableCards tableCards;
    private PlayerCards mainPlayerCards;
    private List<PlayerCards> otherPlayerCards;
}