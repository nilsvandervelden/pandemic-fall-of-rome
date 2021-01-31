package com.groep6.pfor.models.cards.actions.roleActions;

import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Battle ability of the Praefectus Classis role.
 * @author Mitchell van Rijswijk
 *
 */
public class PraefectusClassisAction implements IAction {

	private final Game currentGame = Game.getGameInstance();

	/**
	 * Checks if the current city is a port city. If it is, removes a
	 * barbarian from the city.
	 */
	public void executeEvent() throws CouldNotFindLocalPlayerException {
		Player localPlayer = currentGame.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();

		if (cityHasHarbour(cityPlayerIsCurrentlyLocatedIn)) removeOneBarbarianFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	private boolean cityHasHarbour(City cityPlayerIsCurrentlyLocatedIn) {
		return cityPlayerIsCurrentlyLocatedIn.hasHarbour();
	}

	private void removeOneBarbarianFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeBarbariansFromCurrentCity(1);
	}

	public String getCardName() {
		return "Praefectus Classis";
	}

	public String getCardDescription() {
		return "Verwijder 1 barbaar uit jouw stad als hier een haven is.";
	}
}