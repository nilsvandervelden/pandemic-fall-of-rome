package com.groep6.pfor.models;

import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.util.Observable;
import com.groep6.pfor.util.Vector2f;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a tile 
 *
 * @author Nils van der Velden
 */
public abstract class Tile extends Observable {
	protected Vector2f tilePosition;
	protected Faction[] factionsAllowedOnTile;
	protected List<City> neighbouringCities = new ArrayList<>();

	/**
	 * Initializes a abstract Tile with the given components.
	 * @param tilePosition The Vector2f (position) of a specific tile
	 * @param factionsAllowedOnTile What factions are allowed in a specific tile
	 */
	public Tile(Vector2f tilePosition, Faction[] factionsAllowedOnTile) {
		this.tilePosition = tilePosition;
		this.factionsAllowedOnTile = factionsAllowedOnTile;
	}

	public Vector2f getTilePosition() {
		return tilePosition;
	}

	public City[] getNeighbouringCities() {
		return neighbouringCities.toArray(new City[0]);
	}

	public void addNeighbouringCity(City neighbour) {
		neighbouringCities.add(neighbour);
	}
}
