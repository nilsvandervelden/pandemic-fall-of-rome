package com.groep6.pfor.util.parsers.templates;

import com.google.gson.annotations.SerializedName;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.models.factions.FactionType;
import com.groep6.pfor.util.Vector2f;
import javafx.scene.paint.Color;

/**
 * Json deserializer for factions
 *
 * @author Owen Elderbroek
 */
public class FactionDTO {
    /** The name of the faction */
    @SerializedName("type")
    private FactionType factionType;

    /** The color this faction has whilst displaying */
    @SerializedName("color")
    private Color factionColor;

    /** The color this faction has whilst displaying */
    @SerializedName("cardCountForAlliance")
    private int amountOfCardsRequiredForAlliance;

    /**
     * Get the color of this faction
     * @return The color of this faction
     */
    public Color getFactionColor() {
        return factionColor;
    }

    /**
     * Get which faction-type belongs to this faction
     * @return The type of the faction
     */
    public FactionType getFactionType() {
        return factionType;
    }

    public Faction toModel() {
        return new Faction(factionType, factionColor, amountOfCardsRequiredForAlliance, new Vector2f());
    }
}
