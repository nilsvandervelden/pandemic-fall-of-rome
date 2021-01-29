package com.groep6.pfor.util.parsers.templates;

import com.groep6.pfor.factories.*;
import com.groep6.pfor.models.City;
import com.groep6.pfor.models.cards.*;
import com.groep6.pfor.models.factions.Faction;
import com.groep6.pfor.models.factions.FactionType;

/**
 * The representation of a card in Firebase
 *
 * @author Owen Elderbroek
 */
public class CardDTO {
    public String cardType;
    public String cardName;
    public String cityCardBelongsTo;
    public String factionCardBelongsTo;

    public CardDTO () {}

    private CardDTO(String cardType) {
        this.cardType = cardType;
    }

    private CardDTO(String cardType, String cardName) {
        this(cardType);
        this.cardName = cardName;
    }

    private CardDTO(String cardType, String cardName, String factionCardBelongsTo) {
        this(cardType, cardName);
        this.factionCardBelongsTo = factionCardBelongsTo;
    }

    private CardDTO(String cardType, String cardName, String factionCardBelongsTo, String cityCardBelongsTo) {
        this(cardType, cardName, factionCardBelongsTo);
        this.cityCardBelongsTo = cityCardBelongsTo;
    }

    private CityCard getCityCardByName() {
        CityCardFactory cityFactory = CityCardFactory.getCityCardFactoryInstance();
        return  cityFactory.getCityCardByName(cardName, getFaction());
    }

    private Faction getFaction() {
        FactionFactory factionFactory = FactionFactory.getInstance();
        return factionFactory.getFaction(FactionType.valueOf(factionCardBelongsTo));
    }

    private EventCard getEventCardByName() {
        EventCardFactory eventCardFactory = EventCardFactory.getInstance();
        return eventCardFactory.getCardByName(cardName);
    }

    private InvasionCard getInvasionCardByName() {
        InvasionCardFactory invasionCardFactory = InvasionCardFactory.getInstance();
        return invasionCardFactory.getInvasionCardByName(cardName, getFaction());
    }

    public Card parseToModel() {
        switch (cardType) {
            case "city":
                return getCityCardByName();
            case "event":
                return getEventCardByName();
            case "invasion":
                return getInvasionCardByName();
        }
        return null;
    }

    private static String getCardName(Card card) {
        return card.getCardName();
    }

    private static Faction getFactionCityCardBelongsTo(CityCard card) {
        return card.getFactionCityCardBelongsTo();
    }

    private static FactionType getCityCardFactionType(CityCard cityCard) {
        return getFactionCityCardBelongsTo(cityCard).getFactionType();
    }

    private static String getCityCardFactionTypeAsString(CityCard cityCard) {
        return getCityCardFactionType(cityCard).toString();
    }

    private static City getCorrespondingCity(CityCard cityCard) {
        return cityCard.getCorrespondingCity();
    }

    private static String getCorrespondingCityName(CityCard cityCard) {
        return getCorrespondingCity(cityCard).getCityName();
    }

    private static Faction getInvadingFaction(InvasionCard invasionCard) {
        return invasionCard.getInvadingFaction();
    }

    private static FactionType getInvadingFactionType(InvasionCard invasionCard) {
        return getInvadingFaction(invasionCard).getFactionType();
    }

    private static String getInvadingFactionTypeAsString(InvasionCard invasionCard) {
        return getInvadingFactionType(invasionCard).toString();
    }

    public static CardDTO parseFromModel(Card model) {
        if (model instanceof CityCard) {
            CityCard cityCard = (CityCard) model;
            return new CardDTO("city", getCardName(cityCard) , getCityCardFactionTypeAsString(cityCard), getCorrespondingCityName(cityCard));
        } else if (model instanceof EventCard) {
            return new CardDTO("event", getCardName(model));
        } else if (model instanceof InvasionCard) {
            InvasionCard invasionCard = (InvasionCard) model;
            return new CardDTO("invasion", getCardName(invasionCard), getInvadingFactionTypeAsString(invasionCard));
        } else if (model instanceof RevoltCard) {
            return new CardDTO("revolt");
        }
        return null;
    }
}
