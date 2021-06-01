package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.UncheckedIOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

public final class TutorialGraphicalPlayerAdapter implements Player {

    //=======================================================================================

    private final BlockingQueue<Boolean> tutorialQueue;




    //=========================================================================================
    private TutorialGraphicalPlayer tutorialGraphicalPlayer;

    //BlockingQueue to block the main thread waiting for the player's action
    private final BlockingQueue<SortedBag<Ticket>> ticketsQueue;
    private final BlockingQueue<Player.TurnKind> turnQueue;
    private final BlockingQueue<Integer> slotQueue;
    private final BlockingQueue<Route> routeQueue;
    private final BlockingQueue<SortedBag<Card>> cardsQueue;

    private final static int BLOCKING_CAPACITY = 1;
    private final static String BREAK = "\n";

    /**
     * Instantiates a GraphicalPlayerAdapter with different ArrayBlockingQueues of size 1
     */
    public TutorialGraphicalPlayerAdapter() {
        ticketsQueue = new ArrayBlockingQueue<>(BLOCKING_CAPACITY);
        turnQueue = new ArrayBlockingQueue<>(BLOCKING_CAPACITY);
        slotQueue = new ArrayBlockingQueue<>(BLOCKING_CAPACITY);
        routeQueue = new ArrayBlockingQueue<>(BLOCKING_CAPACITY);
        cardsQueue = new ArrayBlockingQueue<>(BLOCKING_CAPACITY);

        tutorialQueue = new ArrayBlockingQueue<>(BLOCKING_CAPACITY);
    }

    public void next(String nextMessage) {

        runLater(() -> tutorialGraphicalPlayer.fillTutorialHandler(tutorialQueue::add));

        runLater(() -> tutorialGraphicalPlayer.updateTutorialText(nextMessage));

    }

    public void waitsForNext(String nextMessage) {
        try {
            runLater(() -> {
                    tutorialGraphicalPlayer.fillTutorialHandler(tutorialQueue::add);
//                    tutorialGraphicalPlayer.clearHandlerProperty();
                });

            runLater(tutorialQueue.take() ?
                    () -> tutorialGraphicalPlayer.closeTutorial() :
                    () -> tutorialGraphicalPlayer.updateTutorialText(nextMessage));
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

    /**
     * Initializes the GraphicalPlayer
     * Instantiates a new GraphicalPlayer on JavaFX thread
     *
     * @param ownId       Id of the player
     * @param playerNames names of all the players
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> tutorialGraphicalPlayer = new TutorialGraphicalPlayer(ownId, playerNames));
    }

    /**
     * Override receiveInfo(...) from Player
     * Calls receiveInfo(...) on JavaFX thread
     *
     * @param info that has to be transmitted : generally created with Info.java
     */
    @Override
    public void receiveInfo(String info) {
        runLater(() -> tutorialGraphicalPlayer.receiveInfo(info + BREAK));
    }

    /**
     * Updates the state of the game, and hence the state of the GUI
     * Calls setState(...) on JavaFX thread
     *
     * @param newState new PublicGameState that has to be known by the player
     * @param ownState playerState of the player
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(() -> tutorialGraphicalPlayer.setState(newState, ownState));
    }

    /**
     * Adds the initial tickets choice to the ticketsQueue
     * Calls chooseTickets(...) on JavaFX thread
     *
     * @param tickets SortedBag containing the possible Tickets
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(() -> tutorialGraphicalPlayer.chooseTickets(tickets, ticketsQueue::add));
    }

    /**
     * Blocks and waits for the ticketsQueue to be filled with the initial tickets choice
     *
     * @return the ticketsQueue's current content : the player's initial tickets choice as a SortedBag of Ticket
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        return takeTry(ticketsQueue);
    }

    /**
     * Calls startTurn of GraphicalPlayer, fills the right blocking turnQueue in terms of which handler was chosen
     * Fills also the corresponding argument queues
     * Calls startTurn(...) on JavaFX thread
     *
     * @return the turnQueue's current content : the player's turn kind as a TurnKind
     */
    @Override
    public Player.TurnKind nextTurn() {
        runLater(() -> tutorialGraphicalPlayer.startTurn(

                //DRAW_TICKETS
                () -> turnQueue.add(Player.TurnKind.DRAW_TICKETS),

                //DRAW_CARDS
                slot -> {
                    slotQueue.add(slot);
                    turnQueue.add(Player.TurnKind.DRAW_CARDS);
                },

                //CLAIM_ROUTE
                (route, cards) -> {
                    routeQueue.add(route);
                    cardsQueue.add(cards);
                    turnQueue.add(Player.TurnKind.CLAIM_ROUTE);
                })

        );

        return takeTry(turnQueue);
    }

    /**
     * Sets a tickets choice and waits for the player's response
     * Calls chooseTickets(...) in setInitialTicketChoice(...) on JavaFX thread
     *
     * @param options possible tickets to draw
     * @return the ticketsQueue's current content : the player's tickets choice as a SortedBag of Ticket
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        setInitialTicketChoice(options);
        return takeTry(ticketsQueue);
    }

    /**
     * In case of a DRAW_CARDS turn, returns the chosen card corresponding drawSlot
     *
     * @return the chosen card corresponding drawSlot
     * - first drawing : if slotQueue is already filled, just takes its content
     * - second drawing : calls drawCard(...) on JavaFX thread and waits for the player's response
     */
    @Override
    public int drawSlot() {
        if (!slotQueue.isEmpty()) {
            return slotQueue.remove();
        } else {
            runLater(() -> tutorialGraphicalPlayer.drawCard(slotQueue::add));
            return takeTry(slotQueue);
        }
    }

    /**
     * Returns the routeQueue's current content
     *
     * @return the routeQueue's current content : the claimed route
     */
    @Override
    public Route claimedRoute() {
        runLater(() ->  tutorialGraphicalPlayer.startTurn(null, null,
                (route, cards) -> {
                    routeQueue.add(route);
                    cardsQueue.add(cards);
                    turnQueue.add(Player.TurnKind.CLAIM_ROUTE);
                }));
        return takeTry(routeQueue);
    }

    /**
     * Returns the cardsQueue's current content
     *
     * @return the cardsQueue's current content : the player's initial claim cards
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        return cardsQueue.remove();
    }

    /**
     * Blocks and gets the player's additional chosen cards for a tunnel claim
     * Calls chooseAdditionalCards(...) on JavaFX thread
     *
     * @param options list of the possible additional cards combinations (empty if the player can't control or don't want to control the route
     * @return the cardsQueue's current content : the player's additional chosen cards as a SortedBag of Card
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(() -> tutorialGraphicalPlayer.chooseAdditionalCards(options, cardsQueue::add));
        return takeTry(cardsQueue);
    }

    /**
     * Generic method called by the above public methods
     * Tries to take the given queue's contents
     *
     * @param queue the given blocking queue
     * @param <E>   parameter type (TurnKind, SortedBag of Card, SortedBag of Ticket, Route or Integer)
     * @return the queue's current content of E type
     * @throws Error if the .take() attempt fails
     */
    private <E> E takeTry(BlockingQueue<E> queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }

}