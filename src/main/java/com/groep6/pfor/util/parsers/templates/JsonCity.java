package com.groep6.pfor.util.parsers.templates;

import com.google.gson.annotations.SerializedName;
import com.groep6.pfor.factories.FactionFactory;
import com.groep6.pfor.models.City;
import com.groep6.pfor.models.factions.Faction;

/**
 * The Data Transfer Object that represents a City in json
 *
 * @author Owen Elderbroek
 */
public class JsonCity {
    /** The tile that this city is based on, in its DTO form */
    @SerializedName("tile")
    private JsonTile tile;

    /** The name of city */
    @SerializedName("name")
    private String name;

    /** Whether the city has a harbour */
    @SerializedName("harbour")
    private boolean harbour;

    /**
     * Gets the model that is represented by this Data Transfer Object
     * @return The city that this object represents
     */

    private Faction[] getFactions() {
        Faction[] factions = new Faction[this.tile.getFactionsAllowedInCity().length];
        for (int i = 0; i < this.tile.getFactionsAllowedInCity().length; i++) {
            factions[i] = FactionFactory.getInstance().getFaction(this.tile.getFactionsAllowedInCity()[i]);
        }
        return factions;
    }

    public City toModel() {
        Faction[] factions = getFactions();

        return new City(name, harbour, tile.getTilePosition().toModel(), factions);
    }

    public String[] getNeighbours() {
        return tile.getNeighbouringCities();
    }

    public String getName() {
        return name;
    }
}
