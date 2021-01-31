package com.groep6.pfor.models.cards.actions;

import com.groep6.pfor.exceptions.CardNotInDeckException;

/**
 * Represents an action some cards can perform
 * @author Bastiaan Jansen
 */
public interface IAction {

    void executeEvent() throws CardNotInDeckException;

    String getCardName();

    String getCardDescription();

}
