package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

/**
 * Class that adapts the Player class to a GraphicalPlayer in order to enable Game actions execution on the GUI
 * Similar to the Adapter conception
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer graphicalPlayer;

    //BlockingQueue to block the main thread waiting for the player's action
    private final BlockingQueue<SortedBag<Ticket>> ticketsQueue;
    private final BlockingQueue<TurnKind> turnQueue;
    private final BlockingQueue<Integer> slotQueue;
    private final BlockingQueue<Route> routeQueue;
    private final BlockingQueue<SortedBag<Card>> cardsQueue;

    /**
     * Instantiates a GraphicalPlayerAdapter with different ArrayBlockingQueues of size 1
     */
    public GraphicalPlayerAdapter (){
        ticketsQueue = new ArrayBlockingQueue<>(1); //TODO constante pour les 1 ???
        turnQueue = new ArrayBlockingQueue<>(1);
        slotQueue = new ArrayBlockingQueue<>(1);
        routeQueue = new ArrayBlockingQueue<>(1);
        cardsQueue = new ArrayBlockingQueue<>(1);
    }

    /**
     * Initializes the GraphicalPlayer
     * Instantiates a new GraphicalPlayer on JavaFX thread
     * @param ownId Id of the player
     * @param playerNames names of all the players
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    /**
     * Override receiveInfo(...) from Player
     * Calls receiveInfo(...) on JavaFX thread
     * @param info that has to be transmitted : generally created with Info.java
     */
    @Override
    public void receiveInfo(String info) {
        runLater(()-> graphicalPlayer.receiveInfo(info));
    }

    /**
     * Updates the state of the game, and hence the state of the GUI
     * Calls setState(...) on JavaFX thread
     * @param newState new PublicGameState that has to be known by the player
     * @param ownState playerState of the player
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(()-> graphicalPlayer.setState(newState, ownState));
    }

    /**
     * Adds the initial tickets choice to the ticketsQueue
     * Calls chooseTickets(...) on JavaFX thread
     * @param tickets SortedBag containing the possible Tickets
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(()-> graphicalPlayer.chooseTickets(tickets, ticketsQueue::add));
    }

    /**
     * Blocks and waits for the ticketsQueue to be filled with the initial tickets choice
     * @return the ticketsQueue's current content : the player's initial tickets choice as a SortedBag of Ticket
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets(){
        return takeTry(ticketsQueue);
    }

    /**
     * Calls startTurn of GraphicalPlayer, fills the right blocking turnQueue in terms of which handler was chosen
     * Fills also the corresponding argument queues
     * Calls startTurn(...) on JavaFX thread
     * @return the turnQueue's current content : the player's turn kind as a TurnKind
     */
    @Override
    public TurnKind nextTurn() {
        runLater(()-> graphicalPlayer.startTurn(

                //DRAW_TICKETS
                () -> turnQueue.add(TurnKind.DRAW_TICKETS),

                //DRAW_CARDS
                slot -> {
                    slotQueue.add(slot);
                    turnQueue.add(TurnKind.DRAW_CARDS);},

                //CLAIM_ROUTE
                (route, cards) -> {
                    routeQueue.add(route);
                    cardsQueue.add(cards);
                    turnQueue.add(TurnKind.CLAIM_ROUTE);})

        );

        return takeTry(turnQueue);
    }

    /**
     * Sets a tickets choice and waits for the player's response
     * Calls chooseTickets(...) in setInitialTicketChoice(...) on JavaFX thread
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
     * @return the chosen card corresponding drawSlot
     * - first drawing : if slotQueue is already filled, just takes its content
     * - second drawing : calls drawCard(...) on JavaFX thread and waits for the player's response
     */
    @Override
    public int drawSlot() {
        if (!slotQueue.isEmpty()) {
            return slotQueue.remove();
        } else {
            runLater(()-> graphicalPlayer.drawCard(slotQueue::add));
            return takeTry(slotQueue);
        }
    }

    /**
     * Returns the routeQueue's current content
     * @return the routeQueue's current content : the claimed route
     */
    @Override
    public Route claimedRoute() {
        return routeQueue.remove();
    }

    /**
     * Returns the cardsQueue's current content
     * @return the cardsQueue's current content : the player's initial claim cards
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        return cardsQueue.remove();
    }

    /**
     * Blocks and gets the player's additional chosen cards for a tunnel claim
     * Calls chooseAdditionalCards(...) on JavaFX thread
     * @param options list of the possible additional cards combinations (empty if the player can't control or don't want to control the route
     * @return the cardsQueue's current content : the player's additional chosen cards as a SortedBag of Card
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(()-> graphicalPlayer.chooseAdditionalCards(options, cardsQueue::add));
        return takeTry(cardsQueue);
    }

    /**
     * Generic method called by the above public methods
     * Tries to take the given queue's contents
     * @param queue the given blocking queue
     * @param <E> parameter type (TurnKind, SortedBag of Card, SortedBag of Ticket, Route or Integer)
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
