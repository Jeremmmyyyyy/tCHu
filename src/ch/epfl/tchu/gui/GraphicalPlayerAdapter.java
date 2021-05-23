package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import static javafx.application.Platform.runLater;

public final class GraphicalPlayerAdapter implements Player {

    private GraphicalPlayer graphicalPlayer;
    private final BlockingQueue<SortedBag<Ticket>> ticketsQueue;
    private final BlockingQueue<TurnKind> turnQueue;
    private final BlockingQueue<Integer> slotQueue;
    private final BlockingQueue<Route> routeQueue;
    private final BlockingQueue<SortedBag<Card>> cardsQueue;

    public GraphicalPlayerAdapter (){
        ticketsQueue = new ArrayBlockingQueue<>(1);
        turnQueue = new ArrayBlockingQueue<>(1);
        slotQueue = new ArrayBlockingQueue<>(1);
        routeQueue = new ArrayBlockingQueue<>(1);
        cardsQueue = new ArrayBlockingQueue<>(1);
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(() -> graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
    }

    @Override
    public void receiveInfo(String info) {
        runLater(()-> graphicalPlayer.receiveInfo(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        runLater(()-> {
            graphicalPlayer.setState(newState, ownState);
        });
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(()-> graphicalPlayer.chooseTickets(tickets, ticketsQueue::add));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets(){
        return takeTry(ticketsQueue);
    }

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

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        setInitialTicketChoice(options);
        return takeTry(ticketsQueue);
    }

    @Override
    public int drawSlot() {
        if (!slotQueue.isEmpty()) {
            return slotQueue.remove();
        } else {
            runLater(()-> graphicalPlayer.drawCard(slotQueue::add));
            return takeTry(slotQueue);
        }
    }

    @Override
    public Route claimedRoute() {
        return routeQueue.remove();
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return cardsQueue.remove();
    }

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
