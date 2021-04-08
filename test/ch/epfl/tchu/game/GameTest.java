package ch.epfl.tchu.game;

import ch.epfl.tchu.SortedBag;
import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.*;

public class GameTest {
    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    @Test
    void test(){
        TestPlayer player1 = new TestPlayer(165473674358L, ChMapPublic.ALL_ROUTES);
        TestPlayer player2 = new TestPlayer(165473674358L, ChMapPublic.ALL_ROUTES);
        Map<PlayerId, String> playerNames = new TreeMap<>();
        playerNames.putIfAbsent(PlayerId.PLAYER_1, "Alice");
        playerNames.putIfAbsent(PlayerId.PLAYER_2, "Bob");
        Map<PlayerId, Player> players = new TreeMap<>();
        players.putIfAbsent(PlayerId.PLAYER_1, player1);
        players.putIfAbsent(PlayerId.PLAYER_2, player2);

        Game.play(players, playerNames, SortedBag.of(ChMapPublic.ALL_TICKETS), new Random(1L));
        // Seed 9149849847L = player 2 puis player 1 // Seed 2000 Player 1 puis player 2
    }

    @Test
    void BeacoupDeTests(){
        int[][] scores = new int[10000][2];

        for (int i = 0; i < scores.length; i++) {
            int randomSeed = getRandomNumber(0,1000000000);

            TestPlayer player1 = new TestPlayer(randomSeed, ChMapPublic.ALL_ROUTES);
            TestPlayer player2 = new TestPlayer(randomSeed, ChMapPublic.ALL_ROUTES);
            Map<PlayerId, String> playerNames = new TreeMap<>();
            playerNames.putIfAbsent(PlayerId.PLAYER_1, "Alice");
            playerNames.putIfAbsent(PlayerId.PLAYER_2, "Bob");
            Map<PlayerId, Player> players = new TreeMap<>();
            players.putIfAbsent(PlayerId.PLAYER_1, player1);
            players.putIfAbsent(PlayerId.PLAYER_2, player2);

            Game.play(players, playerNames, SortedBag.of(ChMapPublic.ALL_TICKETS), new Random(randomSeed));
            try{
                scores[i][0] = Integer.parseInt(player1.tab[5]);
                scores[i][1] = Integer.parseInt(player1.tab[8]);
            }catch (NullPointerException e){}

        }
        try{
            double moyenneGagnant = 0;
            double moyennePerdant = 0;

            for (int i = 0; i < scores.length; i++) {
                System.out.println(scores[i][0] +" " + scores[i][1]);
                moyenneGagnant +=  scores[i][0];
                moyennePerdant +=  scores[i][1];
            }
            moyenneGagnant = moyenneGagnant/scores.length;
            moyennePerdant = moyenneGagnant/scores.length;

            System.out.println("Moyenne Gagnant " + moyenneGagnant + " | " + moyennePerdant);
        }catch (NullPointerException e ){}
    }



    private static final class TestPlayer implements Player {

        public int getRandomNumber(int min, int max) {
            return (int) ((Math.random() * (max - min)) + min);
        }
        private String tab[];
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
            if(info.contains("remporte la victoire avec")){
                tab = info.split(" ");
            }
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
                System.out.println("DRAW_CARDS ");

                return TurnKind.DRAW_CARDS;
            } else {
                int routeIndex = rng.nextInt(claimableRoutes.size());
                Route route = claimableRoutes.get(routeIndex);
                List<SortedBag<Card>> cards = ownState.possibleClaimCards(route);

                routeToClaim = route;
                initialClaimCards = cards.get(0);
                System.out.println("CLAIM_ROUTE = Route : " + route.id() + " | Level : "
                        + route.level() + " | InitialClaimCards : " + initialClaimCards);
                System.out.println("Deck: " +gameState.cardState().deckSize()+
                                    "| Discard: " +gameState.cardState().discardsSize() +
                                     "| Cards: " + ownState.cards()+
                                        "| AvailableCars: " + gameState.playerState(ownId).carCount() +
                                        " | faceUpCards " + gameState.cardState().faceUpCards());
                gameState.claimedRoutes().forEach((s)-> System.out.print(s.id() + "|"));
                System.out.println();


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
//            return -1;
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

        @Override
        public String toString() {
            return playerNames.get(ownId);
        }

        public String[] tab() {
            return tab;
        }
    }

}