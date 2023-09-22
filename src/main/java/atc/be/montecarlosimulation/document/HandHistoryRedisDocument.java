package atc.be.montecarlosimulation.document;

import atc.be.montecarlosimulation.model.*;
import lombok.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor(staticName = "of")
@NoArgsConstructor
@RedisHash("HandHistoryRedisDocument")
public class HandHistoryRedisDocument {
    @Id
    @Indexed
    private String id;
    @NonNull
    @Lazy
    private List<MainPlayerHand> mainPlayerHands;
    @NonNull
    @Lazy
    private List<PlayerCards> otherPlayerCards;
    @NonNull
    @Lazy
    private List<Card> initialTableCards;
    @NonNull
    @Lazy
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
