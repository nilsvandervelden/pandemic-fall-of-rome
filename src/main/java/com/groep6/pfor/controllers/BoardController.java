package com.groep6.pfor.controllers;

import com.groep6.pfor.Main;
import com.groep6.pfor.models.*;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.models.cards.InvasionCard;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.services.GameService;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.BoardView;

import java.util.Arrays;
import java.util.List;

/**
 * @author Bastiaan Jansen
 * @author Nils van der Velden
 */
public class BoardController extends Controller {

    private final Game currentGame = Game.getInstance();

    public BoardController() {
    	changeMusic();
        viewController.showView(new BoardView(this));
    }
    
    public void goToBattleView() {
    	new BattleController();
    }

    public void goToInstructionView() {
        new InstructionController();
    }

    public Player getPlayerFromCurrentTurn() {
        return currentGame.getPlayerFromCurrentTurn();
    }
    
    public void increaseDecayLevel(int amount) {
        currentGame.increaseDecayLevel(amount);
    }
    
    public int getDecayLevel() {
        return currentGame.getDecayLevel();
    }

    public Tile[] getTiles() {
        return currentGame.getBoard().getTiles();
    }
    
    public List<Player> getPlayers() {
        return currentGame.getAllPlayers();
    }

    public Player getLocalPlayer() {
        return currentGame.getLocalPlayer();
    }

    public boolean playerIsAllowedToMove(Player localPlayer, City selectedCityToMoveTo) {
        return isNeighbouringCity(localPlayer, selectedCityToMoveTo) && playerHasActionsRemaining(localPlayer) && isPlayersTurn(localPlayer);
    }

    public boolean isNeighbouringCity(Player localPlayer, City selectedCityToMoveTo) {
        return (Arrays.asList(localPlayer.getCityPlayerIsCurrentlyLocatedIn().getNeighbouringCities()).contains(selectedCityToMoveTo));
    }

    public boolean playerHasActionsRemaining(Player localPlayer) {
        int minimumAmountOfActionsRequired = 0;
        return localPlayer.getActionsRemaining() > minimumAmountOfActionsRequired;
    }

    public boolean isPlayersTurn(Player localPlayer) {
        return localPlayer.isCurrentTurn();
    }

    public boolean currentCityContainsLegions(Player localPlayer) {
        return (localPlayer.getCityPlayerIsCurrentlyLocatedIn().getLegions().size() > 0);
    }
    
    public void movePlayerToSelectedCity(City selectedCityToMoveTo) {
        Player localPlayer = getLocalPlayer();

        if (!playerIsAllowedToMove(localPlayer, selectedCityToMoveTo)) return;

        if (currentCityContainsLegions(localPlayer)) {
            new MovePlayerToCityController(selectedCityToMoveTo, localPlayer);
        } else localPlayer.moveLocalPlayerToSelectedCity(selectedCityToMoveTo);
    }

    private void addTwoCardToPlayerDeck(Player localPlayer) {
        localPlayer.getPlayerDeck().addCards(currentGame.getPlayerCardsDeck().draw(), currentGame.getPlayerCardsDeck().draw());
    }

    private void showPlayerHandDeck() {
        new HandCardDeckController();
    }

    private void gameHasBeenLost() {
        new LoseGameController();
        currentGame.setLost(true);
    }
    // Open hand when there are more than 7 cards in hand
    private boolean maximumAmountOfCardsInHandDeckHaveBeenReached(Player localPlayer) {
        int maximumAmountOfCardInHand = 7;
        return (localPlayer.getPlayerDeck().getCards().size() > maximumAmountOfCardInHand);

    }

    private boolean maximumDecayLevelHasBeenReached() {
        return currentGame.getDecayLevel() >= currentGame.getMaxDecayLevel() - 1;
    }

    private void setNextTurn() {
        currentGame.nextTurn();
        GameService gameService = new GameService();
        gameService.setGame(currentGame);
    }

    public void nextTurn() {
        Player localPlayer = getLocalPlayer();

        addTwoCardToPlayerDeck(localPlayer);

        checkLoseConditions();

        if (maximumAmountOfCardsInHandDeckHaveBeenReached(localPlayer)) {
            showPlayerHandDeck();
        }

        handleBarbarianInvasions();
    	
    	checkWinConditions();

        setNextTurn();
    }

