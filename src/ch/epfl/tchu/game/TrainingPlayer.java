package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public final class TrainingPlayer implements Player{
    private PlayerState ownState;
    private PublicGameState gameState;
    private SortedBag<Ticket> tickets;
    private Route routeToClaim;
    private SortedBag<Card> initialClaimCards;
    private final Random rng;
    private final List<Route> allRoutes;


    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }


    public TrainingPlayer(){
        this.rng = new Random();
        this.allRoutes = ChMap.routes();
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
    }

    @Override
    public void receiveInfo(String info) {

    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        this.gameState = newState;
        this.ownState = ownState;
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        this.tickets = tickets;
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        List<Ticket> chosenTickets = new ArrayList<>();
        for (int i = 0; i < getRandomNumber(3, 5); ++i) {
            chosenTickets.add(tickets.get(i));
        }
        return SortedBag.of(chosenTickets);
    }

    @Override
    public TurnKind nextTurn() {
        List<Route> claimableRoutes = new ArrayList<>();
        for (Route r : allRoutes) {
            if (ownState.canClaimRoute(r)) {
                claimableRoutes.add(r);
            }
        }
        if (claimableRoutes.isEmpty()) {
            if (!gameState.canDrawTickets()) {
                return TurnKind.DRAW_CARDS;
            }
            else {
                return TurnKind.DRAW_TICKETS;
            }
        } else {
            int routeIndex = rng.nextInt(claimableRoutes.size());
            Route route = claimableRoutes.get(routeIndex);
            List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

            routeToClaim = route;
            initialClaimCards = cards.get(0);

            return TurnKind.CLAIM_ROUTE;
        }
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        List<Ticket> chosenTickets = new ArrayList<>();
        for (int i = 0; i < getRandomNumber(1, 3); ++i) {
            chosenTickets.add(options.get(i));
        }
        return SortedBag.of(chosenTickets);
    }

    @Override
    public int drawSlot() {
        return getRandomNumber(-1, 4);
    }

    @Override
    public Route claimedRoute() {
        return routeToClaim;
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        return initialClaimCards;
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        SortedBag<Card> chosenAdditionalCards = options.get(getRandomNumber(0, options.size() - 1));
        return chosenAdditionalCards;
    }
}
