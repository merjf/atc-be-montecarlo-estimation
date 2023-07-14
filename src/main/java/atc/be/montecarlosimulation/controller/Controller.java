package atc.be.montecarlosimulation.controller;

import atc.be.montecarlosimulation.request.RankingHandRequest;
import atc.be.montecarlosimulation.response.DeckCardExtractedResponse;
import atc.be.montecarlosimulation.response.DeckResponse;
import atc.be.montecarlosimulation.response.RankingHandResponse;
import atc.be.montecarlosimulation.service.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class Controller {

    private final Service service;
    private final ObjectMapper objectMapper;

    @GetMapping(path = "/get-deck")
    public ResponseEntity<DeckResponse> getDeck() {
        ResponseEntity<DeckResponse> response = service.getDeck();
        if(response.getBody() != null){
            return ResponseEntity.ok(response.getBody());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/extract-turn-river")
    public ResponseEntity<DeckCardExtractedResponse> extractTurnRiver() {
        ResponseEntity<DeckCardExtractedResponse> response = service.extractCards(true);
        if(response.getBody() != null){
            return ResponseEntity.ok(response.getBody());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/extract-flop")
    public ResponseEntity<DeckCardExtractedResponse> extractFlop() {
        ResponseEntity<DeckCardExtractedResponse> response = service.extractCards(false);
        if(response.getBody() != null){
            return ResponseEntity.ok(response.getBody());
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping(path = "/evaluate-ranking-hand")
    public ResponseEntity<RankingHandResponse> evaluateRankingHand(@RequestBody String body) throws JsonProcessingException {
        if(body != null){
            RankingHandRequest rankingHandRequest = objectMapper.readValue(body, RankingHandRequest.class);
            ResponseEntity<RankingHandResponse> response = service.evaluateRankingHand(rankingHandRequest);
            if(response.getBody() != null){
                return ResponseEntity.ok(response.getBody());
            }
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
