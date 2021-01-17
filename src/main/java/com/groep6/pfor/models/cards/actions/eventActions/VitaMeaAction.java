package com.groep6.pfor.models.cards.actions.eventActions;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Implements (the first) action on Mors Tua, Vita Mea event card
 * @author Mitchell van Rijswijk
 *
 */
public class VitaMeaAction implements IAction {

	/**
	 * Replace 1 barbarian with 1 legion in the current city.
	 */
	public void executeCard() {
		Game currentGame = Game.getInstance();
		Player localPlayer = currentGame.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();
		executeSpecialAction(cityPlayerIsCurrentlyLocatedIn);
		decreaseAmountOfActionsRemaining(currentGame);
	}

	private void removeOneBarbarianFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeBarbariansFromCurrentCity(1);
	}

	private void addLegionToCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.addLegionsToCurrentCity(1);
	}

	public void executeSpecialAction(City cityPlayerIsCurrentlyLocatedIn) {
		removeOneBarbarianFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
		addLegionToCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	private void decreaseAmountOfActionsRemaining(Game currentGame) {
		currentGame.getPlayerFromCurrentTurn().decreaseAmountOfActionsRemaining();
	}

	/**
	 * Gets the name of the event.
	 * @return The name of the event.
	 * 
	 */
	public String getName() {
		return "Mors Tua, Vita Mea";
	}

	/**
	 * Gets the action description.
	 * @return The action description.
	 * 
	 */
	public String getDescription() {
		return "Vervang 1 barbaar door 1 legioen.";
	}

}
