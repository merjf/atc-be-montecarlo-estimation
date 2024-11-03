package atc.be.montecarlosimulation.service;

import atc.be.montecarlosimulation.mapper.EntityConverter;
import atc.be.montecarlosimulation.document.HandHistoryMongoDocument;
import atc.be.montecarlosimulation.document.HandHistoryRedisDocument;
import atc.be.montecarlosimulation.model.*;
import atc.be.montecarlosimulation.repository.mongo.HandHistoryMongoRepository;
import atc.be.montecarlosimulation.repository.redis.HandHistoryRedisDocumentRepository;
import atc.be.montecarlosimulation.response.HandEvaluationRequest;
import atc.be.montecarlosimulation.response.HandEvaluationResponse;
import atc.be.montecarlosimulation.response.HandHistory;
import atc.be.montecarlosimulation.utility.PokerUtility;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@AllArgsConstructor
@Component
public class MontecarloService {

    @Autowired
    HandHistoryRedisDocumentRepository handHistoryRedisDocumentRepository;

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
        List<Card> table;
        List<PlayerHand> playerHands = new ArrayList<>(),
                otherPlayerHands = new ArrayList<>();
        List<List<Card>> fullTableCards = new ArrayList<>(), initialTableCards = new ArrayList<>();
        List<List<PlayerCards>> otherPlayersCards = new ArrayList<>();
        int win = 0, lose = 0, spare = 0;
        for(int nSample = 0; nSample < handEvaluationRequest.getNSamples(); nSample++){
            Deck alternateDeck = new Deck(deck);

            table = PokerUtility.fillTable(handEvaluationRequest.getTableCards(), alternateDeck);
            playerHands.add(PokerUtility.evaluateRankingHand(handEvaluationRequest.getMainPlayerCards().getCards(), table));
            PlayerHand lastMainPlayerPlayerHandResult = playerHands.get(playerHands.size()-1);
            List<Integer> results = new ArrayList<>();

            // TODO: generate otherPlayerCards randomly
            for(PlayerCards otherPlayer : handEvaluationRequest.getOtherPlayerCards()){
                otherPlayerHands.add(PokerUtility.evaluateRankingHand(otherPlayer.getCards(), table));
                results.add(otherPlayerHands.get(otherPlayerHands.size()-1).getRanking());
            }
            if(Collections.max(results) < lastMainPlayerPlayerHandResult.getRanking()){
                win++;
            }
            if(Collections.max(results).equals(lastMainPlayerPlayerHandResult.getRanking())){
                spare++;
            }
            if(Collections.max(results) > lastMainPlayerPlayerHandResult.getRanking()){
                lose++;
            }

            fullTableCards.add(table);
            otherPlayersCards.add(handEvaluationRequest.getOtherPlayerCards());
            initialTableCards.add(handEvaluationRequest.getTableCards());
        }
        float winRate = (float) (100 * win) / handEvaluationRequest.getNSamples(),
                loseRate = (float) (100 * lose) / handEvaluationRequest.getNSamples(),
                spareRate = (float) (100 * spare) / handEvaluationRequest.getNSamples();

        saveHandHistory(handEvaluationRequest.getNSamples(), initialTableCards,
                fullTableCards, playerHands, otherPlayersCards, winRate, loseRate, spareRate);
        return new HandEvaluationResponse(winRate, loseRate, spareRate, handEvaluationRequest.getNSamples(), UUID.randomUUID().toString());
    }

    public List<HandHistory> getLastHandHistory(){
        return handHistoryRedisDocumentRepository.findAll().stream().map(EntityConverter::redisToResponseDTO).toList();
    }

    public void deleteAllHandHistoryDocument(){
        handHistoryRedisDocumentRepository.deleteAll();
    }

    private void saveHandHistory(int nSamples, List<List<Card>> initialTableCards, List<List<Card>> fullTableCards,
                                 List<PlayerHand> playerHands, List<List<PlayerCards>> otherPlayersCards,
                                 float winRate, float loseRate, float spareRate){
        String ID = UUID.randomUUID().toString();
        HandHistoryRedisDocument handHistoryRedisDocument = new HandHistoryRedisDocument();
        handHistoryRedisDocument.setId(ID);
        handHistoryRedisDocument.setSamples(nSamples);
        handHistoryRedisDocument.setWin(winRate);
        handHistoryRedisDocument.setLose(loseRate);
        handHistoryRedisDocument.setSpare(spareRate);
        handHistoryRedisDocumentRepository.save(handHistoryRedisDocument);

        HandHistoryMongoDocument handHistoryMongoDocument = new HandHistoryMongoDocument();
        handHistoryMongoDocument.initiateLists();
        handHistoryMongoDocument.setId(ID);
        handHistoryMongoDocument.setSamples(nSamples);
        handHistoryMongoDocument.setInitialTableCards(initialTableCards);
        handHistoryMongoDocument.setPlayerHands(playerHands);
        handHistoryMongoDocument.setOtherPlayerCards(otherPlayersCards);
        handHistoryMongoDocument.setFullTableCards(fullTableCards);
        handHistoryMongoDocument.setWin(winRate);
        handHistoryMongoDocument.setLose(loseRate);
        handHistoryMongoDocument.setSpare(spareRate);
        handHistoryMongoRepository.save(handHistoryMongoDocument);
    }
}
