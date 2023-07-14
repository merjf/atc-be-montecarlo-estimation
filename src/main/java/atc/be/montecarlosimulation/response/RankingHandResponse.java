package atc.be.montecarlosimulation.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class RankingHandResponse {
    private List<Integer> ranking;
}
