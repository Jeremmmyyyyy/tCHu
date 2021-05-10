package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.game.PlayerState;
import ch.epfl.tchu.game.PublicGameState;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.ArrayDeque;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class GraphicalPlayer {

    private final ObservableGameState observableGameState;
    private final PlayerId playerId;
    private final Map<PlayerId, String> playerNames;


    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        this.playerId = playerId;
        this.playerNames = playerNames;
        observableGameState = new ObservableGameState(playerId);

        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRouteHandler, cardChooser);
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
        observableGameState.setState(publicGameState, playerState);
    }

    public void receiveInfo(String message){
        Queue<String> infos = new LinkedList<>();
    }

    public void startTurn(){}

    public void chooseTickets(){}

    public void drawCard(){}

    public void chooseClaimCards(){}

    public void chooseAdditionalCards(){}

}
