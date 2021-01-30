package com.groep6.pfor.controllers;

import com.groep6.pfor.Main;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.LoseView;
import javafx.application.Platform;

/**
 * Controller for losing a game. This controller handles everything to do with a losing a game.
 * Things like showing the lose game view, exiting the game and playing some sad music.
 * @author Nils van der Velden
 *
 */
public class LoseGameController extends Controller {

    public LoseGameController() {
    	playLostGameMusic();
        goToLoseGameView();
    }

    private void goToLoseGameView() {
        viewController.showView(new LoseView(this));
    }
    
    public void playLostGameMusic() {
    	stopMusic();
    	Main.musicManager.play("/sounds/music/The_End_of_an_Era.mp3", 0.2, false);
    }

    private void stopMusic() {
        Main.musicManager.stop();
    }

    public void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void registerObserver(IObserver view) {}
}
