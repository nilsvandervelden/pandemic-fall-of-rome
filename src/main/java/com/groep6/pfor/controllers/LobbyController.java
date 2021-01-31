package com.groep6.pfor.controllers;

import com.groep6.pfor.Config;
import com.groep6.pfor.exceptions.CouldNotFindHostException;
import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.models.*;
import com.groep6.pfor.services.GameService;
import com.groep6.pfor.services.LobbyService;
import com.groep6.pfor.util.IEventCallback;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.LobbyView;
import javafx.application.Platform;

import java.util.Collections;
import java.util.List;

/**
 * Controller for the lobby. This controller handles everything to do with a lobby.
 * Things like starting a game and picking a role are handled here.
 * @author Nils van der Velden
 *
 */

public class LobbyController extends Controller {

    private final Game currentGame = Game.getGameInstance();
    private final Lobby currentLobby;

    private void subscribeToLobbyChangeEvent() {
        LobbyService.lobbyChangeEvent.subscribe(onLobbyChange);
    }

    private void displayLobbyView() {
        viewController.showView(new LobbyView(this));
    }

    private Game getCurrentGame() {
        return Game.getGameInstance();
    }

    private boolean noLocalPlayer(Game currentGame) throws CouldNotFindLocalPlayerException {
        return currentGame.getLocalPlayer() != null;
    }

    private  void addPlayersToCurrentGame(Game currentGame) {
        try {
            currentGame.addPlayersToCurrentGame(currentLobby.getLocalPlayer());
        } catch (CouldNotFindLocalPlayerException e) {
            e.printStackTrace();
        }
    }

    private void setGameCode(Game currentGame) {
        currentGame.setGameCode(currentLobby.getGameCode());
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
        Platform.runLater(() -> {
            try {
                new BoardController();
            } catch (CouldNotFindLocalPlayerException e) {
                e.printStackTrace();
            }
        });
    }

    private IEventCallback gameStartEvent() {
        return eventData -> {
            Game currentGame = getCurrentGame();
            try {
                if (noLocalPlayer(currentGame)) return;
            } catch (CouldNotFindLocalPlayerException e) {
                e.printStackTrace();
            }

            addPlayersToCurrentGame(currentGame);
            setGameCode(currentGame);
            onGameChange.onEvent(eventData);
            setGameState();
            subscribeToGameChangeEvent();
            goToBoardView();
        };
    }

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
        try {
            Game.getGameInstance().updateCurrentGame(currentGame);
        } catch (CouldNotFindLocalPlayerException e) {
            e.printStackTrace();
        }
    }

    private void displayLostGameView() {
        Platform.runLater(LoseGameController::new);
    }

    private void displayWonGameView() {
        Platform.runLater(WinGameController::new);
    }

    private boolean gameHasBeenLost(Game currentGame) {
        return currentGame.isGameLost();
    }

    private boolean gameHasBeenWon(Game currentGame) {
        return currentGame.isGameWon();
    }

    private final IEventCallback onGameChange = eventData -> {
        if (inDebugMode()) System.out.println("Server update...");
        Game currentGame = (Game) eventData[0];
        updatedCurrentGame(currentGame);

        if (gameHasBeenLost(currentGame)) displayLostGameView();
        else if (gameHasBeenWon(currentGame)) displayWonGameView();
    };

    public String getLobbyCode() {
        return currentLobby.getGameCode();
    }

    public List<LobbyPlayer> getPlayersFromCurrentLobby() {
        return currentLobby.getPlayers();
    }

    public void goToRoleCardInfoView() {
        new RoleCardController(currentLobby);
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

    public void goToMainMenu() throws CouldNotFindLocalPlayerException {
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

    private void setGameCode() {
        currentGame.setGameCode(getLobbyCode());
    }

    private void addPlayersToCurrentGame(List<LobbyPlayer> playersInCurrentLobby) {
        currentGame.addPlayersToCurrentGame(playersInCurrentLobby.toArray(new LobbyPlayer[0]));
    }

    private void shufflePlayersInCurrentLobby( List<LobbyPlayer> playersInCurrentLobby) {
        Collections.shuffle(playersInCurrentLobby);
    }

    private void giveTurnToFirstPlayerInGame() {
        currentGame.getAllPlayers().get(0).setTurn();
    }

    private GameService startGameService() {
        return new GameService();
    }

    private LobbyService startLobbyService() {
        return new LobbyService();
    }

    private void createGame(GameService gameService) {
        gameService.create(currentGame);
    }

    private void displayBoardView() {
        try {
            new BoardController();
        } catch (CouldNotFindLocalPlayerException e) {
            e.printStackTrace();
        }
    }

    private void deleteLobby(LobbyService lobbyService) {
        lobbyService.remove(currentLobby);
    }

    public void startGame() {
        List<LobbyPlayer> playersInCurrentLobby = getPlayersFromCurrentLobby();
        shufflePlayersInCurrentLobby(playersInCurrentLobby);

        setGameCode();
        addPlayersToCurrentGame(playersInCurrentLobby);
        giveTurnToFirstPlayerInGame();

        GameService gameService = startGameService();
        LobbyService lobbyService = startLobbyService();

        createGame(gameService);
        GameService.gameChangeEvent.subscribe(onGameChange);

        setGameState();
        displayBoardView();
        deleteLobby(lobbyService);
    }

    public LobbyPlayer getLocalPlayer() throws CouldNotFindLocalPlayerException {
        return currentLobby.getLocalPlayer();
    }

    public LobbyPlayer getHost() throws CouldNotFindHostException {
        return currentLobby.getHost();
    }

    @Override
    public void registerObserver(IObserver view) {
        currentLobby.registerObserver(view);
    }

    private boolean serverLobbyEqualsCurrentLobby(Lobby serverLobby) {
        return serverLobby.equals(currentLobby);
    }

    private  void updateCurrentLobby (Lobby serverLobby) {
        try {
            currentLobby.updateLobby(serverLobby);
        } catch (CouldNotFindLocalPlayerException e) {
            e.printStackTrace();
        }
    }

    private final IEventCallback onLobbyChange = new IEventCallback() {
        @Override
        public void onEvent(Object... eventData) {
            Lobby serverLobby = (Lobby) eventData[0];

            if (!serverLobbyEqualsCurrentLobby(serverLobby)) return;

            updateCurrentLobby(serverLobby);
        }
    };
}
