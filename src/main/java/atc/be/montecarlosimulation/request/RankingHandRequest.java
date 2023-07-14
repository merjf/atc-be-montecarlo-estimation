package atc.be.montecarlosimulation.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RankingHandRequest {
    private List<String> playerHand;
    private List<String> table;
}
