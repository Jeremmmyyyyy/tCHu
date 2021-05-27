package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
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

import static ch.epfl.tchu.game.Constants.DECK_SLOT;
import static ch.epfl.tchu.gui.MapViewCreator.NEUTRAL;
import static ch.epfl.tchu.gui.StringsFr.CARDS;
import static ch.epfl.tchu.gui.StringsFr.TICKETS;

/**
 * Abstract class that creates the handView and the cardView of the graphical interface
 *
 * @author Jérémy Barghorn (328403)
 */
abstract class DecksViewCreator { //TODO non instanciable on est d'accord que le abstract suffit ???

    private final static int RECTANGLE_WIDTH_OUT = 60;
    private final static int RECTANGLE_WIDTH_IN = 40;
    private final static int RECTANGLE_HEIGHT_OUT = 90;
    private final static int RECTANGLE_HEIGHT_IN = 70;
    private final static int BACK_RECTANGLE_WIDTH = 50;
    private final static int BACK_RECTANGLE_HEIGHT = 5;

    private final static int DISPLAY_LOWER_BOUND = 1;

    /**
     * Creates a HandView (bottom part of the GUI) given an observableGameState
     * @param observableGameState observable gameState containing all the elements that are changing during the game
     * @return Node containing the entire HandView
     */
    public static Node createHandView(ObservableGameState observableGameState){
        HBox hBoxMain = new HBox();
        hBoxMain.getStylesheets().addAll("decks.css", "colors.css");

        HBox hBoxTickets = new HBox();
        hBoxTickets.setId("hand-pane");

        ListView<Ticket> listView = new ListView<>(observableGameState.ownTickets());
        listView.setId("tickets");

        hBoxMain.getChildren().addAll(listView, hBoxTickets);

        //Creates the graphical player hand cards
        Map<Card, StackPane> cardStack = new HashMap<>();
        for (Card card : Card.ALL) { //TODO bonne facon avec une map ???
            StackPane cardPane = stackPaneCreator(observableGameState.cardCountOnColor(card));
            cardPane.visibleProperty().bind(Bindings.greaterThan(observableGameState.cardCountOnColor(card), 0));
            cardStack.put(card, cardPane);

            observableGameState.cardCountOnColor(card).addListener((o, oV, nV)->{
                if(nV.intValue() > oV.intValue()){
                    cardPane.getStyleClass().add(getColorClass(card));
                }else{ //TODO jcomprends pas le add et le remove
                    cardPane.getStyleClass().remove(getColorClass(card));
                }
            });
        }
        hBoxTickets.getChildren().addAll(cardStack.values());

        return hBoxMain;
    }

    /**
     * Creates a CardView (right part of the GUI) given an observableGameState
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

        Map<Integer, StackPane> cardStack = new HashMap<>(); //TODO Bonne facon avec une map ?

        //Creates the graphical faceUpCards
        for (int i = 0; i < Constants.FACE_UP_CARDS_COUNT; i++) {

            //Argument passed is always equal to 1 in case of a faceUpCard because its counter is not displayed
            StackPane faceUpCardPane = stackPaneCreator(new SimpleIntegerProperty(DISPLAY_LOWER_BOUND));
            cardStack.put(i, faceUpCardPane);

            observableGameState.faceUpCard(i).addListener((o, oV, nV)->{
                //this condition != null avoids a crash at the initialization of the graphical faceUpCards
                if(oV != null){
                    faceUpCardPane.getStyleClass().remove(getColorClass(oV));
                }
                faceUpCardPane.getStyleClass().add(getColorClass(nV));
            });

            int slot = i; //TODO COMME CA AVEC UNE VARIABLE EFFECTIVELY FINAL POUR LA LAMBDA OU PREFERER LIGNE 125 ??
            faceUpCardPane.setOnMouseClicked(e -> drawCardHandler.get().onDrawCard(slot));
            faceUpCardPane.disableProperty().bind(drawCardHandler.isNull());
        }

        //Creates the main card deck button
        Button cardsButton = createButton(CARDS, observableGameState.cardPercentage());
        cardsButton.setOnAction(e -> drawCardHandler.get().onDrawCard(DECK_SLOT));
        cardsButton.disableProperty().bind(drawCardHandler.isNull());

        //Create the main ticket deck button
        Button ticketsButton = createButton(TICKETS, observableGameState.ticketPercentage());
        ticketsButton.setOnAction(e -> drawTicketHandler.get().onDrawTickets());
        ticketsButton.disableProperty().bind(drawTicketHandler.isNull());

//        for (Integer key : cardStack.keySet()) {
//            cardStack.get(key).setOnMouseClicked(e -> drawCardHandler.get().onDrawCard(key));
//        }

        //Respects the expected order
        vBox.getChildren().add(ticketsButton);
        vBox.getChildren().addAll(cardStack.values());
        vBox.getChildren().add(cardsButton);

        return vBox;
    }

    /**
     * Method called by createHandView() and createCardsView() used to get the color class of a given card
     * @param card considered
     * @return the class color of the given card, as a String
     */
    private static String getColorClass(Card card) {
        return card.color() == null ? NEUTRAL : card.color().toString();
    }

    /**
     * Method called by createCardsView() used to create a clickable button
     * @param buttonName name of the button
     * @param property containing the filling percentage of the deck associated to the button
     * @return a new Button
     */
    private static Button createButton(String buttonName, ReadOnlyIntegerProperty property){

        Rectangle backgroundRectangle = new Rectangle(BACK_RECTANGLE_WIDTH, BACK_RECTANGLE_HEIGHT);
        backgroundRectangle.getStyleClass().add("background"); //TODO need constant pour 50 et 100 ??

        Rectangle foregroundRectangle = new Rectangle(property.multiply(50).divide(100).get(), BACK_RECTANGLE_HEIGHT);
        foregroundRectangle.getStyleClass().add("foreground");
        foregroundRectangle.widthProperty().bind(property.multiply(50).divide(100));

        Button button = new Button(buttonName);
        button.getStyleClass().add("gauged");
        button.setGraphic(new Group(backgroundRectangle, foregroundRectangle));

        return button;
    }

    /**
     * Method called by createHandView() and createCardsView() used to create a stackPane of a card
     * @param count of the associated cards
     * @return the graphical representation of a same color card set, with its count if count != null, as a StackPane
     */
    private static StackPane stackPaneCreator(ReadOnlyIntegerProperty count){

        Rectangle rectangleOutside = new Rectangle(RECTANGLE_WIDTH_OUT, RECTANGLE_HEIGHT_OUT);
        rectangleOutside.getStyleClass().add("outside");
        Rectangle rectangleInside = new Rectangle(RECTANGLE_WIDTH_IN, RECTANGLE_HEIGHT_IN);
        rectangleInside.getStyleClass().addAll("filled", "inside");
        Rectangle rectangleImage = new Rectangle(RECTANGLE_WIDTH_IN, RECTANGLE_HEIGHT_IN);
        rectangleImage.getStyleClass().add("train-image");

        Text counter = new Text();
        counter.getStyleClass().add("count");

        StackPane stackPane = new StackPane();
        stackPane.getStyleClass().add("card");

        counter.textProperty().bind(Bindings.convert(count));
        counter.visibleProperty().bind(Bindings.greaterThan(count, DISPLAY_LOWER_BOUND));
        stackPane.getChildren().addAll(rectangleOutside, rectangleInside, rectangleImage, counter);

        return stackPane;
    }
}
