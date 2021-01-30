package com.groep6.pfor.models;

import com.groep6.pfor.models.cards.RoleCard;
import com.groep6.pfor.util.Observable;

/**
 * Represents a lobby player
 * @author Bastiaan Jansen
 */

public class LobbyPlayer extends Observable {
    private final String username;
    private RoleCard roleCard;
    private final boolean isHost;
    private boolean isLocal;
    private final String lobbyCode;

    public LobbyPlayer(String username, RoleCard roleCard, boolean isHost, boolean isLocal, String lobbyCode) {
        this.username = username;
        this.roleCard = roleCard;
        this.isHost = isHost;
        this.isLocal = isLocal;
        this.lobbyCode = lobbyCode;
    }

    public LobbyPlayer(String username, boolean isHost, String lobbyCode) {
        this.username = username;
        this.isHost = isHost;
        this.lobbyCode = lobbyCode;
    }

    public RoleCard getRoleCard() {
        return roleCard;
    }

    public void setRole(RoleCard roleCard) {
        this.roleCard = roleCard;
        notifyObservers();
    }

    public boolean isHost() {
        return isHost;
    }

    public String getUsername() {
        return username;
    }

    public boolean isLocal() {
        return isLocal;
    }

    public void setLocal(boolean isLocal) {
        this.isLocal = isLocal;
    }

    public String getLobbyCode() {
        return lobbyCode;
    }

    private boolean isInstanceOfLobbyPlayer(Object object) {
        return object instanceof LobbyPlayer;
    }

    private LobbyPlayer convertToLobbyPlayer(Object object) {
        return (LobbyPlayer) object;
    }

    private boolean userNameEqualsFireBaseUserName(LobbyPlayer player) {
        return player.getUsername().equals(this.username);
    }

    @Override
    public boolean equals(Object object) {
        if (!isInstanceOfLobbyPlayer(object)) return false;

        LobbyPlayer player = convertToLobbyPlayer(object);

        return userNameEqualsFireBaseUserName(player);
    }
}



