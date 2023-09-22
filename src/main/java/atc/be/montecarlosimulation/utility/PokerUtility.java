package atc.be.montecarlosimulation.utility;

import atc.be.montecarlosimulation.costant.RANKINGS;
import atc.be.montecarlosimulation.costant.SCORE;
import atc.be.montecarlosimulation.costant.SUITS;
import atc.be.montecarlosimulation.model.*;

import java.util.*;

public class PokerUtility {
    private static final List<String> ROYALFLUSHCARDS = List.of("A", "K", "Q", "J", "10");

    public static List<Card> getCardsFromString(List<String> values){
        List<Card> cards = new ArrayList<>();
        for(String value : values){
            cards.add(stringToCard(value));
        }
        return cards;
    }

    public static Card extractCard(Deck deck) {
        return Deck.extractCard(deck);
    }

    public static List<Card> fillTable(List<Card> tableCards, Deck deck){
        tableCards = tableCards.stream().map(card -> card.getRanking() == null ? extractCard(deck) : card ).toList();
        return tableCards;
    }

    public static MainPlayerHand evaluateRankingHand(List<Card> playerHand, List<Card> table){
        MainPlayerHand mainPlayerHand = new MainPlayerHand();
        List<Card> allCards = new ArrayList<>();
        allCards.addAll(playerHand);
        allCards.addAll(table);
        mainPlayerHand.setCards(List.copyOf(allCards));
        for(SCORE score : SCORE.values()){
            getHandRanking(score, allCards, mainPlayerHand);
            if(mainPlayerHand.getRanking() != -1){
                return mainPlayerHand;
            }
        }
        return mainPlayerHand;
    }

     // PRIVATE METHODS

    private static Card stringToCard(String value){
        if(value.length() >= 2){
            RANKINGS ranking = RANKINGS.valueOfLabel(value.substring(0, value.length()-1));
            SUITS suit = SUITS.valueOfLabel(String.valueOf(value.charAt(value.length()-1)));
            return new Card(new Suit(suit.getRanking(), suit.getSuit()), new Ranking(ranking.getRanking(), ranking.getCard()));
        }
        return null;
    }

    private static void getHandRanking(SCORE pokerScore, List<Card> allCards, MainPlayerHand mainPlayerHand){
        List<Card> allCardsDuplicate = new ArrayList<>(allCards);
        String actualScore = SCORE.NONE.toString();
        int score = SCORE.NONE.getRanking();
        switch (pokerScore){
            case ROYAL_FLUSH -> {
                actualScore = SCORE.ROYAL_FLUSH.toString();
                score = getRoyalFlushRanking(allCardsDuplicate);
            }
            case STRAIGHT_FLUSH -> {
                actualScore = SCORE.STRAIGHT_FLUSH.toString();
                score = getStraightFlushRanking(allCardsDuplicate);
            }
            case FOUR_OF_A_KIND -> {
                actualScore = SCORE.FOUR_OF_A_KIND.toString();
                score = getCommonCardRanking(allCardsDuplicate, 4);
            }
            case FULL_HOUSE -> {
                actualScore = SCORE.FULL_HOUSE.toString();
                score = getFullHouseRanking(allCardsDuplicate);
            }
            case FLUSH -> {
                actualScore = SCORE.FLUSH.toString();
                score = getFlushRanking(allCardsDuplicate);
            }
            case STRAIGHT -> {
                actualScore = SCORE.STRAIGHT.toString();
                score = getStraightRanking(allCardsDuplicate);
            }
            case THREE_OF_A_KIND -> {
                actualScore = SCORE.THREE_OF_A_KIND.toString();
                score = getCommonCardRanking(allCardsDuplicate, 3);
            }
            case TWO_PAIR -> {
                actualScore = SCORE.TWO_PAIR.toString();
                score = getTwoPairRanking(allCardsDuplicate);
            }
            case PAIR -> {
                actualScore = SCORE.PAIR.toString();
                score = getCommonCardRanking(allCardsDuplicate, 2);
            }
            case HIGH_CARD -> {
                actualScore = SCORE.HIGH_CARD.toString();
                score = getHighCardRanking(allCardsDuplicate, 5);
            }
            default -> {
                actualScore = SCORE.NONE.toString();
                score = -1;
            }
        }
        mainPlayerHand.setScore(actualScore);
        mainPlayerHand.setRanking(score);
    }

