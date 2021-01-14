package com.groep6.pfor.controllers;

import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.MenuView;

/**
 * @author Bastiaan Jansen
 */
public class MainMenuController extends Controller {

    public MainMenuController() {
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
