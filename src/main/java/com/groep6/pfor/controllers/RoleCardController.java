package com.groep6.pfor.controllers;

import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.factories.RoleCardFactory;
import com.groep6.pfor.models.Lobby;
import com.groep6.pfor.models.LobbyPlayer;
import com.groep6.pfor.models.cards.RoleCard;
import com.groep6.pfor.services.LobbyService;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.RoleCardInfoView;

import java.util.List;

/**
 * Controller for showing role card information. This controller handles everything to do with role cards.
 * these are things like selecting the role card to play.
 * @author Nils van der Velden
 *
 */
public class RoleCardController extends Controller {

    private final Lobby currentLobby;
    private RoleCard currentlySelectedRoleCard;

    private RoleCard getCurrentlySelectedRoleCard(Lobby currentLobby) throws CouldNotFindLocalPlayerException {
        return currentLobby.getLocalPlayer().getRoleCard();
    }

    private void showRoleCardInfoView() {
        viewController.showView(new RoleCardInfoView(this));
    }

    public RoleCardController(Lobby lobby) {
        this.currentLobby = lobby;
        try {
            this.currentlySelectedRoleCard = getCurrentlySelectedRoleCard(currentLobby);
        } catch (CouldNotFindLocalPlayerException e) {
            e.printStackTrace();
        }

        showRoleCardInfoView();
    }

    public List<RoleCard> getRoleCards() {
        return RoleCardFactory.getInstance().getAllRoleCards();
    }

    public RoleCard getCurrentlySelectedRoleCard() {
        return currentlySelectedRoleCard;
    }

    private LobbyPlayer getLocalPlayer() throws CouldNotFindLocalPlayerException {
        return currentLobby.getLocalPlayer();
    }

    private void setRole(LobbyPlayer localPlayer, RoleCard role) {
        localPlayer.setRole(role);
    }

    private LobbyService createLobbyService() {
        return new LobbyService();
    }

    private void updatePlayerRole(LobbyService lobbyService, LobbyPlayer localPlayer) {
        lobbyService.updateRoleCard(localPlayer);
    }

    public void chooseRole(RoleCard roleCard) throws CouldNotFindLocalPlayerException {
        LobbyPlayer localPlayer = getLocalPlayer();
        setRole(localPlayer, roleCard);
        LobbyService lobbyService = createLobbyService();
        updatePlayerRole(lobbyService, localPlayer);
    }

    @Override
    public void registerObserver(IObserver view) {}
}
