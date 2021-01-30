package com.groep6.pfor.models.cards;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.factions.FactionType;

/**
 * Represents a revolt card
 * @author Bastiaan Jansen
 */
public class RevoltCard extends Card {
    private final String revoltCardName;
    private final FactionType revoltingFactionType;

    public RevoltCard(String revoltCardName, FactionType revoltingFactionType) {
        this.revoltCardName = revoltCardName;
        this.revoltingFactionType = revoltingFactionType;
    }

    @Override
    public String getCardName() {
        return revoltCardName;
    }

    public FactionType getRevoltingFactionType() {
        return revoltingFactionType;
    }
}
