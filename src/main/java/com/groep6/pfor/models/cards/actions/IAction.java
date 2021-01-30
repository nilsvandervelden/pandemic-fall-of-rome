package com.groep6.pfor.models.cards.actions;

/**
 * Represents an action some cards can perform
 * @author Bastiaan Jansen
 */
public interface IAction {

    /**
     * Execute this action
     */
    void executeEvent();

    /**
     * @return String
     */
    String getCardName();

    /**
     * @return String
     */
    String getCardDescription();

}
