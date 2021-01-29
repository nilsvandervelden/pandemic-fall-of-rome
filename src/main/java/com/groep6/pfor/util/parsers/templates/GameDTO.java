package com.groep6.pfor.util.parsers.templates;

import com.groep6.pfor.factories.FactionFactory;
import com.groep6.pfor.models.Deck;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.Player;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.models.factions.FactionType;

import java.util.*;

/**
 * The Data Transfer Object that represents the state of a game in Firebase
 *
 * @author Owen ELderbroek
 */
public class GameDTO {
    public BoardDTO board;
    public Map<String, PlayerDTO> players;
    public int decayLevel;
    public int invasionLevel;
    public List<CardDTO> tradeDeck;
    public List<CardDTO> invasionDeck;
    public List<CardDTO> cityDeck;
    public List<CardDTO> invasionDiscardPile;
    public List<CardDTO> cityDiscardPile;
    public List<String> friendlyFactions;
    public Date updateTime;
    public boolean lost = false;
    public boolean won = false;

    private void addPlayersToGame(List<Player> playersInGame) {
        for (PlayerDTO player : this.players.values()) {
            playersInGame.add(player.toModel());
        }
    }

    private void addFactionsToGame(List<Faction> factionsInGame) {
        for (String faction : this.friendlyFactions) {
            factionsInGame.add(FactionFactory.getInstance().getFaction(FactionType.valueOf(faction)));
        }
    }

    public Game parseToModel() {
        List<Player> playersInGame = new ArrayList<>();

        addPlayersToGame(playersInGame);

        List<Faction> factionsInGame = new ArrayList<>();

        addFactionsToGame(factionsInGame);

        return new Game(board.toModel(), playersInGame, factionsInGame, decayLevel, invasionLevel, createDeck(tradeDeck), createDeck(invasionDeck), createDeck(cityDeck), createDeck(invasionDiscardPile), createDeck(cityDiscardPile), lost, won);
    }

    private static Map<String, PlayerDTO> addPlayersToGame(Game currentGame) {
        Map<String, PlayerDTO> playersInCurrentGame = new HashMap<>();
        for (Player player : currentGame.getAllPlayers()) {
            playersInCurrentGame.put(player.getUsername(), PlayerDTO.fromModel(player));
        }
        return playersInCurrentGame;
    }

    private static List<String> addFactionsToGame(Game currentGame) {
        List<String> factions = new ArrayList<>();
        for (Faction faction : currentGame.getFriendlyFactions()) {
            factions.add(faction.getFactionType().toString());
        }
        return factions;
    }

    public static GameDTO parseFromModel(Game currentGame) {
        BoardDTO currentBoard = BoardDTO.fromModel(currentGame.getGameBoard());

        Map<String, PlayerDTO> playersInCurrentGame = addPlayersToGame(currentGame);
        List<String> factions = addFactionsToGame(currentGame);

        return new GameDTO(currentBoard, playersInCurrentGame, factions, currentGame.getDecayLevel(), currentGame.getInvasionLevel(),
                createList(currentGame.getTradeCardDeck()), createList(currentGame.getInvasionCardDeck()), createList(currentGame.getPlayerCardDeck()),
                createList(currentGame.getInvasionCardDiscardPile()), createList(currentGame.getCityCardDiscardPile()), currentGame.isGameLost(), currentGame.isGameWon());
    }

    public GameDTO() {}

    private GameDTO(BoardDTO board, Map<String, PlayerDTO> players, List<String> friendlyFactions, int decayLevel, int invasionLevel,
                List<CardDTO> tradeDeck, List<CardDTO> invasionDeck, List<CardDTO> cityDeck, List<CardDTO> invasionDiscardPile, List<CardDTO> cityDiscardPile, boolean lost, boolean won) {
        this.board = board;
        this.players = players;
        this.friendlyFactions = friendlyFactions;
        this.decayLevel = decayLevel;
        this.invasionLevel = invasionLevel;
        this.tradeDeck = tradeDeck;
        this.invasionDeck = invasionDeck;
        this.cityDeck = cityDeck;
        this.invasionDiscardPile = invasionDiscardPile;
        this.cityDiscardPile = cityDiscardPile;
        this.updateTime = new Date();
        this.lost = lost;
        this.won = won;
    }

    private Deck createDeck(List<CardDTO> cards) {
        Card[] deckCards = new Card[cards.size()];
        for (int i = 0; i < cards.size(); i++) deckCards[i] = cards.get(i).parseToModel();
        return new Deck(deckCards);
    }

    private static List<CardDTO> createList(Deck deck) {
        List<CardDTO> list = new ArrayList<>();
        for (Card card : deck.getCards()) list.add(CardDTO.parseFromModel(card));
        return list;
    }
}
