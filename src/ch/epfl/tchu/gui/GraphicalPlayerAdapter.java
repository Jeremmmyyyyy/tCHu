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
    private BlockingQueue<SortedBag<Ticket>> chooseInitialTickets;
//    private BlockingQueue<Integer> drawCardHandler;
//    private BlockingQueue<Integer> drawTicketsHandler;
//    private BlockingQueue<SortedBag<Card>> claimRouteHandler;
//    private BlockingQueue<Route> claimRouteHandler;
    private BlockingQueue<TurnKind> nextTurn;
    private BlockingQueue<SortedBag<Ticket>> chooseTickets;
    private BlockingQueue<Integer> drawSlot;
    private BlockingQueue<Route> claimedRoute;
    private BlockingQueue<SortedBag<Card>> initialClaimCards;
    private BlockingQueue<SortedBag<Card>> chooseAdditionalCards;

    public GraphicalPlayerAdapter (){
        chooseInitialTickets = new ArrayBlockingQueue<>(1);
//        drawCardHandler = new ArrayBlockingQueue<>(1);
//        drawTicketsHandler = new ArrayBlockingQueue<>(1);
//        claimRouteHandler = new ArrayBlockingQueue<>(1);
        nextTurn = new ArrayBlockingQueue<>(1);
        chooseTickets = new ArrayBlockingQueue<>(1);
        drawSlot = new ArrayBlockingQueue<>(1);
        claimedRoute = new ArrayBlockingQueue<>(1);
        initialClaimCards = new ArrayBlockingQueue<>(1);
        chooseAdditionalCards = new ArrayBlockingQueue<>(1);
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        runLater(()-> graphicalPlayer = new GraphicalPlayer(ownId, playerNames));
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
        runLater(()-> graphicalPlayer.chooseTickets(tickets, e -> chooseInitialTickets.add(e))); //TODO put préférable ?
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() throws InterruptedException {
        return chooseInitialTickets.take();
    }

    @Override
    public TurnKind nextTurn() {
        runLater(()-> graphicalPlayer.startTurn(
                e1 -> chooseInitialTickets.add(e1),
                e2 -> drawSlot.add(e2),
                (e3, e4) -> {
                    claimedRoute.add(e3);
                    initialClaimCards.add(e4);
                }));

        return null;
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) throws InterruptedException {
        setInitialTicketChoice(options);
        return chooseInitialTickets.take();
    }

    @Override
    public int drawSlot() throws InterruptedException {
        int slot = drawSlot.peek();
        drawSlot.remove(slot); //TODO pas ouf
        if (slot != 0){
            return slot;
        }else {
            runLater(()-> graphicalPlayer.drawCard(e -> drawSlot.add(e)));
            return drawSlot.take();
        }
    }

    @Override
    public Route claimedRoute() {
        return claimedRoute.remove();
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return initialClaimCards.remove();
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) throws InterruptedException {
        runLater(()-> graphicalPlayer.chooseAdditionalCards(options, e-> chooseAdditionalCards.add(e)));
        return chooseAdditionalCards.take();
    }
}
