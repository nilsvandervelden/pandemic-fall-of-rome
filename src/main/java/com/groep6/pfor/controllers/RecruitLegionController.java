package com.groep6.pfor.controllers;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.RecruitLegionView;

/**
 * @author Nils van der Velden
 */

public class RecruitLegionController extends Controller {
	
	private final Game game = Game.getInstance();
	private final Player player;
	private final City city;
	
    public RecruitLegionController() {
    	player = game.getPlayerFromCurrentTurn();
    	city = player.getCityPlayerIsCurrentlyLocatedIn();
    	viewController.showView(new RecruitLegionView(this));
    }

    public void recruit(int amountOfLegions) {
		if (city.hasFort()) city.addLegionsToCurrentCity(amountOfLegions);
		player.decreaseAmountOfActionsRemaining();
		showPreviousView();
	}

	@Override
	public void registerObserver(IObserver view) {
		game.registerObserver(view);
	}

}
