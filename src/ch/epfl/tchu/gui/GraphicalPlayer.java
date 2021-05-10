package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.Map;


public class GraphicalPlayer {

    private final int NUMBER_OF_DISPLAYED_MESSAGES = 5;
    private final ObservableGameState observableGameState;
    private final PlayerId playerId;
    private final Map<PlayerId, String> playerNames;
    private ObservableList<Text> gameMessages = new SimpleListProperty<>();

    private final ObjectProperty<DrawTicketsHandler> drawTicketHandler;
    private final ObjectProperty<DrawCardHandler> drawCardHandler;
    private final ObjectProperty<ClaimRouteHandler> claimRouteHandler;

    private final Stage mainStage;


    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        this.playerId = playerId;
        this.playerNames = playerNames;
        observableGameState = new ObservableGameState(playerId);

        this.drawTicketHandler = new SimpleObjectProperty<>();
        this.drawCardHandler = new SimpleObjectProperty<>();
        this.claimRouteHandler = new SimpleObjectProperty<>();

        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRouteHandler, cardChooser);
        Node cardsView = DecksViewCreator
                .createCardsView(observableGameState, drawTicketHandler, drawCardHandler);
        Node handView = DecksViewCreator
                .createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, gameMessages);

        mainStage = new Stage();
        BorderPane borderPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        mainStage.setScene(borderPane.getScene());

        mainStage.setTitle("tCHu \u2014 " + playerNames.get(playerId));

        mainStage.show();


    }

    public void setState(PublicGameState publicGameState, PlayerState playerState){
        observableGameState.setState(publicGameState, playerState);
    }

    public void receiveInfo(String message){
        if (gameMessages.size() == NUMBER_OF_DISPLAYED_MESSAGES){ //TODO bonne maniere de faire
            gameMessages.remove(0);
        }
        gameMessages.add(new Text(message));
    }

    public void startTurn(DrawTicketsHandler drawTicketsHandler, DrawCardHandler drawCardHandler,
                          ClaimRouteHandler claimRouteHandler){
        this.drawTicketHandler.set(observableGameState.canDrawTickets() ? drawTicketsHandler : null); //TODO comme ca ?
        this.drawCardHandler.set(observableGameState.canDrawCards() ? drawCardHandler : null);
        this.claimRouteHandler.set(claimRouteHandler);
    }

    public void chooseTickets(SortedBag<Ticket> tickets, ActionHandlers.DrawTicketsHandler drawTicketsHandler){
        Preconditions.checkArgument(tickets.size() == Constants.IN_GAME_TICKETS_COUNT);
        Preconditions.checkArgument(tickets.size() == Constants.INITIAL_TICKETS_COUNT);

        //Boîtes de dialogue modales
        Stage choiceStage = new Stage(StageStyle.UTILITY);
        choiceStage.initOwner(mainStage);
        choiceStage.initModality(Modality.APPLICATION_MODAL);

        VBox vBox = new VBox();
        vBox.getStylesheets().add("chooser.css");

        //Remplace les % de la constante par le nombre de tickets
        Text textForTextFlow = new Text(StringsFr.CHOOSE_TICKETS.replace("%", String.valueOf(tickets.size())));
        TextFlow textFlow = new TextFlow(textForTextFlow);

        ObservableList<Ticket> ticketObservableList = FXCollections.observableList(tickets.toList());
        ListView<Ticket> listView = new ListView<>(ticketObservableList);
        //Rend la sélection multiple possible sur la liste
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button button = new Button("Choisir");
        button.setOnAction(e ->  drawTicketsHandler.onDrawTickets(listView.getSelectionModel().getSelectedItems())); //TODO

        vBox.getChildren().addAll(textFlow, listView, button);
        choiceStage.setScene(vBox.getScene());
        choiceStage.show();


    }

    public void drawCard(DrawCardHandler drawCardHandler){
        this.drawCardHandler.set(drawCardHandler);
        this.drawTicketHandler.set(null); //TODO pour vider ??
        this.claimRouteHandler.set(null);

    }

    public void chooseClaimCards(){}

    public void chooseAdditionalCards(){}

}
