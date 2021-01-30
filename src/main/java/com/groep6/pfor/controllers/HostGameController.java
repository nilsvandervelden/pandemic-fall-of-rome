package com.groep6.pfor.controllers;

import com.groep6.pfor.exceptions.EmptyFieldException;
import com.groep6.pfor.exceptions.IncorrentPasswordException;
import com.groep6.pfor.models.Lobby;
import com.groep6.pfor.services.LobbyService;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.HostView;

/**
 * Controller for hosting a game. This controller handles hosting a game
 * Things like creating a lobby are handled here.
 * @author Nils van der Velden
 *
 */

public class HostGameController extends Controller {

    public HostGameController() {
        viewController.showView(new HostView(this));
    }

    public void checkIfUserNameIsEmpty(String username) throws EmptyFieldException {
        if (username.isEmpty()) throw new EmptyFieldException("Username cannot be empty");
    }

    public void addHostToCreatedLobby(Lobby lobby, String username, String password, Boolean isLocal) throws IncorrentPasswordException {
        lobby.join(lobby.getCode(), username, password, true);
    }

    public void createLobbyInBackend(Lobby lobby) {
        LobbyService lobbyService = new LobbyService();
        lobbyService.create(lobby);
    }

    public void changeViewToHostedLobby(Lobby lobby) {
        new LobbyController(lobby);
    }

    public void createLobby(String username, String password) throws EmptyFieldException {
        checkIfUserNameIsEmpty(username);

        // Create new lobby
        Lobby lobby = new Lobby(password);

        try {
            addHostToCreatedLobby(lobby, username, password, true);

            // Send to lobby service
            createLobbyInBackend(lobby);

            // Send user to lobby
            changeViewToHostedLobby(lobby);

        } catch (IncorrentPasswordException error) {
            System.out.println("Error: " + error.getMessage());
        }
    }

    @Override
    public void registerObserver(IObserver view) {

    }
}
