package com.groep6.pfor.models;

import com.groep6.pfor.exceptions.CardNotInDeckException;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.util.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a Deck with cards
 * @author Nils van der Velden
 */
public class Deck extends Observable {

    private final List<Card> cards = new ArrayList<>();

    public Deck(Card... cards) {
        this.addCardsToDeck(cards);
    }

    public void addCardsToDeck(Card... cards) {
        this.cards.addAll(Arrays.asList(cards));
    }

    public List<Card> getCards() {
        return cards;
    }

    public void removeCard(Card cardToRemove) {
        cards.remove(cardToRemove);
        notifyObservers();
    }

    public void mergeDecks(Deck deck) {
        cards.addAll(getCardsFromDeck(deck));
    }

    private List<Card> getCardsFromDeck(Deck deck) {
        return deck.getCards();
    }

    public void shuffleDeck() {
        Collections.shuffle(cards);
    }

    private boolean deckContainsCards() {
        return cards.size() > 0;
    }

    public Card drawCardFromDeck() throws CardNotInDeckException {
        if (!deckContainsCards()) throw new CardNotInDeckException();

        int indexOfFirstCard = getIndexOfFirstCardInDeck();
        Card fistCardInDeck = getFirstCardInDeck(indexOfFirstCard);
        removeCardByIndex(indexOfFirstCard);
        return fistCardInDeck;
    }

    private void removeCardByIndex(int indexOfFirstCard) {
        cards.remove(indexOfFirstCard);
    }

    private int getIndexOfFirstCardInDeck() {
        return cards.size() - 1;
    }

    private Card getFirstCardInDeck(int indexOfFirstCard) {
        return cards.get(indexOfFirstCard);
    }

    public int getAmountOfCardsInDeck() {
        return cards.size();
    }
}

