package com.groep6.pfor.factories;

import com.groep6.pfor.models.Deck;
import com.groep6.pfor.models.cards.CityCard;
import com.groep6.pfor.models.factions.FactionType;

public class PlayerCardFactory {
	
	private static final PlayerCardFactory INSTANCE = createPlayerCardFactory();
	private final Deck playerCardDeck = createPlayerCardDeck();

	private static PlayerCardFactory createPlayerCardFactory() {
		return new PlayerCardFactory();
	}

	private Deck createPlayerCardDeck() {
		return new Deck();
	}

	private FactionFactory getFactionFactory() {
		return FactionFactory.getInstance();
	}

	private CityFactory getCityFactory() {
		return 	CityFactory.getInstance();
	}

	private void addCityCardsToPlayerCardDeck() {
		FactionFactory factionFactory = getFactionFactory();
		CityFactory cityFactory = getCityFactory();
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Carnuntum"), factionFactory.getFaction(FactionType.OSTROGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Aquileia"), factionFactory.getFaction(FactionType.OSTROGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Chersonesus"), factionFactory.getFaction(FactionType.OSTROGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Roma"), factionFactory.getFaction(FactionType.OSTROGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Constantinopolis"), factionFactory.getFaction(FactionType.OSTROGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Sinope"), factionFactory.getFaction(FactionType.OSTROGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Narona"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Athenae"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Genua"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Ravenna"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Patrae"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Aquileia"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Corduba"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Narbo"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Constantinopolis"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Tyras"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Nova Carthago"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Roma"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Philippopolis"), factionFactory.getFaction(FactionType.VISIGOTHS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Eburacum"), factionFactory.getFaction(FactionType.ANGLO_SAXSONS_FRANKS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Mogontiacum"), factionFactory.getFaction(FactionType.ANGLO_SAXSONS_FRANKS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Genua"), factionFactory.getFaction(FactionType.ANGLO_SAXSONS_FRANKS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Gesoriacum"), factionFactory.getFaction(FactionType.ANGLO_SAXSONS_FRANKS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Narbo"), factionFactory.getFaction(FactionType.ANGLO_SAXSONS_FRANKS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Roma"), factionFactory.getFaction(FactionType.ANGLO_SAXSONS_FRANKS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Lutetia"), factionFactory.getFaction(FactionType.ANGLO_SAXSONS_FRANKS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Burdigala"), factionFactory.getFaction(FactionType.ANGLO_SAXSONS_FRANKS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Londinium"), factionFactory.getFaction(FactionType.ANGLO_SAXSONS_FRANKS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Brundisium"), factionFactory.getFaction(FactionType.HUNS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Carnuntum"), factionFactory.getFaction(FactionType.HUNS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Iuvavum"), factionFactory.getFaction(FactionType.HUNS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Philippopolis"), factionFactory.getFaction(FactionType.HUNS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Mediolanum"), factionFactory.getFaction(FactionType.HUNS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Roma"), factionFactory.getFaction(FactionType.HUNS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Lutetia"), factionFactory.getFaction(FactionType.HUNS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Patrae"), factionFactory.getFaction(FactionType.HUNS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Lugdunum"), factionFactory.getFaction(FactionType.HUNS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Corduba"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Carthago"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Lugdunum"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Roma"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Athenae"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Mogontiacum"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Caesaraugusta"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Cesarea"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Constantinopolis"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Burdigala"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Syracusae"), factionFactory.getFaction(FactionType.VANDALS)));
		playerCardDeck.addCardsToDeck(new CityCard(cityFactory.getCityByName("Tingi"), factionFactory.getFaction(FactionType.VANDALS)));
	}
	
	private PlayerCardFactory() {
        addCityCardsToPlayerCardDeck();
	}

	public static PlayerCardFactory getInstance() {
		return INSTANCE;
	}
}
