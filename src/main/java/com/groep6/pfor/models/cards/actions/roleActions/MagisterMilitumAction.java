package com.groep6.pfor.models.cards.actions.roleActions;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Battle ability of the Magister Militum role.
 * @author Mitchell van Rijswijk
 *
 */
public class MagisterMilitumAction implements IAction {

	private final Game game = Game.getGameInstance();

	/**
	 * Reduces the amount of barbarians in the current city by 2.
	 * 
	 */
	public void executeCard() {
		Player localPlayer = game.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();
		removeTwoBarbariansFromPlayerCity(cityPlayerIsCurrentlyLocatedIn);
	}

	public void removeTwoBarbariansFromPlayerCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeBarbariansFromCurrentCity(2);
	};

	/**
	 * Gets the name of the role.
	 * @return The name of the role.
	 * 
	 */
	public String getName() {
		return "Magister Militum";
	}

	/**
	 * Gets the action description.
	 * @return The action description.
	 * 
	 */
	public String getDescription() {
		return "Verwijder 2 barbaren uit jouw stad.";
	}

}