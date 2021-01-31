package com.groep6.pfor.models.cards.actions.eventActions;

import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
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
	public void executeEvent() throws CouldNotFindLocalPlayerException {
		Game currentGame = Game.getGameInstance();
		Player localPlayer = currentGame.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();
		exchangeOneBarbarianForOneLegion(cityPlayerIsCurrentlyLocatedIn);
		decreaseAmountOfActionsRemaining(currentGame);
	}

	private void removeOneBarbarianFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeBarbariansFromCurrentCity(1);
	}

	private void addLegionToCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.addLegionsToCurrentCity(1);
	}

	public void exchangeOneBarbarianForOneLegion(City cityPlayerIsCurrentlyLocatedIn) {
		removeOneBarbarianFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
		addLegionToCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	private void decreaseAmountOfActionsRemaining(Game currentGame) {
		currentGame.getPlayerFromCurrentTurn().decreaseAmountOfActionsRemaining();
	}

	public String getCardName() {
		return "Mors Tua, Vita Mea";
	}

	public String getCardDescription() {
		return "Vervang 1 barbaar door 1 legioen.";
	}

}
