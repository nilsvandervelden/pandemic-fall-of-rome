package com.groep6.pfor.models;

import com.groep6.pfor.exceptions.CouldNotFindHostException;
import com.groep6.pfor.exceptions.IncorrectPasswordException;
import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.factories.RoleCardFactory;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.util.Observable;
import com.groep6.pfor.util.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a lobby
 *
 * @author Bastiaan Jansen
 */
public class Lobby extends Observable implements IObserver {

    private final String code;
    private String passwordHash;
    private List<LobbyPlayer> players = new ArrayList<>();

    public Lobby(String password) {
        this.code = generateCode();
        this.passwordHash = PasswordEncoder.hash(password);
    }

    /**
     * Constructs a lobby without a password
     */
    public Lobby() {
        this.code = generateCode();
    }

    public Lobby(String code, String passwordHash, List<LobbyPlayer> players) {
        this.code = code;
        this.passwordHash = passwordHash;
        this.players.addAll(players);
    }

    /**
     * Creates a new lobbyPlayer instance with a random available roleCard, the first player is the host of the lobby
     */
    public LobbyPlayer join(String code, String username, String password, boolean isLocal) throws IncorrectPasswordException {
        if (!validatePassword(password)) throw new IncorrectPasswordException();

        boolean isHost = false;

        if (players.size() == 0) isHost = true;

        LobbyPlayer lobbyPlayer = new LobbyPlayer(username, RoleCardFactory.getInstance().pickRandomRoleCard(), isHost, isLocal, code);
        players.add(lobbyPlayer);
        lobbyPlayer.registerObserver(this);
        return lobbyPlayer;
    }

    /**
     * Checks if the password is the same as the lobby password, if the lobby has no password, the method always returns true
     */
    private boolean validatePassword(String password) {
        if (passwordHash == null) return true;

        return PasswordEncoder.matches(password, passwordHash);
    }

    /**
     * Get the password of the lobby (hashed of course)
     * @return The hashed password of the lobby
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Removes a LobbyPlayer from the lobby
     * @param player
     */
    public void leaveLobby(LobbyPlayer player) {
        players.remove(player);
    }

    /**
     * Generates a 5 digit code
     * @return 5 digit code
     */
    private String generateCode() {
        Random r = new Random( System.currentTimeMillis() );
        int number = ((1 + r.nextInt(2)) * 10000 + r.nextInt(10000));
        return String.valueOf(number);
    }

    public String getGameCode() {
        return code;
    }

    /**
     * @return List of LobbyPlayers
     */
    public List<LobbyPlayer> getPlayers() {
        return players;
    }

    /**
     * Check if a certain lobbyPlayer is the host of this lobby
     * @param lobbyPlayer
     * @return boolean
     */
    public boolean isHost(LobbyPlayer lobbyPlayer) {
        for (LobbyPlayer player : players) {
            if (player == lobbyPlayer) {
                if (player.isHost()) return true;
            }
        }
        return false;
    }

    /**
     * @param lobbyPlayer
     * @return returns whether a lobbyPlayer is in this lobby
     */
    public boolean isInLobby(LobbyPlayer lobbyPlayer) {
        return players.contains(lobbyPlayer);
    }

    /**
     * @return The host of the lobby
     */
    public LobbyPlayer getHost() throws CouldNotFindHostException {
        for (LobbyPlayer player: players) {
            if (player.isHost()) return player;
        }
        throw new CouldNotFindHostException();
    }

    public Game start() {
        return Game.getGameInstance();
    }

    public LobbyPlayer getLocalPlayer() throws CouldNotFindLocalPlayerException {
        for (LobbyPlayer player: players) {
            if (player.isLocal()) return player;
        }
        throw new CouldNotFindLocalPlayerException();
    }

    public void removePlayerFromCurrentLobby(LobbyPlayer player) {
        players.remove(player);
    }

    @Override
    public void update() {
        notifyObservers();
    }

    public void updateLobby(Lobby lobby) throws CouldNotFindLocalPlayerException {

        LobbyPlayer localPlayer = getLocalPlayer();

        players = lobby.getPlayers();

        for (LobbyPlayer player: lobby.getPlayers()) {
            if (player.equals(localPlayer)) {
                player.setLocal(true);
                break;
            }
        }

        update();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Lobby)) return false;
        return ((Lobby) o).code.equals(code);
    }
}
