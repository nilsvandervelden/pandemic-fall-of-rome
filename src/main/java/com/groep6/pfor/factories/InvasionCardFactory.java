package com.groep6.pfor.factories;

import com.groep6.pfor.Config;
import com.groep6.pfor.models.cards.InvasionCard;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.util.parsers.InvasionCardParser;

import java.text.ParseException;

/**
 * Creates InvasionCards
 *
 * @author Owen Elderbroek
 */
public class InvasionCardFactory {
    /** The instance of this singleton class */
    private static final InvasionCardFactory SINGLE_INSTANCE = createInvasionCardFactory();

    /** The list of available invasion cards */
    private InvasionCard[] invasionCards;

    private static InvasionCardFactory createInvasionCardFactory() {
        return new InvasionCardFactory();
    }

    private InvasionCardParser createInvasionCardParser() {
        return new InvasionCardParser();
    }

    private void addInvasionCardsToInvasionCardDeck(InvasionCardParser invasionCardParser) {
        try {
            invasionCards = invasionCardParser.parseFile("/invasions.json");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /** Creates the InvasionCardFactory instance */
    private InvasionCardFactory() {
        InvasionCardParser invasionCardParser = createInvasionCardParser();
        addInvasionCardsToInvasionCardDeck(invasionCardParser);
    }

    /**
     * Returns the instance of the InvasionCardFactory
     * @return The InvasionCardFactory instance
     */
    public static InvasionCardFactory getInstance() {
        return SINGLE_INSTANCE;
    }

    /**
     * Get an array of all the invasion cards currently loaded into the game
     * @return An array of available invasion cards
     */
    public InvasionCard[] getAllInvasionCards() {
        return invasionCards;
    }

    /**
     * Get the amount of invasion cards in the game
     * @return The amount of invasion cards in the game
     */
    public int cardCount() {
        return invasionCards.length;
    }

    /**
     * Obtains an invasion card instance by its name
     * @return The invasion card instance or null if not found
     */

    private boolean invasionCardNameEqualsRequestedInvasionCardName(InvasionCard invasionCard, String requestedInvasionCardName) {
        return invasionCard.getName().toUpperCase().equals(requestedInvasionCardName.toUpperCase());
    }

    private boolean invasionCardFactionEqualsRequestedInvasionCardFaction(InvasionCard invasionCard, Faction requestedInvasionCardFaction) {
        return invasionCard.getFaction().equals(requestedInvasionCardFaction);
    }

    public InvasionCard getInvasionCardByName(String requestedInvasionCardName, Faction requestedInvasionCardFaction) {
        for (InvasionCard invasionCard : invasionCards) {
            if (invasionCardNameEqualsRequestedInvasionCardName(invasionCard, requestedInvasionCardName) && invasionCardFactionEqualsRequestedInvasionCardFaction(invasionCard, requestedInvasionCardFaction)) return invasionCard;
        }
        if (inDebugMode()) printInvasionCardWasNotFound(requestedInvasionCardName);
        return null;
    }

    private boolean inDebugMode() {
        return Config.DEBUG;
    }

    private void printInvasionCardWasNotFound(String requestedInvasionCardName) {
        System.out.printf("[WARNING] No InvasionCard was found with the name '%s'\n", requestedInvasionCardName);
    }
}
