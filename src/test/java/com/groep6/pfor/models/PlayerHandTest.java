package com.groep6.pfor.models;

import com.groep6.pfor.factories.CityCardFactory;
import com.groep6.pfor.models.cards.Card;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author Nils van der Velden
 */

public class PlayerHandTest {

	private PlayerHand playerHand;
	private Card card1;
	private Card card2;

    @BeforeEach
    void setUp() {
        CityCardFactory cityCardFactory = CityCardFactory.getCityCardFactoryInstance();
        List<Card> cards = cityCardFactory.getCityCardDeck().getCards();
        this.card1 = cards.get(0);
        this.card2 = cards.get(1);
        this.playerHand = new PlayerHand();
        playerHand.addCardsToPlayerHand(card1, card2);
    }

    @AfterEach
    void tearDown() {
        this.playerHand = null;
        this.card1 = null;
        this.card2 = null;
    }

    @Test
    void removeCards() {
        playerHand.removeCardFromPlayerHand(card1);
        assertEquals(1, playerHand.getAmountOfCardsInPlayerHand());
    }
}