package atc.be.montecarlosimulation.document;

import atc.be.montecarlosimulation.model.Card;
import atc.be.montecarlosimulation.model.PlayerHand;
import atc.be.montecarlosimulation.model.PlayerCards;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
@Document("HandHistoryDocument")
public class HandHistoryMongoDocument {
    @Id
    @Indexed
    private String id;
    @NonNull
    @Lazy
    private List<PlayerHand> playerHands;
    @NonNull
    @Lazy
    private List<List<PlayerCards>> otherPlayerCards;
    @NonNull
    @Lazy
    private List<List<Card>> initialTableCards;
    @NonNull
    @Lazy
    private List<List<Card>> fullTableCards;
    @Indexed
    private float win;
    @Indexed
    private float lose;
    @Indexed
    private float spare;
    @Indexed
    private int samples;
    @CreatedDate
    private Date creationDate;

    public void initiateLists(){
        this.otherPlayerCards = new ArrayList<>();
        this.playerHands = new ArrayList<>();
        this.initialTableCards = new ArrayList<>();
        this.fullTableCards = new ArrayList<>();
    }

    public void addMainPlayerHandItem(PlayerHand playerHand){
        this.playerHands.add(playerHand);
    }

    public void addOtherPlayerHandItem(List<PlayerCards> otherPlayerCards){
        this.otherPlayerCards.add(otherPlayerCards);
    }
}
