package com.groep6.pfor.controllers;

import com.groep6.pfor.Config;
import com.groep6.pfor.views.View;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.Stack;

/**
 * With the ViewController it is easy to switch to different Views
 * @author Bastiaan Jansen
 */
public class ViewController {

    private static final int MIN_SCREEN_WIDTH = 1280;
    private static final int MIN_SCREEN_HEIGHT = 720;

    private static final ViewController INSTANCE = new ViewController();
    public static ViewController getInstance() { return INSTANCE; }

    private final Stack<View> visitedViews = new Stack<>();
    private Stage stage;

    private ViewController() {}

    private void setStageWith() {
        stage.setWidth(MIN_SCREEN_WIDTH);
    }

    private void setStageHeight() {
        stage.setHeight(MIN_SCREEN_HEIGHT);
    }

    private void toggleFullScreen(boolean fullScreen) {
        stage.setFullScreen(fullScreen);
    }
    public void setPrimaryStage(Stage stage) {
        this.stage = stage;

        setStageWith();
        setStageHeight();
        toggleFullScreen(true);
    }

    private void removeFullScreenKeyCombination() {
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
    }

    private double getScreenWidth() {
        return stage.getWidth();
    }

    private double getScreenHeight() {
        return  stage.getHeight();
    }

    private boolean isFullScreen() {
        return stage.isFullScreen();
    }

    private void addViewToVisitedViewStack(View visitedView) {
        visitedViews.push(visitedView);
    }

    private boolean isDebugMode() {
        return Config.DEBUG;
    }

    private void printVisitedViews() {
        for (View newView: visitedViews) {
            System.out.println(newView.toString());
        }
        System.out.println("------------");
    }

    private Pane getRootPane(View view) {
        return view.getRoot();
    }

    private Scene getScene(Stage stage) {
        return stage.getScene();
    }

    private boolean sceneExists(Scene scene) {
        return scene != null;
    }

    private void setRoot(Scene scene, Pane root) {
        scene.setRoot(root);
    }

    private Scene createScene(Pane root) {
        return new Scene(root);
    }

    private void setScene(Scene scene) {
        stage.setScene(scene);
    }

    public void showView(View view, boolean preventPush) {
    	
        removeFullScreenKeyCombination();

        double width = getScreenWidth();
        double height = getScreenHeight();

        if (!preventPush) addViewToVisitedViewStack(view);

        if (isDebugMode()) {
            printVisitedViews();
        }
        
        Pane root = getRootPane(view);

        Scene scene = getScene(stage);

        if (sceneExists(scene)) {
            setRoot(scene, root);
        } else {
            Scene newScene = createScene(root);
            setScene(newScene);
        }

        if (isFullScreen()) {
            toggleFullScreen(true);
        } else {
            setWidth(width);
            setHeight(height);
        }
        stage.show();
    }

    public void showView(View view) {
        showView(view, false);
    }
    
    public void toggleFullscreen() {
    	if (isFullScreen()) {
    		toggleFullScreen(false);
    		return;
    	}
    	toggleFullScreen(true);
    }

    public Stage getPrimaryStage() {
        return stage;
    }

    public void showPreviousView() {
        if (visitedViews.size() <= 1) return;
        visitedViews.pop();
        showView(visitedViews.peek(), true);
    }
    
    public Stack<View> getVisitedViews() {
    	return visitedViews;
    }

    public void setWidth(double width) {
        stage.setWidth(width);
    }

    public void setHeight(double height) {
        stage.setHeight(height);
    }

    public void setTitle(String title) {
        stage.setTitle(title);
    }
}
