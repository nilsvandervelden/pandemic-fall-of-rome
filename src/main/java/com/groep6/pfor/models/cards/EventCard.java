package com.groep6.pfor.models.cards;

import com.groep6.pfor.exceptions.CardNotInDeckException;
import com.groep6.pfor.models.cards.actions.IAction;

/**
 * Represents an event card
 * @author Bastiaan Jansen
 */
public class EventCard extends Card {

    private final String eventName;
    private final IAction correspondingEvent;

    public EventCard(String eventName, IAction event) {
        this.eventName = eventName;
        this.correspondingEvent = event;
    }

    @Override
    public String getCardName() {
        return eventName;
    }

    public IAction getCorrespondingEvent() {
        return correspondingEvent;
    }

    public void executeEvent() {
        try {
            correspondingEvent.executeEvent();
        } catch (CardNotInDeckException e) {
            e.printStackTrace();
        }
    }
}
