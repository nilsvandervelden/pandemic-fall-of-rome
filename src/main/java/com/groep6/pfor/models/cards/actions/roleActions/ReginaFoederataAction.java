package com.groep6.pfor.models.cards.actions.roleActions;

import com.groep6.pfor.models.City;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Battle ability of the Regina Foederata role.
 * @author Mitchell van Rijswijk
 *
 */
public class ReginaFoederataAction implements IAction {
	
	private final Game game = Game.getGameInstance();

	/**
	 * Gets the battle result. If the current city still has barbarians after the battle,
	 * 1 extra barbarian gets deleted. The legion count in the city is incremented. 
	 */
	public void executeCard() {
		Player localPlayer = game.getLocalPlayer();
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

	/**
	 * Gets the name of the role.
	 * @return The name of the role.
	 * 
	 */
	public String getName() {
		return "Regina Foederata";
	}

	/**
	 * Gets the action description.
	 * @return The action description.
	 * 
	 */
	public String getDescription() {
		return "Verwijder na het gevecht 1 barbaar uit jouw stad. Plaats hierbij ook maximaal 1 legioen op jouw stad.";
	}

}