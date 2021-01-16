package com.groep6.pfor.controllers;

import com.groep6.pfor.Main;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.GameState;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.WinView;
import javafx.application.Platform;

/**
 * 
 * @author Mitchell van Rijswijk
 *
 */
public class WinGameController extends Controller {

    private void showWinGameView() {
        viewController.showView(new WinView(this));
    }

    public WinGameController() {
    	changeMusic();
        showWinGameView();
    }
    
    public void changeMusic() {
    	Main.musicManager.stop();
    	Main.musicManager.play("/sounds/music/To_fight_another_day.mp3", 0.2, false);
    }

    public void exitGame() {
        Platform.exit();
        System.exit(0);
    }

    @Override
    public void registerObserver(IObserver view) {}
}
