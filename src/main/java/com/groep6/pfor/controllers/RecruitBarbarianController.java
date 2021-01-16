package com.groep6.pfor.controllers;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.RecruitBarbarianView;

public class RecruitBarbarianController extends Controller {

    private final Game game = Game.getInstance();
    private final Player localPlayer;
    private final City cityLocalPlayerIsCurrentlyStandingIn;
	
    public RecruitBarbarianController() {
        localPlayer = getPlayerFromCurrentTurn();
        cityLocalPlayerIsCurrentlyStandingIn = getCityPlayerIsCurrentlyLocatedIn();
        showRecruitBarbariansView();
    }

    private Player getPlayerFromCurrentTurn() {
        return game.getPlayerFromCurrentTurn();
    }

    private City getCityPlayerIsCurrentlyLocatedIn() {
        return localPlayer.getCityPlayerIsCurrentlyLocatedIn();
    }

    private void showRecruitBarbariansView() {
        viewController.showView(new RecruitBarbarianView(this));
    }

    private Faction[] getFactionsThatAreAllowedInCity() {
        return cityLocalPlayerIsCurrentlyStandingIn.getFactions();
    }

    private boolean isNotFriendlyFaction(Faction faction) {
        return !game.isFriendlyFaction(faction);
    }

    private boolean noBarbarianInCurrentCity() {
        return cityLocalPlayerIsCurrentlyStandingIn.getAmountOfBarbariansLocatedInCurrentCity() <= 0;
    }

    private void removeAlliedBarbariansFromCurrentCity(Faction faction, int amount) {
        cityLocalPlayerIsCurrentlyStandingIn.removeBarbariansFromCurrentCity(faction.getFactionType(), amount);
    }

    private void addLegionsToCurrentCity(int amount) {
        cityLocalPlayerIsCurrentlyStandingIn.addLegionsToCurrentCity(amount);
    }

    private void decreaseAmountOfActionsRemaining() {
        localPlayer.decreaseAmountOfActionsRemaining();
    }
    
    public void recruitBarbariansFromCurrentCity(int amount) {
        Faction[] factionsThatAreAllowedInCity = getFactionsThatAreAllowedInCity();

        for (Faction faction: factionsThatAreAllowedInCity) {
            if (isNotFriendlyFaction(faction)) return;
            if (noBarbarianInCurrentCity()) return;

            removeAlliedBarbariansFromCurrentCity(faction, amount);
            addLegionsToCurrentCity(amount);

            decreaseAmountOfActionsRemaining();
            showPreviousView();
            return;
        }
    }

    private int getAmountOfBarbariansLocatedInCurrentCity(Faction faction) {
        return cityLocalPlayerIsCurrentlyStandingIn.getAmountOfBarbariansLocatedInCurrentCity(faction.getFactionType());
    }

    public int getAmountOfBarbariansInCity() {
        for (Faction faction: cityLocalPlayerIsCurrentlyStandingIn.getFactions()) {
            if (isNotFriendlyFaction(faction)) return 0;

            return getAmountOfBarbariansLocatedInCurrentCity(faction);
        }

        return 0;
    }

	@Override
	public void registerObserver(IObserver view) {
		
	}
}
