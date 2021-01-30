package com.groep6.pfor.models.cards.actions.roleActions;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Battle ability of the Vestalin role.
 * @author Mitchell van Rijswijk
 *
 */
public class VestalinAction implements IAction {

	private final Game game = Game.getGameInstance();

	/**
	 * Decreases the amount of legions in the current city by 1.
	 * 
	 */
	public void executeEvent() {
		Player localPlayer = game.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();
		removeOneLegionFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	public void removeOneLegionFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeLegions(1);
	};

	/**
	 * Gets the name of the role.
	 * @return The name of the role.
	 * 
	 */
	public String getEventName() {
		return "Vestalin";
	}

	/**
	 * Gets the action description.
	 * @return The action description.
	 * 
	 */
	public String getEventDescription() {
		return "Verwijder 1 legioen uit jouw stad.";
	}

}