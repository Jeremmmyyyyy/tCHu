package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.ActionHandlers.ChooseCardsHandler;
import ch.epfl.tchu.gui.ActionHandlers.ClaimRouteHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawCardHandler;
import ch.epfl.tchu.gui.ActionHandlers.DrawTicketsHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Constants.INITIAL_TICKETS_COUNT;
import static ch.epfl.tchu.game.Constants.IN_GAME_TICKETS_COUNT;
import static ch.epfl.tchu.gui.StringsFr.*;
import static javafx.application.Platform.isFxApplicationThread;

public final class GraphicalPlayer {

    private static final int NUMBER_OF_DISPLAYED_MESSAGES = 5;

    private final ObservableGameState observableGameState;
    private final PlayerId playerId;
    private final Map<PlayerId, String> playerNames;
    private final ObservableList<Text> gameMessages;

    private final ObjectProperty<DrawTicketsHandler> drawTicketHandler;
    private final ObjectProperty<DrawCardHandler> drawCardHandler;
    private final ObjectProperty<ClaimRouteHandler> claimRouteHandler;

    private final Stage mainStage;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        assert isFxApplicationThread();

        this.playerId = playerId;
        this.playerNames = playerNames;
        observableGameState = new ObservableGameState(playerId);
        gameMessages = FXCollections.observableArrayList();

        this.drawTicketHandler = new SimpleObjectProperty<>();
        this.drawCardHandler = new SimpleObjectProperty<>();
        this.claimRouteHandler = new SimpleObjectProperty<>();

        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRouteHandler, null); //TODO cardChooser ?
        Node cardsView = DecksViewCreator
                .createCardsView(observableGameState, drawTicketHandler, drawCardHandler);
        Node handView = DecksViewCreator
                .createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, gameMessages);

        mainStage = new Stage();
        BorderPane borderPane = new BorderPane(mapView, null, cardsView, handView, infoView);
        mainStage.setScene(new Scene(borderPane));

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

        this.drawTicketHandler.set(observableGameState.canDrawTickets() ? drawTicketsHandler : null);
        this.drawCardHandler.set(observableGameState.canDrawCards() ? drawCardHandler : null);
        this.claimRouteHandler.set(claimRouteHandler);
    }

    public void chooseTickets(SortedBag<Ticket> tickets, DrawTicketsHandler drawTicketsHandler){
        assert isFxApplicationThread();
        Preconditions.checkArgument(tickets.size() == IN_GAME_TICKETS_COUNT); //TODO ?
        Preconditions.checkArgument(tickets.size() == INITIAL_TICKETS_COUNT);

        //Boîtes de dialogue modales
        Stage choiceStage = new Stage(StageStyle.UTILITY);
        choiceStage.initOwner(mainStage);
        choiceStage.initModality(Modality.APPLICATION_MODAL);

        VBox vBox = new VBox();

        //Remplace les % de la constante par le nombre de tickets
        int ticketsSize = tickets.size();
        Text actionText = new Text(String.format(CHOOSE_TICKETS, ticketsSize, plural(ticketsSize)));

        ListView<Ticket> ticketsView = new ListView<>(FXCollections.observableList(tickets.toList()));
        //Rend la sélection multiple possible sur la liste
        ticketsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        Button button = new Button("Choisir");
//        button.disableProperty().bind(Bindings.size(ticketsView.getSelectionModel().getSelectedItems()) < ticketsSize - Constants.DISCARDABLE_TICKETS_COUNT);
//        button.setOnAction(e ->  drawTicketsHandler.onDrawTickets(ticketsView.getSelectionModel().getSelectedItems())); //TODO

        vBox.getChildren().addAll(new TextFlow(actionText), ticketsView, button);

        Scene scene = vBox.getScene();
        scene.getStylesheets().add("chooser.css");

        choiceStage.setScene(vBox.getScene());
        choiceStage.show();
    }

    public void drawCard(DrawCardHandler drawCardHandler){
        assert isFxApplicationThread();
        this.drawCardHandler.set(drawCardHandler);
        this.drawTicketHandler.set(null); //TODO pour vider ??
        this.claimRouteHandler.set(null);

    }

    public void chooseClaimCards(List<SortedBag<Card>> initialClaimCards, ChooseCardsHandler chooseCardsHandler){
        assert isFxApplicationThread();

        //Boîtes de dialogue modales
        Stage choiceStage = new Stage(StageStyle.UTILITY);
        choiceStage.initOwner(mainStage);
        choiceStage.initModality(Modality.APPLICATION_MODAL);

        VBox vBox = new VBox();

        //Remplace les % de la constante par le nombre de tickets
        Text actionText = new Text(CHOOSE_CARDS);

        ListView<SortedBag<Card>> cardsView = new ListView<>(FXCollections.observableList(initialClaimCards));
        //Rend la sélection multiple possible sur la liste
        cardsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cardsView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));

        Button button = new Button("Choisir");
//        button.setOnAction(e ->  drawTicketHandler.onDrawTickets(cardsView.getSelectionModel().getSelectedItems())); //TODO

        vBox.getChildren().addAll(new TextFlow(actionText), cardsView, button);

        Scene scene = vBox.getScene();
        scene.getStylesheets().add("chooser.css");

        choiceStage.setScene(vBox.getScene());
        choiceStage.show();

    }

    public void chooseAdditionalCards(List<SortedBag<Card>> possibleAdditionalCards,
                                      ChooseCardsHandler chooseCardsHandler){
        assert isFxApplicationThread();

        //Boîtes de dialogue modales
        Stage choiceStage = new Stage(StageStyle.UTILITY);
        choiceStage.initOwner(mainStage);
        choiceStage.initModality(Modality.APPLICATION_MODAL);

        VBox vBox = new VBox();

        //Remplace les % de la constante par le nombre de tickets
        Text actionText = new Text(CHOOSE_ADDITIONAL_CARDS);

        ListView<SortedBag<Card>> cardsView = new ListView<>(FXCollections.observableList(possibleAdditionalCards));
        //Rend la sélection multiple possible sur la liste
        cardsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        cardsView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));

        Button button = new Button("Choisir");
//        button.disableProperty().bind();
//        button.setOnAction(e ->  drawTicketHandler.onDrawTickets(cardsView.getSelectionModel().getSelectedItems())); //TODO

        vBox.getChildren().addAll(new TextFlow(actionText), cardsView, button);

        Scene scene = vBox.getScene();
        scene.getStylesheets().add("chooser.css");

        choiceStage.setScene(vBox.getScene());
        choiceStage.show();

    }

    static final class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

        @Override
        public String toString(SortedBag<Card> object) {
            List<String> toStrings = new ArrayList<>();
            for (Card card : object) {
                int countOf = object.countOf(card);
                String toString = String.format("%s %s", countOf, Info.cardName(card, countOf));
                if (!toStrings.contains(toString)) {
                    toStrings.add(toString);
                }
            }
            return String.join(AND_SEPARATOR, toStrings);
        }

        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }


}
