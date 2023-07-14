package atc.be.montecarlosimulation.utility;

import atc.be.montecarlosimulation.model.*;
import atc.be.montecarlosimulation.request.RankingHandRequest;

import java.util.*;
import java.util.stream.Collectors;

public class PokerUtility {
    private static final List<String> ROYALFLUSHCARDS = List.of("A", "K", "Q", "J", "10");

    public static List<Card> getCardsFromString(List<String> values){
        List<Card> cards = new ArrayList<>();
        for(String value : values){
            cards.add(stringToCard(value));
        }
        return cards;
    }

    public static Ranking evaluateRankingHand(List<Card> playerHand, List<Card> table){
        Ranking ranking = new Ranking();
        List<Card> allCards = new ArrayList<>();
        boolean x = allCards.addAll(playerHand);
        x = allCards.addAll(table);
        for(SCORE score : SCORE.values()){
            ranking.setRanking1lvl(getHandRanking(score, allCards));
        }
        return ranking;
    }

    // PRIVATE METHODS

    private static Card stringToCard(String value){
        if(value.length() >= 2){
            ORDER order = ORDER.valueOfLabel(value.substring(0, value.length()-1));
            SUIT suit = SUIT.valueOfLabel(String.valueOf(value.charAt(value.length()-1)));
            return new Card(order, suit);
        }
        return null;
    }

    private static int getHandRanking(SCORE score, List<Card> allCards){
        switch (score){
            case ROYAL_FLUSH -> {
                List<Card> royalFlush = new ArrayList<>();
                for(Card card : allCards){
                    if(card != null && ROYALFLUSHCARDS.contains(card.getOrder().card)){
                        royalFlush.add(card);
                    }
                }
                if(royalFlush.size() >= 5 && isFlush(allCards)){
                    return SCORE.ROYAL_FLUSH.getRanking();
                }
            }
            case FOUR_OF_A_KIND -> {
                countCards(allCards, 4);
            }
            default -> {
                return -1;
            }
        }
        return -1;
    }

    private static boolean countCards(List<Card> cards, int threshold){
        List<String> orders = cards.stream().map(Card::getOrder).map(ORDER::getCard).toList();
        // return the most frequent size == threshold
        return false;
    }

    private static boolean isFlush(List<Card> cards){
        for(SUIT suit : SUIT.values()){
            short flush = 0;
            for(Card card : cards){
                if(suit.getSuit().equals(card.getSuit().getSuit())){
                    flush++;
                }
            }
            if(flush >= 5){
                return true;
            }
        }
        return false;
    }

    private static <T> T mostCommon(List<T> list) {
        Map<T, Integer> map = new HashMap<>();
        for (T t : list) {
            Integer val = map.get(t);
            map.put(t, val == null ? 1 : val + 1);
        }
        Map.Entry<T, Integer> max = null;
        for (Map.Entry<T, Integer> e : map.entrySet()) {
            if (max == null || e.getValue() > max.getValue())
                max = e;
        }
        return max.getKey();
    }
}
