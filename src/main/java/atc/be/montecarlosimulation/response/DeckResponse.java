package atc.be.montecarlosimulation.response;

import atc.be.montecarlosimulation.model.Deck;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeckResponse {
    private Deck deck;
    private String message;
}
