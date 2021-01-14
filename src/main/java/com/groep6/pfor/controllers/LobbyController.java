package com.groep6.pfor.controllers;

import com.groep6.pfor.Config;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.GameState;
import com.groep6.pfor.models.Lobby;
import com.groep6.pfor.models.LobbyPlayer;
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
    private final Lobby currentLoby;

    /**
     * @param currentLoby
     */
    public LobbyController(Lobby currentLoby) {
        this.currentLoby = currentLoby;
        LobbyService.lobbyChangeEvent.subscribe(onLobbyChange);
        viewController.showView(new LobbyView(this));

        IEventCallback gameStartEvent = eventData -> {
            Game currentGame = Game.getInstance();
            if (currentGame.getLocalPlayer() != null && currentGame.getLocalPlayer().isHost()) return;

            currentGame.addPlayersToCurrentGame(currentLoby.getLocalPlayer());
            currentGame.setGameCode(currentLoby.getCode());
            onGameChange.onEvent(eventData);
            Game.setGameState(GameState.GAME);
            GameService.gameChangeEvent.subscribe(onGameChange);
            Platform.runLater(BoardController::new);
        };
        LobbyService.gameStartEvent.subscribe(gameStartEvent);
    }

    private final IEventCallback onGameChange = eventData -> {
        if (Config.DEBUG) System.out.println("Server update...");
        Game currentGame = (Game) eventData[0];
        Game.getInstance().updateGame(currentGame);

        if (currentGame.isLost()) Platform.runLater(LoseController::new);
        else if (currentGame.isWon()) Platform.runLater(WinController::new);
    };

    /**
     * @return lobby code
     */
    public String getLobbyCode() {
        return currentLoby.getCode();
    }

    /**
     * @return list of current lobbyPlayers in the lobby
     */
    public List<LobbyPlayer> getLobbyPlayers() {
        return currentLoby.getPlayers();
    }

    /**
     * Go to a new view: roleCardInfoView
     */
    public void goToRoleCardInfoView() {
        new RoleCardInfoController(currentLoby);
    }

    /**
     * Go to new view: MenuView
     */
    public void goToMenu() {
        // Delete from lobby
        LobbyService lobbyService = new LobbyService();
        LobbyPlayer localPlayer = currentLoby.getLocalPlayer();
        currentLoby.removePlayerFromCurrentLobby(localPlayer);
        lobbyService.removePlayerFromCurrentLobby(localPlayer);

        if (localPlayer.isHost()) {
            boolean lobbyIsEmpty = currentLoby.getPlayers().size() == 0;

            if (lobbyIsEmpty) {
                lobbyService.remove(currentLoby);
            } else {
                lobbyService.setHost(currentLoby.getPlayers().get(0));
            }
        }

        showPreviousView();
        showPreviousView();
    }

    public void startGame() {
        List<LobbyPlayer> playersInCurrentLobby = currentLoby.getPlayers();
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
        lobbyService.remove(currentLoby);
    }

    /**
     * @return local lobbyPlayer
     */
    public LobbyPlayer getLocalPlayer() {
        return currentLoby.getLocalPlayer();
    }

    /**
     * @return Host of the lobby
     */
    public LobbyPlayer getHost() {
        return currentLoby.getHost();
    }

    @Override
    public void registerObserver(IObserver view) {
        currentLoby.registerObserver(view);
    }


    /**
     * Run code every time the server sends an update
     */
    private final IEventCallback onLobbyChange = new IEventCallback() {
        @Override
        public void onEvent(Object... eventData) {
            Lobby serverLobby = (Lobby) eventData[0];

            if (!serverLobby.equals(currentLoby)) return;

            currentLoby.updateLobby(serverLobby);
        }
    };
}
