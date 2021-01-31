package com.groep6.pfor.models.cards.actions.roleActions;

import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Battle ability of the Praefectus Fabrum role.
 * @author Mitchell van Rijswijk
 *
 */
public class PraefectusFabrumAction implements IAction {

	private final Game currentGame = Game.getGameInstance();

	/**
	 * Checks if the current city has a fort. If it has, removes 2
	 * barbarians from the city.
	 */
	public void executeEvent() throws CouldNotFindLocalPlayerException {
		Player localPlayer = currentGame.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();

		if (cityHasFort(cityPlayerIsCurrentlyLocatedIn)) removeTwoBarbarianFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	private boolean cityHasFort(City cityPlayerIsCurrentlyLocatedIn) {
		return cityPlayerIsCurrentlyLocatedIn.hasFort();
	}

	private void removeTwoBarbarianFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeBarbariansFromCurrentCity(2);
	}

	public String getCardName() {
		return "Praefectus Fabrum";
	}

	public String getCardDescription() {
		return "Verwijder 2 barbaren uit jouw stad als hier een fort is.";
	}
}