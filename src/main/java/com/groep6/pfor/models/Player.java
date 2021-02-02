package com.groep6.pfor.models;

import com.groep6.pfor.exceptions.CardNotInDeckException;
import com.groep6.pfor.factories.CityFactory;
import com.groep6.pfor.factories.FactionFactory;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.models.cards.CityCard;
import com.groep6.pfor.models.cards.RoleCard;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.services.GameService;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.Observable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * @author Bastiaan Jansen
 */
public class Player extends Observable implements IObserver {

    private final PlayerHand playerDeck = new PlayerHand();
    private final RoleCard playerRole;
    private City city;
    private final String username;
    private boolean isTurn = false;
    private int actionsRemaining = 4;
    private boolean isLocal;

    private void initialisePlayerDeck(Game currentGame) {
        int amountOfCardsToDraw = 3;
        for (int i = 0; i < amountOfCardsToDraw; i++) {
            try {
                playerDeck.addCardsToPlayerHand(drawCardFromDeck(currentGame));
            } catch (CardNotInDeckException e) {
                e.printStackTrace();
            }
        }
    }

    private Deck getPlayerCardDeck(Game currentGame) {
        return currentGame.getPlayerCardDeck();
    }

    private Card drawCardFromDeck(Game currentGame) throws CardNotInDeckException {
        return getPlayerCardDeck(currentGame).drawCardFromDeck();
    }

    private CityFactory getCityFactory() {
        return CityFactory.getInstance();
    }

    private List<City> getCitiesFromCityFactory() {
        CityFactory cityFactory = getCityFactory();
        return Arrays.asList(cityFactory.getAllCities());
    }

    private City selectRandomCity() {
        Random randomNumberGenerator = new Random();
        List<City> cities = getCitiesFromCityFactory();
        return city = cities.get(randomNumberGenerator.nextInt(cities.size() - 1));
    }

    private void addLegionsToCity(City city, int amountOfLegions) {
        city.addLegionsToCurrentCity(amountOfLegions);
    }

    public Player(LobbyPlayer player) {
        playerRole = player.getRoleCard();
        username = player.getUsername();
        isLocal = player.isLocal();

        Game currentGame = Game.getGameInstance();

        initialisePlayerDeck(currentGame);

        // Selects the city the players starts in.
        city = selectRandomCity();

        city.registerObserver(this);

        // Add two legions to start city
        addLegionsToCity(city, 2);
    }

    public Player(String username, City city, RoleCard playerRole, boolean isTurn, boolean isLocal, int actionsRemaining) {
        this.playerRole = playerRole;
        this.city = city;
        this.username = username;
        this.isLocal = isLocal;
        this.actionsRemaining = actionsRemaining;
        this.isTurn = isTurn;
    }

    public boolean isCurrentTurn() {
        return isTurn;
    }

    /**
     * Set the player turn to true
     * The player can now perform 4 actions
     */
    public void setTurn() {
        isTurn = true;
        actionsRemaining = 4;
    }

    public int getActionsRemaining() {
        return actionsRemaining;
    }

    private boolean hasActionsLeft() {
        return actionsRemaining > 0;
    }

    private void decrementActionsRemaining() {
        actionsRemaining--;
    }

    private GameService createGameService() {
        return new GameService();
    }

    private void setGameService() {
        GameService gameService = createGameService();
        gameService.setGame(Game.getGameInstance());
    }

    public void decreaseAmountOfActionsRemaining() {
        if (!hasActionsLeft()) return;
        decrementActionsRemaining();
        notifyObservers();

        // Sync with server
        setGameService();
    }

    public PlayerHand getPlayerDeck() {
        return playerDeck;
    }

    public RoleCard getPlayerRole() {
        return playerRole;
    }

    public String getUsername() {
        return username;
    }

    private boolean lessThenThreeLegionsInCity(List<Legion> legionsBeforeBattle) {
        return legionsBeforeBattle.size() <= 3;
    }

    private boolean legionsInCity(List<Legion> legionsBeforeBattle) {
        return legionsBeforeBattle.size() > 0;
    }

    private List<Legion> getLegionsInCity() {
        return city.getLegionsInCity();
    }

