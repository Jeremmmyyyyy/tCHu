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
 * GraphicalPlayer that creates the whole GUI for a player
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

        //Creates the different nodes of the gui
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
     * @param publicGameState used to update the observableGameState
     * @param playerState used to update the observableGameState
    */
    public void setState(PublicGameState publicGameState, PlayerState playerState){
        assert isFxApplicationThread();
        observableGameState.setState(publicGameState, playerState);
    }

    /**
     * Adds the given message to the InfoView which display only the 5 last messages
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
     * Different ActionHandlers are given so that for each TurnKind the right Handler
     * is called and completes the authorised actions
     * @param drawTicketsHandler to handle a DRAW_TICKETS call
     * @param drawCardHandler to handle a DRAW_CARDS call
     * @param claimRouteHandler to handle a CLAIM_ROUTE call
     */
    public void startTurn(DrawTicketsHandler drawTicketsHandler, DrawCardHandler drawCardHandler,
                          ClaimRouteHandler claimRouteHandler){
        assert isFxApplicationThread();

        //TurnKind = DRAW_TICKETS
        drawTicketsHandlerProperty.set(observableGameState.canDrawTickets() ? () -> {
            drawTicketsHandler.onDrawTickets();
            clearHandlerProperties();
        } : null);

        //TurnKind = DRAW_CARDS
        drawCardHandlerProperty.set(observableGameState.canDrawCards() ? drawSlot -> {
            drawCardHandler.onDrawCard(drawSlot);
            clearHandlerProperties();
        } : null);

        //TurnKind = CLAIM_ROUTE (always filled)
        claimRouteHandlerProperty.set((route, cards) -> {
            claimRouteHandler.onClaimRoute(route, cards);
            clearHandlerProperties();
        });

    }

    /**
     * Method called above, used to set all handlerProperties to null in order to prevent the player from playing an
     * other additional action
     */
    private void clearHandlerProperties() {
        drawTicketsHandlerProperty.set(null);
        drawCardHandlerProperty.set(null);
        claimRouteHandlerProperty.set(null);
    }

    /**
     * Takes a List of tickets and pop-ups a window so that the player can choose the wished Tickets
     * @param tickets SortedBag of tickets (5 at the beginning of the game and 3 otherwise)
     * @param chooseTicketsHandler ActionHandler that handles which tickets are chosen
     */
    public void chooseTickets(SortedBag<Ticket> tickets, ChooseTicketsHandler chooseTicketsHandler){
        assert isFxApplicationThread();
        Preconditions.checkArgument(tickets.size() == INITIAL_TICKETS_COUNT ||
                tickets.size() == IN_GAME_TICKETS_COUNT);

        //Arguments needed for stageCreator(...)
        ListView<Ticket> ticketsView = new ListView<>(FXCollections.observableList(tickets.toList()));
        Button button = new Button(CHOOSE);
        int minimumDrawCount = tickets.size() - DISCARDABLE_TICKETS_COUNT;

        //Creates the choiceStage
        Stage choiceStage = stageCreator(
                new Text(String.format(CHOOSE_TICKETS, minimumDrawCount,
                        plural(minimumDrawCount))), button, ticketsView);

        //Specific to chooseTickets(...)
        choiceStage.setTitle(TICKETS_CHOICE);
        button.disableProperty().bind(
                Bindings.lessThan(Bindings.size(ticketsView.getSelectionModel().getSelectedItems()), minimumDrawCount));
        button.setOnAction(e -> {
            choiceStage.hide();
            chooseTicketsHandler.onChooseTickets(SortedBag.of(ticketsView.getSelectionModel().getSelectedItems()));
        });

    }

    /**
     * Authorises the player to draw a card from the deck (button "Cartes") or from the FaceUpCards
     * Especially called on the second obligatory draw
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
     * Pop-ups a window with the different card possibilities to claim a route
     * @param initialClaimCards list of the different possible cards that can claim the route
     * @param chooseCardsHandler ActionHandler that handles which Card possibility is used to take the route
     */
    public void chooseClaimCards(List<SortedBag<Card>> initialClaimCards, ChooseCardsHandler chooseCardsHandler){
        assert isFxApplicationThread();

        //Selection button can't be clicked without an empty selection, thus "true" argument
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

        //Selection button can be clicked without any choice selected, thus "false" argument
        createCardsStage(false, new Text(CHOOSE_ADDITIONAL_CARDS), possibleAdditionalCards, chooseCardsHandler);
    }

    /**
     * Method called by chooseTickets(...) or createCardsStage(...), used to create a generic stage
     * @param text name of the selection box
     * @param button choiceButton
     * @param listView options on selection
     * @param <E> parameter type (Ticket or SortedBag<Card>)
     * @return a new generic stage for multiple selection window
     */
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

        //Arguments needed for stageCreator
        ListView<SortedBag<Card>> cardsView = new ListView<>(FXCollections.observableList(cards));
        cardsView.setCellFactory(v -> new TextFieldListCell<>(new CardBagStringConverter()));
        Button button = new Button(CHOOSE);

        //Creates the choiceStage
        Stage choiceStage = stageCreator(text, button, cardsView);

        //Specific to chooseClaimCards(...) and chooseAdditionalCards(...)
        choiceStage.setTitle(CARDS_CHOICE);
        if (buttonCanBeDisabled) {
            //Specific to chooseAdditionalCards : empty selection means the player didn't want to play additional cards
            button.disableProperty().bind(Bindings.isEmpty(cardsView.getSelectionModel().getSelectedItems()));
        }
        button.setOnAction(e -> {
            choiceStage.hide();
            SortedBag<Card> selectedCards = cardsView.getSelectionModel().getSelectedItem();
            //Handles an empty sortedBag in case of an empty selection
            chooseCardsHandler.onChooseCards(selectedCards == null ? SortedBag.of() : SortedBag.of(selectedCards));
        });

    }

    /**
     * Nested class that displays a convenient StringFormat for a SortedBag
     */
    private static final class CardBagStringConverter extends StringConverter<SortedBag<Card>> {

        /**
         * Overridden toString method for a sortedBag of Card
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
         * Overridden fromString method that throws UnsupportedOperationException when called.
         * The inverse operation of toString is not used in this project so it has no specific implementation
         * @param string to convert into a SortedBag
         * @return throws an UnsupportedOperationException
         */
        @Override
        public SortedBag<Card> fromString(String string) {
            throw new UnsupportedOperationException();
        }
    }


}
