package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Constants;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
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
import java.util.List;
import java.util.Map;


abstract class DecksViewCreator {

    public static Node createHandView(ObservableGameState observableGameState){
        HBox hBoxMain = new HBox();
        hBoxMain.getStylesheets().addAll("decks.css", "colors.css");

        HBox hBoxTickets = new HBox();
        hBoxTickets.setId("hand-pane");

        ObservableList<Ticket> observableList = FXCollections.unmodifiableObservableList(FXCollections.observableList(ChMap.tickets()));
        //TODO afficher que les Tickets actuels du joueur avec observableGameState
        ListView<Ticket> listView = new ListView<>(observableList);
        listView.setId("tickets");

        hBoxMain.getChildren().addAll(listView, hBoxTickets);


        Map<Card, StackPane> cardStack = new HashMap<>();
        for (Card card : Card.ALL) {
            StackPane s = stackPaneCreator(card, observableGameState.carCountOnCard(card), true);
            s.visibleProperty().bind(Bindings.greaterThan(observableGameState.carCountOnCard(card), 0));
            cardStack.put(card, s);

            observableGameState.carCountOnCard(card).addListener((o, oV, nV)->{
                if(nV.intValue()-oV.intValue() > 0){
                    s.getStyleClass().add(card.color() == null ? "NEUTRAL" : card.color().toString());
                }else{
                    s.getStyleClass().remove(card.color() == null ? "NEUTRAL" : card.color().toString());
                }
            });
        }
        hBoxTickets.getChildren().addAll(cardStack.values());

        return hBoxMain;
    }

    public static Node createCardsView(ObservableGameState observableGameState,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketHandler,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandler){


        VBox vBox = new VBox();
        vBox.getStylesheets().addAll("decks.css", "colors.css");
        vBox.setId("card-pane");
        vBox.disableProperty().bind(drawCardHandler.isNull()); //TODO c'est juste ??
        vBox.disableProperty().bind(drawTicketHandler.isNull()); //TODO c'est juste ??


        Map<Integer, StackPane> cardStack = new HashMap<>();

        for (int i = 0; i < Constants.FACE_UP_CARDS_COUNT; i++) {  //TODO c'est juste ??
            StackPane s = stackPaneCreator(observableGameState.faceUpCard(i).get(), null, false);
            cardStack.put(i, s);

            observableGameState.faceUpCard(i).addListener((o, oV, nV)->{
                if(oV != null){
                    s.getStyleClass().remove(oV.color().toString()); // TODO probleme
                }
                s.getStyleClass().add(nV.color().toString());
            });
        }

        vBox.getChildren().add(createButton("Cartes", observableGameState.cardPercentage()));
        vBox.getChildren().addAll(cardStack.values());
        vBox.getChildren().add(createButton("Tickets", observableGameState.ticketPercentage()));


        return vBox;
    }

    private static Button createButton(String buttonName, ReadOnlyIntegerProperty property){
        Rectangle backgroundRectangle = new Rectangle(50, 5);
        backgroundRectangle.getStyleClass().add("background");
        Rectangle foregroundRectangle = new Rectangle(property.multiply(50).divide(100).get(), 5);
        foregroundRectangle.getStyleClass().add("foreground");
        foregroundRectangle.widthProperty().bind(property.multiply(50).divide(100));

        Group group = new Group();
        group.getChildren().addAll(backgroundRectangle, foregroundRectangle);

        Button button = new Button(buttonName);
        button.getStyleClass().add("gauged");
        button.setGraphic(group);

        return button;
    }

    private static StackPane stackPaneCreator(Card card, ReadOnlyIntegerProperty count , Boolean displayCounter){ //TODO Card inutile
        Rectangle rectangleOutside = new Rectangle(60, 90);
        rectangleOutside.getStyleClass().add("outside");
        Rectangle rectangleInside = new Rectangle(40, 70);
        rectangleInside.getStyleClass().addAll("filled", "inside");
        Rectangle rectangleImage = new Rectangle(40, 70);
        rectangleImage.getStyleClass().add("train-image");
        Text counter = new Text();
        counter.getStyleClass().add("count");
        if(count != null){
            counter.textProperty().bind(Bindings.convert(count)); //TODO juste comme ça
            counter.visibleProperty().bind(Bindings.greaterThan(count, 0)); //TODO juste comme ça
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