    private static int getCommonCardRanking(List<Card> cards, int threshold){
        List<String> cardLabels = cards.stream().map(Card::getRanking).map(Ranking::getLabel).toList();
        Map.Entry<String, Integer> firstMostCommonCard = mostCommon(cardLabels);
        if(firstMostCommonCard.getValue() == threshold){
            switch (threshold) {
                case 4 -> {
                    cards.removeIf(card -> (card.getRanking().getLabel().equals(firstMostCommonCard.getKey())));
                    return SCORE.FOUR_OF_A_KIND.getRanking() + getHighCardRanking(cards, 2);
                }
                case 3 -> {
                    List<String> filteredCards = new ArrayList<>(cardLabels);
                    filteredCards.removeIf(s -> s.equals(firstMostCommonCard.getKey()));
                    Map.Entry<String, Integer> secondMostCommonCard = mostCommon(filteredCards);
                    RANKINGS firstMostRanking = RANKINGS.valueOfLabel(firstMostCommonCard.getKey());
                    RANKINGS secondMostRanking = RANKINGS.valueOfLabel(secondMostCommonCard.getKey());
                    int score;
                    if(secondMostCommonCard.getValue() == 3 && firstMostRanking.getRanking() < secondMostRanking.getRanking()){
                        cards.removeIf(card -> (card.getRanking().getLabel().equals(secondMostCommonCard.getKey())));
                        score = secondMostRanking.getRanking() * 3;
                    } else {
                        cards.removeIf(card -> (card.getRanking().getLabel().equals(firstMostCommonCard.getKey())));
                        score = firstMostRanking.getRanking() * 3;
                    }
                    return SCORE.THREE_OF_A_KIND.getRanking() + score + getHighCardRanking(cards, 2);
                }
                case 2 -> {
                    List<RANKINGS> pairCards = getPairCards(cardLabels);
                    int ranking = pairCards.get(0).getRanking();
                    cards.removeIf(card -> (card.getRanking().getLabel().equals(pairCards.get(0).getCard())));
                    return SCORE.PAIR.getRanking()
                            + ranking * 2
                            + getHighCardRanking(cards, 3);
                }
                default -> {
                    return -1;
                }
            }

        }
        return -1;
    }

    private static int getTwoPairRanking(List<Card> cards){
        List<String> cardLabels = cards.stream().map(Card::getRanking).map(Ranking::getLabel).toList();
        List<RANKINGS> pairCards = getPairCards(cardLabels);
        if(pairCards.size() > 1){
            List<Integer> rankings = new ArrayList<>(pairCards.stream().map(RANKINGS::getRanking).sorted(Integer::compareTo).toList());
            Collections.reverse(rankings);
            if(pairCards.size() == 3){
                cards.removeIf(card -> (card.getRanking().getLabel().equals(
                        RANKINGS.valueOfValue(rankings.get(2)).getCard()
                )));
            }
            cards.removeIf(card -> (card.getRanking().getLabel().equals(
                    RANKINGS.valueOfValue(rankings.get(0)).getCard()
            )));
            cards.removeIf(card -> (card.getRanking().getLabel().equals(
                    RANKINGS.valueOfValue(rankings.get(1)).getCard()
            )));
            return SCORE.TWO_PAIR.getRanking()
                    + rankings.subList(0, 2).stream().reduce(0, Integer::sum)*2
                    + getHighCardRanking(cards, 1);
        }
        return -1;
    }

    private static int getFullHouseRanking(List<Card> cards){
        List<String> cardLabels = cards.stream().map(Card::getRanking).map(Ranking::getLabel).toList();
        Map.Entry<RANKINGS, Integer> threeOfKind = mostCommonCards(cardLabels);
        if(threeOfKind.getValue() == 3){
            List<RANKINGS> pairCards = getPairCards(cardLabels);
            if(pairCards.size() == 2){
                if(pairCards.get(0).getRanking() > pairCards.get(1).getRanking()){
                    return threeOfKind.getKey().getRanking() * 3 + pairCards.get(0).getRanking() * 2;
                } else {
                    return threeOfKind.getKey().getRanking() * 3 + pairCards.get(1).getRanking() * 2;
                }
            }
        }
        return -1;
    }

    private static int getRoyalFlushRanking(List<Card> cards){
        List<Card> royalFlush = new ArrayList<>();
        for(Card card : cards){
            if(card != null && SCORE.getRoyalflushcards().contains(card.getRanking().getLabel())){
                royalFlush.add(card);
            }
        }
        if(royalFlush.size() >= 5 && !getFlushCards(cards).isEmpty()){
            return SCORE.ROYAL_FLUSH.getRanking();
        }
         return -1;
    }

