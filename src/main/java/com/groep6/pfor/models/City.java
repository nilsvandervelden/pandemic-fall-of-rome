package com.groep6.pfor.models;

import com.groep6.pfor.factories.CityFactory;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.models.factions.FactionType;
import com.groep6.pfor.util.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Represents a city tile
 *
 * @author Nils van der Velden
 */

public class City extends Tile {
	
	private List<Barbarian> barbariansInCity = new ArrayList<>();
	private List<Legion> legionsInCity = new ArrayList<>();
	private boolean hasFort = false;
	private boolean hasHarbour;
	private String cityName;

	/**
	 * Initializes a new City with the given components.
     * @param cityName The name of a specific city
     * @param hasHarbour Whether or not a city has a harbour
     * @param positionOfCity The Vector2f (positionOfCity) of a specific city
     * @param factionsAllowedInCity What factionsAllowedInCity are allowed in a specific city
     */
	public City(String cityName, boolean hasHarbour, Vector2f positionOfCity, Faction[] factionsAllowedInCity) {
		super(positionOfCity, factionsAllowedInCity);
		this.cityName = cityName;
		this.hasHarbour = hasHarbour;
	}

	/**
	 * Constructs a new city with data from a city from the remote server
	 * @param cityName The name of the city
	 * @param legionsInCity The legions in this city
	 * @param barbariansInCity The barbarians in this city
	 * @param hasFort whether this city has a fort in it
	 * @return A new city instance
	 */

	public City(String cityName, Stack<Legion> legionsInCity, Stack<Barbarian> barbariansInCity, boolean hasFort) {
		super(CityFactory.getInstance().getCityByName(cityName).position, CityFactory.getInstance().getCityByName(cityName).getFactions());
		this.legionsInCity = legionsInCity;
		this.barbariansInCity = barbariansInCity;
		this.hasFort = hasFort;
	}

	/**
	 * Updates this model with data from a city from the remote server
	 * @param city The city to copy the data from
	 */
	public void updateCity(City city) {
		this.barbariansInCity = city.barbariansInCity;
		this.legionsInCity = city.legionsInCity;
		this.hasFort = city.hasFort;
	}

    /**
     * @returns An array with factions that can access the city
     */
	public Faction[] getFactions() {
		return factions;
	}

	/**
     * @returns the name of a specific city
     */
	public String getCityName() {
		return cityName;
	}

	/**
	 * @return The total amount of barbarians in this city of all factions combined
	 */
	public int getAmountOfBarbariansLocatedInCurrentCity() {
    	return barbariansInCity.size();
	}
	
    /**
     * @returns the amount of legions in a specific city
     */
	
	public int getLegionCount() {
		return legionsInCity.size();
	}
	
    /**
     * @returns a arrayList with barbarians in a specific city
     */
	
	public List<Barbarian> getBarbariansInCity() {
		return barbariansInCity;
	}
	
	/**
     * @returns a arrayList with legions in a specific city
     */
	
	public List<Legion> getLegionsInCity() {
		return legionsInCity;
	}
	
    /**
     * @returns a boolean that represents whether a specific city has a fort or not
     */
	
	public boolean hasFort() {
		return hasFort;
	}
	
    /**
     * @returns a boolean that represents whether a specific city has a harbour or not
     */
	
	public boolean hasHarbour() {
		return hasHarbour;
	}

	/**
	 * Check if a city has a specific requestedFaction (barbarian) in it
	 * @param requestedFaction The requestedFaction you want to check for
	 * @return Whether the requestedFaction is in it or not
	 */
	public boolean requestedFactionInCity(Faction requestedFaction) {
		for (Barbarian barbarianInCity : barbariansInCity) {
			if (barbarianOfRequestedFactionInCity(barbarianInCity, requestedFaction)) return true;
		}
		return false;
	}

	private boolean barbarianOfRequestedFactionInCity(Barbarian barbarianInCity, Faction requestedFaction) {
		return requestedFaction.getFactionType().equals(getFactionType(barbarianInCity));
	}

	private FactionType getFactionType(Barbarian barbarianInCity) {
		return barbarianInCity.getFactionType();
	}

	private void addBarbarianToCity(FactionType factionType) {
		barbariansInCity.add(new Barbarian(factionType));
	}

	private boolean maximumAmountOfBarbariansInCity() {
		int maximumAmountOfBarbariansInCity = 4;
		return barbariansInCity.size() >= maximumAmountOfBarbariansInCity;
	}

	private void removeAllBarbariansFromCity() {
		barbariansInCity.clear();
	}

	private Game getGame() {
		return Game.getGameInstance();
	}

	private void increaseDecayLevel(Game currentGame) {
		currentGame.increaseDecayLevel(1);
	}

	private boolean gameExists(Game currentGame) {
		return currentGame != null;
	}
	
