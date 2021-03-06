package com.groep6.pfor.controllers;

import com.groep6.pfor.Main;
import com.groep6.pfor.exceptions.CardNotInDeckException;
import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.models.*;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.models.cards.InvasionCard;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.models.factions.FactionType;
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

    public BoardController() throws CouldNotFindLocalPlayerException {
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

    public Player getLocalPlayer() throws CouldNotFindLocalPlayerException {
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

    
    public void movePlayerToSelectedCity(City selectedCityToMoveTo) throws CouldNotFindLocalPlayerException {
        Player localPlayer = getLocalPlayer();

        if (!playerIsAllowedToMove(localPlayer, selectedCityToMoveTo)) return;

        if (currentCityContainsLegions(localPlayer)) {
            selectAmountOfLegionsToTakeWithYou(selectedCityToMoveTo, localPlayer);
        } else {
            moveToSelectedCity(selectedCityToMoveTo, localPlayer);
        }
    }

    private void addTwoCardToPlayerDeck(Player localPlayer) {
        try {
            addTwoCardsToPlayerHand(localPlayer, drawCardFromDeck(), drawCardFromDeck());
        } catch (CardNotInDeckException e) {
            e.printStackTrace();
        }
    }

    private Deck getPlayerCardDeck() {
        return currentGame.getPlayerCardDeck();
    }

    private Card drawCardFromDeck() throws CardNotInDeckException {
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

    public void nextTurn() throws CouldNotFindLocalPlayerException {
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

    private InvasionCard drawInvasionCardFromDeck(Deck invasionCardsDeck) throws CardNotInDeckException {
        return (InvasionCard) invasionCardsDeck.drawCardFromDeck();
    }
    private void invadeCities(int amountOfInvasions, Card[] invasionCards, Deck invasionCardsDeck) {
        for (int i = 0; i < amountOfInvasions; i++) {
            InvasionCard invasionCard = null;
            try {
                invasionCard = drawInvasionCardFromDeck(invasionCardsDeck);
            } catch (CardNotInDeckException e) {
                e.printStackTrace();
            }
            addBarbariansToCitiesThatAreBeingInvadedInTheInvasionRoute(invasionCard);
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

        invasionCardsDeck.addCardsToDeck(invasionCards);

        shuffleInvasionCardDeck( invasionCardsDeck );
    }

    private List<City> getInvasionRouteFromInvasionCard(InvasionCard card) {
        return card.getInvasionRoute();
    }
//_______________________________________________________________________________________________________________
    private boolean noBarbariansInCityOnInvasionRoute(City cityOnInvasionRoute, InvasionCard invasionCard) {
        return getAmountOfBarbariansLocatedInGivenCity(cityOnInvasionRoute, invasionCard) < 1;
    }

    private int getAmountOfBarbariansLocatedInGivenCity(City cityOnInvasionRoute, InvasionCard invasionCard) {
        FactionType invadingFactionType = getInvadingFactionType(invasionCard);
        return cityOnInvasionRoute.getAmountOfBarbariansLocatedInCurrentCity(invadingFactionType, cityOnInvasionRoute.getBarbariansInCity());
    }

    private Faction getInvadingFaction(InvasionCard invasionCard) {
        return invasionCard.getInvadingFaction();
    }

    private FactionType getInvadingFactionType(InvasionCard invasionCard) {
        Faction invadingFaction = getInvadingFaction(invasionCard);
        return invadingFaction.getFactionType();
    }

    private void addOneBarbariansToCityOnInvasionRoute(City cityOnInvasionRoute, InvasionCard invasionCard) {
        FactionType invadingFactionType = getInvadingFactionType(invasionCard);
        cityOnInvasionRoute.addBarbariansToCity(invadingFactionType, 1);
    }

    private boolean cityToInvadeIsNotTheFirstCityOnTheInvasionRoute(int indexOfCityOnInvasionRoute) {
        return indexOfCityOnInvasionRoute > 0;
    }

    private City getCityOnInvasionRouteByIndex(List<City> citiesOnInvasionRoute, int indexOfCityOnInvasionRoute) {
        return citiesOnInvasionRoute.get(indexOfCityOnInvasionRoute);
    }

    private boolean oneOrLessBarbariansInSecondToLastCityOnInvasionRoute(City secondToLastCityOnInvasionRoute, InvasionCard invasionCard) {
        return getAmountOfBarbariansLocatedInGivenCity(secondToLastCityOnInvasionRoute, invasionCard) >= 1;
    }

    private void addBarbariansToTheFirstEmptyCityInTheInvasionRoute(City cityOnInvasionRoute, InvasionCard invasionCard) {
        addOneBarbariansToCityOnInvasionRoute(cityOnInvasionRoute, invasionCard);
    }

    private void addBarbariansToTheCityBeforeTheFirstEmptyCityInTheInvasionRoute(int indexOfCityOnInvasionRoute, List<City> citiesOnInvasionRoute, InvasionCard invasionCard ) {
        City previousCityOnInvasionRoute = getCityOnInvasionRouteByIndex(citiesOnInvasionRoute, indexOfCityOnInvasionRoute - 1);
        addOneBarbariansToCityOnInvasionRoute(previousCityOnInvasionRoute, invasionCard);
    }

    private void addBarbariansToTheSecondToLastCityInTheInvasionRoute(List<City> citiesOnInvasionRoute, InvasionCard invasionCard) {
        City secondToLastCityOnInvasionRoute = getCityOnInvasionRouteByIndex(citiesOnInvasionRoute, citiesOnInvasionRoute.size() - 1);
        if (oneOrLessBarbariansInSecondToLastCityOnInvasionRoute(secondToLastCityOnInvasionRoute, invasionCard)){
            addOneBarbariansToCityOnInvasionRoute(secondToLastCityOnInvasionRoute, invasionCard);
        }
    }
    
    private void addBarbariansToCitiesThatAreBeingInvadedInTheInvasionRoute(InvasionCard invasionCard) {
        List<City> citiesOnInvasionRoute = getInvasionRouteFromInvasionCard(invasionCard);

        for (int i = 0; i < citiesOnInvasionRoute.size(); i++) {
            City cityOnInvasionRoute = getCityOnInvasionRouteByIndex(citiesOnInvasionRoute, i);
            if (noBarbariansInCityOnInvasionRoute(cityOnInvasionRoute, invasionCard)) {
                addBarbariansToTheFirstEmptyCityInTheInvasionRoute(cityOnInvasionRoute, invasionCard);

                if (cityToInvadeIsNotTheFirstCityOnTheInvasionRoute(i)) {
                    addBarbariansToTheCityBeforeTheFirstEmptyCityInTheInvasionRoute(i, citiesOnInvasionRoute, invasionCard);
                    break;
                }
            }
            addBarbariansToTheSecondToLastCityInTheInvasionRoute(citiesOnInvasionRoute, invasionCard);
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

    public void buildFortInCityPlayerIsCurrentlyStandingIn() throws CouldNotFindLocalPlayerException {
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

    public boolean canBattle() throws CouldNotFindLocalPlayerException {
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

    public boolean canRecruitBarbarians() throws CouldNotFindLocalPlayerException {
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

    public boolean canRecruitLegions() throws CouldNotFindLocalPlayerException {
        Player localPlayer = getLocalPlayer();
        City cityPlayerIsCurrentlyStandingIn = getCityPlayerIsCurrentlyStandingIn(localPlayer);

        return cityHasFort(cityPlayerIsCurrentlyStandingIn);
    }

    public boolean canBuildFort() throws CouldNotFindLocalPlayerException {
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

    public void formAlliance() throws CouldNotFindLocalPlayerException {
        Player localPlayer = getLocalPlayer();
        Faction factionToBecomeAllied = localPlayer.getAvailableAlliances().get(0);

        changeFactionStatusToAllied(factionToBecomeAllied);

        removeCardsFromAlliedFactions(localPlayer, factionToBecomeAllied);

       decreaseAmountOfActionsRemaining(localPlayer);
    }
    
    public void changeMusic() {
    	Main.musicManager.stop();
    	Main.musicManager.playPlaylist();
    }

    public boolean canFormAlliance() throws CouldNotFindLocalPlayerException {
        Player localPlayer = getLocalPlayer();
        return localPlayer.getAvailableAlliances().size() > 0;
    }

    public List<Faction> getFriendlyFactions() {
        return Game.getGameInstance().getFriendlyFactions();
    }

    @Override
    public void registerObserver(IObserver view) {
        currentGame.registerObserver(view);
    }
}
