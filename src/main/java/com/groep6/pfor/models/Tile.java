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
	
    /**
     * @returns the position of a tile
     */
	public Vector2f getTilePosition() {
		return tilePosition;
	}
	
    /**
     * @returns an array of neighboring cities
     */
	public City[] getNeighbouringCities() {
		return neighbouringCities.toArray(new City[0]);
	}

	/**
	 * Add a neighbouring city to this tile
	 * @param neighbour
	 */
	public void addNeighbour(City neighbour) {
		neighbouringCities.add(neighbour);
	}
}
