package com.groep6.pfor.models.factions;

/**
 * @author Bastiaan Jansen
 */
public enum FactionType {
    ANGLO_SAXSONS_FRANKS("ANGLO_SAXSONS_FRANKS"),
    VANDALS("VANDALS"),
    HUNS("HUNS"),
    VISIGOTHS("VISIGOTHS"),
    OSTROGOTHS("OSTROGOTHS");

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


