package atc.be.montecarlosimulation.service;

import atc.be.montecarlosimulation.model.*;
import atc.be.montecarlosimulation.request.RankingHandRequest;
import atc.be.montecarlosimulation.response.DeckCardExtractedResponse;
import atc.be.montecarlosimulation.response.DeckResponse;
import atc.be.montecarlosimulation.response.RankingHandResponse;
import atc.be.montecarlosimulation.utility.PokerUtility;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@org.springframework.stereotype.Service
@AllArgsConstructor
@Data
public class Service {

    private Deck deck;

    public Service(){
        this.deck = new Deck();
    }

    public ResponseEntity<DeckResponse> getDeck(){
        Deck.shuffleDeck(deck);
        DeckResponse deckResponse = new DeckResponse(deck, "ok");
        return ResponseEntity.ok(deckResponse);
    }

    public ResponseEntity<DeckCardExtractedResponse> extractCards(boolean isRiverOrTurn){
        List<Card> extractedCards = new ArrayList<>();
        if(isRiverOrTurn){
            extractedCards.add(Deck.extractCard(this.deck));
        } else{
            for(int i = 0; i<3; i++){
                extractedCards.add(Deck.extractCard(this.deck));
            }
        }
        DeckCardExtractedResponse deckResponse = new DeckCardExtractedResponse(deck, extractedCards, "ok");
        return ResponseEntity.ok(deckResponse);
    }

    public ResponseEntity<RankingHandResponse> evaluateRankingHand(RankingHandRequest rankingHandRequest){
        List<Card> playerHand = PokerUtility.getCardsFromString(rankingHandRequest.getPlayerHand());
        List<Card> table = PokerUtility.getCardsFromString(rankingHandRequest.getTable());
        Ranking ranking = PokerUtility.evaluateRankingHand(playerHand, table);
        return ResponseEntity.ok(new RankingHandResponse(List.of(ranking.getRanking1lvl())));
    }
}
