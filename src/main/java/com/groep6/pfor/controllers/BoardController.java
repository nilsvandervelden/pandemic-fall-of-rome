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
 * Controller for the board. This controller handles the complete board.
 * Things like movement, invasions, alliances, winning and losing are handled in this controller
 * @author Nils van der Velden
 *
 */
public class BoardController extends Controller {

    private final Game currentGame = Game.getGameInstance();

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
        return currentGame.getGameBoard().getTiles();
    }
    
    public List<Player> getPlayers() {
        return currentGame.getAllPlayers();
    }

    public Player getLocalPlayer() {
        return currentGame.getLocalPlayer();
    }



    public boolean isNeighbouringCity(Player localPlayer, City selectedCityToMoveTo) {
        return (Arrays.asList(localPlayer.getCityPlayerIsCurrentlyLocatedIn().getNeighbouringCities()).contains(selectedCityToMoveTo));
    }

    private boolean playerHasActionsRemaining(Player localPlayer) {
        int minimumAmountOfActionsRequired = 0;
        return localPlayer.getActionsRemaining() > minimumAmountOfActionsRequired;
    }

    private boolean isPlayersTurn(Player localPlayer) {
        return localPlayer.isCurrentTurn();
    }

    private boolean currentCityContainsLegions(Player localPlayer) {
        return (localPlayer.getCityPlayerIsCurrentlyLocatedIn().getLegionsInCity().size() > 0);
    }

    private boolean playerIsAllowedToMove(Player localPlayer, City selectedCityToMoveTo) {
        return isNeighbouringCity(localPlayer, selectedCityToMoveTo) && playerHasActionsRemaining(localPlayer) && isPlayersTurn(localPlayer);
    }

    private void selectAmountOfLegionsToTakeWithYou(City selectedCityToMoveTo, Player localPlayer) {
        new MovementController(selectedCityToMoveTo, localPlayer);
    }

    private void moveToSelectedCity(City selectedCityToMoveTo, Player localPlayer) {
        localPlayer.moveLocalPlayerToSelectedCity(selectedCityToMoveTo);
    }

    
    public void movePlayerToSelectedCity(City selectedCityToMoveTo) {
        Player localPlayer = getLocalPlayer();

        if (!playerIsAllowedToMove(localPlayer, selectedCityToMoveTo)) return;

        if (currentCityContainsLegions(localPlayer)) {
            selectAmountOfLegionsToTakeWithYou(selectedCityToMoveTo, localPlayer);
        } else {
            moveToSelectedCity(selectedCityToMoveTo, localPlayer);
        }
    }

    private void addTwoCardToPlayerDeck(Player localPlayer) {
        addTwoCardsToPlayerHand(localPlayer, drawCardFromDeck(), drawCardFromDeck());
    }

    private Deck getPlayerCardDeck() {
        return currentGame.getPlayerCardDeck();
    }

    private Card drawCardFromDeck() {
        return getPlayerCardDeck().drawCardFromDeck();
    }
    private void addTwoCardsToPlayerHand(Player localPlayer, Card card1, Card card2) {
        getPlayerDeck(localPlayer).addCardsToPlayerHand(card1, card2);
    }

    private PlayerHand getPlayerDeck(Player localPlayer) {
        return localPlayer.getPlayerDeck();
    }

    private void showPlayerHandDeck(Player localPlayer) {
        if (maximumAmountOfCardsInHandDeckHaveBeenReached(localPlayer)) {
            new PlayerHandDeckController();
        }
    }

    private void gameHasBeenLost() {
        new LoseGameController();
        currentGame.setGameLost(true);
    }
    // Open hand when there are more than 7 cards in hand
    private boolean maximumAmountOfCardsInHandDeckHaveBeenReached(Player localPlayer) {
        int maximumAmountOfCardInHand = 7;
        return (localPlayer.getPlayerDeck().getPlayerHandCards().size() > maximumAmountOfCardInHand);

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
        checkIfGameHasBeenLost();
        checkIfGameHasBeenWon();
        addTwoCardToPlayerDeck(localPlayer);
        showPlayerHandDeck(localPlayer);
        barbariansInvadeCities();
        setNextTurn();
    }

    private boolean playerHasNoMoreCards() {
        int minimumAmountOfCardsInPlayerCardDeck = 1;
        return currentGame.getPlayerCardDeck().getCards().size() <= minimumAmountOfCardsInPlayerCardDeck;
    }

    public void checkIfGameHasBeenLost() {
        if (playerHasNoMoreCards()) {
            gameHasBeenLost();
        } else if (maximumDecayLevelHasBeenReached()) {
            gameHasBeenLost();
        }
    }

    private void gameHasBeenWon() {
        new WinGameController();
        currentGame.setGameWon(true);
    }
    
    public void checkIfGameHasBeenWon() {
    	if (getFriendlyFactions().size() == 5) {
    	    gameHasBeenWon();
        }
    }

    public int getAmountOfFortsInCity() {
        int amountOfFortsInCity = 0;

        for (Tile tile: currentGame.getGameBoard().getTiles()) {
            if (tile instanceof City) {
                City city = (City) tile;
                if (city.hasFort()) amountOfFortsInCity++;
            }
        }
        return amountOfFortsInCity;
    }

    private Deck getInvasionCardsDeck() {
        return currentGame.getInvasionCardDeck();
    }

    private Card[] getInvasionCards(int amountOfInvasions) {
        return new Card[amountOfInvasions];
    }

    private InvasionCard drawInvasionCardFromDeck(Deck invasionCardsDeck) {
        return (InvasionCard) invasionCardsDeck.drawCardFromDeck();
    }
    private void invadeCities(int amountOfInvasions, Card[] invasionCards, Deck invasionCardsDeck) {
        for (int i = 0; i < amountOfInvasions; i++) {
            InvasionCard invasionCard = drawInvasionCardFromDeck(invasionCardsDeck);
            invadeCity(invasionCard);
            invasionCards[i] = invasionCard;
        }
    }

    private void shuffleInvasionCardDeck(Deck invasionCardsDeck) {
        invasionCardsDeck.shuffleDeck();
    }

    private void barbariansInvadeCities() {
        int amountOfInvasions = 2;

        Card[] invasionCards = getInvasionCards(amountOfInvasions);

        Deck invasionCardsDeck = getInvasionCardsDeck();

        invadeCities(amountOfInvasions, invasionCards, invasionCardsDeck);

        invasionCardsDeck.addCardToDeck(invasionCards);

        shuffleInvasionCardDeck( invasionCardsDeck );
    }

    private List<City> getInvasionRoute(InvasionCard card) {
        return card.getInvasionRoute();
    }

    
    private void invadeCity(InvasionCard card) {
        List<City> route = getInvasionRoute(card);

        for (int i = 0; i < route.size(); i++) {
        	if(route.get(i).getAmountOfBarbariansLocatedInCurrentCity(card.getInvadingFaction().getFactionType(), route.get(i).getBarbariansInCity()) < 1) {
                route.get(i).addBarbariansToCity(card.getInvadingFaction().getFactionType(), 1);
                if (i > 0) route.get(i - 1).addBarbariansToCity(card.getInvadingFaction().getFactionType(), 1);
                break;
        	}
        }
        if (route.get(route.size() - 1).getAmountOfBarbariansLocatedInCurrentCity(card.getInvadingFaction().getFactionType(), route.get(route.size() - 1).getBarbariansInCity()) >= 1){
    		route.get(route.size() - 1).addBarbariansToCity(card.getInvadingFaction().getFactionType(), 1);
        }
    }

    private City getCityPlayerIsCurrentlyStandingIn(Player localPlayer) {
        return localPlayer.getCityPlayerIsCurrentlyLocatedIn();
    }

    private void placeFortInCurrentCity(City cityPlayerIsCurrentlyStandingIn) {
        cityPlayerIsCurrentlyStandingIn.placeFortInCity();
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
        factionToBecomeAllied.allyEnemyFaction();
    }

    private void removeCardsFromAlliedFactions(Player localPlayer, Faction factionToBecomeAllied) {
        List<Card> cardsToDiscard = localPlayer.getCityCardsOfAlliedFaction(factionToBecomeAllied);
        localPlayer.getPlayerDeck().removeCardsFromPlayerHand(cardsToDiscard.toArray(new Card[0]));
    }

    public void formAlliance() {
        Player localPlayer = getLocalPlayer();
        Faction factionToBecomeAllied = localPlayer.checkAvailableAlliances().get(0);

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
        return localPlayer.checkAvailableAlliances().size() > 0;
    }

    public List<Faction> getFriendlyFactions() {
        return Game.getGameInstance().getFriendlyFactions();
    }

    @Override
    public void registerObserver(IObserver view) {
        currentGame.registerObserver(view);
    }
}
