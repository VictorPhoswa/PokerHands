package com.sprinthive.pokerhands;

import com.sprinthive.pokerhands.exception.NotEnoughCardsInDeckException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private List<Card> cards = new ArrayList<Card>(52);

    public Deck() {
        for (Suit suit : Suit.values()) {
            for (CardRank rank : CardRank.values()) {
                cards.add(new Card(rank, suit));
            }
        }
        Collections.shuffle(cards);
    }

    public synchronized int getNumberOfCards() {
        return cards.size();
    }

    public synchronized Card[] pick(int numberOfCards) throws NotEnoughCardsInDeckException {
        if(numberOfCards > 52){
            throw new IllegalArgumentException("Number of cards to pick from a deck must be 52 or less.");
        }
        else if(cards.size() < numberOfCards){
            throw new IllegalArgumentException("Number of cards to pick from a deck must be less than the deck size.");
        }
        else if(cards.size() == 0){
            throw new IllegalArgumentException("The deck size is zero.");
        }
        
        //Todo: This method still needs to be implemented
        List<Card> DeckpickedCards = new ArrayList<>(cards.subList(cards.size() - numberOfCards, cards.size()));
        cards.removeAll(DeckpickedCards);

        return DeckpickedCards.toArray(new Card[0]);
    }
}

//Created a new list to store the picked number of cards
//Picked the cards from the deck of 52 cards using subList
//Updated the deck of cards by removing the picked cards