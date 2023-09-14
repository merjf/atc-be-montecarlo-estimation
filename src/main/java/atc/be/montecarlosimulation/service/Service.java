package atc.be.montecarlosimulation.service;

import atc.be.montecarlosimulation.costant.SCORE;
import atc.be.montecarlosimulation.model.*;
import atc.be.montecarlosimulation.utility.PokerUtility;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@org.springframework.stereotype.Service
@AllArgsConstructor
@Data
public class Service {

    private Deck deck;

    public Service(){
        this.deck = new Deck();
        Deck.shuffleDeck(deck);
    }

    public Deck shuffleDeck() {
        this.deck = new Deck();
        Deck.shuffleDeck(deck);
        return this.deck;
    }

    public GameCards drawCards(int nPlayers, boolean flop, boolean turn, boolean river){
        GameCards gameCards = new GameCards();

        PlayerCards mainPlayerCards = new PlayerCards();
        mainPlayerCards.setCards(List.of(PokerUtility.extractCard(deck), PokerUtility.extractCard(deck)));
        gameCards.setMainPlayerCards(mainPlayerCards);

        TableCards tableCards = new TableCards(new ArrayList<>());
        if(flop) {
            tableCards.setCards(IntStream.range(0, 3).mapToObj(i -> PokerUtility.extractCard(deck)).collect(Collectors.toList()));
            if(turn){
                tableCards.getCards().add(PokerUtility.extractCard(deck));
                if(river){
                    tableCards.getCards().add(PokerUtility.extractCard(deck));
                }
            }
        } else {
            tableCards.setCards(new ArrayList<>());
        }
        gameCards.setTableCards(tableCards);

        gameCards.setOtherPlayerCards(
            IntStream.range(0, nPlayers)
                .mapToObj(player -> List.of(PokerUtility.extractCard(deck), PokerUtility.extractCard(deck)))
                .map(PlayerCards::new)
                .collect(Collectors.toList())
        );
        return gameCards;
    }
    
    public List<RankingHand> evaluateRankingHand(MontecarloRequest montecarloRequest){
        List<Card> table;
        List<RankingHand> rankingHands = new ArrayList<>();
        for(int nSample = 0; nSample < montecarloRequest.getNSamples(); nSample++){
            Deck alternateDeck = new Deck(deck);
            table = PokerUtility.fillTable(montecarloRequest.getTableCards().getCards(), alternateDeck);
            rankingHands.add(PokerUtility.evaluateRankingHand(montecarloRequest.getMainPlayerCards().getCards(), table));
        }
        return rankingHands;
    }
}
