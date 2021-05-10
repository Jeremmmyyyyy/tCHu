package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;
import com.sun.javafx.collections.ElementObservableListDecorator;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

import static javafx.application.Platform.isFxApplicationThread;

public class GraphicalPlayer {

    private final int NUMBER_OF_DISPLAYED_MESSAGES = 5;
    private final ObservableGameState observableGameState;
    private final PlayerId playerId;
    private final Map<PlayerId, String> playerNames;
    private final ObservableList<Text> gameMessages = new SimpleListProperty<>();

    private final ObjectProperty<DrawTicketsHandler> drawTicketHandler;
    private final ObjectProperty<DrawCardHandler> drawCardHandler;
    private final ObjectProperty<ClaimRouteHandler> claimRouteHandler;


    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        assert isFxApplicationThread();

        this.playerId = playerId;
        this.playerNames = playerNames;
        observableGameState = new ObservableGameState(playerId);

        this.drawTicketHandler = new SimpleObjectProperty<>();
        this.drawCardHandler = new SimpleObjectProperty<>();
        this.claimRouteHandler = new SimpleObjectProperty<>();

        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRouteHandler, null);
        Node cardsView = DecksViewCreator
                .createCardsView(observableGameState, drawTicketHandler, drawCardHandler);
        Node handView = DecksViewCreator
                .createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, gameMessages);

        Stage mainStage = new Stage();
        BorderPane borderPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        mainStage.setScene(borderPane.getScene());

        mainStage.setTitle("tCHu \u2014 " + playerNames.get(playerId));

        mainStage.show();


    }

    public void setState(PublicGameState publicGameState, PlayerState playerState){
        assert isFxApplicationThread();

        observableGameState.setState(publicGameState, playerState);
    }

    public void receiveInfo(String message){
        assert isFxApplicationThread();

        if (gameMessages.size() == NUMBER_OF_DISPLAYED_MESSAGES){ //TODO bonne maniere de faire
            gameMessages.remove(0);
        }
        gameMessages.add(new Text(message));
    }

    public void startTurn(DrawTicketsHandler drawTicketsHandler, DrawCardHandler drawCardHandler,
                          ClaimRouteHandler claimRouteHandler){
        assert isFxApplicationThread();

        this.drawTicketHandler.set(observableGameState.canDrawTickets() ? drawTicketsHandler : null); //TODO comme ca ?
        this.drawCardHandler.set(observableGameState.canDrawCards() ? drawCardHandler : null);
        this.claimRouteHandler.set(claimRouteHandler);
    }

    public void chooseTickets(){}

    public void drawCard(DrawCardHandler drawCardHandler){
        assert isFxApplicationThread();
        this.drawCardHandler.set(drawCardHandler);
        this.drawTicketHandler.set(null); //TODO pour vider ??
        this.claimRouteHandler.set(null);

    }

    public void chooseClaimCards(){
        assert isFxApplicationThread();

    }

    public void chooseAdditionalCards(){
        assert isFxApplicationThread();

    }

}
