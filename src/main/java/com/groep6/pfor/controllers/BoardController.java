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

    public void nextTurn() {
        int maximumAmountOfCardInHand = 7;
        // Draw 2 cards from game deck
        Player player = currentGame.getLocalPlayer();
        player.getPlayerDeck().addCards(currentGame.getPlayerCardsDeck().draw(), currentGame.getPlayerCardsDeck().draw());

        checkLoseConditions();

        // Open hand when there are more than 7 cards in hand
        if (player.getPlayerDeck().getCards().size() > maximumAmountOfCardInHand) new HandCardDeckController();

        invadeCities();
              
    	if(currentGame.getDecayLevel() >= currentGame.getMaxDecayLevel() - 1) {
    		new LoseGameController();
    		currentGame.setLost(true);
    	}
    	
    	checkWinConditions();

        // Next turn
        currentGame.nextTurn();
        GameService gameService = new GameService();
        gameService.setGame(currentGame);
    }

    public void checkLoseConditions() {
        int minimumAmountOfCardsInPlayerCardDeck = 0;
        // Go to lose screen when there are no more cards in players
        if (currentGame.getPlayerCardsDeck().getCards().size() <= minimumAmountOfCardsInPlayerCardDeck) {
            currentGame.setLost(true);
        } else if (currentGame.getDecayLevel() >= currentGame.getMaxDecayLevel() - 1) {
            currentGame.setLost(true);
        }
    }
    
    public void checkWinConditions() {
    	if (getFriendlyFactions().size() == 5) {
            currentGame.setWon(true);
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

    private void invadeCities() {
        int amountOfInvasions = 2;
        Card[] invasionCards = new Card[amountOfInvasions];
        Deck invasionCardsDeck = currentGame.getInvasionCardsDeck();
        for (int i = 0; i < amountOfInvasions; i++) {
            InvasionCard invasionCard = (InvasionCard) invasionCardsDeck.draw();
            invadeCity(invasionCard);
            invasionCards[i] = invasionCard;
        }
        invasionCardsDeck.addCards(invasionCards);
        invasionCardsDeck.shuffle();
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

    public void buildFort() {
        Player player = currentGame.getLocalPlayer();
        City city = player.getCityPlayerIsCurrentlyLocatedIn();
        city.placeFort();
        player.decreaseAmountOfActionsRemaining();
    }

    public boolean canBattle() {
        Player player = currentGame.getLocalPlayer();
        City city = player.getCityPlayerIsCurrentlyLocatedIn();

        return city.getAmountOfBarbariansLocatedInCurrentCity() > 0 && city.getLegionCount() > 0;
    }

    public boolean canRecruitBarbarians() {
        Player player = currentGame.getLocalPlayer();
        City city = player.getCityPlayerIsCurrentlyLocatedIn();
        Faction[] factions = city.getFactions();

        for (Faction faction: factions) {
            if (currentGame.isFriendlyFaction(faction) && city.getAmountOfBarbariansLocatedInCurrentCity(faction.getFactionType()) > 0) return true;
        }

        return false;
    }

    public boolean canRecruitLegions() {
        Player player = currentGame.getLocalPlayer();
        City city = player.getCityPlayerIsCurrentlyLocatedIn();

        return city.hasFort();
    }

    public boolean canBuildFort() {
        Player player = currentGame.getLocalPlayer();
        City city = player.getCityPlayerIsCurrentlyLocatedIn();

        return (!city.hasFort() && getAmountOfFortsInCity() < 6);
    }

    @Override
    public void registerObserver(IObserver view) {
        currentGame.registerObserver(view);
    }

    public void formAlliance() {
        Player player = getLocalPlayer();
        Faction faction = player.availableAlliances().get(0);

        // Ally this faction
        faction.makeAlly();

        // Remove cards
        List<Card> cardsToDiscard = player.getCitycardsOfAliedFaction(faction);
        player.getPlayerDeck().removeCards(cardsToDiscard.toArray(new Card[0]));

        player.decreaseAmountOfActionsRemaining();
    }
    
    public void changeMusic() {
    	Main.musicManager.stop();
    	Main.musicManager.playPlaylist();
    }

    public boolean canFormAlliance() {
        Player player = getLocalPlayer();
        return player.availableAlliances().size() > 0;
    }

    public List<Faction> getFriendlyFactions() {
        return Game.getInstance().getFriendlyFactions();
    }
}
