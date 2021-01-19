package com.groep6.pfor.models.factions;

import com.groep6.pfor.Config;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.util.Vector2f;
import javafx.scene.paint.Color;

public class Faction {

    private final FactionType factionType;
    private final Color factionColor;
    private final int requiredAmountOfCardsToFormAAlliance;
    private final Vector2f factionPosition;

    public Faction(FactionType factionType, Color factionColor, int requiredAmountOfCardsToFormAAlliance, Vector2f factionPosition) {
        this.factionType = factionType;
        this.factionColor = factionColor;
        this.requiredAmountOfCardsToFormAAlliance = requiredAmountOfCardsToFormAAlliance;
        this.factionPosition = factionPosition;
    }

    public FactionType getFactionType() {
        return factionType;
    }

    public Color getFactionColor() {
        return factionColor;
    }

    public int getRequiredAmountOfCardsToFormAAlliance() {
        return requiredAmountOfCardsToFormAAlliance;
    }

    private Game getCurrentGame() {
        return Game.getGameInstance();
    }

    public void allyEnemyFaction() {
        Game currentGame = getCurrentGame();
        currentGame.addFriendlyFaction(this);
    }

    /**
     * Get position of the faction on the map (lower left)
     * @return A Vector2f object
     */
    public Vector2f getFactionPosition() {
        return factionPosition;
    }

    @Override
    public String toString() {
        return factionType.name();
    }

    private boolean inDebugMode() {
        return Config.DEBUG;
    }

    private void printWarningMessage() {
        System.out.println("[WARNING] Faction compare failed, because types were incompatible");
    }

    @Override
    public boolean equals(Object factionType) {
        if (!(factionType instanceof Faction)) {
            if (inDebugMode()) printWarningMessage();
            return false;
        }
        return ((Faction) factionType).factionType == this.factionType;
    }
}
