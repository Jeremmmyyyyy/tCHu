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
    private BlockingQueue<SortedBag<Ticket>> ticketsQueue;
    private BlockingQueue<TurnKind> turnQueue;
    private BlockingQueue<Integer> slotQueue;
    private BlockingQueue<Route> routeQueue;
    private BlockingQueue<SortedBag<Card>> cardsQueue;

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
        runLater(()-> graphicalPlayer.setState(newState, ownState));
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        runLater(()-> graphicalPlayer.chooseTickets(tickets, ticketChoice -> ticketsQueue.add(ticketChoice)));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() throws InterruptedException { //TODO bloc try and catch ou blc ?
        return ticketsQueue.take();
    }

    @Override
    public TurnKind nextTurn() throws InterruptedException {
        runLater(()-> graphicalPlayer.startTurn(

                //DrawTicketsHandler
                () -> turnQueue.add(TurnKind.DRAW_TICKETS),

                //DrawCardsHandler
                slot -> {
                    slotQueue.add(slot);
                    turnQueue.add(TurnKind.DRAW_CARDS);
                },

                //ClaimRouteHandler
                (route, cards) -> {
                    routeQueue.add(route);
                    cardsQueue.add(cards);
                    turnQueue.add(TurnKind.CLAIM_ROUTE);
                })
        );
        return turnQueue.take();
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) throws InterruptedException {
        setInitialTicketChoice(options);
        return ticketsQueue.take();
    }

    @Override
    public int drawSlot() throws InterruptedException {
        if (!slotQueue.isEmpty()) {
            return slotQueue.remove();
        } else {
            runLater(()-> graphicalPlayer.drawCard(slot -> slotQueue.add(slot)));
            return slotQueue.take();
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
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) throws InterruptedException {
        runLater(()-> graphicalPlayer.chooseAdditionalCards(options, cards -> cardsQueue.add(cards)));
        return cardsQueue.take();
    }
}
