package com.groep6.pfor.controllers;

import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.SoundEffectManager;
import com.groep6.pfor.views.TradeView;

import java.util.List;

public class TradeController extends Controller {
	
	private final Game currentGame = Game.getGameInstance();
    private Card currentlySelectedCard;

    private void showTradeView() {
        viewController.showView(new TradeView(this));
    }

    public TradeController() {
        showTradeView();
    }
    
    public List<Card> getTradeCards() {
        return currentGame.getTradeCardDeck().getCards();
    }

    private void registerTradeDeckObserver(IObserver view) {
        currentGame.getTradeCardDeck().registerObserver(view);
    }

    private void registerPlayerDeckObserver(IObserver view) {
        currentGame.getLocalPlayer().getPlayerDeck().registerObserver(view);
    }

    @Override
    public void registerObserver(IObserver view) {
    	registerTradeDeckObserver(view);
        registerPlayerDeckObserver(view);
    }
    
    public void selectCard(Card card) {
    	this.currentlySelectedCard = card;
    }

    private Player getPlayerFromCurrentTurn() {
        return currentGame.getPlayerFromCurrentTurn();
    }

    private boolean playerHasActionsRemaining(Player playerFromCurrentTurn) {
        int minimumAmountOfActionsRequired = 0;
        return playerFromCurrentTurn.getActionsRemaining() > minimumAmountOfActionsRequired;
    }

    private void removeCurrentlySelectedCardFromTradeDeck() {
        currentGame.getTradeCardDeck().removeCard(currentlySelectedCard);
    }

    private void addCurrentlySelectedCardToPlayerHandDeck() {
        currentGame.getLocalPlayer().getPlayerDeck().addCardsToPlayerHand(currentlySelectedCard);
    }

    private void decreaseAmountOfActionsRemaining(Player playerFromCurrentTurn) {
        playerFromCurrentTurn.decreaseAmountOfActionsRemaining();
    }

    private void playDrawCardSound() {
        SoundEffectManager.playMusic("/sounds/effects/DrawCardSound.mp3");
    }

    
    public void withdrawCardFromTradeDeck() {
        Player playerFromCurrentTurn = getPlayerFromCurrentTurn();
        if (!playerHasActionsRemaining(playerFromCurrentTurn)) return;

        removeCurrentlySelectedCardFromTradeDeck();
        addCurrentlySelectedCardToPlayerHandDeck();
    	decreaseAmountOfActionsRemaining(playerFromCurrentTurn);

        playDrawCardSound();
    	
    	refresh();
    }
    
    public Card getCard(Card card) {
    	return card;
    }

    public Player getLocalPlayer() {
        return currentGame.getLocalPlayer();
    }

    private void createTradeView() {
        new TradeController();
    }
    
    public void refresh() {
    	showPreviousView();
    	createTradeView();
    }
}