    private static int getFlushRanking(List<Card> cards){
        List<Card> flushCards = getFlushCards(cards);
        if(!flushCards.isEmpty()){
            return SCORE.FLUSH.getRanking() + flushCards.stream().limit(5).map(card -> card.getRanking().getValue()).reduce(0, Integer::sum);
        }
        return -1;
    }

    private static int getStraightRanking(List<Card> cards){
        List<Card> straightCards = getStraightCards(cards);
        if(!straightCards.isEmpty()){
            return SCORE.STRAIGHT.getRanking() + straightCards.stream().map(card -> card.getRanking().getValue()).reduce(0, Integer::sum);
        }
        return -1;
    }

    private static int getStraightFlushRanking(List<Card> cards){
        List<Card> flushCards = getFlushCards(cards);
        if(!flushCards.isEmpty()){
            List<Card> straightCards = getStraightCards(flushCards);
            if(!straightCards.isEmpty()){
                return SCORE.STRAIGHT_FLUSH.getRanking() + straightCards.stream().map(card -> card.getRanking().getValue()).reduce(0, Integer::sum);
            }
        }
        return -1;
    }

    private static List<Card> getFlushCards(List<Card> cards){
        for(SUITS suit : SUITS.values()){
            short flush = 0;
            for(Card card : cards){
                if(suit.getSuit().equals(card.getSuit().getLabel())){
                    flush++;
                }
            }
            if(flush >= 5){
                List<Card> flushCards = new ArrayList<>(cards.stream().filter(card -> card.getSuit().getLabel().equals(suit.getSuit())).toList());
                flushCards.sort(Comparator.comparingInt(c -> c.getRanking().getValue()));
                Collections.reverse(flushCards);
                return flushCards;
            }
        }
        return new ArrayList<>();
    }

    private static List<Card> getStraightCards(List<Card> cards){
        Map<Integer, Card> straightDistinct = new HashMap<Integer, Card>();
        for (Card card : cards) {
            if (!straightDistinct.containsKey(card.getRanking().getValue())) {
                straightDistinct.put(card.getRanking().getValue(), card);
            }
        }
        List<Card> straight = new ArrayList<>(straightDistinct.values().stream().toList());
        Collections.reverse(straight);
        if(straight.size() > 4){
            for(int i = 0; i < straight.size()-4; i++){
                int straightCount = 0, j = 0;
                while(straight.get(j+i).getRanking().getValue() == straight.get(j+i+1).getRanking().getValue()+1 && j < 4){
                    j++;
                    straightCount++;
                    if(straightCount == 4){
                        return straight.subList(i, straight.size()-1).stream().limit(5).toList();
                    }
                }
            }
        }
        return new ArrayList<>();
    }

    private static <T> Map.Entry<T, Integer> mostCommon(List<T> list) {
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
        return max;
    }

    private static <T> Map.Entry<RANKINGS, Integer> mostCommonCards(List<T> list) {
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
        return Map.entry(RANKINGS.valueOfLabel(String.valueOf(max.getKey())), max.getValue());
    }

    private static int getHighCardRanking(List<Card> cards, int nCardsToCount){
        List<Card> orderedCards = new ArrayList<>(cards);
        List<Integer> rankings = new ArrayList<>(orderedCards.stream().map(card -> card.getRanking().getValue()).toList());
        Collections.sort(rankings);
        Collections.reverse(rankings);
        return rankings.subList(0, nCardsToCount).stream().reduce(0, Integer::sum);
    }

    private static List<RANKINGS> getPairCards(List<String> cardLabels){
        ArrayList<Map.Entry<RANKINGS, Integer>> commonCards = new ArrayList<>();
        // First common card
        List<String> filteredCards = new ArrayList<>(cardLabels);
        commonCards.add(mostCommonCards(filteredCards));
        // Second common card
        filteredCards.removeIf(s -> s.equals(commonCards.get(0).getKey().getCard()));
        commonCards.add(mostCommonCards(filteredCards));
        // Third common card
        filteredCards.removeIf(s -> s.equals(commonCards.get(1).getKey().getCard()));
        commonCards.add(mostCommonCards(filteredCards));
        ArrayList<RANKINGS> pairCards = new ArrayList<>();
        for(Map.Entry<RANKINGS, Integer> commonCard : commonCards){
            if(commonCard.getValue() == 2){
                pairCards.add(commonCard.getKey());
            }
        }
        return pairCards;
    }
}
