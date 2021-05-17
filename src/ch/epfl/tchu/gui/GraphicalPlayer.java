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
    private final PlayerId playerId;
    private final Map<PlayerId, String> playerNames; //TODO enlever ces deux attributs ??
    private final ObservableList<Text> gameMessages;

    private final ObjectProperty<DrawTicketsHandler> drawTicketsHandler;
    private final ObjectProperty<DrawCardHandler> drawCardHandler;
    private final ObjectProperty<ClaimRouteHandler> claimRouteHandler;

    private final Stage mainStage;

    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        assert isFxApplicationThread();

        this.playerId = playerId;
        this.playerNames = playerNames;
        observableGameState = new ObservableGameState(playerId);
        gameMessages = FXCollections.observableArrayList();

        this.drawTicketsHandler = new SimpleObjectProperty<>();
        this.drawCardHandler = new SimpleObjectProperty<>();
        this.claimRouteHandler = new SimpleObjectProperty<>();

        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRouteHandler, this::chooseClaimCards); //TODO demander comment ca marche
        Node cardsView = DecksViewCreator
                .createCardsView(observableGameState, drawTicketsHandler, drawCardHandler);
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
        gameMessages.add(new Text(message + "\n"));
    }

    public void startTurn(DrawTicketsHandler drawTicketsHandler, DrawCardHandler drawCardHandler,
                          ClaimRouteHandler claimRouteHandler){
        assert isFxApplicationThread();

        this.drawCardHandler.set(observableGameState.canDrawCards() ? drawSlot -> {
            drawCardHandler.onDrawCard(drawSlot); //TODO comme ca ?
            this.drawTicketsHandler.set(null);
            this.claimRouteHandler.set(null);
            this.drawCardHandler.set(null);
        } : null);

        this.drawTicketsHandler.set(observableGameState.canDrawTickets() ? () -> {
            drawTicketsHandler.onDrawTickets();
            this.drawCardHandler.set(null);
            this.claimRouteHandler.set(null);
            this.drawTicketsHandler.set(null);
        } : null);

        this.claimRouteHandler.set((route, cards) -> {
            claimRouteHandler.onClaimRoute(route, cards);
            this.drawTicketsHandler.set(null);
            this.drawCardHandler.set(null);
            this.claimRouteHandler.set(null);
        });

    }

    public void chooseTickets(SortedBag<Ticket> tickets, ChooseTicketsHandler chooseTicketsHandler){
        assert isFxApplicationThread();
        System.out.println("entre dans chooseticket");

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

        this.drawCardHandler.set(drawSlot-> {
            drawCardHandler.onDrawCard(drawSlot);
            this.drawTicketsHandler.set(null); //TODO setValue ou set ??
            this.claimRouteHandler.set(null);
            this.drawCardHandler.set(null); //TODO aussi cette ligne ?
        });

    }

    public void chooseClaimCards(List<SortedBag<Card>> initialClaimCards, ChooseCardsHandler chooseCardsHandler){
        assert isFxApplicationThread();

        System.out.println("entre dans chooseClaimCards");

        createCardsStage(true, new Text(CHOOSE_CARDS), initialClaimCards, chooseCardsHandler);
//        ListView<SortedBag<Card>> cardsView = new ListView<>(FXCollections.observableList(initialClaimCards));
//        cardsView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
//
//        Button button = new Button("Choisir");
//
//        Stage choiceStage = new Stage(StageStyle.UTILITY);
//        choiceStage.initOwner(mainStage);
//        choiceStage.initModality(Modality.APPLICATION_MODAL);
////        choiceStage.setOnCloseRequest(Event::consume);
//
//        //Rend la sélection multiple possible sur la liste
//        cardsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//        //BIND
//        VBox vBox = new VBox(new Text(CHOOSE_CARDS), cardsView, button);
//
////        Scene scene = vBox.getScene();
//        Scene scene = new Scene(vBox);
//        scene.getStylesheets().add("chooser.css");
////        vBox.getStylesheets().add("chooser.css");
//
//
//        choiceStage.setScene(scene);
//        choiceStage.show();

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

        //Rend la sélection multiple possible sur la liste
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        //BIND
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

//    private void stageCreator(Text text,
//                              boolean typeOfEntry,
//                              SortedBag<Ticket> tickets,
//                              List<SortedBag<Card>> sortedBagList,
//                              ActionHandlers actionHandler){
//
//        Stage choiceStage = new Stage(StageStyle.UTILITY);
//        choiceStage.initOwner(mainStage);
//        choiceStage.initModality(Modality.APPLICATION_MODAL);
//        choiceStage.setOnCloseRequest(Event::consume);
//
//        VBox vBox = new VBox();
//
//        Button button = new Button("Choisir");
//
//        if (typeOfEntry){
//
//            ListView<Ticket> ticketsView = new ListView<>(FXCollections.observableList(tickets.toList()));
//            //Rend la sélection multiple possible sur la liste
//            ticketsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//            //BIND
//            button.disableProperty().bind(Bindings.lessThan(
//                    Bindings.size(ticketsView.getSelectionModel().getSelectedItems()),
//                    tickets.size() - Constants.DISCARDABLE_TICKETS_COUNT));
//            vBox.getChildren().addAll(new TextFlow(text), ticketsView, button);
//            button.setOnAction(e ->  {
//                ((ChooseTicketsHandler)actionHandler).onChooseTickets(SortedBag.of(ticketsView.getSelectionModel().getSelectedItems()));
//                choiceStage.hide();
//            });
//
//        }else{
//
//            ListView<SortedBag<Card>> cardsView = new ListView<>(FXCollections.observableList(sortedBagList));
//            cardsView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
//            cardsView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
//            button.disableProperty().bind(Bindings.lessThan(Bindings.size(cardsView.getSelectionModel().getSelectedItems()), 1));
//            button.setOnAction(e ->  {
//                choiceStage.hide();
//                ((ChooseCardsHandler)actionHandler).onChooseCards(SortedBag.of(cardsView.getSelectionModel().getSelectedItem()));
//            });
//
//        }
//
//        Scene scene = vBox.getScene();
//        scene.getStylesheets().add("chooser.css");
//
//        choiceStage.setScene(vBox.getScene());
//        choiceStage.show();
//
//    }

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
