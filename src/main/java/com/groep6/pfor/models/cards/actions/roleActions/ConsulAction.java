package com.groep6.pfor.models.cards.actions.roleActions;

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

	private final Game game = Game.getGameInstance();

	/**
	 * Increases the amount of legions in the current city by 1.
	 * 
	 */
	public void executeCard() {
		Player localPlayer = game.getLocalPlayer();
		City cityPlayerIsCurrentlyLocatedIn = localPlayer.getCityPlayerIsCurrentlyLocatedIn();
		addLegionToCurrentCity(cityPlayerIsCurrentlyLocatedIn);
	}

	public void addLegionToCurrentCity(City cityPlayerIsCurrentlyLocatedIn) {
		cityPlayerIsCurrentlyLocatedIn.addLegionsToCurrentCity(1);
	};

	/**
	 * Gets the name of the role.
	 * @return The name of the role.
	 * 
	 */
	public String getCardName() {
		return "Consul";
	}

	/**
	 * Gets the action description.
	 * @return The action description.
	 * 
	 */
	public String getCardDescription() {
		return "Plaats 1 legioen op jouw stad.";
	}

}