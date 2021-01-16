package com.groep6.pfor.factories;

import com.groep6.pfor.Config;
import com.groep6.pfor.models.cards.InvasionCard;
import com.groep6.pfor.models.cards.RoleCard;
import com.groep6.pfor.models.cards.actions.roleActions.*;
import javafx.scene.paint.Color;
import sun.dc.pr.PRError;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Bastiaan Jansen
 */
public class RoleCardFactory {

    private static final RoleCardFactory SINGLE_INSTANCE = createRoleCardFactory();
    private final List<RoleCard> roleCards = new ArrayList<>();

    private static RoleCardFactory createRoleCardFactory() {
        return new RoleCardFactory();
    }
    /**
     * Constructs a RoleCardFactory with all role cards
     */
    private void addRoleCards() {
        roleCards.add(new RoleCard("Magister Militum", Color.YELLOW, new MagisterMilitumAction()));
        roleCards.add(new RoleCard("Consul", Color.BLUE, new ConsulAction()));
        roleCards.add(new RoleCard("Regina Foederata", Color.GREEN, new ReginaFoederataAction()));
        roleCards.add(new RoleCard("Mercator", Color.TEAL, new MercatorAction()));
        roleCards.add(new RoleCard("Praefectus Classis", Color.LIME, new PraefectusClassisAction()));
        roleCards.add(new RoleCard("Praefectus Fabrum", Color.DARKORANGE, new PraefectusFabrumAction()));
        roleCards.add(new RoleCard("Vestalin", Color.BLUEVIOLET, new VestalinAction()));
    }
    private RoleCardFactory() {
        addRoleCards();
    }

    /**
     * Makes sure you always get the same RoleCardFactory instance
     * @return Instance of RoleCardFactory
     */
    public static RoleCardFactory getInstance() {
        return SINGLE_INSTANCE;
    }

    /**
     * @return List of role cards
     */
    public List<RoleCard> getAllRoleCards() {
        return roleCards;
    }

    /**
     * Pick a random role card
     * @return Random role card
     */

    private int generateRandomNumber() {
        Random randomizer = new Random();
        return randomizer.nextInt(roleCards.size());
    }
    public RoleCard pickRandomRoleCard() {
        return roleCards.get(generateRandomNumber());
    }

    /**
     * @return Size of all role cards
     */
    public int getAmountOfRoleCards() {
        return roleCards.size();
    }

    /**
     * @return The rolecard with the specified name
     */
    private boolean roleCardNameEqualsRequestedRoleCardName(RoleCard roleCard, String requestedRoleCardName) {
        return roleCard.getName().toUpperCase().equals(requestedRoleCardName.toUpperCase());
    }

    public RoleCard getRoleCardByName(String requestedRoleCardName) {
        for (RoleCard roleCard : roleCards) {
            if (roleCardNameEqualsRequestedRoleCardName(roleCard, requestedRoleCardName)) return roleCard;
        }
        if (inDebugMode()) printRoleCardNotFoundError(requestedRoleCardName);
        return null;
    }

    private boolean inDebugMode() {
        return Config.DEBUG;
    }

    private void printRoleCardNotFoundError(String requestedRoleCardName) {
        System.out.printf("[WARNING] No roleCard was found with the name '%s'\n", requestedRoleCardName);
    }
}