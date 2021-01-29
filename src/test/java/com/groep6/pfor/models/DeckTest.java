package com.groep6.pfor.models;

import com.groep6.pfor.factories.CityCardFactory;
import com.groep6.pfor.models.cards.Card;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Bastiaan Jansen
 */
class DeckTest {

    private Deck deck;
    private Card card1;
    private Card card2;

    @BeforeEach
    void setUp() {
        CityCardFactory cityCardFactory = CityCardFactory.getCityCardFactoryInstance();
        List<Card> cards = cityCardFactory.getCityCardDeck().getCards();
        this.deck = new Deck();
        this.card1 = cards.get(0);
        this.card2 = cards.get(1);
    }

    @AfterEach
    void tearDown() {
        this.deck = null;
        this.card1 = null;
        this.card2 = null;
    }

    @Test
    void addCards() {
        deck.addCardToDeck(card1);
        assertEquals(1, deck.getAmountOfCardsInDeck());
    }

    @Test
    void getCards() {
        deck.addCardToDeck(card1, card2);
        List<Card> cards = new ArrayList<>();
        cards.add(card1);
        cards.add(card2);
        assertEquals(2, deck.getAmountOfCardsInDeck());
        assertEquals(cards, deck.getCards());
    }

    @Test
    void removeCard() {
        deck.addCardToDeck(card1, card2);
        deck.removeCard(card1);
        assertEquals(1, deck.getAmountOfCardsInDeck());
        assertEquals(card2, deck.drawCardFromDeck());
    }

    @Test
    void merge() {
        deck.addCardToDeck(card1);
        Deck deck2 = new Deck(card2);
        deck.mergeDecks(deck2);
        assertEquals(2, deck.getAmountOfCardsInDeck());
    }

    @Test
    void draw() {
        deck.addCardToDeck(card1, card2);
        Card card = deck.drawCardFromDeck();
        assertEquals(card2, card);
    }

    @Test
    void getCardCount() {
        deck.addCardToDeck(card1, card2);
        assertEquals(2, deck.getAmountOfCardsInDeck());
    }
}