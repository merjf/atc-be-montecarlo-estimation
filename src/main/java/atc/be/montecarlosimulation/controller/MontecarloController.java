package atc.be.montecarlosimulation.controller;

import atc.be.montecarlosimulation.document.HandHistoryRedisDocument;
import atc.be.montecarlosimulation.model.*;
import atc.be.montecarlosimulation.response.BasicResponse;
import atc.be.montecarlosimulation.service.MontecarloService;
import com.fasterxml.jackson.databind.ObjectMapper;
//import com.netflix.discovery.EurekaClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/atc-be-montecarlo")
public class MontecarloController {

    private final MontecarloService montecarloService;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/shuffle-deck")
    public ResponseEntity<BasicResponse> shuffleDeck() {
        Deck deck = montecarloService.shuffleDeck();;
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
        GameCards gameCards = montecarloService.drawCards(nPlayers, flop, turn, river);
        if(gameCards != null){
            return ResponseEntity.ok(new BasicResponse(gameCards, "OK"));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/evalutate-hand")
    public ResponseEntity<BasicResponse> evaluateHand(@RequestBody HandEvaluationRequest handEvaluationRequest) {
        HandEvaluationResponse handEvaluation = montecarloService.evaluateHand(handEvaluationRequest);
        if(handEvaluation != null){
            return ResponseEntity.ok(new BasicResponse(handEvaluation, "OK"));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/get-last-hands")
    public ResponseEntity<BasicResponse> getLastHands(){
        Iterable<HandHistoryRedisDocument> handHistoryDocuments = montecarloService.getLastHandHistory();
        if(handHistoryDocuments != null){
            return ResponseEntity.ok(new BasicResponse(handHistoryDocuments, "OK"));
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/delete-all")
    public ResponseEntity<BasicResponse> deleteAll(){
        montecarloService.deleteAllHandHistoryDocument();

        return ResponseEntity.ok(new BasicResponse("done", "OK"));
    }
}
