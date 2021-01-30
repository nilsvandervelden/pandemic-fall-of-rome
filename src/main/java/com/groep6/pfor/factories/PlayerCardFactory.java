package com.groep6.pfor.factories;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Deck;
import com.groep6.pfor.models.cards.CityCard;
import com.groep6.pfor.models.factions.Faction;

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
		String[][] cities = {
				{"Carnuntum", "OSTROGOTHS"}, {"Aquileia", "OSTROGOTHS"}, {"Chersonesus", "OSTROGOTHS"}, {"Roma", "OSTROGOTHS"},
				{"Constantinopolis", "OSTROGOTHS"}, {"Sinope", "OSTROGOTHS"}, {"Narona", "VISIGOTHS"}, {"Athenae", "VISIGOTHS"},
				{"Genua", "VISIGOTHS"}, {"Ravenna", "VISIGOTHS"}, {"Patrae", "VISIGOTHS"}, {"Aquileia", "VISIGOTHS"},
				{"Corduba", "VISIGOTHS"}, {"Narbo", "VISIGOTHS"}, {"Constantinopolis", "VISIGOTHS"}, {"Tyras", "VISIGOTHS"},
				{"Nova Carthago", "VISIGOTHS"}, {"Roma", "VISIGOTHS"}, {"Philippopolis", "VISIGOTHS"}, {"Eburacum", "ANGLO_SAXSONS_FRANKS"},
				{"Mogontiacum", "ANGLO_SAXSONS_FRANKS"}, {"Genua", "ANGLO_SAXSONS_FRANKS"}, {"Gesoriacum", "ANGLO_SAXSONS_FRANKS"}, {"Narbo", "ANGLO_SAXSONS_FRANKS"},
				{"Roma", "ANGLO_SAXSONS_FRANKS"}, {"Lutetia", "ANGLO_SAXSONS_FRANKS"}, {"Burdigala", "ANGLO_SAXSONS_FRANKS"}, {"Londinium", "ANGLO_SAXSONS_FRANKS"},
				{"Brundisium", "HUNS"}, {"Carnuntum", "HUNS"}, {"Iuvavum", "HUNS"}, {"Philippopolis", "HUNS"}, {"Mediolanum", "HUNS"}, {"Roma", "HUNS"},
				{"Lutetia", "HUNS"}, {"Patrae", "HUNS"}, {"Lugdunum", "HUNS"}, {"Corduba", "VANDALS"}, {"Carthago", "VANDALS"},
				{"Lugdunum", "VANDALS"}, {"Roma", "VANDALS"}, {"Athenae", "VANDALS"}, {"Mogontiacum", "VANDALS"}, {"Caesaraugusta", "VANDALS"},
				{"Cesarea", "VANDALS"}, {"Constantinopolis", "VANDALS"}, {"Burdigala", "VANDALS"}, {"Syracusae", "VANDALS"}, {"Tingi", "VANDALS"}
		};

		for (String[] city : cities)
			playerCardDeck.addCardsToDeck(createNewCityCard(city));

	}

	private CityCard createNewCityCard(String[] city) {
		return new CityCard(getCityByName(city), getFactionTypeByFactionName(city));
	}

	private City getCityByName(String[] city) {
		CityFactory cityFactory = getCityFactory();
		return cityFactory.getCityByName(city[0]);
	}

	private Faction getFactionTypeByFactionName(String[] city) {
		FactionFactory factionFactory = getFactionFactory();
		return factionFactory.getFaction(factionFactory.getFactionTypeByFactionName(city[1]));
	}
	
	private PlayerCardFactory() {
        addCityCardsToPlayerCardDeck();
	}

	public static PlayerCardFactory getInstance() {
		return INSTANCE;
	}
}
