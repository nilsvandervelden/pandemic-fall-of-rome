package com.groep6.pfor.controllers;

import com.groep6.pfor.Main;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.LoseView;
import javafx.application.Platform;

public class LoseGameController extends Controller {

    public LoseGameController() {
    	playLostGameMusic();
        goToLoseGameView();
    }

    private void goToLoseGameView() {
        viewController.showView(new LoseView(this));
    }
    
    public void playLostGameMusic() {
    	Main.musicManager.stop();
    	Main.musicManager.play("/sounds/music/The_End_of_an_Era.mp3", 0.2, false);
    }

    public void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void registerObserver(IObserver view) {}
}
