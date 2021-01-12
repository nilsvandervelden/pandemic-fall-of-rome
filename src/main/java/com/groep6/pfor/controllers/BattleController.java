package com.groep6.pfor.controllers;

import com.groep6.pfor.models.*;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.SoundEffectManager;
import com.groep6.pfor.views.BattleView;

import java.util.Stack;

/**
 * Controller for the battle system. Gets information from the current city and performs a battle.
 * Afterwards the results of the battle are shown in the BattleView.
 * @author Mitchell van Rijswijk
 *
 */

public class BattleController extends Controller {

	private final Game game = Game.getInstance();

	// ArrayList simulation of barbarians and legions in a city.
	// TODO implement data acquisition from the current players current city.
	Stack<Barbarian> barbarians = new Stack<>();
	Stack<Legion> legions = new Stack<>();

	/**
	 * Constructor for BattleController. First performs a battle, then places the result in a new BattleView.
	 * 
	 */

	public BattleController() {
		Player player = game.getPlayerFromCurrentTurn();

		int amountOfLegionsInCurrentCity = player.getCity().getLegionCount();
		int amountOfBarbariansInCurrentCity = player.getCity().getBarbarianCount();

		DiceFace[] battleResults = player.battle();

		int amountOfLegionsLost = 0;
		int amountOfBarbariansLost = 0;

		for (DiceFace battleResult : battleResults) {
			amountOfLegionsLost += battleResult.getLegionCount();
			amountOfBarbariansLost += battleResult.getBarbarianCount();
		}

		amountOfLegionsLost = Math.min(amountOfLegionsInCurrentCity, amountOfLegionsLost);
		amountOfBarbariansLost = Math.min(amountOfBarbariansInCurrentCity, amountOfBarbariansLost);

		viewController.showView(new BattleView(this, amountOfLegionsLost, amountOfBarbariansLost));
		SoundEffectManager.play("/sounds/effects/BattleSound.mp3");
	}

	@Override
	public void registerObserver(IObserver view) {

	}
}
