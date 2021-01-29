package com.groep6.pfor.util.parsers.templates;

import com.google.gson.annotations.SerializedName;
import com.groep6.pfor.models.factions.FactionType;

/**
 * The Data Transfer Object that represents a Tile in json
 *
 * @author Owen Elderbroek
 */
public class JsonTile {
    /** The position of the tile on the board in its DTO form */
    @SerializedName("position")
    private JsonVector2f tilePosition;

    /** The names of the neighbouring cities */
    @SerializedName("neighbours")
    private String[] neighbouringCities;

    /** The factions that are allowed in the city */
    @SerializedName("factions")
    private FactionType[] factionsAllowedInCity;

    /**
     * Obtain the position vector of this city in its DTO form
     * @return The position Data Transfer Object
     */
    public JsonVector2f getTilePosition() {
        return tilePosition;
    }

    /**
     * Obtain the list of factions in this city
     * @return The list of factions in this city
     */
    public FactionType[] getFactionsAllowedInCity() {
        return factionsAllowedInCity;
    }

    /**
     * Obtain a list of names of the cities that neighbour this one
     * @return A list of names of neighbouring cities
     */
    public String[] getNeighbouringCities() {
        return neighbouringCities;
    }
}
