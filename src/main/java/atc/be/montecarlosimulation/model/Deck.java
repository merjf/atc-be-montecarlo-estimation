package atc.be.montecarlosimulation.model;

import atc.be.montecarlosimulation.costant.RANKINGS;
import atc.be.montecarlosimulation.costant.SUITS;
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
        for(SUITS suit : SUITS.values()){
            for (RANKINGS ranking : RANKINGS.values()) {
                cards.add(new Card(new Suit(suit.getRanking(), suit.getSuit()), new Ranking(ranking.getRanking(), ranking.getCard())));
            }
        }
    }

    public Deck(Deck originalDeck){
        this.cards = new ArrayList<>(originalDeck.getCards());
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
