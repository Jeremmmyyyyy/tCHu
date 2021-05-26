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

/**
 * GraphicalPlayer that creates the whole GUI for the players
 * All the different possible actions are called on the javaFX thread
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class GraphicalPlayer {

    private static final int NUMBER_OF_DISPLAYED_MESSAGES = 5;

    private final ObservableGameState observableGameState;
    private final ObservableList<Text> gameMessages;

    private final ObjectProperty<DrawTicketsHandler> drawTicketsHandlerProperty;
    private final ObjectProperty<DrawCardHandler> drawCardHandlerProperty;
    private final ObjectProperty<ClaimRouteHandler> claimRouteHandlerProperty;

    private final Stage mainStage;

    /**
     * Creates the graphical part of the game composed by the 4 parts of the GUI and instantiate all the properties
     * @param playerId of the player who owns the current GraphicalPlayer (so the window can be called according to the playerName)
     * @param playerNames Map containing the PlayerId and their names
     */
    public GraphicalPlayer(PlayerId playerId, Map<PlayerId, String> playerNames){
        assert isFxApplicationThread();

        observableGameState = new ObservableGameState(playerId);
        gameMessages = FXCollections.observableArrayList();

        drawTicketsHandlerProperty = new SimpleObjectProperty<>();
        drawCardHandlerProperty = new SimpleObjectProperty<>();
        claimRouteHandlerProperty = new SimpleObjectProperty<>();

        Node mapView = MapViewCreator
                .createMapView(observableGameState, claimRouteHandlerProperty, this::chooseClaimCards);
        Node cardsView = DecksViewCreator
                .createCardsView(observableGameState, drawTicketsHandlerProperty, drawCardHandlerProperty);
        Node handView = DecksViewCreator
                .createHandView(observableGameState);
        Node infoView = InfoViewCreator.createInfoView(playerNames, observableGameState, gameMessages);

        mainStage = new Stage();
        mainStage.setScene(new Scene(new BorderPane(mapView, null, cardsView, handView, infoView)));

        mainStage.setTitle("tCHu \u2014 " + playerNames.get(playerId));

        mainStage.show();

    }

    /**
     * Calls the method of the same name in ObservableGameState
     * @param publicGameState that updates the old one
     * @param playerState that updates the old one
     */
    public void setState(PublicGameState publicGameState, PlayerState playerState){
        assert isFxApplicationThread();
        observableGameState.setState(publicGameState, playerState);
    }

    /**
     * Add the given message to the InfoView displays only the 5 last messages
     * @param message new message to add
     */
    public void receiveInfo(String message){
        assert isFxApplicationThread();
        if (gameMessages.size() == NUMBER_OF_DISPLAYED_MESSAGES){
            gameMessages.remove(0);
        }
        gameMessages.add(new Text(message));
    }

    /**
     * Is called at the beginning of a turn of a player so he can choose the TurnKind
     * Different ActionHandlers are given in argument so that for each TurnKind the right Handler is called and complete the authorised actions
     * @param drawTicketsHandler to handle what happens when the TurnKind DRAW_TICKETS is called
     * @param drawCardHandler to handle what happens when the TurnKind DRAW_CARDS is called
     * @param claimRouteHandler to handle what happens when the TurnKind CLAIM_ROUTE is called
     */
    public void startTurn(DrawTicketsHandler drawTicketsHandler, DrawCardHandler drawCardHandler,
                          ClaimRouteHandler claimRouteHandler){
        assert isFxApplicationThread();

        drawCardHandlerProperty.set(observableGameState.canDrawCards() ? drawSlot -> {
            drawCardHandler.onDrawCard(drawSlot);
            clearHandlerProperties();
        } : null);

        drawTicketsHandlerProperty.set(observableGameState.canDrawTickets() ? () -> {
            drawTicketsHandler.onDrawTickets();
            clearHandlerProperties();
        } : null);

        claimRouteHandlerProperty.set((route, cards) -> {
            claimRouteHandler.onClaimRoute(route, cards);
            clearHandlerProperties();
        });

    }

    private void clearHandlerProperties() {
        drawTicketsHandlerProperty.set(null);
        drawCardHandlerProperty.set(null);
        claimRouteHandlerProperty.set(null);
    }

    /**
     * Takes a List of tickets and pop-ups a window so the player can choose the wished Tickets
     * @param tickets SortedBag of tickets (5 at the beginning of the game and 3 otherwise)
     * @param chooseTicketsHandler ActionHandler that handles which tickets are chosen
     */
    public void chooseTickets(SortedBag<Ticket> tickets, ChooseTicketsHandler chooseTicketsHandler){
        assert isFxApplicationThread();

        Preconditions.checkArgument(tickets.size() == INITIAL_TICKETS_COUNT ||
                tickets.size() == IN_GAME_TICKETS_COUNT);

        ListView<Ticket> ticketsView = new ListView<>(FXCollections.observableList(tickets.toList()));

        Button button = new Button(CHOOSE);

        int ticketsSize = tickets.size();

        Stage choiceStage = stageCreator(
                new Text(String.format(CHOOSE_TICKETS, ticketsSize - DISCARDABLE_TICKETS_COUNT,
                        plural(ticketsSize - DISCARDABLE_TICKETS_COUNT))), button, ticketsView);

        choiceStage.setTitle(TICKETS_CHOICE);

        button.disableProperty().bind(Bindings.lessThan(
                    Bindings.size(ticketsView.getSelectionModel().getSelectedItems()),
                    ticketsSize - Constants.DISCARDABLE_TICKETS_COUNT));
        button.setOnAction(e -> {
            choiceStage.hide();
            chooseTicketsHandler.onChooseTickets(SortedBag.of(ticketsView.getSelectionModel().getSelectedItems()));
        });

    }

    /**
     * Authorise the player to draw a card from the deck (button "cartes") or from the FaceUpCards
     * @param drawCardHandler ActionHandler that handles which cards from which slot are chosen
     */
    public void drawCard(DrawCardHandler drawCardHandler){
        assert isFxApplicationThread();

        this.drawCardHandlerProperty.set(drawSlot-> {
            drawCardHandler.onDrawCard(drawSlot);
            clearHandlerProperties();
        });

    }

    /**
     * Pop-ups a window with the different card possibilities to take a route
     * @param initialClaimCards list of the different possible cards that can take the route
     * @param chooseCardsHandler ActionHandler that handles which Card possibility is used to take the route
     */
    public void chooseClaimCards(List<SortedBag<Card>> initialClaimCards, ChooseCardsHandler chooseCardsHandler){
        assert isFxApplicationThread();

        createCardsStage(true, new Text(CHOOSE_CARDS), initialClaimCards, chooseCardsHandler);

    }

    /**
     * Pop-ups a window with the different possibilities of additional cards to take a tunnel
     * @param possibleAdditionalCards SortedBag of the different possibilities the player has to take the tunnel
     * @param chooseCardsHandler ActionHandler that handles which AdditionalCards are used to take the tunnel
     */
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

        Button button = new Button(CHOOSE);

        Stage choiceStage = stageCreator(text, button, cardsView);

        choiceStage.setTitle(CARDS_CHOICE);

        if (buttonCanBeDisabled) {
            button.disableProperty().bind(Bindings.isEmpty(cardsView.getSelectionModel().getSelectedItems()));
        }
        button.setOnAction(e -> {
            choiceStage.hide();
            SortedBag<Card> selectedCards = cardsView.getSelectionModel().getSelectedItem();
            chooseCardsHandler.onChooseCards(selectedCards == null ? SortedBag.of() : SortedBag.of(selectedCards));
        });

    }

    /**
     * Nested Class that displays a convenient StringFormat for a SortedBag
     */
    private static final class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

        /**
         * toString method for a sortedBag
         * @param object SortedBag of Cards to display in String format
         * @return String for the given SortedBag
         */
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

        /**
         * fromString method that throw UnsupportedOperationException when called. The inverse operation of toString is not used in our case so we don't want to implement it
         * @param string to convert into a SortedBag
         * @return UnsupportedOperationException
         * @throws UnsupportedOperationException
         */
        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }


}
