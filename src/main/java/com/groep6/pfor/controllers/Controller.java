package com.groep6.pfor.controllers;

import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.util.IObserver;

/**
 * Abstract controller which every controller extends
 * This is needed to enforce the use of the MVC principle.
 * @author Nils van der Velden
 *
 */
public abstract class Controller {

    protected ViewController viewController;
    protected Game game = Game.getGameInstance();

    public Controller() {
        viewController = ViewController.getInstance();
    }

    /**
     * Go back to the previous View
     */
    public void showPreviousView() {
        viewController.showPreviousView();
    }

    /**
     * Every controller should have a registerObserver method so the view can call the method and register to models
     * @param view
     */
    public abstract void registerObserver(IObserver view) throws CouldNotFindLocalPlayerException;
}
