package com.groep6.pfor.factories;

import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.models.factions.FactionType;
import com.groep6.pfor.util.Vector2f;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class FactionFactory {

    private static final FactionFactory SINGLE_INSTANCE = createFactionFactory();

    List<Faction> factions = new ArrayList<>();

    private static FactionFactory createFactionFactory() {
        return new FactionFactory();
    }

    private void addFactionsToFactionList() {
        float positionOffset = 0.96662f;
        factions.add(new Faction(FactionType.ANGLO_SAXSONS_FRANKS, Color.ORANGE, 4, new Vector2f(0.20166f, positionOffset)));
        factions.add(new Faction(FactionType.VANDALS, Color.BLACK, 5, new Vector2f(0.0545f, positionOffset)));
        factions.add(new Faction(FactionType.HUNS, Color.GREEN, 4, new Vector2f(0.1483f, positionOffset)));
        factions.add(new Faction(FactionType.VISIGOTHS, Color.WHITE, 5, new Vector2f(0.10083f, positionOffset)));
        factions.add(new Faction(FactionType.OSTROGOTHS, Color.BLUE, 3, new Vector2f(0.25666f, positionOffset)));
    }

    private FactionFactory() {
        addFactionsToFactionList();
    }

    public static FactionFactory getInstance() {
        return SINGLE_INSTANCE;
    }

    public List<Faction> getFactions() {
        return factions;
    }

    public boolean factionTypeEqualsRequestedFactionType(Faction faction, FactionType factionType) {
        return faction.getFactionType() == factionType;
    }

    public Faction getFaction(FactionType factionType) {
        for (Faction faction: factions) {
            if (factionTypeEqualsRequestedFactionType(faction, factionType)) return faction;
        }
        return null;
    }

    public FactionType getFactionTypeByFactionName(String factionName) {
        if (factionName.equals(FactionType.ANGLO_SAXSONS_FRANKS.getFactionName())) {
            return FactionType.ANGLO_SAXSONS_FRANKS;
        } else if (factionName.equals(FactionType.VANDALS.getFactionName())) {
            return FactionType.VANDALS;
        } else if (factionName.equals(FactionType.HUNS.getFactionName())) {
            return FactionType.HUNS;
        } else if (factionName.equals(FactionType.VISIGOTHS.getFactionName())) {
            return FactionType.VISIGOTHS;
        } else if (factionName.equals(FactionType.OSTROGOTHS.getFactionName())) {
            return FactionType.OSTROGOTHS;
        }
        return null;
    }
}
