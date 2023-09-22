package atc.be.montecarlosimulation.service;

import atc.be.montecarlosimulation.converter.EntityConverter;
import atc.be.montecarlosimulation.document.HandHistoryMongoDocument;
import atc.be.montecarlosimulation.document.HandHistoryRedisDocument;
import atc.be.montecarlosimulation.model.*;
import atc.be.montecarlosimulation.repository.HandHistoryMongoRepository;
import atc.be.montecarlosimulation.repository.HandHistoryRedisRepository;
import atc.be.montecarlosimulation.utility.PokerUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
@Service
public class MontecarloService {

    @Autowired
    HandHistoryRedisRepository handHistoryRedisRepository;

    @Autowired
    HandHistoryMongoRepository handHistoryMongoRepository;

    private Deck deck;

    public MontecarloService(){
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

        List<Card> tableCards;
        if(flop) {
            tableCards = new ArrayList<>(IntStream.range(0, 3).mapToObj(i -> PokerUtility.extractCard(deck)).toList());
            if(turn){
                tableCards.add(PokerUtility.extractCard(deck));
                if(river){
                    tableCards.add(PokerUtility.extractCard(deck));
                }
            }
        } else {
            tableCards = new ArrayList<>();
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
    
    public HandEvaluationResponse evaluateHand(HandEvaluationRequest handEvaluationRequest){
        HandHistoryRedisDocument handHistoryRedisDocument = new HandHistoryRedisDocument();
        handHistoryRedisDocument.initiateLists();
        handHistoryRedisDocument.setId(UUID.randomUUID().toString());
        handHistoryRedisDocument.setSamples(handEvaluationRequest.getNSamples());
        handHistoryRedisDocument.setInitialTableCards(handEvaluationRequest.getTableCards());
        List<Card> table;
        List<MainPlayerHand> mainPlayerMainPlayerHands = new ArrayList<>(),
                otherPlayerMainPlayerHands = new ArrayList<>();
        int win = 0, lose = 0, spare = 0;
        for(int nSample = 0; nSample < handEvaluationRequest.getNSamples(); nSample++){
            Deck alternateDeck = new Deck(deck);
            table = PokerUtility.fillTable(handEvaluationRequest.getTableCards(), alternateDeck);
            mainPlayerMainPlayerHands.add(PokerUtility.evaluateRankingHand(handEvaluationRequest.getMainPlayerCards().getCards(), table));
            MainPlayerHand lastMainPlayerMainPlayerHandResult = mainPlayerMainPlayerHands.get(mainPlayerMainPlayerHands.size()-1);
            List<Integer> results = new ArrayList<>();
            for(PlayerCards otherPlayer : handEvaluationRequest.getOtherPlayerCards()){
                otherPlayerMainPlayerHands.add(PokerUtility.evaluateRankingHand(otherPlayer.getCards(), table));
                results.add(otherPlayerMainPlayerHands.get(otherPlayerMainPlayerHands.size()-1).getRanking());
                handHistoryRedisDocument.addOtherPlayerHandItem(otherPlayer);
            }
            if(Collections.max(results) < lastMainPlayerMainPlayerHandResult.getRanking()){
                win++;
            }
            if(Collections.max(results).equals(lastMainPlayerMainPlayerHandResult.getRanking())){
                spare++;
            }
            if(Collections.max(results) > lastMainPlayerMainPlayerHandResult.getRanking()){
                lose++;
            }
            handHistoryRedisDocument.addMainPlayerHandItem(lastMainPlayerMainPlayerHandResult);
            handHistoryRedisDocument.setFullTableCards(table);
        }
        float winRate = (float) (100 * win) / handEvaluationRequest.getNSamples(),
                loseRate = (float) (100 * lose) / handEvaluationRequest.getNSamples(),
                spareRate = (float) (100 * spare) / handEvaluationRequest.getNSamples();
        handHistoryRedisDocument.setWin(winRate);
        handHistoryRedisDocument.setLose(loseRate);
        handHistoryRedisDocument.setSpare(spareRate);
        handHistoryRedisRepository.save(handHistoryRedisDocument);
        float total = winRate+loseRate+spareRate;
        return new HandEvaluationResponse(winRate, loseRate, spareRate, handEvaluationRequest.getNSamples(), UUID.randomUUID().toString());
    }

    public Iterable<HandHistoryRedisDocument> getLastHandHistory(){
        long count = handHistoryRedisRepository.count();
        if(count > 0){
            Iterable<HandHistoryRedisDocument> handHistoryDocuments = handHistoryRedisRepository.findAll();
            List<HandHistoryMongoDocument> handHistoryMongoDocuments = new ArrayList<>();
            handHistoryDocuments.forEach(handHistoryRedisDocument -> handHistoryMongoDocuments.add(EntityConverter.redisToMongoEntity(handHistoryRedisDocument)));
            handHistoryMongoRepository.saveAll(handHistoryMongoDocuments);
            return handHistoryDocuments;
        }
        return new ArrayList<>();
    }

    public void deleteAllHandHistoryDocument(){
        handHistoryRedisRepository.deleteAll();
    }
}
