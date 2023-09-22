package atc.be.montecarlosimulation.document;

import atc.be.montecarlosimulation.model.Card;
import atc.be.montecarlosimulation.model.MainPlayerHand;
import atc.be.montecarlosimulation.model.PlayerCards;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
@Document("HandHistoryDocument")
public class HandHistoryMongoDocument {
    @Id
    @Indexed
    private String id;
    private List<MainPlayerHand> mainPlayerHands;
    @NonNull
    private List<PlayerCards> otherPlayerCards;
    @NonNull
    private List<Card> initialTableCards;
    @NonNull
    private List<Card> fullTableCards;
    @Indexed
    private float win;
    @Indexed
    private float lose;
    @Indexed
    private float spare;
    @Indexed
    private int samples;

    public void initiateLists(){
        this.otherPlayerCards = new ArrayList<>();
        this.mainPlayerHands = new ArrayList<>();
        this.initialTableCards = new ArrayList<>();
        this.fullTableCards = new ArrayList<>();
    }

    public void addMainPlayerHandItem(MainPlayerHand mainPlayerHand){
        this.mainPlayerHands.add(mainPlayerHand);
    }

    public void addOtherPlayerHandItem(PlayerCards otherPlayerCards){
        this.otherPlayerCards.add(otherPlayerCards);
    }
}
