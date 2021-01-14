package com.groep6.pfor.controllers;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.RecruitBarbarianView;

public class RecruitBarbarianController extends Controller {

    private final Game game = Game.getInstance();
    private final Player player;
    private final City city;
	
    public RecruitBarbarianController() {
        player = game.getPlayerFromCurrentTurn();
        city = player.getCityPlayerIsCurrentlyLocatedIn();
        viewController.showView(new RecruitBarbarianView(this));
    }
    
    public int getAmountOfBarbariansLocatedInCurrentCity() {
    	return city.getBarbarians().size();
    }
    
    public void recruitBarbariansFromCurrentCity(int amount) {
        Faction[] factions = city.getFactions();

        for (Faction faction: factions) {
            if (game.isFriendlyFaction(faction)) {
                if (city.getAmountOfBarbariansLocatedInCurrentCity() <= 0) return;
                city.removeBarbariansFromCurrentCity(faction.getFactionType(), amount);
                city.addLegionsToCurrentCity(amount);
                player.decreaseAmountOfActionsRemaining();
                showPreviousView();
                return;
            }
        }
    }

    public int getAmountOfBarbariansInFaction() {
        Player player = game.getLocalPlayer();
        City city = player.getCityPlayerIsCurrentlyLocatedIn();
        for (Faction faction: city.getFactions()) {
            boolean isFriendlyFaction = game.isFriendlyFaction(faction);
            if (isFriendlyFaction) return city.getAmountOfBarbariansLocatedInCurrentCity(faction.getFactionType());
        }

        return 0;
    }

	@Override
	public void registerObserver(IObserver view) {
		
	}

}
