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

	private final Game currentGame = Game.getGameInstance();

	/**
	 * Decreases the amount of legions in the current city by 1.
	 */
	public void executeEvent() {
		Player localPlayer = currentGame.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();
		removeOneLegionFromCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	public void removeOneLegionFromCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.removeLegions(1);
	};

	public String getCardName() {
		return "Vestalin";
	}

	public String getCardDescription() {
		return "Verwijder 1 legioen uit jouw stad.";
	}
}