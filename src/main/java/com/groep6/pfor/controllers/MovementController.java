package com.groep6.pfor.controllers;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.SoundEffectManager;
import com.groep6.pfor.views.MoveView;

/**
 * @author Nils van der Velden
 */

public class MovementController extends Controller {

	private final City selectedCityToMoveTo;
	private final Player player;

	private void showMoveView() {
		viewController.showView(new MoveView(this));
	}
	
    public MovementController(City selectedCityToMoveTo, Player player) {
    	this.selectedCityToMoveTo = selectedCityToMoveTo;
    	this.player = player;

        showMoveView();
    }

    private void removeLegionsFromCityPlayerIsCurrentlyStandingIn(int amountOfLegionsToRemove) {
		player.getCityPlayerIsCurrentlyLocatedIn().removeLegions(amountOfLegionsToRemove);
	}

	private void addLegionsToSelectedCityToMoveTo(int amountOfLegionsToAdd) {
		selectedCityToMoveTo.addLegionsToCurrentCity(amountOfLegionsToAdd);
	}

	private void playMarchingSound() {
		SoundEffectManager.playMusic("/sounds/effects/MarchSound.mp3");
	}

	private void moveLocalPlayerToSelectedCity() {
		player.moveLocalPlayerToSelectedCity(selectedCityToMoveTo);
	}

	public void moveLegionsToSelectedCity(int amountOfLegions) {
		removeLegionsFromCityPlayerIsCurrentlyStandingIn(amountOfLegions);
		addLegionsToSelectedCityToMoveTo(amountOfLegions);
	}

	public void moveLegionsAndPlayerToSelectedCity(int amountOfLegions) {
    	moveLegionsToSelectedCity(amountOfLegions);
		moveLocalPlayerToSelectedCity();

		playMarchingSound();

    	showPreviousView();
    }

    public int getAmountOfLegionsInCurrentCity() {
    	return player.getCityPlayerIsCurrentlyLocatedIn().getLegionsInCity().size();
	}

	@Override
	public void registerObserver(IObserver view) {}
}
