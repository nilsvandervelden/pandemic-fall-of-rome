package com.groep6.pfor.controllers;

import com.groep6.pfor.Main;
import com.groep6.pfor.models.Game;
import com.groep6.pfor.models.GameState;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.OptionsView;

/**
 * Controller for game options. This controller handles everything to do with the game options.
 * This controller enables players to mute the game, go to the instructions and toggle fullscreen.
 * @author Nils van der Velden
 *
 */
public class OptionController extends Controller {

	public void goToOptionsView() {
		viewController.showView(new OptionsView(this));
	}

	public OptionController() {
		goToOptionsView();
	}
	
	public GameState checkGameState() {
		return Game.getGameState();
	}
	
	public void toggleFullscreen() {
		ViewController.getInstance().toggleFullscreen();
	}
	
	public void toggleMute() {
		Main.musicManager.toggleMute();
	}
	
	public void goToInstructionsView() {
		new InstructionController();
	}
	
	@Override
	public void registerObserver(IObserver view) {
		game.registerObserver(view);
	}

}
