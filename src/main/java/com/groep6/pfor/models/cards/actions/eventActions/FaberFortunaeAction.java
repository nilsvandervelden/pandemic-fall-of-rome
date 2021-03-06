package com.groep6.pfor.models.cards.actions.eventActions;

import com.groep6.pfor.exceptions.CardNotInDeckException;
import com.groep6.pfor.models.Deck;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.CityCard;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Implements (the first) action on Homo Faber Fortunae Suae event card
 * @author Mitchell van Rijswijk
 *
 */
public class FaberFortunaeAction implements IAction {


	/**
	 * Draws a cityCard for the player and adds it to his hand.
	 */
	public void executeEvent() throws CardNotInDeckException {
		Game currentGame = Game.getGameInstance();
		CityCard cityCard = drawCityCard(currentGame);
		Player playerFromCurrentTurn = currentGame.getPlayerFromCurrentTurn();
		addCityCardToPlayerHandDeck(playerFromCurrentTurn, cityCard);
		decreaseAmountOfActionsRemaining(currentGame);
	}

	public CityCard drawCityCard(Game currentGame) throws CardNotInDeckException {
		Deck cityDeck = getCityCardDeck(currentGame);
		return (CityCard) cityDeck.drawCardFromDeck();
	}

	private Deck getCityCardDeck(Game currentGame) {

		return currentGame.getPlayerCardDeck();
	}

	public void addCityCardToPlayerHandDeck(Player player, CityCard cityCard) {
		player.getPlayerDeck().addCardsToPlayerHand(cityCard);
	}

	private void decreaseAmountOfActionsRemaining(Game currentGame) {
		currentGame.getPlayerFromCurrentTurn().decreaseAmountOfActionsRemaining();
	}
	/**
	 * Gets the name of the event.
	 * @return The name of the event.
	 * 
	 */
	public String getCardName() {
		return "Homo Faber Fortunae Suae";
	}

	/**
	 * Gets the action description.
	 * @return The action description.
	 * 
	 */
	public String getCardDescription() {
		return "De huidige speler mag een stadkaart uit de aflegstapel (voor speelkaarten) trekken die correspondeert met zijn stad.";
	}

}
