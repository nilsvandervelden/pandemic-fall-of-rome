package com.groep6.pfor.models.cards;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.factions.Faction;


/**
 * Represents a city card
 * @author Bastiaan Jansen
 */
public class CityCard extends Card {

    private final City correspondingCity;
    private final Faction factionCityCardBelongsTo;

    /**
     * @param correspondingCity
     * @param factionCityCardBelongsTo
     */
    public CityCard(City correspondingCity, Faction factionCityCardBelongsTo) {
        this.correspondingCity = correspondingCity;
        this.factionCityCardBelongsTo = factionCityCardBelongsTo;
    }

    @Override
    public String getCardName() {
        return correspondingCity.getName();
    }

    /**
     * @return City
     */
    public City getCorrespondingCity() {
        return correspondingCity;
    }

    /**
     * @return Faction
     */
    public Faction getFactionCityCardBelongsTo() {
        return factionCityCardBelongsTo;
    }
}
