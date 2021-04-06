package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

final class GameTest$TestPlayer implements Player {
    private static final int TURN_LIMIT = 1000;
    private final Random rng;
    private final List<Route> allRoutes;
    private int turnCounter;
    private PlayerState ownState;
    private PublicGameState gameState;
    private Route routeToClaim;
    private SortedBag<Card> initialClaimCards;
    private Map<PlayerId, String> playerNames;
    private PlayerId ownId;
    private SortedBag<Ticket> tickets;
    private int numberOfInfoReceived = 0;

    public int getRandomNumber(int min, int max) {
        return (int)(Math.random() * (double)(max - min) + (double)min);
    }

    public GameTest$TestPlayer(long randomSeed, List<Route> allRoutes) {
        this.rng = new Random(randomSeed);
        this.allRoutes = List.copyOf(allRoutes);
        this.turnCounter = 0;
    }

    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        this.ownId = ownId;
        this.playerNames = playerNames;
    }

    public void receiveInfo(String info) {
        ++this.numberOfInfoReceived;
        System.out.println(this.numberOfInfoReceived + " | " + info);
    }

    public void updateState(PublicGameState newState, PlayerState ownState) {
        this.gameState = newState;
        this.ownState = ownState;
    }

    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        this.tickets = tickets;
    }

    public SortedBag<Ticket> chooseInitialTickets() {
        List<Ticket> chosenTickets = new ArrayList();

        for(int i = 0; i < this.getRandomNumber(3, 5); ++i) {
            chosenTickets.add((Ticket)this.tickets.get(i));
        }

        return SortedBag.of(chosenTickets);
    }

    public TurnKind nextTurn() {
        ++this.turnCounter;
        if (this.turnCounter > 1000) {
            throw new Error("Trop de tours joués !");
        } else {
            List<Route> claimableRoutes = new ArrayList();
            Iterator var2 = this.allRoutes.iterator();

            Route route;
            while(var2.hasNext()) {
                route = (Route)var2.next();
                if (this.ownState.canClaimRoute(route)) {
                    claimableRoutes.add(route);
                }
            }

            if (claimableRoutes.isEmpty()) {
                System.out.println("DRAW_CARDS");
                return TurnKind.DRAW_CARDS;
            } else {
                int routeIndex = this.rng.nextInt(claimableRoutes.size());
                route = (Route)claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = this.ownState.possibleClaimCards(route);
                this.routeToClaim = route;
                this.initialClaimCards = (SortedBag)cards.get(0);
                PrintStream var10000 = System.out;
                String var10001 = route.id();
                var10000.println("CLAIM_ROUTE = Route : " + var10001 + " | Level : " + route.level() + " | InitialClaimCards : " + this.initialClaimCards);
                return TurnKind.CLAIM_ROUTE;
            }
        }
    }

    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        List<Ticket> chosenTickets = new ArrayList();

        for(int i = 0; i < this.getRandomNumber(1, 3); ++i) {
            chosenTickets.add((Ticket)options.get(i));
        }

        return SortedBag.of(chosenTickets);
    }

    public int drawSlot() {
        return this.getRandomNumber(-1, 4);
    }

    public Route claimedRoute() {
        return this.routeToClaim;
    }

    public SortedBag<Card> initialClaimCards() {
        return this.initialClaimCards;
    }

    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        SortedBag<Card> chosenAdditionalCards = (SortedBag)options.get(this.getRandomNumber(0, options.size() - 1));
        System.out.println("ChosenAdditionalCards : " + chosenAdditionalCards);
        return chosenAdditionalCards;
    }
}
