package com.groep6.pfor.views;

import com.groep6.pfor.controllers.TradeDeckController;
import com.groep6.pfor.exceptions.CouldNotFindLocalPlayerException;
import com.groep6.pfor.models.cards.Card;
import com.groep6.pfor.models.cards.CityCard;
import com.groep6.pfor.models.cards.EventCard;
import com.groep6.pfor.util.IObserver;
import com.groep6.pfor.views.components.UIButton;
import com.groep6.pfor.views.components.UICard;
import com.groep6.pfor.views.components.UICityCard;
import com.groep6.pfor.views.components.UIEventCard;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

/**
 * The view where you can trade cards with the trade deck
 * @author Nils van der Velden
 */
public class TradeView extends View implements IObserver {
    /** The tradeDeckController */
    private final TradeDeckController tradeDeckController;
    
    private List<Card> cards = new ArrayList<>();
    
    private BorderPane root;
    private Button withdrawButton;
    private Button goBackButton;
    private ScrollPane scrollPane;
    private FlowPane cardsPane;
    private final List<UICard> uiCards = new ArrayList<>();

    /**
     * The constructor
     * @param tradeDeckController the tradeDeckController
     */
    public TradeView(TradeDeckController tradeDeckController) {
        this.tradeDeckController = tradeDeckController;
        cards = tradeDeckController.getTradeCards();
        tradeDeckController.registerObserver(this);
        
        createView();
        
        update();
    }
    
    private void createView() {
        root = new BorderPane();
        
        scrollPane = new ScrollPane();
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPadding(new Insets(-1));
        
        VBox buttonsPane = new VBox(20);
        buttonsPane.setAlignment(Pos.CENTER);
        buttonsPane.setPadding(new Insets(50, 50, 50, 50));
        
        withdrawButton = new UIButton("Kaart opnemen");
        withdrawButton.setDisable(true);
        withdrawButton.setPrefWidth(150);
        withdrawButton.addEventFilter(MouseEvent.MOUSE_CLICKED, withdrawCard);
        
        goBackButton = new UIButton("Ga terug");
        goBackButton.setDisable(false);
        goBackButton.setPrefWidth(150);
        goBackButton.setBackground(new Background(new BackgroundFill(Color.web("#878787"), CornerRadii.EMPTY, Insets.EMPTY)));
        goBackButton.addEventFilter(MouseEvent.MOUSE_CLICKED, goBack);

        
        buttonsPane.getChildren().addAll(withdrawButton, goBackButton);
        buttonsPane.setBackground(new Background(new BackgroundFill(Color.web("#D5544F"), CornerRadii.EMPTY, Insets.EMPTY)));

        root.setCenter(scrollPane);
        root.setRight(buttonsPane);
        
    }
    
    private void createCards() {
    	
        cardsPane = new FlowPane();
        setBackground(cardsPane, "/images/background-4.jpg");
        cardsPane.setPadding(new Insets(20, 20, 20, 20));
        cardsPane.setVgap(50);
        cardsPane.setHgap(50);
        
        scrollPane.setContent(cardsPane);
        
        for (Card card: cards) {
            UICard uiCard = null;

            if (card instanceof CityCard) uiCard = new UICityCard((CityCard) card);
            else if (card instanceof EventCard) uiCard = new UIEventCard((EventCard) card);

            if (uiCard != null) {
                uiCard.addEventFilter(MouseEvent.MOUSE_CLICKED, selectCard);
                uiCards.add(uiCard);
                cardsPane.getChildren().add(uiCard);
            }
        }
    }
    
    // Select a card
    EventHandler<MouseEvent> selectCard = new EventHandler<javafx.scene.input.MouseEvent>() {
        @Override
        public void handle(javafx.scene.input.MouseEvent e) {
            deselectAllCards();
            UICard source = (UICard) e.getSource();
            source.select();
            tradeDeckController.selectCard(source.getCard());
            try {
                if (tradeDeckController.getLocalPlayer().getActionsRemaining() > 0) withdrawButton.setDisable(false);
            } catch (CouldNotFindLocalPlayerException couldNotFindLocalPlayerException) {
                couldNotFindLocalPlayerException.printStackTrace();
            }
        }
    };

    public void deselectAllCards() {
        for (UICard card: uiCards) {
            card.deselect();
        }
    }
    
    EventHandler<MouseEvent> goBack = new EventHandler<javafx.scene.input.MouseEvent>() {
        @Override
        public void handle(javafx.scene.input.MouseEvent e) {
            tradeDeckController.showPreviousView();
        }
    };
    
    EventHandler<MouseEvent> withdrawCard = new EventHandler<javafx.scene.input.MouseEvent>() {
        @Override
        public void handle(javafx.scene.input.MouseEvent e) {
            tradeDeckController.withdrawCardFromTradeDeck();
            try {
                if (tradeDeckController.getLocalPlayer().getActionsRemaining() <= 0) withdrawButton.setDisable(true);
            } catch (CouldNotFindLocalPlayerException couldNotFindLocalPlayerException) {
                couldNotFindLocalPlayerException.printStackTrace();
            }
        }
    };
        

	@Override
	public void update() {
        createCards();
        
	}

	@Override
	public Pane getRoot() {
		return root;
	}

}
