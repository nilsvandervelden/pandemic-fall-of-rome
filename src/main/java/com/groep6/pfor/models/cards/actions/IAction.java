package com.groep6.pfor.models.cards.actions;

import com.groep6.pfor.exceptions.CardNotInDeckException;
import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;

/**
 * Represents an action some cards can perform
 * @author Bastiaan Jansen
 */
public interface IAction {

    void executeEvent() throws CardNotInDeckException, CouldNotFindLocalPlayerException;

    String getCardName();

    String getCardDescription();

}