    private boolean playerHasNoMoreCards() {
        int minimumAmountOfCardsInPlayerCardDeck = 1;
        return currentGame.getPlayerCardsDeck().getCards().size() <= minimumAmountOfCardsInPlayerCardDeck;
    }

    public void checkLoseConditions() {
        if (playerHasNoMoreCards()) {
            gameHasBeenLost();
        } else if (maximumDecayLevelHasBeenReached()) {
            gameHasBeenLost();
        }
    }

    private void gameHasBeenWon() {
        new WinGameController();
        currentGame.setWon(true);
    }
    
    public void checkWinConditions() {
    	if (getFriendlyFactions().size() == 5) {
    	    gameHasBeenWon();
        }
    }

    public int getAmountOfFortsInCity() {
        int amountOfFortsInCity = 0;

        for (Tile tile: currentGame.getBoard().getTiles()) {
            if (tile instanceof City) {
                City city = (City) tile;
                if (city.hasFort()) amountOfFortsInCity++;
            }
        }
        return amountOfFortsInCity;
    }

    private Deck getInvasionCardsDeck() {
        return currentGame.getInvasionCardsDeck();
    }

    private Card[] getInvasionCards(int amountOfInvasions) {
        return new Card[amountOfInvasions];
    }

    private void invadeCities(int amountOfInvasions, Card[] invasionCards, Deck invasionCardsDeck) {
        for (int i = 0; i < amountOfInvasions; i++) {
            InvasionCard invasionCard = (InvasionCard) invasionCardsDeck.draw();
            invadeCity(invasionCard);
            invasionCards[i] = invasionCard;
        }
    }

    private void shuffleInvasionCardDeck(Deck invasionCardsDeck) {
        invasionCardsDeck.shuffle();
    }

    private void handleBarbarianInvasions() {
        int amountOfInvasions = 2;

        Card[] invasionCards = getInvasionCards(amountOfInvasions);

        Deck invasionCardsDeck = getInvasionCardsDeck();

        invadeCities(amountOfInvasions, invasionCards, invasionCardsDeck);

        invasionCardsDeck.addCards(invasionCards);

        shuffleInvasionCardDeck( invasionCardsDeck );
    }

    
    private void invadeCity(InvasionCard card) {
        List<City> route = card.getRoute();

        for (int i = 0; i < route.size(); i++) {
        	if(route.get(i).getAmountOfBarbariansLocatedInCurrentCity(card.getFaction().getFactionType(), route.get(i).getBarbarians()) < 1) {
                route.get(i).addBarbarians(card.getFaction().getFactionType(), 1);
                if (i > 0) route.get(i - 1).addBarbarians(card.getFaction().getFactionType(), 1);
                break;
        	}
        }
        if (route.get(route.size() - 1).getAmountOfBarbariansLocatedInCurrentCity(card.getFaction().getFactionType(), route.get(route.size() - 1).getBarbarians()) >= 1){
    		route.get(route.size() - 1).addBarbarians(card.getFaction().getFactionType(), 1);
        }
    }

    private City getCityPlayerIsCurrentlyStandingIn(Player localPlayer) {
        return localPlayer.getCityPlayerIsCurrentlyLocatedIn();
    }

    private void placeFortInCurrentCity(City cityPlayerIsCurrentlyStandingIn) {
        cityPlayerIsCurrentlyStandingIn.placeFort();
    }

    private void decreaseAmountOfActionsRemaining(Player localPlayer) {
        localPlayer.decreaseAmountOfActionsRemaining();
    }

    public void buildFortInCityPlayerIsCurrentlyStandingIn() {
        Player localPlayer = getLocalPlayer();
        City cityPlayerIsCurrentlyStandingIn = getCityPlayerIsCurrentlyStandingIn(localPlayer);
        placeFortInCurrentCity(cityPlayerIsCurrentlyStandingIn);
        decreaseAmountOfActionsRemaining(localPlayer);
    }

