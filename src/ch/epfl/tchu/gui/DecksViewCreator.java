package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.Card;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Ticket;
import javafx.beans.property.ObjectProperty;
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

import java.util.List;


final class DecksViewCreator {

    public static Node createHandView(ObservableGameState observableGameState){
        HBox hBoxMain = new HBox();
        hBoxMain.getStylesheets().addAll("decks.css", "colors.css");

        HBox hBoxTickets = new HBox();
        hBoxTickets.setId("hand-pane");

        ObservableList<Ticket> observableList = FXCollections.unmodifiableObservableList(FXCollections.observableList(ChMap.tickets()));
        //TODO afficher que les Tickets actuelles du joueur avec observableGameState
        ListView<Ticket> listView = new ListView<>(observableList);
        listView.setId("tickets");

        hBoxMain.getChildren().addAll(listView, hBoxTickets);


        for (Card card : Card.ALL) {
            hBoxTickets.getChildren().add(stackPaneCreator(card, true));
        }

        return hBoxMain;
    }

    public static Node createCardsView(ObservableGameState observableGameState,
                                       ObjectProperty<ActionHandlers.DrawTicketsHandler> drawTicketHandler,
                                       ObjectProperty<ActionHandlers.DrawCardHandler> drawCardHandler){

        VBox vBox = new VBox();
        vBox.getStylesheets().addAll("decks.css", "colors.css");
        vBox.setId("card-pane");

        List<Card> tempCards = List.of(Card.BLUE, Card.RED, Card.RED, Card.YELLOW, Card.LOCOMOTIVE); // TODO changer liste par valeur de l'attribut plus tard

        vBox.getChildren().add(createButton("Cartes", 30));  // TODO remplacer par le pourcentage pour la jauge que le foreground
        for (Card card : tempCards) { vBox.getChildren().add(stackPaneCreator(card, false)); }
        vBox.getChildren().add(createButton("Tickets", 45)); // TODO remplacer par le pourcentage pour la jauge que le foreground

//        vBox.getChildren().addAll( // TODO pas possible ??
//                createButton("Cartes"),
//                tempCards.forEach(t -> stackPaneCreator(t, false)),
//                createButton("Tickets"));
        return vBox;
    }

    private static Button createButton(String buttonName, int percentage){
        Rectangle backgroundRectangle = new Rectangle(50, 5);
        backgroundRectangle.getStyleClass().add("background");
        Rectangle foregroundRectangle = new Rectangle(percentage, 5);
        foregroundRectangle.getStyleClass().add("foreground");

        Group group = new Group();
        group.getChildren().addAll(backgroundRectangle, foregroundRectangle);

        Button button = new Button(buttonName);
        button.getStyleClass().add("gauged");
        button.setGraphic(group);

        return button;
    }

    private static StackPane stackPaneCreator(Card card, Boolean displayCounter){
        Rectangle rectangleOutside = new Rectangle(60, 90);
        rectangleOutside.getStyleClass().add("outside");
        Rectangle rectangleInside = new Rectangle(40, 70);
        rectangleInside.getStyleClass().addAll("filled", "inside");
        Rectangle rectangleImage = new Rectangle(40, 70);
        rectangleImage.getStyleClass().add("train-image");
        Text counter = new Text();
        counter.getStyleClass().add("count");

        StackPane stackPane = new StackPane();
        if (card.color() == null){
            stackPane.getStyleClass().addAll("NEUTRAL", "card");
        }else{
            stackPane.getStyleClass().addAll(card.color().toString(), "card");
        }
        if (displayCounter){
            stackPane.getChildren().addAll(rectangleOutside, rectangleInside, rectangleImage, counter);
        }else{
            stackPane.getChildren().addAll(rectangleOutside, rectangleInside, rectangleImage);
        }

        return stackPane;
    }

}
