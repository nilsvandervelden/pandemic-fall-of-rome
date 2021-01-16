package com.groep6.pfor.controllers;

import com.groep6.pfor.Config;
import com.groep6.pfor.models.*;
import com.groep6.pfor.services.GameService;
import com.groep6.pfor.services.LobbyService;
import com.groep6.pfor.util.IEventCallback;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.LobbyView;
import javafx.application.Platform;

import java.util.Collections;
import java.util.List;

public class LobbyController extends Controller {

    public static final int MIN_PLAYERS = 0;

    private final Game game = Game.getInstance();
    private final Lobby currentLobby;

    private void subscribeToLobbyChangeEvent() {
        LobbyService.lobbyChangeEvent.subscribe(onLobbyChange);
    }

    private void displayLobbyView() {
        viewController.showView(new LobbyView(this));
    }

    private Game getCurrentGame() {
        return Game.getInstance();
    }

    private boolean noLocalPlayer(Game currentGame) {
        return currentGame.getLocalPlayer() != null;
    }

    private boolean isHost(Game currentGame) {
        return currentGame.getLocalPlayer().isHost();
    }

    private  void addPlayersToCurrentGame(Game currentGame) {
        currentGame.addPlayersToCurrentGame(currentLobby.getLocalPlayer());
    }

    private void setGameCode(Game currentGame) {
        currentGame.setGameCode(currentLobby.getCode());
    }

    private void setGameState() {
        Game.setGameState(GameState.GAME);
    }

    private void subscribeToGameChangeEvent() {
        GameService.gameChangeEvent.subscribe(onGameChange);
    }

    private void subscribeToGameStartEvent(IEventCallback gameStartEvent) {
        LobbyService.gameStartEvent.subscribe(gameStartEvent);
    }

    private void goToBoardView() {
        Platform.runLater(BoardController::new);
    }

    private IEventCallback gameStartEvent() {
        return eventData -> {
            Game currentGame = getCurrentGame();
            if (noLocalPlayer(currentGame) && isHost(currentGame)) return;

            addPlayersToCurrentGame(currentGame);
            setGameCode(currentGame);
            onGameChange.onEvent(eventData);
            setGameState();
            subscribeToGameChangeEvent();
            goToBoardView();
        };
    }

    /**
     * @param currentLobby
     */
    public LobbyController(Lobby currentLobby) {
        this.currentLobby = currentLobby;
        subscribeToLobbyChangeEvent();
        displayLobbyView();

        IEventCallback gameStartEvent = gameStartEvent();

        subscribeToGameStartEvent(gameStartEvent);
    }

    private boolean inDebugMode() {
        return Config.DEBUG;
    }

    private void updatedCurrentGame(Game currentGame) {
        Game.getInstance().updateGame(currentGame);
    }

    private void displayLostGameView() {
        Platform.runLater(LoseGameController::new);
    }

    private void displayWonGameView() {

    }

    private boolean gameHasBeenLost(Game currentGame) {
        return currentGame.isLost();
    }

    private boolean gameHasBeenWon(Game currentGame) {
        return currentGame.isWon();
    }

    private final IEventCallback onGameChange = eventData -> {
        if (inDebugMode()) System.out.println("Server update...");
        Game currentGame = (Game) eventData[0];
        updatedCurrentGame(currentGame);

        if (gameHasBeenLost(currentGame)) displayLostGameView();
        else if (gameHasBeenWon(currentGame)) displayWonGameView();
    };

    /**
     * @return lobby code
     */
    public String getLobbyCode() {
        return currentLobby.getCode();
    }

    /**
     * @return list of current lobbyPlayers in the lobby
     */
    public List<LobbyPlayer> getLobbyPlayers() {
        return currentLobby.getPlayers();
    }

    /**
     * Go to a new view: roleCardInfoView
     */
    public void goToRoleCardInfoView() {
        new RoleCardInfoController(currentLobby);
    }

    public void removeLocalPlayerFromCurrentLobby(LobbyService lobbyService, LobbyPlayer localPlayer) {
        currentLobby.removePlayerFromCurrentLobby(localPlayer);
        lobbyService.removePlayerFromCurrentLobby(localPlayer);
    }

    private boolean localPlayerIsHost(LobbyPlayer localPlayer) {
        return localPlayer.isHost();
    }

    private boolean lobbyIsEmpty() {
        return currentLobby.getPlayers().size() == 0;
    }

    private void removeLobby(LobbyService lobbyService) {
        lobbyService.remove(currentLobby);
    }

    private void setLobbyHost(LobbyService lobbyService) {
        lobbyService.setHost(currentLobby.getPlayers().get(0));
    }

    /**
     * Go to new view: MenuView
     */
    public void goToMainMenu() {
        // Delete from lobby
        LobbyService lobbyService = new LobbyService();
        LobbyPlayer localPlayer = getLocalPlayer();
        removeLocalPlayerFromCurrentLobby(lobbyService, localPlayer);

        if (localPlayerIsHost(localPlayer)) {
            if (lobbyIsEmpty()) {
                removeLobby(lobbyService);
            } else {
                setLobbyHost(lobbyService);
            }
        }
        showPreviousView();
        showPreviousView();
    }

    public void startGame() {
        List<LobbyPlayer> playersInCurrentLobby = currentLobby.getPlayers();
        Collections.shuffle(playersInCurrentLobby);

        game.setGameCode(getLobbyCode());
        game.addPlayersToCurrentGame(playersInCurrentLobby.toArray(new LobbyPlayer[0]));
        game.getAllPlayers().get(0).setTurn();

        GameService gameService = new GameService();
        LobbyService lobbyService = new LobbyService();

        gameService.create(game);
        GameService.gameChangeEvent.subscribe(onGameChange);

        Game.setGameState(GameState.GAME);
        new BoardController();
        lobbyService.remove(currentLobby);
    }

    /**
     * @return local lobbyPlayer
     */
    public LobbyPlayer getLocalPlayer() {
        return currentLobby.getLocalPlayer();
    }

    /**
     * @return Host of the lobby
     */
    public LobbyPlayer getHost() {
        return currentLobby.getHost();
    }

    @Override
    public void registerObserver(IObserver view) {
        currentLobby.registerObserver(view);
    }


    /**
     * Run code every time the server sends an update
     */
    private final IEventCallback onLobbyChange = new IEventCallback() {
        @Override
        public void onEvent(Object... eventData) {
            Lobby serverLobby = (Lobby) eventData[0];

            if (!serverLobby.equals(currentLobby)) return;

            currentLobby.updateLobby(serverLobby);
        }
    };
}
