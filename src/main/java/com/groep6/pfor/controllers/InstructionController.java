package com.groep6.pfor.controllers;

import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.InstructionView;

/**
 * Controller for showing game instructions a game.
 * This controller handles showing instructions.
 * @author Nils van der Velden
 *
 */

public class InstructionController extends Controller {

    public InstructionController() {
        viewController.showView(new InstructionView(this));
    }

    @Override
    public void registerObserver(IObserver view) {

    }
}
