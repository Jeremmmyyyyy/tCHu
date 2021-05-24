package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import static javafx.application.Platform.runLater;

/**
 * final class that adapts the GraphicalPlayer to a Player so that the actions called in Game can be executed on the GUI
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer graphicalPlayer;
    private final BlockingQueue<SortedBag<Ticket>> ticketsQueue;
    private final BlockingQueue<TurnKind> turnQueue;
    private final BlockingQueue<Integer> slotQueue;
    private final BlockingQueue<Route> routeQueue;
    private final BlockingQueue<SortedBag<Card>> cardsQueue;

    /**
     * Instantiate a GraphicalPlayerAdapter with different ArrayBlockingQueues of size 1
     */
    public GraphicalPlayerAdapter (){
        ticketsQueue = new ArrayBlockingQueue<>(1);
        turnQueue = new ArrayBlockingQueue<>(1);
        slotQueue = new ArrayBlockingQueue<>(1);
        routeQueue = new ArrayBlockingQueue<>(1);
        cardsQueue = new ArrayBlockingQueue<>(1);
    }

    /**
     * Create a new GraphicalPlayer on the javaFX thread
     * @param ownId Id of the player
     * @param playerNames names of all the players
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    /**
     * Call on the javaFX thread the same called method of GraphicalPlayer
     * @param info that has to be transmitted : generally created with Info.java
     */
    @Override
    public void receiveInfo(String info) {
        runLater(()-> graphicalPlayer.receiveInfo(info));
    }

    /**
     * Update the state of the game, and so the state of the GUI
     * @param newState new PublicGameState that has to be known by the player
     * @param ownState PlayerState of the PublicPlayerState
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(()-> graphicalPlayer.setState(newState, ownState));
    }

    /**
     * Waits until the queue also used by setInitialTickets is not empty anymore
     * @param tickets SortedBag containing the possible Tickets
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(()-> graphicalPlayer.chooseTickets(tickets, ticketsQueue::add));
    }

    /**
     * Ask the player which tickets he wants from the 5 one
     * @return at least 3 of the chosen tickets
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets(){
        return takeTry(ticketsQueue);
    }

    /**
     * Call startTurn of GraphicalPlayer, fills the right blockingQueue in terms of which handler was chosen
     * @return the TurnKind
     */
    @Override
    public TurnKind nextTurn() {
        runLater(()-> graphicalPlayer.startTurn(

                //DrawTicketsHandler
                () -> turnQueue.add(TurnKind.DRAW_TICKETS),

                //DrawCardsHandler
                slot -> {
                    slotQueue.add(slot);
                    turnQueue.add(TurnKind.DRAW_CARDS);},

                //ClaimRouteHandler
                (route, cards) -> {
                    routeQueue.add(route);
                    cardsQueue.add(cards);
                    turnQueue.add(TurnKind.CLAIM_ROUTE);})

        );

        return takeTry(turnQueue);
    }

    /**
     * Ask the player which tickets he wants from the 3 one
     * @param options possible tickets to draw
     * @return at least one of the 3 given tickets
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        setInitialTicketChoice(options);
        return takeTry(ticketsQueue);
    }

    /**
     * If the player has chosen the TurnKind DRAW_CARD, let the player choose 2 cards
     * @return the two chosen cards (called two times)
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
     * return the route that was claimed by the player
     * @return the claimed route
     */
    @Override
    public Route claimedRoute() {
        return routeQueue.remove();
    }

    /**
     * Automatically draw the first four cards (at random) for each player
     * @return the SortedBag of these four cards
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        return cardsQueue.remove();
    }

    /**
     * Allow the player to choose which card combination he want to add to take a tunnel
     * @param options list of the possible additional cards combinations (empty if the player can't control or don't want to control the route
     * @return the SortedBag of additional cards chosen by the player
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        runLater(()-> graphicalPlayer.chooseAdditionalCards(options, cardsQueue::add));
        return takeTry(cardsQueue);
    }

    private <E> E takeTry(BlockingQueue<E> queue) {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new Error();
        }
    }
}
