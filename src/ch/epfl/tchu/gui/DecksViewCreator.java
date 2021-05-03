package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Ticket;
import javafx.application.Application;
import javafx.beans.Observable;
import javafx.beans.property.ObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;


final class DecksViewCreator {

    public Node createHandView(ObservableGameState observableGameState){
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

        StackPane stackPaneBlack = new StackPane();
        stackPaneBlack.getStyleClass().addAll("BLACK", "card");
        stackPaneBlack.getChildren().addAll(rectangleOutside, rectangleInside, rectangleImage, counter);

        return hBoxMain;
    }

    public Node createCardsView(ObservableGameState observableGameState,
                                ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketHandler,
                                ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandler){


        return null;
    }

}