    /**
     * adds barbarians to a specific city
     * @param factionTypeOfBarbarian
     */
	public void addBarbariansToCity(FactionType factionTypeOfBarbarian, int amountOfBarbariansToAdd) {
		for (int i = 0; i < amountOfBarbariansToAdd; i++) {
			addBarbarianToCity(factionTypeOfBarbarian);

			if (maximumAmountOfBarbariansInCity()) {
				removeAllBarbariansFromCity();

				Game currentGame = getGame();
				if (gameExists(currentGame)) increaseDecayLevel(currentGame);
				return;
			}
		}
	}
	
    /**
     * @param requestedFactionType The faction to count the barbarians of
     * @returns the amount of barbarians in this city of the specified faction
     */
	
    public int getAmountOfBarbariansLocatedInCurrentCity(FactionType requestedFactionType, List<Barbarian> barbariansInCity) {
		int amountOfBarbariansInCity = 0;

		for (Barbarian barbarianInCity : barbariansInCity) {
			if (barbarianFactionEqualsRequestedFactionType(requestedFactionType, barbarianInCity)) amountOfBarbariansInCity++;
		}

		return amountOfBarbariansInCity;
	}

	public int getAmountOfBarbariansLocatedInCurrentCity(FactionType requestedFactionType) {
		int amountOfBarbariansInCity = 0;

		for (Barbarian barbarianInCity : barbariansInCity) {
			if (barbarianFactionEqualsRequestedFactionType(requestedFactionType, barbarianInCity)) amountOfBarbariansInCity++;
		}

		return amountOfBarbariansInCity;
	}

	private boolean barbarianFactionEqualsRequestedFactionType(FactionType requestedFactionType, Barbarian barbarian) {
    	return requestedFactionType == barbarian.getFactionType();
	}
	
    /**
     * adds a legion to a specific city
     */
	public void addLegionsToCurrentCity(int amountOfLegions) {
		for (int i = 0; i < amountOfLegions; i++) {
			addLegionToCity();
		}
		notifyObservers();
	}

	private void addLegionToCity() {
		legionsInCity.add(new Legion());
	}
	
    /**
     * @param factionType
     * @return a barbarian and removes that specific barbarian from a specific city
     */
	
	public void removeBarbariansFromCurrentCity(FactionType factionType, int amountOfBarbariansToRemove) {
		Barbarian barbarian = getBarbarian();
			for (int i = 0; i < amountOfBarbariansToRemove; i++) {
				if (isBarbarian(barbarian) && barbarianFactionEqualsRequestedFactionType(factionType, barbarian)) {
					removeBarbarian(barbarian);
				}
			}
		notifyObservers();
	}

	private Barbarian getBarbarian() {
		for (Barbarian barbarian : barbariansInCity) {
			return barbarian;
		}
		return null;
	}

	private boolean isBarbarian(Barbarian barbarian) {
		return barbarian != null;
	}

	private void removeBarbarian(Barbarian barbarian) {
		barbariansInCity.remove(barbarian);
	}


	/**
	 * @param amountOfBarbariansToRemove
	 * @return Removed barbarian
	 */
	public void removeBarbariansFromCurrentCity(int amountOfBarbariansToRemove) {
		for (int i = 0; i < amountOfBarbariansToRemove; i++) {
			if (cityContainsBarbarians()) removeBarbarian();
		}
		notifyObservers();
	}

	private boolean cityContainsBarbarians() {
		return barbariansInCity.size() > 0;
	}

	private void removeBarbarian() {
		barbariansInCity.remove(0);
	}

    /**
	 * @param amountOfLegionsToRemove
     */
	
	public void removeLegions(int amountOfLegionsToRemove) {
		for (int i = 0; i < amountOfLegionsToRemove; i++) {
			if (cityContainsLegions()) removeLegion();
		}

		notifyObservers();
	}

	private boolean cityContainsLegions() {
		return legionsInCity.size() > 0;
	}

	private void removeLegion() {
		legionsInCity.remove(0);
	}
	
    /**
     * places a fort in a specific city
     */
	public void placeFortInCity() {
		this.hasFort = true;
		notifyObservers();
	}

	@Override
	public String toString() {
		String formattedString = String.format("City: %formattedString, harbour: %b, position: %formattedString, factions: [", cityName, hasHarbour, position);
		for (Faction faction : factions) {
			formattedString += faction.getFactionType().name() + ", ";
		}
		if (factions.length > 0) formattedString = formattedString.substring(0, formattedString.length()-2);
		formattedString += "], neighbours: [";
		for (City neighbour : neighbouringCities) {
			formattedString += neighbour.getCityName() + ", ";
		}
		if (neighbouringCities.size() > 0) formattedString = formattedString.substring(0, formattedString.length()-2);
		return formattedString + "]";
	}
	
}
