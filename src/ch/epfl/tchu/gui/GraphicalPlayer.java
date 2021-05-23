package ch.epfl.tchu.gui;

import ch.epfl.tchu.Preconditions;
import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.ActionHandlers.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
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

import static ch.epfl.tchu.game.Constants.*;
import static ch.epfl.tchu.gui.StringsFr.*;
import static javafx.application.Platform.isFxApplicationThread;

public final class GraphicalPlayer {

    private static final int NUMBER_OF_DISPLAYED_MESSAGES = 5;

    private final ObservableGameState observableGameState;
    private final ObservableList<Text> gameMessages;

    private final ObjectProperty<DrawTicketsHandler> drawTicketsHandlerProperty;
    private final ObjectProperty<DrawCardHandler> drawCardHandlerProperty;
    private final ObjectProperty<ClaimRouteHandler> claimRouteHandlerProperty;

    private final Stage mainStage;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        assert isFxApplicationThread();

        observableGameState = new ObservableGameState(playerId);
        gameMessages = FXCollections.observableArrayList();

        drawTicketsHandlerProperty = new SimpleObjectProperty<>();
        drawCardHandlerProperty = new SimpleObjectProperty<>();
        claimRouteHandlerProperty = new SimpleObjectProperty<>();

        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRouteHandlerProperty, this::chooseClaimCards); //TODO demander comment ca marche
        Node cardsView = DecksViewCreator
                .createCardsView(observableGameState, drawTicketsHandlerProperty, drawCardHandlerProperty);
        Node handView = DecksViewCreator
                .createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerId, playerNames, observableGameState, gameMessages);

        mainStage = new Stage();
        mainStage.setScene(new Scene(new BorderPane(mapView, null, cardsView, handView, infoView)));

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

//        drawCardHandlerProperty.set(observableGameState.canDrawCards() ? drawSlot -> {
//            drawCardHandler.onDrawCard(drawSlot);
//            clearHandlerProperties();
//        } : null);
//
//        drawTicketsHandlerProperty.set(observableGameState.canDrawTickets() ? () -> {
//            drawTicketsHandler.onDrawTickets();
//            clearHandlerProperties();
//        } : null);
//
//        claimRouteHandlerProperty.set((route, cards) -> {
//            claimRouteHandler.onClaimRoute(route, cards);
//            clearHandlerProperties();
//        });

        if (observableGameState.canDrawTickets()) {
            drawTicketsHandlerProperty.set(() -> {
                drawTicketsHandler.onDrawTickets();
                drawCardHandlerProperty.set(null);
                claimRouteHandlerProperty.set(null);
            });
        } else
            drawTicketsHandlerProperty.set(null);

        if (observableGameState.canDrawCards()) {
            drawCardHandlerProperty.set(drawSlot -> {
                drawCardHandler.onDrawCard(drawSlot);
                drawTicketsHandlerProperty.set(null);
                claimRouteHandlerProperty.set(null);
            });
        } else
            drawTicketsHandlerProperty.set(null);

        claimRouteHandlerProperty.set((route, cards) -> {
            claimRouteHandler.onClaimRoute(route, cards);
            drawTicketsHandlerProperty.set(null);
            drawCardHandlerProperty.set(null);
        });

    }

    private void clearHandlerProperties() {
        drawTicketsHandlerProperty.set(null);
        drawCardHandlerProperty.set(null);
        claimRouteHandlerProperty.set(null);
    }

    public void chooseTickets(SortedBag<Ticket> tickets, ChooseTicketsHandler chooseTicketsHandler){
        assert isFxApplicationThread();

        Preconditions.checkArgument(tickets.size() == INITIAL_TICKETS_COUNT ||
                tickets.size() == IN_GAME_TICKETS_COUNT);

        ListView<Ticket> ticketsView = new ListView<>(FXCollections.observableList(tickets.toList()));

        Button button = new Button("Choisir");

        int ticketsSize = tickets.size();

        Stage choiceStage = stageCreator(
                new Text(String.format(CHOOSE_TICKETS, ticketsSize, plural(ticketsSize))), button, ticketsView);

        button.disableProperty().bind(Bindings.lessThan(
                    Bindings.size(ticketsView.getSelectionModel().getSelectedItems()),
                    ticketsSize - Constants.DISCARDABLE_TICKETS_COUNT));
        button.setOnAction(e -> {
            choiceStage.hide();
            chooseTicketsHandler.onChooseTickets(SortedBag.of(ticketsView.getSelectionModel().getSelectedItems()));
        });

    }

    public void drawCard(DrawCardHandler drawCardHandler){ //doit juste mettre à jour
        assert isFxApplicationThread();

        this.drawCardHandlerProperty.set(drawSlot-> {
            drawCardHandler.onDrawCard(drawSlot);
            clearHandlerProperties();
        });

    }

    public void chooseClaimCards(List<SortedBag<Card>> initialClaimCards, ChooseCardsHandler chooseCardsHandler){
        assert isFxApplicationThread();

        createCardsStage(true, new Text(CHOOSE_CARDS), initialClaimCards, chooseCardsHandler);

    }

    public void chooseAdditionalCards(List<SortedBag<Card>> possibleAdditionalCards,
                                      ChooseCardsHandler chooseCardsHandler){
        assert isFxApplicationThread();

        createCardsStage(false, new Text(CHOOSE_ADDITIONAL_CARDS), possibleAdditionalCards, chooseCardsHandler);
    }


    private <E> Stage stageCreator(Text text, Button button, ListView<E> listView) {

        Stage choiceStage = new Stage(StageStyle.UTILITY);
        choiceStage.initOwner(mainStage);
        choiceStage.initModality(Modality.APPLICATION_MODAL);
        choiceStage.setOnCloseRequest(Event::consume);

        //Enables multiple selection
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        VBox vBox = new VBox(new TextFlow(text), listView, button);

        Scene scene = new Scene(vBox);
        scene.getStylesheets().add("chooser.css");

        choiceStage.setScene(scene);
        choiceStage.show();

        return choiceStage;
    }

    private void createCardsStage(boolean buttonCanBeDisabled, Text text, List<SortedBag<Card>> cards,
                                  ChooseCardsHandler chooseCardsHandler) {

        ListView<SortedBag<Card>> cardsView = new ListView<>(FXCollections.observableList(cards));
        cardsView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));

        Button button = new Button("Choisir");

        Stage choiceStage = stageCreator(text, button, cardsView);

        if (buttonCanBeDisabled) {
            button.disableProperty().bind(Bindings.isEmpty(cardsView.getSelectionModel().getSelectedItems()));
        }
        button.setOnAction(e -> {
            choiceStage.hide();
            chooseCardsHandler.onChooseCards(SortedBag.of(cardsView.getSelectionModel().getSelectedItem())); //TODO getSelectecItems ?
        });

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
