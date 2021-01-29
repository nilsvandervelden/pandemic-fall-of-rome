package com.groep6.pfor.util.parsers.templates;

import com.groep6.pfor.models.Lobby;
import com.groep6.pfor.models.LobbyPlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The Data Transfer Object that represents a Lobby in Firebase.
 *
 * @author Owen Elderbroek
 */
public class LobbyDTO {
    /** The lobby code that is required to join a lobby */
    public String lobbyCode;

    /** The password that is required to join the lobby */
    public String lobbyPassword;

    /** The players in this lobby */
    public Map<String, LobbyPlayerDTO> playersInLobby;

    public boolean started;

    public LobbyDTO() {}

    /**
     * Construct a Data Transfer Object with the specified fields
     * @param lobbyCode The lobby code
     * @param lobbyPassword The password for the lobbies
     * @param playersInLobby The players in the lobby
     */
    private LobbyDTO(String lobbyCode, String lobbyPassword, Map<String, LobbyPlayerDTO> playersInLobby) {
        this.lobbyCode = lobbyCode;
        this.lobbyPassword = lobbyPassword;
        this.playersInLobby = playersInLobby;
    }

    /**
     * Converts this Data Transfer Object to its corresponding business model
     * @return The lobby object that this instance represents
     */

    private List<LobbyPlayer> addPlayersToLobby() {
        List<LobbyPlayer> players = new ArrayList<>();
        for (LobbyPlayerDTO player : this.playersInLobby.values()) {
            players.add(player.toModel(lobbyCode));
        }
        return players;
    }
    public Lobby toModel() {
        List<LobbyPlayer> players =  addPlayersToLobby();
        return new Lobby(lobbyCode, lobbyPassword, players);
    }

    private static Map<String, LobbyPlayerDTO> addPlayersToLobby(Lobby lobby) {
        Map<String, LobbyPlayerDTO> playersInLobby = new HashMap<>();
        for (LobbyPlayer player : lobby.getPlayers()) {
            playersInLobby.put(player.getUsername(), LobbyPlayerDTO.fromModel(player));
        }
        return playersInLobby;
    }

    /**
     * Constructs a Data Transfer Object from a model
     * @param lobby The model to construct the DTO of
     * @return The DTO of the model
     */
    public static LobbyDTO fromModel(Lobby lobby) {
        Map<String, LobbyPlayerDTO> playersInLobby = addPlayersToLobby(lobby);
        return new LobbyDTO(lobby.getCode(), lobby.getPasswordHash(), playersInLobby);
    }
}
