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
    
    public void playCard() {
        if (currentlySelectedCard == null) return;
        if (!currentGame.getLocalPlayer().isCurrentTurn()) return;

        if (currentlySelectedCard instanceof EventCard) {
            currentGame.getLocalPlayer().getPlayerDeck().removeCard(currentlySelectedCard);
        	currentGame.getInvasionCardsDiscardPile().addCards(currentlySelectedCard);
        	((EventCard) currentlySelectedCard).executeEvent();
        }

        SoundEffectManager.playMusic("/sounds/effects/DrawCardSound.mp3");
        
        refresh();
    }

	public void depositCard() {
        if (currentlySelectedCard == null) return;
        
        Player localPlayer = currentGame.getLocalPlayer();
        Deck tradeCardDeck = currentGame.getTradeCardsDeck();
        Player player = currentGame.getPlayerFromCurrentTurn();

        if (player.getActionsRemaining() <= 0) return;

        localPlayer.getPlayerDeck().removeCard(currentlySelectedCard);
		tradeCardDeck.addCards(currentlySelectedCard);
		
        player.decreaseAmountOfActionsRemaining();
        SoundEffectManager.playMusic("/sounds/effects/DrawCardSound.mp3");

        refresh();

	}
	
    public void removeSelectedCard() {
        if (currentlySelectedCard == null) return;
        Player localPlayer = currentGame.getLocalPlayer();

        localPlayer.getPlayerDeck().removeCard(currentlySelectedCard);

        if (currentlySelectedCard instanceof CityCard) {
        	currentGame.getCityCardsDiscardPile().addCards(currentlySelectedCard);
        } else if (currentlySelectedCard instanceof EventCard) {
        	currentGame.getInvasionCardsDiscardPile().addCards(currentlySelectedCard);
        }

        SoundEffectManager.playMusic("/sounds/effects/DrawCardSound.mp3");
        
        refresh();
    }
    
    public void goToBoard() {
    	showPreviousView();
    }
    
    public void refresh() {
    	showPreviousView();
    	new HandCardDeckController();
    }

	public Player getLocalPlayer() {
        return currentGame.getLocalPlayer();
    }
}
