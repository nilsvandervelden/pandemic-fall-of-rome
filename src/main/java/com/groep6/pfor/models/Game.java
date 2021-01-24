package com.groep6.pfor.models;

import com.groep6.pfor.factories.CityCardFactory;
import com.groep6.pfor.factories.CityFactory;
import com.groep6.pfor.factories.EventCardFactory;
import com.groep6.pfor.factories.InvasionCardFactory;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.models.factions.FactionType;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.Observable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game extends Observable implements IObserver {

    private static final Game SINGLE_INSTANCE = new Game();
    private static GameState GAME_STATE = GameState.MENU;

    private Board gameBoard = new Board();
    private List<Player> playersInCurrentGame = new ArrayList<>();
    private final List<City> invadedCities = new ArrayList<>();
    private int decayLevel = 0;
    private final int MAX_DECAY_LEVEL = 9;
    private int invasionLevel = 0;
    private Deck tradeCardDeck = new Deck();
    private Deck invasionCardDeck;
    private Deck playerCardDeck;
    private Deck invasionCardDiscardPile = new Deck();
    private Deck cityCardDiscardPile = new Deck();
    private final Dice[] die = new Dice[3];
    private List<Faction> friendlyFactions = new ArrayList<>();
    private String gameCode;
    private boolean gameLost = false;
    private boolean gameWon = false;

    public static Game getGameInstance() {
        return SINGLE_INSTANCE;
    }
    
    public static GameState getGameState() {
    	return GAME_STATE;
    }
    
    public static void setGameState(GameState state) {
    	GAME_STATE = state;
    }

    private Deck createPlayerCardDeck() {
        return new Deck(parseCityCardListToArray());
    }

    private Card[] parseCityCardListToArray() {
        return getCardsFromCityCardDeck().toArray(new Card[0]);
    }

    private List<Card> getCardsFromCityCardDeck() {
        return getCityCardDeck().getCards();
    }

    private Deck getCityCardDeck() {
        return getCityCardFactory().getCityCardDeck();
    }

    private CityCardFactory getCityCardFactory() {
        return CityCardFactory.getCityCardFactoryInstance();
    }

    private EventCardFactory getEventCardFactory() {
        return EventCardFactory.getInstance();
    }

    private Deck getEventCardDeck() {
        return getEventCardFactory().getEventCardDeck();
    }

    private void mergePlayerCardAndEventCardDecks(Deck playerCardDeck) {
        playerCardDeck.mergeDecks(getEventCardDeck());
    }

    private void shuffleDeck(Deck deckToShuffle) {
        deckToShuffle.shuffleDeck();
    }

    private InvasionCardFactory getInvasionCardFactory() {
        return InvasionCardFactory.getInstance();
    }

    private Card[] getInvasionCards() {
        return getInvasionCardFactory().getAllInvasionCards();
    }

    private Deck createInvasionCardDeck() {
        return new Deck(getInvasionCards());
    }

    private Random createRandomNumberGenerator() {
        return new Random();
    }

    private CityFactory getCityFactory() {
        return CityFactory.getInstance();
    }

    private City[] getCities() {
        return getCityFactory().getAllCities();
    }

    private City determineWhatCityToInvade(City[] cities, Random randomAmountOfBarbarians) {
       return cities[randomAmountOfBarbarians.nextInt(cities.length)];
    }

    private boolean cityHasAlreadyBeenInvaded(City cityToInvade) {
        return invadedCities.contains(cityToInvade);
    }

    private boolean cityToInvadeIsRome(City cityToInvade) {
        return cityToInvade.getCityName().equals("Roma");
    }

    private int determineAmountOfBarbariansToInvade(Random randomAmountOfBarbarians) {
        int maximumAmountOfInvadingBarbarian = 2;
        return randomAmountOfBarbarians.nextInt(maximumAmountOfInvadingBarbarian);
    }

    private boolean moreThenTwoBarbariansInCityToInvade(City cityToInvade) {
        return cityToInvade.getAmountOfBarbariansLocatedInCurrentCity() > 2;
    }

    private void addCityToInvadedCityList(City cityToInvade) {
        invadedCities.add(cityToInvade);
    }

    private Faction[] getFactionsAllowedInCity(City cityToInvade) {
        return cityToInvade.getFactions();
    }

    private FactionType getFactionTypeOfAllowedFaction(City cityToInvade, Random randomNumberGenerator ) {
        return getFactionsAllowedInCity(cityToInvade)[generateRandomInt(randomNumberGenerator, getFactionsAllowedInCity(cityToInvade))].getFactionType();
    }

    private int generateRandomInt(Random randomNumberGenerator, Faction[] factionsAllowedInCityToInvade) {
        return randomNumberGenerator.nextInt(factionsAllowedInCityToInvade.length);
    }

    private void addBarbariansToCityToInvade(City cityToInvade, Random randomNumberGenerator, int amountOfBarbarianToInvadeCity) {
        cityToInvade.addBarbariansToCity(getFactionTypeOfAllowedFaction(cityToInvade, randomNumberGenerator), amountOfBarbarianToInvadeCity);
    }

    private void invadeCities() {
        int amountOfCitiesToInvade = 20;
        Random randomNumberGenerator = createRandomNumberGenerator();
        City[] cities = getCities();

        for (int i = 0; i < amountOfCitiesToInvade; i++) {
            City cityToInvade = determineWhatCityToInvade(cities, randomNumberGenerator);

            if (cityHasAlreadyBeenInvaded(cityToInvade) || cityToInvadeIsRome(cityToInvade)) {
                i--;
                continue;
            }

            int amountOfBarbarianToInvadeCity = determineAmountOfBarbariansToInvade(randomNumberGenerator);

            if (moreThenTwoBarbariansInCityToInvade(cityToInvade)) {
                addCityToInvadedCityList(cityToInvade);
                amountOfBarbarianToInvadeCity = 2;
                i--;
            }

            addBarbariansToCityToInvade(cityToInvade, randomNumberGenerator, amountOfBarbarianToInvadeCity);
        }
    }

    private Game() {
        playerCardDeck = createPlayerCardDeck();
        mergePlayerCardAndEventCardDecks(playerCardDeck);
        shuffleDeck(playerCardDeck);

        invasionCardDeck = createInvasionCardDeck();
        shuffleDeck(invasionCardDeck);

        // Create new dice instances
        for (int i = 0; i < die.length; i++) die[i] = new Dice();

        // Add barbarians to cities
        invadeCities();
    }

    public Game(Board gameBoard, List<Player> playersInCurrentGame, List<Faction> friendlyFactions, int decayLevel, int invasionLevel,
                Deck tradeDeck, Deck invasionDeck, Deck cityDeck, Deck invasionDiscardPile, Deck cityDiscardPile, boolean gameLost, boolean gameWon) {
        this.gameBoard = gameBoard;
        this.playersInCurrentGame = playersInCurrentGame;
        this.friendlyFactions = friendlyFactions;
        this.decayLevel = decayLevel;
        this.invasionLevel = invasionLevel;
        this.tradeCardDeck = tradeDeck;
        this.invasionCardDeck = invasionDeck;
        this.playerCardDeck = cityDeck;
        this.invasionCardDiscardPile = invasionDiscardPile;
        this.cityCardDiscardPile = cityDiscardPile;
        this.gameLost = gameLost;
        this.gameWon = gameWon;

        // Create new dice instances
        for (int i = 0; i < die.length; i++) {
            die[i] = new Dice();
        }
    }

    public String getGameCode() {
        return gameCode;
    }

    public void setGameCode(String code) {
        this.gameCode = code;
    }

    private void removeAllPlayersFromCurrentGame() {
        playersInCurrentGame.clear();
    }

    private boolean playerEqualsLocalPlayer(Player player, Player localPlayer) {
        return player.equals(localPlayer);
    }

    private PlayerHand getPlayerHand(Player player) {
        return player.getPlayerDeck();
    }

    private void addCardsToPlayerHand(Player player, Player localPlayer) {
        getPlayerHand(player).addCardsToPlayerHand(parsePlayerHandCardsToArray(localPlayer));
    }

    private List<Card> getPlayerHandCards(Player player) {
       return getPlayerHand(player).getPlayerHandCards();
    }

    private Card[] parsePlayerHandCardsToArray(Player player) {
        return getPlayerHandCards(player).toArray(new Card[0]);
    }

    private void updatePlayers(Game fireBaseGame, Player localPlayer) {
        for (Player player: fireBaseGame.getAllPlayers()) {
            addPlayersToCurrentGame(player);
            if (playerEqualsLocalPlayer(player, localPlayer)) {
                addCardsToPlayerHand(player, localPlayer);
                setLocalPlayer(player);
            }
        }
    }

    private void updateGameVariables(Game fireBaseGame) {
        gameBoard.updateBoard(fireBaseGame.gameBoard);
        decayLevel = fireBaseGame.decayLevel;
        invasionLevel = fireBaseGame.invasionLevel;
        invasionCardDeck = fireBaseGame.invasionCardDeck;
        invasionCardDiscardPile = fireBaseGame.invasionCardDiscardPile;
        playerCardDeck = fireBaseGame.playerCardDeck;
        cityCardDiscardPile = fireBaseGame.cityCardDiscardPile;
        tradeCardDeck = fireBaseGame.tradeCardDeck;
        friendlyFactions = fireBaseGame.friendlyFactions;
    }
    /**
     * Update the local game with the data from the fireBaseGame version
     * @param fireBaseGame The fireBaseGame version of the game
     */
    public void updateCurrentGame(Game fireBaseGame) {
        Player localPlayer = getLocalPlayer();
        removeAllPlayersFromCurrentGame();
        updatePlayers(fireBaseGame, localPlayer);
        updateGameVariables(fireBaseGame);
        notifyObservers();
    }

    private void addPlayerToCurrentGame(Player player) {
        this.playersInCurrentGame.add(player);
    }

    private void registerPlayerObserver(Player player) {
        player.registerObserver(this);
    }

    public void addPlayersToCurrentGame(Player... players) {
        for (Player player: players) {
            addPlayerToCurrentGame(player);
            registerPlayerObserver(player);
        }
    }

    private boolean noPlayersInGame() {
        return playersInCurrentGame.size() <= 0;
    }

    private void setTurn(Player nextPlayer) {
        nextPlayer.setTurn();
    }
    private void removeIsTurn(Player playerFromCurrentTurn) {
        playerFromCurrentTurn.removeIsTurn();
    }

    private boolean notAllPlayersHaveHadATurn(int indexOfPlayerFromCurrentTurn) {
        return playersInCurrentGame.size() > indexOfPlayerFromCurrentTurn + 1;
    }

    private Player giveTurnToNextPlayer(int indexOfPlayerFromCurrentTurn) {
        return playersInCurrentGame.get(indexOfPlayerFromCurrentTurn + 1);
    }

    private Player giveTurnToFirstPlayer() {
        return playersInCurrentGame.get(0);
    }

    public void nextTurn() {
        if (noPlayersInGame()) return;

        // Get current turn player
        Player playerFromCurrentTurn = getPlayerFromCurrentTurn();
        Player nextPlayer;

        int indexOfPlayerFromCurrentTurn = playersInCurrentGame.indexOf(playerFromCurrentTurn);

        if (notAllPlayersHaveHadATurn(indexOfPlayerFromCurrentTurn)) {
            nextPlayer = giveTurnToNextPlayer(indexOfPlayerFromCurrentTurn);
        } else nextPlayer = giveTurnToFirstPlayer();

        removeIsTurn(playerFromCurrentTurn);
        setTurn(nextPlayer);

        notifyObservers();
    }

    private Player createPlayer(LobbyPlayer lobbyPlayer) {
        return new Player(lobbyPlayer);
    }

    /**
     * Adds LobbyPlayers to game
     * @param lobbyPlayers
     */
    public void addPlayersToCurrentGame(LobbyPlayer... lobbyPlayers) {
        for (LobbyPlayer lobbyPlayer: lobbyPlayers) {
            Player player = createPlayer(lobbyPlayer);
            registerPlayerObserver(player);
            addPlayerToCurrentGame(player);
        }
        notifyObservers();
    }

    /**
     * @return board object
     */
    public Board getGameBoard() {
        return gameBoard;
    }

    /**
     * @return All players in game
     */
    public List<Player> getAllPlayers() {
        return playersInCurrentGame;
    }

    private boolean playerHasTurn(Player player) {
        return player.isCurrentTurn();
    }

    public Player getPlayerFromCurrentTurn() {
        for (Player player: playersInCurrentGame) {
            if (playerHasTurn(player)) return player;
        }
        return null;
    }

    /**
     * @return invasion deck
     */
    public Deck getInvasionCardDeck() {
        return invasionCardDeck;
    }

    public int getInvasionLevel() {
        return invasionLevel;
    }

    public Deck getTradeCardDeck() {
    	return tradeCardDeck;
    }

    /**
     * @return decay level
     */
    public int getDecayLevel() {
        return decayLevel;
    }
    
   public int getMaxDecayLevel() {
       return MAX_DECAY_LEVEL;
   }

    /**
     * Increase decay level, when decay level. When reached the max, return.
     * @param amount
     */
    private boolean maxDecayLevelHasBeenReached(int amount) {
        return decayLevel + amount >= MAX_DECAY_LEVEL;
    }

    private void incrementDecayLevel(int amount) {
        decayLevel += amount;
    }

    public void increaseDecayLevel(int amount) {
        if (maxDecayLevelHasBeenReached(amount)) return;

        incrementDecayLevel(amount);
        notifyObservers();
    }

    public Deck getPlayerCardDeck() {
        return playerCardDeck;
    }

    public Deck getInvasionCardDiscardPile() {
        return invasionCardDiscardPile;
    }

    public Deck getCityCardDiscardPile() {
        return cityCardDiscardPile;
    }

    public Dice[] getDie() {
        return die;
    }

    public boolean isFriendlyFaction(Faction faction) {
        return friendlyFactions.contains(faction);
    }

    private boolean playerIsLocalPlayer(Player player) {
        return player.isLocalPlayer();
    }

    public Player getLocalPlayer() {
        for (Player player: playersInCurrentGame) {
            if (playerIsLocalPlayer(player)) return player;
        }
        return null;
    }
    
    public void setLocalPlayer(Player player) {
        for (Player p : getAllPlayers()) if (player.equals(p)) p.setLocal(true);
    }

    @Override
    public void update() {
        notifyObservers();
    }

    private boolean factionIsFriendly(Faction faction) {
        return friendlyFactions.contains(faction);
    }

    private void allieFaction(Faction faction) {
        friendlyFactions.add(faction);
    }

    public void addFriendlyFaction(Faction faction) {
        if (!factionIsFriendly(faction)) {
            allieFaction(faction);
            notifyObservers();
        }
    }

    public List<Faction> getFriendlyFactions() {
        return friendlyFactions;
    }

    public boolean isGameWon() {
        return gameWon;
    }

    public boolean isGameLost() {
        return gameLost;
    }

    public void setGameWon(boolean gameWon) {
        this.gameWon = gameWon;
    }

    public void setGameLost(boolean gameLost) {
        this.gameLost = gameLost;
    }
}
