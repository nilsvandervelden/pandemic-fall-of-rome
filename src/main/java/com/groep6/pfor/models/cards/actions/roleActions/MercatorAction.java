package com.groep6.pfor.models.cards.actions.roleActions;

import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Battle ability of the Mercator role.
 * @author Mitchell van Rijswijk
 *
 */
public class MercatorAction implements IAction {

	private final Game currentGame = Game.getGameInstance();

	/**
	 * Deletes 1 barbarian from the current city.
	 * Deletes 1 legion from the current city.
	 */
	public void executeEvent() throws CouldNotFindLocalPlayerException {
		Player localPlayer = currentGame.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();
		removeOneLegionAndBarbarianFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	private void removeOneBarbarianFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeBarbariansFromCurrentCity(1);
	}

	private void removeOneLegionFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeLegions(1);
	}

	public void removeOneLegionAndBarbarianFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		removeOneBarbarianFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
		removeOneLegionFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	public String getCardName() {
		return "Mercator";
	}

	public String getCardDescription() {
		return "Verwijder 1 barbaar en 1 legioen uit jouw stad.";
	}

}
