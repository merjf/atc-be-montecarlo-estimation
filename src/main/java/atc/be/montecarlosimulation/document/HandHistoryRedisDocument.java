package atc.be.montecarlosimulation.document;

import atc.be.montecarlosimulation.model.*;
import com.redis.om.spring.annotations.Document;
import com.redis.om.spring.annotations.Indexed;
import lombok.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Document("HandHistoryRedisDocument")
public class HandHistoryRedisDocument {
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
