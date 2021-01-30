package com.groep6.pfor.models.cards.actions.eventActions;

import com.groep6.pfor.models.Deck;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.CityCard;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Implements (the first) action on Audentes Fortuna Iuvat event card
 * @author Mitchell van Rijswijk
 *
 */
public class AudentesFortunaAction implements IAction {

	/**
	 * Allows player to draw 2 extra cards during drawPlayerCards phase.
	 */
	public void executeEvent() {
		Game currentGame = Game.getGameInstance();
		Player playerFromCurrentTurn = currentGame.getPlayerFromCurrentTurn();

		addTwoCardsToPlayerHandDeck(playerFromCurrentTurn, currentGame);

		decreaseAmountOfActionsRemaining(currentGame);
	}

	private CityCard drawCardFromCityDeck(Deck cityDeck) {
		return (CityCard) cityDeck.drawCardFromDeck();
	}

	public void addTwoCardsToPlayerHandDeck(Player playerFromCurrentTurn, Game currentGame) {
		Deck cityDeck = getPlayerCardDeck(currentGame);
		playerFromCurrentTurn.getPlayerDeck().addCardsToPlayerHand(drawCardFromCityDeck(cityDeck) , drawCardFromCityDeck(cityDeck));
	};

	private void decreaseAmountOfActionsRemaining(Game currentGame) {
		currentGame.getPlayerFromCurrentTurn().decreaseAmountOfActionsRemaining();
	}
	
	private Deck getPlayerCardDeck(Game currentGame) {
		return currentGame.getPlayerCardDeck();
	}

	public String getEventName() {
		return "Audentes Fortuna Iuvat";
	}

	public String getEventDescription() {
		return "De speler trekt 2 speelkaarten.";
	}
}
