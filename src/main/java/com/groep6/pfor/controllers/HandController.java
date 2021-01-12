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

public class HandController extends Controller {

    private final Game currentGame = Game.getInstance();
    private Card selectedCard;

    public HandController() {
    
        viewController.showView(new HandView(this));
    }

    public List<Card> getCards() {
        return currentGame.getLocalPlayer().getPlayerDeck().getCards();
    }

    @Override
    public void registerObserver(IObserver view) {
        currentGame.getLocalPlayer().getPlayerDeck().registerObserver(view);
    }

    public void selectCard(Card card) {
    	this.selectedCard = card;
    }
    
    public void playCard() {
        if (selectedCard == null) return;
        if (!currentGame.getLocalPlayer().isCurrentTurn()) return;

        if (selectedCard instanceof EventCard) {
            currentGame.getLocalPlayer().getPlayerDeck().removeCard(selectedCard);
        	currentGame.getInvasionCardsDiscardPile().addCards(selectedCard);
        	((EventCard) selectedCard).executeEvent();
        }

        SoundEffectManager.play("/sounds/effects/DrawCardSound.mp3");
        
        refresh();
    }

	public void depositCard() {
        if (selectedCard == null) return;
        
        Player localPlayer = currentGame.getLocalPlayer();
        Deck tradeDeck = currentGame.getTradeCardsDeck();
        Player player = currentGame.getPlayerFromCurrentTurn();

        if (player.getActionsRemaining() <= 0) return;

        localPlayer.getPlayerDeck().removeCard(selectedCard);
		tradeDeck.addCards(selectedCard);
		
        player.decreaseActionsRemaining();
        SoundEffectManager.play("/sounds/effects/DrawCardSound.mp3");

        refresh();

	}
	
    public void removeSelectedCard() {
        if (selectedCard == null) return;
        Player localPlayer = currentGame.getLocalPlayer();

        localPlayer.getPlayerDeck().removeCard(selectedCard);

        if (selectedCard instanceof CityCard) {
        	currentGame.getCityCardsDiscardPile().addCards(selectedCard);
        } else if (selectedCard instanceof EventCard) {
        	currentGame.getInvasionCardsDiscardPile().addCards(selectedCard);
        }

        SoundEffectManager.play("/sounds/effects/DrawCardSound.mp3");
        
        refresh();
    }
    
    public void goToBoard() {
    	showPreviousView();
    }
    
    public void refresh() {
    	showPreviousView();
    	new HandController();
    }

	public Player getLocalPlayer() {
        return currentGame.getLocalPlayer();
    }
}
