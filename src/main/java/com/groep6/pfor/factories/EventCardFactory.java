package com.groep6.pfor.factories;

import com.groep6.pfor.Config;
import com.groep6.pfor.models.Deck;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.models.cards.EventCard;
import com.groep6.pfor.models.cards.actions.eventActions.AudentesFortunaAction;
import com.groep6.pfor.models.cards.actions.eventActions.CarpeDiemAction;
import com.groep6.pfor.models.cards.actions.eventActions.FaberFortunaeAction;
import com.groep6.pfor.models.cards.actions.eventActions.VitaMeaAction;

/**
* @author Nils van der Velden
*/

public class EventCardFactory {
	private static final EventCardFactory SINGLE_INSTANCE = createEventCardFactory();
	private final Deck eventCardDeck =  createEventCardDeck();

	private static EventCardFactory createEventCardFactory() {
		return new EventCardFactory();
	}

	private Deck createEventCardDeck() {
		return  new Deck();
	}

	private void addCardsToEventCardDeck() {
		eventCardDeck.addCardsToDeck(new EventCard("Mors Tua, Vita Mea", new VitaMeaAction()));

		eventCardDeck.addCardsToDeck(new EventCard("Homo Faber Fortunae Suae", new FaberFortunaeAction()));

		eventCardDeck.addCardsToDeck(new EventCard("Audentes Fortuna Iuvat", new AudentesFortunaAction()));

		eventCardDeck.addCardsToDeck(new EventCard("Carpe Diem", new CarpeDiemAction()));
		eventCardDeck.addCardsToDeck(new EventCard("Carpe Diem", new CarpeDiemAction()));
		eventCardDeck.addCardsToDeck(new EventCard("Carpe Diem", new CarpeDiemAction()));
		eventCardDeck.addCardsToDeck(new EventCard("Carpe Diem", new CarpeDiemAction()));
	}
	
	private EventCardFactory() {
		addCardsToEventCardDeck();
	}
	
    public static EventCardFactory getInstance() {
        return SINGLE_INSTANCE;
    }

	public Deck getEventCardDeck() {
		return eventCardDeck;
	}

	private boolean eventCardNameEqualsRequestedEventCardName(Card card, String requestedEventCardName) {
		return card.getCardName().equals(requestedEventCardName);
	}

	public EventCard getCardByName(String requestedEventCardName) {
		for (Card card : eventCardDeck.getCards()) {
			if (eventCardNameEqualsRequestedEventCardName(card, requestedEventCardName)) return (EventCard) card;
		}
		if (inDebugMode()) printEventCardWasNotFound(requestedEventCardName);
		return null;
	}

	private void printEventCardWasNotFound(String requestedEventCardName) {
		System.out.printf("[WARNING] Event card with name %s not found!\n", requestedEventCardName);
	}

	private boolean inDebugMode() {
		return Config.DEBUG;
	}
}