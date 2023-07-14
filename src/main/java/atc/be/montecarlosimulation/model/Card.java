package atc.be.montecarlosimulation.model;

import lombok.Data;

@Data
public class Card{
    public SUIT suit;
    public ORDER order;

    public Card(ORDER order, SUIT suit) {
        this.order = order;
        this.suit = suit;
    }
}