package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class GameTest {

    private final static long randomSeed = 10;


    // Les joueurs ne peuvent / veulent jamais lorsqu'il y a un coût additionnel;
    @Test
    void playWorks(){

        for (int randomSeed = 0; randomSeed < 5; randomSeed++) {
            var player1 = new TestPlayer(randomSeed, ChMap.routes());
            var player2 = new TestPlayer(randomSeed, ChMap.routes());

            Map<PlayerId, Player> players = new EnumMap<>(PlayerId.class);
            players.put(PlayerId.PLAYER_1, player1);
            players.put(PlayerId.PLAYER_2, player2);

            Map<PlayerId, String> playerNames = new TreeMap<>();
            playerNames.put(PlayerId.PLAYER_1, "joueur1");
            playerNames.put(PlayerId.PLAYER_2, "joueur2");

            var tickets = SortedBag.of(ChMap.tickets());

            Random rng = new Random();

            Game.play(players, playerNames, tickets, rng);

            System.out.println("Nombre de tours joués : ");
            System.out.println(player1.getTurnCounter());
            System.out.println("Nombre d'appels à info : ");
            System.out.println(player1.getInfoCounter());
            System.out.println("Nombre d'appels à update : ");
            System.out.println(player1.getUpdateCounter());
        }
    }



//    @Test
//    void topTicketsFailsWithWrongCountOfTickets() {
//        var initialGS = GameState.initial(someTickets, random);
//        assertThrows(IllegalArgumentException.class, () -> {
//            initialGS.topTickets(5);
//        });
//        assertThrows(IllegalArgumentException.class, () -> {
//            initialGS.topTickets(-1);
//        });
//    }

    private static final class TestPlayer implements Player {
        private static final int TURN_LIMIT = 1000;

        private final Random rng;
        // Toutes les routes de la carte
        private final List<Route> allRoutes;

        //Rajouté au squelette
        private PlayerId ownId;
        private SortedBag<Ticket> initialTicketChoice;
        private int updateCounter;
        private int infoCounter;


        private int turnCounter;
        private PlayerState ownState;
        private PublicGameState gameState;

        // Lorsque nextTurn retourne CLAIM_ROUTE
        private Route routeToClaim;
        private SortedBag<Card> initialClaimCards;



        public TestPlayer(long randomSeed, List<Route> allRoutes) {
            this.rng = new Random(randomSeed);
            this.allRoutes = List.copyOf(allRoutes);
            this.turnCounter = 0;
            this.infoCounter = 0;
        }

        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
            System.out.println("OwnId : " + ownId);
            this.ownId = ownId;
            for (PlayerId playerId : playerNames.keySet()) {
                System.out.println("Le nom de "+ playerId + " est " + playerNames.get(playerId));
            }
        }

        @Override
        public void receiveInfo(String info) {
            infoCounter += 1;
            System.out.println(info);
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            updateCounter += 1;
            this.gameState = newState;
            this.ownState = ownState;
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            this.initialTicketChoice = tickets;
            System.out.println("Le choix initial de tickets pour le joueur est : " + tickets);
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            List<SortedBag<Ticket>> options = new ArrayList<>();
            options.addAll(initialTicketChoice.subsetsOfSize(3));
            options.addAll(initialTicketChoice.subsetsOfSize(4));
            options.addAll(initialTicketChoice.subsetsOfSize(5));
            int ticketIndex = rng.nextInt(options.size());

            System.out.println("Option initiale de tickets choisie par le joueur : " + options.get(ticketIndex));

            return options.get(ticketIndex);
        }

        @Override
        public TurnKind nextTurn() {
            turnCounter += 1;
            if (turnCounter > TURN_LIMIT)
                throw new Error("Trop de tours joués !");

            if (turnCounter < 5)
                return TurnKind.DRAW_TICKETS;
            else if (turnCounter < 20)
                return TurnKind.DRAW_CARDS;

            // Détermine les routes dont ce joueur peut s'emparer
            List<Route> claimableRoutes = new ArrayList<>();
            List<Route> availableRoutes = new ArrayList<>(allRoutes);
            availableRoutes.removeAll(gameState.claimedRoutes());
            for (Route route : availableRoutes) {
                if(ownState.canClaimRoute(route))
                    claimableRoutes.add(route);
            }
            if (claimableRoutes.isEmpty()) {
                return TurnKind.DRAW_CARDS;
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
            List<SortedBag<Ticket>> chooseOptions = new ArrayList<>();
            chooseOptions.addAll(options.subsetsOfSize(1));
            chooseOptions.addAll(options.subsetsOfSize(2));
            chooseOptions.addAll(options.subsetsOfSize(3));
            int ticketIndex = rng.nextInt(chooseOptions.size());

            System.out.println("Option de pioche de tickets choisie par le joueur : " + chooseOptions.get(ticketIndex));

            return chooseOptions.get(ticketIndex);
        }

        //Tire une carte parmi les cartes face visible
        @Override
        public int drawSlot() {
            //return rng.nextInt(4);
            List<Integer> slotValues = List.of(-1, 0, 1, 2, 3, 4);
            int index = rng.nextInt(slotValues.size());
            return slotValues.get(index);
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
            return options.get(rng.nextInt(options.size()));
        }

        public int getInfoCounter() {
            return infoCounter;
        }

        public int getTurnCounter() {
            return turnCounter;
        }

        public int getUpdateCounter() {
            return updateCounter;
        }
    }
}
