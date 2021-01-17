package com.groep6.pfor.models;

import com.groep6.pfor.factories.CityFactory;
import com.groep6.pfor.factories.FactionFactory;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.util.Vector2f;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the board
 *
 * @author Nils van der Velden
 */
public class Board {
	private Map<String, Tile> tiles = new HashMap<>();
	private Map<Faction, Base<Barbarian>> barbarianBases = new HashMap<>();
	private Base<Legion> legionBase = new Base<>(new Vector2f(0, 0), new Faction[] {});

	public Board() {
		initiateBoard();
	}

	private FactionFactory getFactionFactory() {
		return FactionFactory.getInstance();
	}

	private List<Faction> getAllFactions() {
		FactionFactory factionFactory = getFactionFactory();
		return factionFactory.getFactions();
	}

	private Base createBarbarianBase(Faction faction) {
		return new Base(new Vector2f(0, 0), new Faction[]{faction});
	}

	private void createBarbarianBases() {
		List<Faction> factions = getAllFactions();
		for (Faction faction : factions)
			barbarianBases.put(faction, createBarbarianBase(faction));
	}

	private CityFactory getCityFactory() {
		return CityFactory.getInstance();
	}

	private City[] getAllCitiesFromCityFactory() {
		CityFactory cityFactory = getCityFactory();
		return cityFactory.getAllCities();
	}

	private void addAllCitiesToBoard() {
		City[] cities = getAllCitiesFromCityFactory();

		for (City city : cities) {
			tiles.put(city.getName(), city);
		}
	}

	private void initiateBoard() {
		addAllCitiesToBoard();
		createBarbarianBases();
	}

	/**
	 * Construct a base from firebase data
	 * @param legionBase The base with legions
	 * @param barbarianBases A map containing all factions with their respective bases
	 * @return A Firebase instance of the board
	 */
	public Board(Map<String, Tile> tiles, Base<Legion> legionBase, Map<Faction, Base<Barbarian>> barbarianBases) {
		this.tiles = tiles;
		this.legionBase = legionBase;
		this.barbarianBases = barbarianBases;
	}

	private void updateCity(City city, Board board) {
		city.updateCity(getCityByName(city, board));
	}

	private City getCityByName(City city, Board board) {
		return (City) board.getTileByName(getCityName(city));
	}

	private String  getCityName(City city) {
		return city.getName();
	}

	private void updateLegionBase(Board fireBaseBoard) {
		this.legionBase.updateBase(fireBaseBoard.getLegionBase());
	}

	private Faction getFactionsAllowedInBase(Base <Barbarian> barbarianBase) {
		return barbarianBase.getFactionAllowedInBase();
	}


	private void updatedBarbarianBases(Board fireBaseBoard) {
		for (Base <Barbarian> barbarianBase : barbarianBases.values()) {
			barbarianBase.updateBase(fireBaseBoard.getBarbarianBase(getFactionsAllowedInBase(barbarianBase)));
		}
	}

	private void updateCities(Board fireBaseBoard) {
		for (Tile tile : this.tiles.values()) {
			City city = (City) tile;
			updateCity(city, fireBaseBoard);
		}
	}
	/**
	 * Update this fireBaseBoard with data from a firebase fireBaseBoard
	 * @param fireBaseBoard A fireBaseBoard containing data from firebase
	 */
	public void updateBoard(Board fireBaseBoard) {
		updateLegionBase(fireBaseBoard);
		updatedBarbarianBases(fireBaseBoard);
		updateCities(fireBaseBoard);
	}

	public Tile[] getTiles() {
		return tiles.values().toArray(new Tile[0]);
	}

	public Tile getTileByName(String name) {
		return tiles.get(name);
	}

	public Base<Legion> getLegionBase() {
		return legionBase;
	}

	public Base<Barbarian> getBarbarianBase(Faction faction) {
		return barbarianBases.get(faction);
	}

	public Map<Faction, Base<Barbarian>> getBarbarianBases() {
		return barbarianBases;
	}
}
