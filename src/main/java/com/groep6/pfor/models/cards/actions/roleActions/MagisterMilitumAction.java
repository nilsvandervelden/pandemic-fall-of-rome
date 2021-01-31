package com.groep6.pfor.models.cards.actions.roleActions;

import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
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

	private final Game currentGame = Game.getGameInstance();

	/**
	 * Reduces the amount of barbarians in the current city by 2.
	 */
	public void executeEvent() throws CouldNotFindLocalPlayerException {
		Player localPlayer = currentGame.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();
		removeTwoBarbariansFromPlayerCity(cityPlayerIsCurrentlyLocatedIn);
	}

	public void removeTwoBarbariansFromPlayerCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeBarbariansFromCurrentCity(2);
	};

	public String getCardName() {
		return "Magister Militum";
	}

	public String getCardDescription() {
		return "Verwijder 2 barbaren uit jouw stad.";
	}

}