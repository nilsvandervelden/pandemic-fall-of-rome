package com.groep6.pfor.models.cards.actions.roleActions;

import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Battle ability of the Consul role.
 * @author Mitchell van Rijswijk
 *
 */
public class ConsulAction implements IAction {

	private final Game currentGame = Game.getGameInstance();

	/**
	 * Increases the amount of legions in the current city by 1.
	 */
	public void executeEvent() throws CouldNotFindLocalPlayerException {
		Player localPlayer = currentGame.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();
		addLegionToCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	public void addLegionToCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.addLegionsToCurrentCity(1);
	};

	public String getCardName() {
		return "Consul";
	}

	public String getCardDescription() {
		return "Plaats 1 legioen op jouw stad.";
	}

}