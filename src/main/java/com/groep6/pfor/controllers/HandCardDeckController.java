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

public class HandCardDeckController extends Controller {

    private final Game currentGame = Game.getInstance();
    private Card currentlySelectedCard;

    public HandCardDeckController() {
        viewController.showView(new HandView(this));
    }

    public List<Card> getPlayerHandDeckCards() {
        return currentGame.getLocalPlayer().getPlayerDeck().getCards();
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
        currentGame.getLocalPlayer().getPlayerDeck().removeCard(currentlySelectedCard);
    }

    public void addCardToInvasionCardDiscardPile() {
        currentGame.getInvasionCardsDiscardPile().addCards(currentlySelectedCard);
    }

    public void executeEventCard() {
        ((EventCard) currentlySelectedCard).executeEvent();
    }

    public void playDrawCardSoundEffect() {
        SoundEffectManager.playMusic("/sounds/effects/DrawCardSound.mp3");
    }

    public void playCard() {
        if (getCurrentlySelectedCard() == null) return;
        if (!isLocalPlayersTurn()) return;
        if (!currentlySelectedCardIsEventCard()) return;

        removeCurrentlySelectedCardFromPlayerDeck();
        addCardToInvasionCardDiscardPile();
        executeEventCard();

        playDrawCardSoundEffect();
        
        refresh();
    }

    public Player getLocalPlayer() {
        return currentGame.getLocalPlayer();
    }

    private Player getPlayerFromCurrentTurn() {
        return currentGame.getPlayerFromCurrentTurn();
    }

    private Deck getTradeDeck() {
        return currentGame.getTradeCardsDeck();
    }

    public boolean playerHasActionsRemaining(Player playerFromCurrentTurn) {
        int minimumAmountOfActionsRequired = 0;
        return playerFromCurrentTurn.getActionsRemaining() > minimumAmountOfActionsRequired;
    }

    public void removeCurrentlySelectedCardFromPlayerCardDeck(Player localPlayer) {
        localPlayer.getPlayerDeck().removeCard(currentlySelectedCard);
    }

    public void addCurrentlySelectedCardToTradeDeck() {
        Deck tradeCardDeck = getTradeDeck();
        tradeCardDeck.addCards(currentlySelectedCard);
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

        refresh();

	}

	private boolean currentlySelectedCardIsCityCard() {
        return currentlySelectedCard instanceof CityCard;
    }

    public boolean currentlySelectedCardIsEventCard() {
        return currentlySelectedCard instanceof EventCard;
    }

    private void discardCityCard() {
        currentGame.getCityCardsDiscardPile().addCards(currentlySelectedCard);
    }

    private void discardEventCard() {
        currentGame.getInvasionCardsDiscardPile().addCards(currentlySelectedCard);
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
        
        refresh();
    }
    
    public void goToBoard() {
    	showPreviousView();
    }
    
    public void refresh() {
    	showPreviousView();
    	new HandCardDeckController();
    }
}