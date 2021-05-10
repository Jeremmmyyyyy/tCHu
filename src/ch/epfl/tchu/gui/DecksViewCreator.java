package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

import static ch.epfl.tchu.gui.MapViewCreator.NEUTRAL;

/**
 * Abstract class that creates the handView and the cardView of the graphical interface
 *
 * @author Jérémy Barghorn (328403)
 */
abstract class DecksViewCreator {

    private final static int RECTANGLE_WIDTH_OUT = 60;
    private final static int RECTANGLE_WIDTH_IN = 40;
    private final static int RECTANGLE_HEIGHT_OUT = 90;
    private final static int RECTANGLE_HEIGHT_IN = 70;
    private final static int BACK_RECTANGLE_WIDTH = 50;
    private final static int BACK_RECTANGLE_HEIGHT = 5;

    /**
     * Create a HandView (bottom part of the GUI) given an observableGameState
     * @param observableGameState observable gameState containing all the elements that are changing during the game
     * @return Node containing the entire HandView
     */
    public static Node createHandView(ObservableGameState observableGameState){
        HBox hBoxMain = new HBox();
        hBoxMain.getStylesheets().addAll("decks.css", "colors.css");

        HBox hBoxTickets = new HBox();
        hBoxTickets.setId("hand-pane");

        ObservableList<Ticket> observableList = observableGameState.ownTickets();
        ListView<Ticket> listView = new ListView<>(observableList);
        listView.setId("tickets");


        hBoxMain.getChildren().addAll(listView, hBoxTickets);


        Map<Card, StackPane> cardStack = new HashMap<>();
        for (Card card : Card.ALL) {
            StackPane s = stackPaneCreator(card, observableGameState.cardCountOnColor(card), true);
            s.visibleProperty().bind(Bindings.greaterThan(observableGameState.cardCountOnColor(card), 0));
            cardStack.put(card, s);

            observableGameState.cardCountOnColor(card).addListener((o, oV, nV)->{
                if(nV.intValue()-oV.intValue() > 0){
                    s.getStyleClass().add(card.color() == null ? NEUTRAL : card.color().toString());
                }else{
                    s.getStyleClass().remove(card.color() == null ? NEUTRAL : card.color().toString());
                }
            });
        }
        hBoxTickets.getChildren().addAll(cardStack.values());

        return hBoxMain;
    }

    /**
     * Create a CardView (right part of the GUI) given an observableGameState
     * @param observableGameState observable gameState containing all the elements that are changing during the game
     * @param drawTicketHandler Interface that is used to handle how the tickets are drawn
     * @param drawCardHandler Interface that is used to handle how the Cards are drawn
     * @return Node containing the entire CardView
     */
    public static Node createCardsView(ObservableGameState observableGameState,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketHandler,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandler){


        VBox vBox = new VBox();
        vBox.getStylesheets().addAll("decks.css", "colors.css");
        vBox.setId("card-pane");
        vBox.disableProperty().bind(drawCardHandler.isNull());
        vBox.disableProperty().bind(drawTicketHandler.isNull());


        Map<Integer, StackPane> cardStack = new HashMap<>();

        for (int i = 0; i < Constants.FACE_UP_CARDS_COUNT; i++) {
            StackPane s = stackPaneCreator(observableGameState.faceUpCard(i).get(), null, false);
            cardStack.put(i, s);

            observableGameState.faceUpCard(i).addListener((o, oV, nV)->{
                if(oV != null){
                    s.getStyleClass().remove(oV.color().toString());
                }
                s.getStyleClass().add(nV.color().toString());
            });
        }
        Button cartes = createButton("Cartes", observableGameState.cardPercentage());
        cartes.setOnAction(e -> drawCardHandler.get().onDrawCard(-1)); //TODO set on action ok ?
        Button tickets = createButton("Billets", observableGameState.ticketPercentage());
        tickets.setOnAction(e -> drawTicketHandler.get().onDrawTickets());

        for (Integer key : cardStack.keySet()) {
            cardStack.get(key).setOnMouseClicked(e -> drawCardHandler.get().onDrawCard(key));
        }

        vBox.getChildren().add(tickets);
        vBox.getChildren().addAll(cardStack.values());
        vBox.getChildren().add(cartes);


        return vBox;
    }

    private static Button createButton(String buttonName, ReadOnlyIntegerProperty property){
        Rectangle backgroundRectangle = new Rectangle(BACK_RECTANGLE_WIDTH, BACK_RECTANGLE_HEIGHT);
        backgroundRectangle.getStyleClass().add("background");
        Rectangle foregroundRectangle = new Rectangle(property.multiply(50).divide(100).get(), BACK_RECTANGLE_HEIGHT);
        foregroundRectangle.getStyleClass().add("foreground");
        foregroundRectangle.widthProperty().bind(property.multiply(50).divide(100));

        Group group = new Group();
        group.getChildren().addAll(backgroundRectangle, foregroundRectangle);

        Button button = new Button(buttonName);
        button.getStyleClass().add("gauged");
        button.setGraphic(group);

        return button;
    }

    private static StackPane stackPaneCreator(Card card, ReadOnlyIntegerProperty count , Boolean displayCounter){ //TODO enlever Card
        Rectangle rectangleOutside = new Rectangle(RECTANGLE_WIDTH_OUT, RECTANGLE_HEIGHT_OUT);
        rectangleOutside.getStyleClass().add("outside");
        Rectangle rectangleInside = new Rectangle(RECTANGLE_WIDTH_IN, RECTANGLE_HEIGHT_IN);
        rectangleInside.getStyleClass().addAll("filled", "inside");
        Rectangle rectangleImage = new Rectangle(RECTANGLE_WIDTH_IN, RECTANGLE_HEIGHT_IN);
        rectangleImage.getStyleClass().add("train-image");

        Text counter = new Text();
        counter.getStyleClass().add("count");
        if(count != null){
            counter.textProperty().bind(Bindings.convert(count));
            counter.visibleProperty().bind(Bindings.greaterThan(count, 1));
        }

        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().add("card");
        if (displayCounter){
            stackPane.getChildren().addAll(rectangleOutside, rectangleInside, rectangleImage, counter);
        }else{
            stackPane.getChildren().addAll(rectangleOutside, rectangleInside, rectangleImage);
        }

        return stackPane;
    }
}
