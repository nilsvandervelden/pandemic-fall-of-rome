package com.groep6.pfor.models;

import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.util.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Bastiaan Jansen
 * @author Nils van der Velden
 */
public class PlayerHand extends Observable {

    private final List<Card> playerHandCards = new ArrayList<>();

    public PlayerHand(Card... cards) {
        addCardsToPlayerHand(cards);
    }

    public void addCardsToPlayerHand(Card... cards) {
        this.playerHandCards.addAll(Arrays.asList(cards));
        notifyObservers();
    }

    public void removeCardsFromPlayerHand(Card... cards) {
        this.playerHandCards.removeAll(Arrays.asList(cards));
        notifyObservers();
    }

    public List<Card> getPlayerHandCards() {
        return playerHandCards;
    }

    public void removeCardFromPlayerHand(Card card) {
        playerHandCards.remove(card);
        notifyObservers();
    }
    
    public int getAmountOfCardsInPlayerHand() {
        return playerHandCards.size();
    }
}

