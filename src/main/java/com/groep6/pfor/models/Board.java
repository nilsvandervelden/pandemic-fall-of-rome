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

	public Board() {
		initiateBoard();
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
			tiles.put(city.getCityName(), city);
		}
	}

	private void initiateBoard() {
		addAllCitiesToBoard();
	}

	/**
	 * Construct a base from firebase data
	 * @return A Firebase instance of the board
	 */
	public Board(Map<String, Tile> tiles) {
		this.tiles = tiles;
	}

	private void updateCity(City city, Board board) {
		city.updateCity(getCityByName(city, board));
	}

	private City getCityByName(City city, Board board) {
		return (City) board.getTileByName(getCityName(city));
	}

	private String  getCityName(City city) {
		return city.getCityName();
	}

	private void updateCities(Board fireBaseBoard) {
		for (Tile tile : this.tiles.values()) {
			City city = (City) tile;
			updateCity(city, fireBaseBoard);
		}
	}

	public void updateBoardWithFireBaseData(Board fireBaseBoard) {
		updateCities(fireBaseBoard);
	}

	public Tile[] getTiles() {
		return tiles.values().toArray(new Tile[0]);
	}

	public Tile getTileByName(String name) {
		return tiles.get(name);
	}
}
