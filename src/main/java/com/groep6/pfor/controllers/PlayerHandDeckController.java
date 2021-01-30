package com.groep6.pfor.controllers;

import com.groep6.pfor.models.Deck;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.models.cards.CityCard;
import com.groep6.pfor.models.cards.EventCard;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.SoundEffectManager;
import com.groep6.pfor.views.HandView;

import java.util.List;

/**
 * Controller for the player hand. These are the cards the player is currently holding. This controller handles everything within the player hand
 * these are things like selecting and playing cards and adding cards to the global trade deck
 * @author Nils van der Velden
 *
 */

public class PlayerHandDeckController extends Controller {

    private final Game currentGame = Game.getGameInstance();
    private Card currentlySelectedCard;

    public PlayerHandDeckController() {
        viewController.showView(new HandView(this));
    }

    public List<Card> getPlayerHandDeckCards() {
        return currentGame.getLocalPlayer().getPlayerDeck().getPlayerHandCards();
    }

    @Override
    public void registerObserver(IObserver view) {
        currentGame.getLocalPlayer().getPlayerDeck().registerObserver(view);
    }

    public void currentlySelectedCard(Card card) {
    	this.currentlySelectedCard = card;
    }

    public Card getCurrentlySelectedCard() {
        return (currentlySelectedCard);
    }

    public boolean isLocalPlayersTurn() {
        return currentGame.getLocalPlayer().isCurrentTurn();
    }

    public void removeCurrentlySelectedCardFromPlayerDeck() {
        currentGame.getLocalPlayer().getPlayerDeck().removeCardFromPlayerHand(currentlySelectedCard);
    }

    public void addCardToInvasionCardDiscardPile() {
        currentGame.getInvasionCardDiscardPile().addCardToDeck(currentlySelectedCard);
    }

    public void executeEventCard() {
        ((EventCard) currentlySelectedCard).executeEvent();
    }

    public void playDrawCardSoundEffect() {
        SoundEffectManager.playMusic("/sounds/effects/DrawCardSound.mp3");
    }

    private boolean noCardSelected() {
        return getCurrentlySelectedCard() == null;
    }

    public void playCard() {
        if (noCardSelected()) return;
        if (!isLocalPlayersTurn()) return;
        if (!currentlySelectedCardIsEventCard()) return;

        removeCurrentlySelectedCardFromPlayerDeck();
        addCardToInvasionCardDiscardPile();
        executeEventCard();

        playDrawCardSoundEffect();
        
        updateView();
    }

    public Player getLocalPlayer() {
        return currentGame.getLocalPlayer();
    }

    private Player getPlayerFromCurrentTurn() {
        return currentGame.getPlayerFromCurrentTurn();
    }

    private Deck getTradeDeck() {
        return currentGame.getTradeCardDeck();
    }

    public boolean playerHasActionsRemaining(Player playerFromCurrentTurn) {
        int minimumAmountOfActionsRequired = 0;
        return playerFromCurrentTurn.getActionsRemaining() > minimumAmountOfActionsRequired;
    }

    public void removeCurrentlySelectedCardFromPlayerCardDeck(Player localPlayer) {
        localPlayer.getPlayerDeck().removeCardFromPlayerHand(currentlySelectedCard);
    }

    public void addCurrentlySelectedCardToTradeDeck() {
        Deck tradeCardDeck = getTradeDeck();
        tradeCardDeck.addCardToDeck(currentlySelectedCard);
    }

    public void decreaseAmountOfActionsRemaining(Player playerFromCurrentTurn) {
        playerFromCurrentTurn.decreaseAmountOfActionsRemaining();
    }

	public void depositCardIntoTradingDeck() {
        if (getCurrentlySelectedCard() == null) return;
        
        Player localPlayer = getLocalPlayer();
        Player playerFromCurrentTurn = getPlayerFromCurrentTurn();

        if (!playerHasActionsRemaining(playerFromCurrentTurn)) return;

        removeCurrentlySelectedCardFromPlayerCardDeck(localPlayer);
		addCurrentlySelectedCardToTradeDeck();
		
        decreaseAmountOfActionsRemaining(playerFromCurrentTurn);

        playDrawCardSoundEffect();

        updateView();

	}

	private boolean currentlySelectedCardIsCityCard() {
        return currentlySelectedCard instanceof CityCard;
    }

    public boolean currentlySelectedCardIsEventCard() {
        return currentlySelectedCard instanceof EventCard;
    }

    private void discardCityCard() {
        currentGame.getCityCardDiscardPile().addCardToDeck(currentlySelectedCard);
    }

    private void discardEventCard() {
        currentGame.getInvasionCardDiscardPile().addCardToDeck(currentlySelectedCard);
    }
	
    public void removeSelectedCard() {
        if (getCurrentlySelectedCard() == null) return;
        Player localPlayer = getLocalPlayer();

        removeCurrentlySelectedCardFromPlayerCardDeck(localPlayer);

        if (currentlySelectedCardIsCityCard()) {
        	discardCityCard();
        } else if (currentlySelectedCardIsEventCard()) {
        	discardEventCard();
        }

        playDrawCardSoundEffect();
        
        updateView();
    }
    
    public void goToBoard() {
    	showPreviousView();
    }
    
    public void updateView() {
    	showPreviousView();
    	new PlayerHandDeckController();
    }
}
