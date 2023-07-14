package atc.be.montecarlosimulation.response;

import atc.be.montecarlosimulation.model.Card;
import atc.be.montecarlosimulation.model.Deck;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeckCardExtractedResponse {
    private Deck deck;
    private List<Card> cards;
    private String message;
}