    private int decideAmountOfDiceToRole(List<Legion> legionsBeforeBattle) {
        if (lessThenThreeLegionsInCity(legionsBeforeBattle) && legionsInCity(legionsBeforeBattle)) {
            return legionsBeforeBattle.size();
        }
        return 3;
    }

    private DiceFace[] determineBattleOutcome() {
        Dice dice = new Dice();
        List<Legion> legionsBeforeBattle = getLegionsInCity();

        // Decide amount of dice to roll.
        int amountOfDice = decideAmountOfDiceToRole(legionsBeforeBattle);

        DiceFace[] diceFaces = new DiceFace[amountOfDice];
        for (int i = 0; i < amountOfDice; i++) {
            diceFaces[i] = dice.determineBattleOutcome(city);
        }
        return diceFaces;
    }

    public DiceFace[] fightBattle() {

        DiceFace[] diceFaces = determineBattleOutcome();

        decreaseAmountOfActionsRemaining();

        return diceFaces;
    }
    
    public void moveLocalPlayerToSelectedCity(City city) {
    	if (!this.city.neighbouringCities.contains(city)) return;
        this.city = city;

    	decreaseAmountOfActionsRemaining();
    	notifyObservers();
    }

    public void addActions(int amount) {
        actionsRemaining += amount;
        notifyObservers();

        GameService gameService = new GameService();
        gameService.setGame(Game.getGameInstance());
    }

    public void setLocal(boolean local) {
        this.isLocal = local;
    }

    public City getCityPlayerIsCurrentlyLocatedIn() {
        return city;
    }

    public void removeIsTurn() {
        isTurn = false;
    }

    public boolean isLocalPlayer() {
        return isLocal;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Player)) return false;
        return ((Player) o).username.equals(username);
    }

    public void update() {
        notifyObservers();
    }

    private FactionFactory getFactionFactory() {
        return FactionFactory.getInstance();
    }

    private List<Faction> getFactions() {
        FactionFactory factionFactory = getFactionFactory();
        return factionFactory.getFactions();
    }

    private List<Card> getCardsInPlayerDeck() {
        return getPlayerDeck().getPlayerHandCards();
    }

    private boolean factionCanEnterCity(Faction faction) {
        return city.requestedFactionInCity(faction);
    }

    private boolean cardInstanceOfCityCard(Card card) {
        return card instanceof CityCard;
    }

    private boolean cityCardBelongsToRequestedFaction(Card card, Faction faction) {
        return ((CityCard) card).getFactionCityCardBelongsTo().equals(faction);
    }

    private boolean enoughCardsToFormAlliance(int cardCount, Faction faction) {
        return cardCount >= faction.getRequiredAmountOfCardsToFormAAlliance();
    }

    /**
     * Get list of factions that an alliance can be formed with.
     */
    public List<Faction> getAvailableAlliances() {
        List<Faction> factions = getFactions();
        List<Card> cardsInPlayerDeck = getCardsInPlayerDeck();
        List<Faction> availableAlliances = new ArrayList<>();

        // Loop through all factions
        for (Faction faction : factions) {
            // If current city does not have this faction, continue
            if (!factionCanEnterCity(faction)) continue;

            int cardCount = 0;

            // Check if player has required card count to form alliance
            for (Card card : cardsInPlayerDeck) {
                if (cardInstanceOfCityCard(card) && cityCardBelongsToRequestedFaction(card, faction)) {
                    cardCount++;
                }
            }
            if (enoughCardsToFormAlliance(cardCount, faction)) {
                availableAlliances.add(faction);
            }
        }
        return availableAlliances;
    }

    /**
     * Get the cityCards that this player holds with the type of faction that you give as parameter
     * @param faction The faction you want the cards from
     * @return A list of cards
     */
    public List<Card> getCityCardsOfAlliedFaction(Faction faction) {
        List<Card> cardsInPlayerDeck = getCardsInPlayerDeck();
        List<Card> factionCards = new ArrayList<>();
        for (Card card : cardsInPlayerDeck) {
            if (cardInstanceOfCityCard(card) && cityCardBelongsToRequestedFaction(card, faction)) {
                factionCards.add(card);
            }
        }
        return factionCards;
    }
}
