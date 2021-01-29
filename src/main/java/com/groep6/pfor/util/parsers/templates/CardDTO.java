package com.groep6.pfor.util.parsers.templates;

import com.groep6.pfor.factories.*;
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

    public static CardDTO fromModel(Card model) {
        if (model instanceof CityCard) {
            CityCard card = (CityCard) model;
            return new CardDTO("city", card.getCardName(), card.getFactionCityCardBelongsTo().getFactionType().toString(), card.getCorrespondingCity().getCityName());
        } else if (model instanceof EventCard) {
            return new CardDTO("event", model.getCardName());
        } else if (model instanceof InvasionCard) {
            InvasionCard card = (InvasionCard) model;
            return new CardDTO("invasion", card.getCardName(), card.getInvadingFaction().getFactionType().toString());
        } else if (model instanceof RevoltCard) {
            return new CardDTO("revolt");
        }
        return null;
    }
}
