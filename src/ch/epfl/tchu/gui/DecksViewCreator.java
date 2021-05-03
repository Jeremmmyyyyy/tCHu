package ch.epfl.tchu.gui;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import org.w3c.dom.Node;

final class DecksViewCreator {

    public Node createHandView(ObservableGameState observableGameState){
        HBox hBoxMain = new HBox();
        hBoxMain.getStylesheets().add("deck.css");
        hBoxMain.getStylesheets().add("colors.css");

        HBox hBoxTickets = new HBox();
        hBoxTickets.setId("hand-pane");

        ListView listView = new ListView();
        listView.setId("tickets");

        hBoxMain.getChildren().addAll(hBoxTickets, listView);
        return null;
    }

    public Node createCardsView(ObservableGameState observableGameState,
                                ObjectProperty<ActionHandlers.DrawTicketHandler> drawTicketHandler,
                                ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandler){


        return null;
    }

}
