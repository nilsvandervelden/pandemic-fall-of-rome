package com.groep6.pfor.models.cards;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.factions.Faction;

import java.util.List;

/**
 * Represents an invasion card
 * @author Bastiaan Jansen
 */
public class InvasionCard extends Card {

    private final String invasionCardName;
    private final List<City> invasionRoute;
    private final Faction invadingFaction;

    public InvasionCard(String invasionCardName, Faction invadingFaction, List<City> invasionRoute) {
        this.invasionCardName = invasionCardName;
        this.invasionRoute = invasionRoute;
        this.invadingFaction = invadingFaction;
    }
    @Override
    public String getCardName() {
        return invasionCardName;
    }

    public Faction getInvadingFaction() {
        return invadingFaction;
    }

    public List<City> getInvasionRoute() {
        return invasionRoute;
    }
}
