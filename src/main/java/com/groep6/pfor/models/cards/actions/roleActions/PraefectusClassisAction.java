package com.groep6.pfor.models.cards.actions.roleActions;

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

	private final Game game = Game.getGameInstance();

	/**
	 * Checks if the current city is a port city. If it is, removes a
	 * barbarian from the city.
	 */
	public void executeCard() {
		Player localPlayer = game.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();

		if (cityHasHarbour(cityPlayerIsCurrentlyLocatedIn)) removeOneBarbarianFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	private boolean cityHasHarbour(City cityPlayerIsCurrentlyLocatedIn) {
		return cityPlayerIsCurrentlyLocatedIn.hasHarbour();
	}

	private void removeOneBarbarianFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeBarbariansFromCurrentCity(1);
	}

	/**
	 * Gets the name of the role.
	 * @return The name of the role.
	 * 
	 */
	public String getName() {
		return "Praefectus Classis";
	}

	/**
	 * Gets the action description.
	 * @return The action description.
	 * 
	 */
	public String getDescription() {
		return "Verwijder 1 barbaar uit jouw stad als hier een haven is.";
	}

}