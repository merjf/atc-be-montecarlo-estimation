package atc.be.montecarlosimulation.controller;

import atc.be.montecarlosimulation.model.*;
import atc.be.montecarlosimulation.response.BasicResponse;
import atc.be.montecarlosimulation.service.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/atc-be-montecarlo")
public class Controller {

    private final Service service;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/shuffle-deck")
    public ResponseEntity<BasicResponse> shuffleDeck() {
        Deck deck = service.shuffleDeck();;
        if(deck != null){
            return ResponseEntity.ok(new BasicResponse(deck, "OK"));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/draw-cards")
    public ResponseEntity<BasicResponse> drawCards(@RequestParam int nPlayers,
                                                   @RequestParam boolean flop,
                                                   @RequestParam boolean turn,
                                                   @RequestParam boolean river) {
        GameCards gameCards = service.drawCards(nPlayers, flop, turn, river);
        if(gameCards != null){
            return ResponseEntity.ok(new BasicResponse(gameCards, "OK"));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/montecarlo-evaluation")
    public ResponseEntity<BasicResponse> evaluateRankingHand(@RequestBody MontecarloRequest montecarloRequest) {
        List<RankingHand> rankingHands = service.evaluateRankingHand(montecarloRequest);
        if(rankingHands != null){
            return ResponseEntity.ok(new BasicResponse(rankingHands, "OK"));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
