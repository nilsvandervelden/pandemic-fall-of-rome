package com.groep6.pfor.controllers;

/**
 * Controller for joining a game. This controller handles joining a game
 * Things like joining a lobby are handled here.
 * @author Nils van der Velden
 *
 */

import com.groep6.pfor.exceptions.EmptyFieldException;
import com.groep6.pfor.exceptions.IncorrectPasswordException;
import com.groep6.pfor.exceptions.NoDocumentException;
import com.groep6.pfor.exceptions.UsernameAlreadyUsed;
import com.groep6.pfor.models.Lobby;
import com.groep6.pfor.models.LobbyPlayer;
import com.groep6.pfor.services.LobbyService;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.JoinView;

public class JoinGameController extends Controller {

    public JoinGameController() {
        viewController.showView(new JoinView(this));
    }

    public void checkIfUserNameIsEmpty(String username) throws EmptyFieldException {
        if (username.isEmpty()) throw new EmptyFieldException("Username cannot be empty");
    }

    public void checkIfLobbyCodeIsEmpty(String code) throws EmptyFieldException {
        if (code.isEmpty()) throw new EmptyFieldException("Lobby code cannot be empty");
    }

    private Lobby getLobby(LobbyService lobbyService, String code) throws NoDocumentException {
        return lobbyService.get(code);
    }

    private void checkIfUserNameHasAlreadyBeenTaken(Lobby lobby, String username) throws UsernameAlreadyUsed {
        for (LobbyPlayer player: lobby.getPlayers()) {
            if (player.getUsername().equals(username)) throw new UsernameAlreadyUsed();
        }
    }

    private LobbyPlayer createLobbyPlayer(Lobby lobby, String code, String username, String password, Boolean isLocal) throws IncorrectPasswordException {
       return lobby.join(code, username, password, isLocal);
    }

    private void addPlayerToLobby(LobbyService lobbyService, LobbyPlayer player) {
        lobbyService.join(player);
    }

    private void updateLobby(LobbyService lobbyService, LobbyPlayer player) {
        lobbyService.join(player);
    }

    private void refreshLobbyView(Lobby lobby) {
        new LobbyController(lobby);
    }
    
    public void joinLobby(String code, String username, String password) throws EmptyFieldException, UsernameAlreadyUsed {
        checkIfUserNameIsEmpty(username);
        checkIfLobbyCodeIsEmpty(code);
        
        try {
            LobbyService lobbyService = new LobbyService();
            Lobby lobby = getLobby(lobbyService, code);

            checkIfUserNameHasAlreadyBeenTaken(lobby, username);

            LobbyPlayer player = createLobbyPlayer(lobby, code, username, password, true);

            addPlayerToLobby(lobbyService, player);
            updateLobby(lobbyService, player);

            refreshLobbyView(lobby);
        	
        } catch (IncorrectPasswordException | NoDocumentException error) {
            System.out.println("Error: " + error.getMessage());
        } 
    }

    @Override
    public void registerObserver(IObserver view) {

    }
}
