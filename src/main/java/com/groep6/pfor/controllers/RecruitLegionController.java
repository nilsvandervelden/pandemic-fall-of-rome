package com.groep6.pfor.controllers;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.RecruitLegionView;

/**
 * Controller for recruiting legions. This controller handles everything to do with recruiting legions
 * these are things like selecting the amount of legions to recruit.
 * @author Nils van der Velden
 *
 */
public class RecruitLegionController extends Controller {
	
	private final Game currentGame = Game.getGameInstance();
	private final Player playerFromCurrentTurn;
	private final City cityPlayerIsCurrentlyLocatedIn;

	private Player getPlayerFromCurrentTurn() {
		return currentGame.getPlayerFromCurrentTurn();
	}

	private City getCityPlayerIsCurrentlyLocatedIn() {
		return playerFromCurrentTurn.getCityPlayerIsCurrentlyLocatedIn();
	}

	private void showRecruitLegionsView() {
		viewController.showView(new RecruitLegionView(this));
	}
	
    public RecruitLegionController() {
    	playerFromCurrentTurn = getPlayerFromCurrentTurn();
    	cityPlayerIsCurrentlyLocatedIn = getCityPlayerIsCurrentlyLocatedIn();
    	showRecruitLegionsView();
    }

    private boolean cityHasFort() {
		return cityPlayerIsCurrentlyLocatedIn.hasFort();
	}

	private void addLegionsToCityPlayerIsCurrentlyLocatedIn(int amountOfLegions) {
		cityPlayerIsCurrentlyLocatedIn.addLegionsToCurrentCity(amountOfLegions);
	}

	private void decreaseAmountOfActionsRemaining() {
		playerFromCurrentTurn.decreaseAmountOfActionsRemaining();
	}

    public void recruit(int amountOfLegions) {
		if (!cityHasFort()) return;
		addLegionsToCityPlayerIsCurrentlyLocatedIn(amountOfLegions);
		decreaseAmountOfActionsRemaining();
		showPreviousView();
	}

	@Override
	public void registerObserver(IObserver view) {
		currentGame.registerObserver(view);
	}

}
