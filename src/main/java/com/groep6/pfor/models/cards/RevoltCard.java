package com.groep6.pfor.models.cards;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.factions.FactionType;

/**
 * Represents a revolt card
 * @author Bastiaan Jansen
 */
public class RevoltCard extends Card {
    private final String revoltCardName;
    private final City revoltingCity;
    private final FactionType revoltingFactionType;

    /**
     * @param revoltCardName
     * @param revoltingCity
     * @param revoltingFactionType
     */
    public RevoltCard(String revoltCardName, City revoltingCity, FactionType revoltingFactionType) {
        this.revoltCardName = revoltCardName;
        this.revoltingCity = revoltingCity;
        this.revoltingFactionType = revoltingFactionType;
    }

    @Override
    public String getCardName() {
        return revoltCardName;
    }

    /**
     * @return City
     */
    public City getRevoltingCity() {
        return revoltingCity;
    }

    /**
     * @return Faction
     */
    public FactionType getRevoltingFactionType() {
        return revoltingFactionType;
    }
}
