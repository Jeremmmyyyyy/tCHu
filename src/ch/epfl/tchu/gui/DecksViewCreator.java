package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Color;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;


final class DecksViewCreator {

    public static Node createHandView(ObservableGameState observableGameState){
        HBox hBoxMain = new HBox();
        hBoxMain.getStylesheets().add("deck.css");
        hBoxMain.getStylesheets().add("colors.css");

        HBox hBoxTickets = new HBox();
        hBoxTickets.setId("hand-pane");

        ObservableList<Ticket> observableList = FXCollections.unmodifiableObservableList(FXCollections.observableList(ChMap.tickets()));
        ListView<Ticket> listView = new ListView<Ticket>(observableList);
        listView.setId("tickets");

        hBoxMain.getChildren().addAll(hBoxTickets, listView);

        Rectangle rectangleOutside = new Rectangle();
        rectangleOutside.getStyleClass().add("outside");
        Rectangle rectangleInside = new Rectangle();
        rectangleInside.getStyleClass().addAll("filled", "inside");
        Rectangle rectangleImage = new Rectangle();
        rectangleImage.getStyleClass().add("train-image");
        Text counter = new Text();
        counter.getStyleClass().add("count");

        List<StackPane> stackPaneList = new ArrayList<>();
        for (Card color : Card.values()) {
            StackPane stackPane = new StackPane();
            if (color.color() == null){
                stackPane.getStyleClass().addAll("NEUTRAL", "card");
            }else{
                stackPane.getStyleClass().addAll(color.color().toString(), "card");
            }
            stackPane.getChildren().addAll(rectangleOutside, rectangleInside, rectangleImage, counter);
            stackPaneList.add(stackPane);
        }
        stackPaneList.forEach(stackPane -> hBoxTickets.getChildren().add(stackPane));

        return hBoxMain;
    }



    public static Node createCardsView(ObservableGameState observableGameState,
                                ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketHandler,
                                ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandler){


        return null;
    }

}
