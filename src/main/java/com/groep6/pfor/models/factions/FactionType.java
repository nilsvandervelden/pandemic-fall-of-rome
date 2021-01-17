package com.groep6.pfor.models.factions;

/**
 * @author Bastiaan Jansen
 */
public enum FactionType {
    ANGLO_SAXSONS_FRANKS("Angelsaksen"),
    VANDALS("Vandalen"),
    HUNS("Hunnen"),
    VISIGOTHS("Visigoten"),
    OSTROGOTHS("Ostrogoten");

    private final String factionName;

    /**
     * @param factionName
     */
    FactionType(String factionName) {
        this.factionName = factionName;
    }

    /**
     * @return Dutch name of faction
     */
    public String getFactionName() {
        return factionName;
    }
}
