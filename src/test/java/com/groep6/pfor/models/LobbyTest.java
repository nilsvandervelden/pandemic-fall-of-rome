package com.groep6.pfor.models;

import com.groep6.pfor.exceptions.CouldNotFindHostException;
import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.exceptions.IncorrectPasswordException;
import com.groep6.pfor.models.cards.RoleCard;
import com.groep6.pfor.models.cards.actions.roleActions.ConsulAction;
import javafx.scene.paint.Color;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bastiaan Jansen
 */
class LobbyTest {

    private String password;
    private Lobby lobby;
    private String code;

    @BeforeEach
    void setUp() {
        this.password = "password";
        this.lobby = new Lobby(password);
        this.code = lobby.getGameCode();
    }

    @AfterEach
    void tearDown() {
        this.lobby = null;
    }

    @Test
    void join() {
        assertDoesNotThrow(() -> lobby.join(code, "username", password, true));
        assertEquals(1, lobby.getPlayers().size());
    }

    @Test
    void leave() throws IncorrectPasswordException {
        LobbyPlayer player = lobby.join(code, "username", password, true);
        assertEquals(1, lobby.getPlayers().size());
        lobby.leaveLobby(player);
        assertEquals(0, lobby.getPlayers().size());
    }

    @Test
    void getPlayers() throws IncorrectPasswordException {
        LobbyPlayer player = lobby.join(code, "username", password, true);
        List<LobbyPlayer> players = new ArrayList<>();
        players.add(player);
        assertEquals(players, lobby.getPlayers());
    }

    @Test
    void isHost() throws IncorrectPasswordException {
        LobbyPlayer player = lobby.join(code, "username", password, true);
        LobbyPlayer player2 = lobby.join(code, "username2", password, true);
        assertTrue(lobby.isHost(player));
        assertFalse(lobby.isHost(player2));
    }

    @Test
    void isInLobby() throws IncorrectPasswordException {
        LobbyPlayer player = lobby.join(code, "username", password, true);
        LobbyPlayer player2 = new LobbyPlayer("username2", new RoleCard("Rolecard", Color.ORANGE, new ConsulAction()), false, true, "lobby");
        assertTrue(lobby.isInLobby(player));
        assertFalse(lobby.isInLobby(player2));
    }

    @Test
    void getHost() throws IncorrectPasswordException, CouldNotFindHostException {
        LobbyPlayer player = lobby.join(code, "username", password, true);
        LobbyPlayer player2 = lobby.join(code, "username2", password, false);
        assertEquals(player, lobby.getHost());
        assertNotEquals(player2, lobby.getHost());
    }

    @Test
    void getLocalPlayer() throws IncorrectPasswordException, CouldNotFindLocalPlayerException {
        LobbyPlayer player = lobby.join(code, "username", password, true);
        LobbyPlayer player2 = lobby.join(code, "username2", password, false);
        assertEquals(player, lobby.getLocalPlayer());
        assertNotEquals(player2, lobby.getLocalPlayer());
    }

    @Test
    void removePlayer() throws IncorrectPasswordException {
        LobbyPlayer player = lobby.join(code, "username", password, true);
        assertEquals(1, lobby.getPlayers().size());
        lobby.removePlayerFromCurrentLobby(player);
        assertEquals(0, lobby.getPlayers().size());
    }
}