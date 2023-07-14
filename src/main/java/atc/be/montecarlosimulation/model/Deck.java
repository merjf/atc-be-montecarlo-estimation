package atc.be.montecarlosimulation.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
public class Deck {

    // H = Hearth, D = Diamonds, C = Clubs, S = Spades
    // A, K, Q, J, 10, 9, 8, 7, 6, 5, 4, 3, 2
    // Total Cards = 52

    private List<Card> cards;

    public Deck(){
        this.cards = new ArrayList<>();
        for(SUIT suit : SUIT.values()){
            for (ORDER order : ORDER.values()) {
                cards.add(new Card(order, suit));
            }
        }
    }

    public static void shuffleDeck(Deck deck){
        Collections.shuffle(deck.getCards());
    }

    public static Card extractCard(Deck deck){
        int randomCard = (int) (Math.random() * deck.getCards().size());
        Card cardExtracted = deck.getCards().get(randomCard);
        deck.getCards().remove(randomCard);
        return cardExtracted;
    }
}
