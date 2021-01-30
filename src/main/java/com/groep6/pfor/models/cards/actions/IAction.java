package com.groep6.pfor.models.cards.actions;

/**
 * Represents an action some cards can perform
 * @author Bastiaan Jansen
 */
public interface IAction {

    void executeEvent();

    String getCardName();

    String getCardDescription();

}
