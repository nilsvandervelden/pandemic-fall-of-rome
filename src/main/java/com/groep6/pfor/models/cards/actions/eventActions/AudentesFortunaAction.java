package com.groep6.pfor.models.cards.actions.eventActions;

import com.groep6.pfor.exceptions.CardNotInDeckException;
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

	private CityCard drawCardFromCityDeck(Deck cityDeck) throws CardNotInDeckException {
		return (CityCard) cityDeck.drawCardFromDeck();
	}

	public void addTwoCardsToPlayerHandDeck(Player playerFromCurrentTurn, Game currentGame) {
		Deck cityDeck = getPlayerCardDeck(currentGame);
		try {
			playerFromCurrentTurn.getPlayerDeck().addCardsToPlayerHand(drawCardFromCityDeck(cityDeck) , drawCardFromCityDeck(cityDeck));
		} catch (CardNotInDeckException e) {
			e.printStackTrace();
		}
	}

	private void decreaseAmountOfActionsRemaining(Game currentGame) {
		currentGame.getPlayerFromCurrentTurn().decreaseAmountOfActionsRemaining();
	}
	
	private Deck getPlayerCardDeck(Game currentGame) {
		return currentGame.getPlayerCardDeck();
	}

	public String getCardName() {
		return "Audentes Fortuna Iuvat";
	}

	public String getCardDescription() {
		return "De speler trekt 2 speelkaarten.";
	}
}
