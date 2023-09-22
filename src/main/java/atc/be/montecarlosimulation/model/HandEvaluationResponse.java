package atc.be.montecarlosimulation.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HandEvaluationResponse {
    private float win;
    private float lose;
    private float spare;
    private int nSamples;
    private String id;
}
