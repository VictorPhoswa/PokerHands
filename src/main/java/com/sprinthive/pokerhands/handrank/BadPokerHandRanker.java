package com.sprinthive.pokerhands.handrank;

import com.sprinthive.pokerhands.Card;
import com.sprinthive.pokerhands.CardRank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class BadPokerHandRanker implements HandRanker {

    public HandRank findBestHandRank(List<Card> cards) {

        Collections.sort(cards);
        Collections.reverse(cards);

        if (cards.size() != 5) {
            return new NotRankableHandRanker(cards);
        }
         
        else if (CheckThreeOfAKind(cards)) {

            return new ThreeOfAKindHandRank(cards.get(2).getRank());

        }
        else if (CheckTwoPairs(cards)) {
            Collections.reverse(cards);
            return new TwoPairHandRank(cards.get(3).getRank(), cards.get(1).getRank(), cards.get(4).getRank());

        }
            
        else if (CheckOnePair(cards)) {
            
            List<CardRank> restRanks = new ArrayList<>();
            for (int i = 2; i < cards.size(); i++) {
                restRanks.add(cards.get(i).getRank());
            }

            return new OnePairHandRank(cards.get(1).getRank(), restRanks);

        } 
        
        

    
        // High card
        return new HighCardHandRank(cards);

        
    }


   
    private boolean CheckStraight(List<Card> cards) {
        for (int i = 1; i < cards.size(); i++) {
            if (cards.get(i).getRank().getValue() != cards.get(i - 1).getRank().getValue() + 1) {
                return false;
            }
        }
        return true;
    }

    private boolean CheckThreeOfAKind(List<Card> cards) {
        return cards.get(0).getRank() == cards.get(2).getRank() ||
               cards.get(1).getRank() == cards.get(3).getRank() ||
               cards.get(2).getRank() == cards.get(4).getRank();
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
