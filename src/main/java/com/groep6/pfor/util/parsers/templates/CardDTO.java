package com.groep6.pfor.util.parsers.templates;

import com.groep6.pfor.factories.CityCardFactory;
import com.groep6.pfor.factories.EventCardFactory;
import com.groep6.pfor.factories.FactionFactory;
import com.groep6.pfor.factories.InvasionCardFactory;
import com.groep6.pfor.models.cards.*;
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

    public Card toModel() {
        switch (cardType) {
            case "city":
                return CityCardFactory.getCityCardFactoryInstance().getCardByName(cardName, FactionFactory.getInstance().getFaction(FactionType.valueOf(factionCardBelongsTo)));
            case "event":
                return EventCardFactory.getInstance().getCardByName(cardName);
            case "invasion":
                return InvasionCardFactory.getInstance().getInvasionCardByName(cardName, FactionFactory.getInstance().getFaction(FactionType.valueOf(factionCardBelongsTo)));
        }
        return null;
        // TODO cast card from the database to correct model
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
