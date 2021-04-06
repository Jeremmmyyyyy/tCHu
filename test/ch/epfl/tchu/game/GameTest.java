package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.*;

public class GameTest {


    @Test
    void test(){
        TestPlayer player1 = new TestPlayer(165473674358L, ChMapPublic.ALL_ROUTES);
        TestPlayer player2 = new TestPlayer(165473674358L, ChMapPublic.ALL_ROUTES);
        Map<PlayerId, String> playerNames = new HashMap<>();
        playerNames.putIfAbsent(PlayerId.PLAYER_1, "Alice");
        playerNames.putIfAbsent(PlayerId.PLAYER_2, "Bob");
        Map<PlayerId, Player> players = new HashMap<>();
        players.putIfAbsent(PlayerId.PLAYER_1, player1);
        players.putIfAbsent(PlayerId.PLAYER_2, player2);

        Game.play(players, playerNames, SortedBag.of(ChMapPublic.ALL_TICKETS), new Random(137829475L));
    }



    private static final class TestPlayer implements Player {

        public int getRandomNumber(int min, int max) {
            return (int) ((Math.random() * (max - min)) + min);
        }

        private static final int TURN_LIMIT = 1000;

        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;
        private Map<PlayerId, String> playerNames;
        private PlayerId ownId;
        private SortedBag<Ticket> tickets;
        private int numberOfInfoReceived = 0;

        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
        }


        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            this.ownId = ownId;
            this.playerNames = playerNames;
        }

        @Override
        public void receiveInfo(String info) {
            ++numberOfInfoReceived;
            System.out.println(numberOfInfoReceived + " | " + info);
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
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            // Détermine les routes dont ce joueur peut s'emparer
            List<Route> claimableRoutes = new ArrayList<>();
            for (Route r : allRoutes) {
                if (ownState.canClaimRoute(r)) {
                    claimableRoutes.add(r);
                }
            }
            if (claimableRoutes.isEmpty()) {
                System.out.println("DRAW_CARDS");
                return TurnKind.DRAW_CARDS;
            } else {
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                System.out.println("CLAIM_ROUTE = Route : " + route.id() + " | Level : "
                        + route.level() + " | InitialClaimCards : " + initialClaimCards);
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
            System.out.println("ChosenAdditionalCards : " + chosenAdditionalCards);
            return chosenAdditionalCards;
        }

    }

}
