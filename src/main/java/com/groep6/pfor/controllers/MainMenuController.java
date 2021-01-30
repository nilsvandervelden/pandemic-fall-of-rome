package com.groep6.pfor.controllers;

import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.LoseView;
import com.groep6.pfor.views.MenuView;

/**
 * Controller for the main menu. This controller handles everything to do with the main menu.
 * Basically this controller navigates users to the view they want to go to.
 * @author Nils van der Velden
 *
 */
public class MainMenuController extends Controller {

    public MainMenuController() {
        goToMainMenuView();
    }

    private void goToMainMenuView() {
        viewController.showView(new MenuView(this));
    }

    public void goToHostView() {
        new HostGameController();
    }

    public void goToJoinView() {
        new JoinGameController();
    }

    @Override
    public void registerObserver(IObserver view) {}
}
