package com.groep6.pfor.controllers;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.SoundEffectManager;
import com.groep6.pfor.views.MoveView;

/**
 * @author Nils van der Velden
 */

public class MovePlayerToCityController extends Controller {

	private final City selectedCityToMoveTo;
	private final Player player;
	
    public MovePlayerToCityController(City selectedCityToMoveTo, Player player) {
    	this.selectedCityToMoveTo = selectedCityToMoveTo;
    	this.player = player;

        viewController.showView(new MoveView(this));
    }

	public void moveLegionsToSelectedCity(int amount) {
    	player.getCurrentCity().removeLegions(amount);
    	selectedCityToMoveTo.addLegions(amount);

		SoundEffectManager.playMusic("/sounds/effects/MarchSound.mp3");
		player.movePlayerToSelectedCity(selectedCityToMoveTo);
    	showPreviousView();
    }

    public int getAmountOfLegionsInCurrentCity() {
    	return player.getCurrentCity().getLegions().size();
	}

	@Override
	public void registerObserver(IObserver view) {}
}
