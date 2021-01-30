package com.groep6.pfor.controllers;

import com.groep6.pfor.models.*;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.SoundEffectManager;
import com.groep6.pfor.views.BattleView;

/**
 * Controller for the battle system. Gets information from the current city and performs a battle.
 * Afterwards the results of the battle are shown in the BattleView.
 * @author Nils van der Velden
 *
 */

public class BattleController extends Controller {

	private final Game game = Game.getGameInstance();

	/**
	 * Constructor for BattleController. First performs a battle, then places the result in a new BattleView.
	 * 
	 */

	public BattleController() {
		Player playerOfCurrentTurn = game.getPlayerFromCurrentTurn();

		int amountOfLegionsToBattleWith = getAmountOfLegionsInCityPlayerIsCurrentlyLocatedIn(playerOfCurrentTurn);
		int amountOfBarbariansToBattleWith = getAmountOfBarbariansInCityPlayerIsCurrentlyLocatedIn(playerOfCurrentTurn);

		DiceFace[] battleResults = determineBattleOutcome(playerOfCurrentTurn);

		int amountOfLegionsLostInBattle = calculateAmountOfLegionsLostInBattle(battleResults);
		int amountOfBarbariansLostInBattle = calculateAmountOfBarbariansLostInBattle(battleResults);

		amountOfLegionsLostInBattle = determineHowManyLostLegionsToDisplay(amountOfLegionsToBattleWith, amountOfLegionsLostInBattle);
		amountOfBarbariansLostInBattle = determineHowManyLostBarbariansToDisplay(amountOfBarbariansToBattleWith, amountOfBarbariansLostInBattle);

		displayBattleResult(this, amountOfLegionsLostInBattle, amountOfBarbariansLostInBattle);
		playBattleSound();
	}

	private int determineHowManyLostLegionsToDisplay(int amountOfLegionsLost, int amountOfLegionsInCurrentCity) {
		return Math.min(amountOfLegionsInCurrentCity, amountOfLegionsLost);
	}

	private int determineHowManyLostBarbariansToDisplay(int amountOfBarbariansLost, int amountOfBarbariansInCurrentCity) {
		return Math.min(amountOfBarbariansInCurrentCity, amountOfBarbariansLost);
	}

	private int getAmountOfLegionsInCityPlayerIsCurrentlyLocatedIn(Player player) {
		return player.getCityPlayerIsCurrentlyLocatedIn().getLegionCount();
	}

	private int getAmountOfBarbariansInCityPlayerIsCurrentlyLocatedIn(Player player) {
		return player.getCityPlayerIsCurrentlyLocatedIn().getAmountOfBarbariansLocatedInCurrentCity();
	}

	private DiceFace[] determineBattleOutcome(Player player) {
		return player.fightBattle();
	}

	private int calculateAmountOfLegionsLostInBattle(DiceFace[] battleResults) {
		int amountOfLegionsLost = 0;
		for (DiceFace battleResult : battleResults) {
			amountOfLegionsLost += battleResult.getLegionCount();
		}
		return amountOfLegionsLost;
	}

	private int calculateAmountOfBarbariansLostInBattle(DiceFace[] battleResults) {
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