    private boolean barbariansLocatedInCurrentCity(City cityPlayerIsCurrentlyStandingIn) {
        return cityPlayerIsCurrentlyStandingIn.getAmountOfBarbariansLocatedInCurrentCity() > 0;
    }

    private boolean legionsLocatedInCurrentCity(City cityPlayerIsCurrentlyStandingIn) {
        return cityPlayerIsCurrentlyStandingIn.getLegionCount() > 0;
    }

    public boolean canBattle() {
        Player localPlayer = getLocalPlayer();
        City cityPlayerIsCurrentlyStandingIn = getCityPlayerIsCurrentlyStandingIn(localPlayer);

        return barbariansLocatedInCurrentCity(cityPlayerIsCurrentlyStandingIn) && legionsLocatedInCurrentCity(cityPlayerIsCurrentlyStandingIn);
    }

    private Faction[] determineWhichFactionCanAccessCurrentCity(City cityPlayerIsCurrentlyStandingIn) {
        return cityPlayerIsCurrentlyStandingIn.getFactions();
    }

    private boolean isFriendlyFaction(Faction faction) {
        return currentGame.isFriendlyFaction(faction);
    }

    private boolean hasAlliedBarbarians(City cityPlayerIsCurrentlyStandingIn, Faction faction) {
        return cityPlayerIsCurrentlyStandingIn.getAmountOfBarbariansLocatedInCurrentCity(faction.getFactionType()) > 0;
    }

    public boolean canRecruitBarbarians() {
        Player localPlayer = getLocalPlayer();
        City cityPlayerIsCurrentlyStandingIn = getCityPlayerIsCurrentlyStandingIn(localPlayer);
        Faction[] factionsThatCanAccessThisCity = determineWhichFactionCanAccessCurrentCity(cityPlayerIsCurrentlyStandingIn);

        for (Faction faction: factionsThatCanAccessThisCity) {
            if (isFriendlyFaction(faction) && hasAlliedBarbarians(cityPlayerIsCurrentlyStandingIn, faction)) return true;
        }
        return false;
    }

    public boolean cityHasFort(City cityPlayerIsCurrentlyStandingIn) {
        return cityPlayerIsCurrentlyStandingIn.hasFort();
    }

    public boolean canRecruitLegions() {
        Player localPlayer = getLocalPlayer();
        City cityPlayerIsCurrentlyStandingIn = getCityPlayerIsCurrentlyStandingIn(localPlayer);

        return cityHasFort(cityPlayerIsCurrentlyStandingIn);
    }

    public boolean canBuildFort() {
        int maximumAmountOfFortsInGame = 6;
        Player localPlayer = getLocalPlayer();
        City cityPlayerIsCurrentlyStandingIn = getCityPlayerIsCurrentlyStandingIn(localPlayer);

        return (!cityHasFort(cityPlayerIsCurrentlyStandingIn) && getAmountOfFortsInCity() < maximumAmountOfFortsInGame);
    }

    private void changeFactionStatusToAllied(Faction factionToBecomeAllied) {
        factionToBecomeAllied.makeAlly();
    }

    private void removeCardsFromAlliedFactions(Player localPlayer, Faction factionToBecomeAllied) {
        List<Card> cardsToDiscard = localPlayer.getCitycardsOfAliedFaction(factionToBecomeAllied);
        localPlayer.getPlayerDeck().removeCards(cardsToDiscard.toArray(new Card[0]));
    }

    public void formAlliance() {
        Player localPlayer = getLocalPlayer();
        Faction factionToBecomeAllied = localPlayer.availableAlliances().get(0);

        changeFactionStatusToAllied(factionToBecomeAllied);

        removeCardsFromAlliedFactions(localPlayer, factionToBecomeAllied);

       decreaseAmountOfActionsRemaining(localPlayer);
    }
    
    public void changeMusic() {
    	Main.musicManager.stop();
    	Main.musicManager.playPlaylist();
    }

    public boolean canFormAlliance() {
        Player localPlayer = getLocalPlayer();
        return localPlayer.availableAlliances().size() > 0;
    }

    public List<Faction> getFriendlyFactions() {
        return Game.getInstance().getFriendlyFactions();
    }

    @Override
    public void registerObserver(IObserver view) {
        currentGame.registerObserver(view);
    }
}
