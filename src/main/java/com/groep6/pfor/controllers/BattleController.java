package com.groep6.pfor.controllers;

import com.groep6.pfor.models.*;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.SoundEffectManager;
import com.groep6.pfor.views.BattleView;

/**
 * Controller for the battle system. Gets information from the current city and performs a battle.
 * Afterwards the results of the battle are shown in the BattleView.
 * @author Mitchell van Rijswijk
 *
 */

public class BattleController extends Controller {

	private final Game game = Game.getInstance();

	/**
	 * Constructor for BattleController. First performs a battle, then places the result in a new BattleView.
	 * 
	 */

	public BattleController() {
		Player player = game.getPlayerFromCurrentTurn();

		int amountOfLegionsInCurrentCity = getAmountOfLegionsInCurrentCity(player);
		int amountOfBarbariansInCurrentCity = getAmountOfBarbariansInCurrentCity(player);

		DiceFace[] battleResults = fightBattle(player);

		int amountOfLegionsLost = calculateAmountOfLostLegions(battleResults);
		int amountOfBarbariansLost = calculateAmountOfLostBarbarians(battleResults);

		amountOfLegionsLost = determineHowManyLostLegionsToDisplay(amountOfLegionsInCurrentCity, amountOfLegionsLost);
		amountOfBarbariansLost = determineHowManyLostBarbariansToDisplay(amountOfBarbariansInCurrentCity, amountOfBarbariansLost);

		displayBattleResult(this, amountOfLegionsLost, amountOfBarbariansLost);
		playBattleSound();
	}

	private int determineHowManyLostLegionsToDisplay(int amountOfLegionsLost, int amountOfLegionsInCurrentCity) {
		return Math.min(amountOfLegionsInCurrentCity, amountOfLegionsLost);
	}

	private int determineHowManyLostBarbariansToDisplay(int amountOfBarbariansLost, int amountOfBarbariansInCurrentCity) {
		return Math.min(amountOfBarbariansInCurrentCity, amountOfBarbariansLost);
	}

	private int getAmountOfLegionsInCurrentCity(Player player) {
		return player.getCityPlayerIsCurrentlyLocatedIn().getLegionCount();
	}

	private int getAmountOfBarbariansInCurrentCity(Player player) {
		return player.getCityPlayerIsCurrentlyLocatedIn().getAmountOfBarbariansLocatedInCurrentCity();
	}

	private DiceFace[] fightBattle(Player player) {
		return player.battle();
	}

	private int calculateAmountOfLostLegions(DiceFace[] battleResults) {
		int amountOfLegionsLost = 0;
		for (DiceFace battleResult : battleResults) {
			amountOfLegionsLost += battleResult.getLegionCount();
		}
		return amountOfLegionsLost;
	}

	private int calculateAmountOfLostBarbarians(DiceFace[] battleResults) {
		int amountOfBarbariansLost = 0;
		for (DiceFace battleResult : battleResults) {
			amountOfBarbariansLost += battleResult.getBarbarianCount();
		}
		return amountOfBarbariansLost;
	}

	private void playBattleSound() {
		SoundEffectManager.playMusic("/sounds/effects/BattleSound.mp3");
	}

	private void displayBattleResult(BattleController battleController, int amountOfLegionsLost, int amountOfBarbariansLost ) {
		viewController.showView(new BattleView(battleController, amountOfLegionsLost, amountOfBarbariansLost));
	}

	@Override
	public void registerObserver(IObserver view) {

	}
}
