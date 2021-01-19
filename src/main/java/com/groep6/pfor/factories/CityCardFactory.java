package com.groep6.pfor.factories;

import com.groep6.pfor.Config;
import com.groep6.pfor.models.Deck;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.models.cards.CityCard;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.util.parsers.CityCardParser;

import java.text.ParseException;

/**
 * @author Nils van der Velden
 */

/**
 * Creates CityCards
 *
 * @author Owen Elderbroek
 */
public class CityCardFactory {
	/** The instance of this singleton class */
	private static final CityCardFactory INSTANCE = createCityCardFactory();

	/** The list of available city cards */
	private final Deck cityCardDeck = createCityCardDeck();

	private static CityCardFactory createCityCardFactory() {
		return new CityCardFactory();
	}

	private Deck createCityCardDeck() {
		return new Deck();
	}

	private CityCardParser createNewCityCardParser() {
		return new CityCardParser();
	}

	private void addCityCardsToCityCardDeck(Deck cityCardDeck, CityCardParser cityCardParser) {
		try {
			cityCardDeck.addCardsToDeck(cityCardParser.parseFile("/citycards.json"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates the CityCardFactory instance
	 * @return The new instance
	 */
	private CityCardFactory() {
		CityCardParser cityCardParser = createNewCityCardParser();
		addCityCardsToCityCardDeck(cityCardDeck, cityCardParser);
	}

	private boolean cardIsInstanceOfCiyCard(Card card) {
		return card instanceof CityCard;
	}

	private CityCard parseCardToCityCard(Card card) {
		return (CityCard) card;
	}

	private boolean cityCardNameEqualsRequestedCardName(CityCard cityCard, String requestedCardName) {
		return cityCard.getCardName().equals(requestedCardName);
	}

	private boolean cityCardFactionEqualsRequestedCardFaction(CityCard cityCard, Faction requestedCardFaction) {
		return cityCard.getFactionCityCardBelongsTo().equals(requestedCardFaction);
	}

	private boolean inDebugMode() {
		return Config.DEBUG;
	}

	public CityCard getCardByName(String requestedCardName, Faction requestedCardFaction) {
		for (Card card: cityCardDeck.getCards()) {
			if (cardIsInstanceOfCiyCard(card)) {
				CityCard cityCard = parseCardToCityCard(card);
				if (cityCardNameEqualsRequestedCardName(cityCard, requestedCardName) && cityCardFactionEqualsRequestedCardFaction(cityCard, requestedCardFaction)) return cityCard;
			}
		}
		if (inDebugMode()) printNoCityCardNotFound();
		return null;
	}

	private void printNoCityCardNotFound() {
		System.out.println("[ERROR] No city card found");
	}

	/**
	 * Returns the instance of the CityCardFactory
	 * @return The CityCardFactory instance
	 */
	public static CityCardFactory getCityCardFactoryInstance() {
		return INSTANCE;
	}

	/**
	 * Get a deck containing all city cards currently loaded into the game
	 * @return A deck of city cards
	 */
	public Deck getCityCardDeck() {
		return cityCardDeck;
	}
}

