package com.sprinthive.pokerhands.handrank;

import com.sprinthive.pokerhands.Card;
import com.sprinthive.pokerhands.CardRank;
import com.sprinthive.pokerhands.Suit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BadPokerHandRanker implements HandRanker {

    public HandRank findBestHandRank(List<Card> cards) {

        Collections.sort(cards);

        if (cards.size() != 5) {
            return new NotRankableHandRanker(cards);
        }
        else if(CheckStraight(cards) && CheckFlush(cards) && cards.get(0).getRank().getValue() == 10){
            return new RoyalFlushHandRank(cards.get(0).getSuit());
        }
        else if(CheckStraight(cards) && CheckFlush(cards)){
            return new StraightFlushHandRank(cards.get(4).getRank());
        }
        else if (CheckFourOfAKind(cards)) {
            return new FourOfAKindHandRank(cards.get(2).getRank());
        }
        else if (CheckFullHouse(cards)) {
            return new FullHouseHandRank(cards.get(2).getRank(), cards.get(4).getRank());
        }
        else if (CheckFlush(cards)) {
            return new FlushHandRank(cards);
        } 
        else if (CheckStraight(cards)) {
            return new StraightHandRank(cards.get(4).getRank());
        }
        else if (CheckThreeOfAKind(cards)) {

            return new ThreeOfAKindHandRank(cards.get(2).getRank());

        }
        else if (CheckTwoPairs(cards)) {

            int firstPairIndex = findPairIndex(cards);
            int secondPairIndex = findSecondPairIndex(cards);
            int kicker = 0;

            for (int i = 0; i < cards.size(); i++) {
                
                if(i != firstPairIndex && i != (firstPairIndex + 1) && i != secondPairIndex && i != (secondPairIndex + 1)){

                    kicker = i;
                }
            }
            return new TwoPairHandRank(cards.get(secondPairIndex + 1).getRank(), cards.get(firstPairIndex + 1).getRank(), cards.get(kicker).getRank());

        }
            
        else if (CheckOnePair(cards)) {

            List<CardRank> restRanks = new ArrayList<>();

            int pairIndex = findPairIndex(cards);

            
            for (int i = 0; i < cards.size(); i++) {
                
                if(i != pairIndex && i != (pairIndex + 1)){

                    restRanks.add(cards.get(i).getRank());
                }
            }

            return new OnePairHandRank(cards.get(pairIndex + 1).getRank(), restRanks);

        } 
        
        Collections.reverse(cards);
        // High card
        return new HighCardHandRank(cards);

        
    }

    

    private boolean CheckFourOfAKind(List<Card> cards) {

        // Check for the first upper four
        if (cards.get(0).getRank() == cards.get(3).getRank() && cards.get(4).getRank() != cards.get(0).getRank()) {
            return true;
        }
    
        // Check for the last low four
        if (cards.get(0).getRank() == cards.get(1).getRank() && cards.get(2).getRank() == cards.get(4).getRank()) {
            return true;
        }
    
        return false;
    }

    private boolean CheckFullHouse(List<Card> cards) {
        return (cards.get(0).getRank() == cards.get(1).getRank() && cards.get(3).getRank() == cards.get(4).getRank()) ||
               (cards.get(0).getRank() == cards.get(1).getRank() && cards.get(2).getRank() == cards.get(4).getRank());
    }

    private boolean CheckFlush(List<Card> cards) {
        return cards.get(0).getSuit() == cards.get(1).getSuit() &&
                cards.get(1).getSuit() == cards.get(2).getSuit() &&
                cards.get(2).getSuit() == cards.get(3).getSuit() &&
                cards.get(3).getSuit() == cards.get(4).getSuit();
    }

    public static boolean CheckStraight(List<Card> cards) {

        // Check for consecutive numbers
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getRank().getValue() + 1 != cards.get(i + 1).getRank().getValue()) {
                return false; // Not a straight
            }
        }

        return true; // Found a straight
    }

    private boolean CheckThreeOfAKind(List<Card> cards) {
        return cards.get(0).getRank() == cards.get(2).getRank() ||
               cards.get(1).getRank() == cards.get(3).getRank() ||
               cards.get(2).getRank() == cards.get(4).getRank();
    }

    private int findSecondPairIndex(List<Card> cards) {

        int firstPairIndex = findPairIndex(cards);
    
        for (int i = firstPairIndex + 2; i < cards.size() - 1; i++) {
            if (cards.get(i).getRank() == cards.get(i + 1).getRank()) {
                return i;
            }
        }
        throw new IllegalStateException("No second pair found.");
    }

    private boolean CheckTwoPairs(List<Card> cards) {

        // Check for consecutive pairs
        int pairCount = 0;
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getRank()  == cards.get(i + 1).getRank()) {
                pairCount++;
                i++; // Skip the next element of the pair
            }
        }

        return pairCount >= 2; // Return true if at least two pairs are found
    }

    private int findPairIndex(List<Card> cards) {
        for (int i = 0; i < cards.size() - 1; i++) {
            if (cards.get(i).getRank() == cards.get(i + 1).getRank()) {
                return i; // Found the pair, return the index of the first card in the pair
            }
        }
        // If no pair is found, you may want to handle this case accordingly.
        throw new IllegalStateException("No pair found in the ordered cards.");
    }
    
    private boolean CheckOnePair(List<Card> cards){

        // Check for adjacent equal elements
        for (int i = 0; i < cards.size() - 1; i++) {
            //pair found
            if (cards.get(i).getRank() == cards.get(i + 1).getRank()) {
                return true;
            }
        }

        return false; // No pair found
    }
    
}
