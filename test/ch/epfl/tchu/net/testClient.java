package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.test.ChMapPublic;

import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Card.*;

public class testClient {
    public static void main(String[] args){
        System.out.println("Client Started");
        RemotePlayerClient remotePlayerClient = new RemotePlayerClient(new TestPlayer(), "localhost", 5108);

        remotePlayerClient.run();

        System.out.println("Client Done");
    }





    private final static class TestPlayer implements Player {
        @Override
        public void initPlayers(PlayerId ownId, Map<PlayerId, String> names) {
            System.out.printf("ownId: %s\n", ownId);
            System.out.printf("playerNames: %s\n", names);
            System.out.println("----------------------------------------");
        }

        @Override
        public void receiveInfo(String info) {
            System.out.printf("info: %s\n", info);
            System.out.println("----------------------------------------");
        }

        @Override
        public void updateState(PublicGameState newState, PlayerState ownState) {
            System.out.printf("PublicGameState: ticketCount %s\n", newState.ticketsCount());
            System.out.printf("PublicGameState: faceUpCards %s\n", newState.cardState().faceUpCards());
            System.out.printf("PublicGameState: id %s\n", newState.currentPlayerId());

            System.out.printf("PlayerState: cards %s\n", ownState.cards());
            System.out.printf("PlayerState: carCount %s\n", ownState.carCount());
            System.out.printf("PlayerState: finalPoints %s\n", ownState.finalPoints());
            System.out.println("----------------------------------------");
        }

        @Override
        public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
            System.out.printf("Tickets:  %s\n", tickets);
            System.out.println("----------------------------------------");
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {
            System.out.println("chooseInitialTickets() called");
//            System.out.println(SortedBag.of(tickets));
            System.out.println("----------------------------------------");
            return SortedBag.of(tickets);
        }

        @Override
        public TurnKind nextTurn() {
            System.out.println("nextTurn() called");
            System.out.println("----------------------------------------");
            return TurnKind.CLAIM_ROUTE;
        }

        @Override
        public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {

            System.out.println("----------------------------------------");
            return null;
        }

        @Override
        public int drawSlot() {

            System.out.println("----------------------------------------");
            return 0;
        }

        @Override
        public Route claimedRoute() {

            System.out.println("----------------------------------------");
            return null;
        }

        @Override
        public SortedBag<Card> initialClaimCards() {

            System.out.println("----------------------------------------");
            return null;
        }

        @Override
        public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {

            System.out.println("----------------------------------------");
            return null;
        }

        private static final Map<PlayerId, String> playerNames = Map.of(PlayerId.PLAYER_1, "BOB", PlayerId.PLAYER_2, "ALICE");
        private static final List<Card> cards = List.of(RED, WHITE, BLUE, BLACK, RED);
        private static final List<Ticket> tickets = List.of(ChMapPublic.ALL_TICKETS.get(0), ChMapPublic.ALL_TICKETS.get(1), ChMapPublic.ALL_TICKETS.get(2), ChMapPublic.ALL_TICKETS.get(3));
        private static final List<Route> routes = List.of(ChMapPublic.ALL_ROUTES.get(0), ChMapPublic.ALL_ROUTES.get(1), ChMapPublic.ALL_ROUTES.get(2), ChMapPublic.ALL_ROUTES.get(3));
        private static final PublicCardState publicCardState = new PublicCardState(cards, 30, 31);
        private static final List<Route> routeList = ChMap.routes().subList(0, 2);
        private static final Map<PlayerId, PublicPlayerState> publicPlayerStateMap = Map.of(
                PlayerId.PLAYER_1, new PublicPlayerState(10, 11, routeList),
                PlayerId.PLAYER_2, new PublicPlayerState(20, 21, List.of()));
        private static final PublicGameState publicGameState =
                new PublicGameState(40, publicCardState, PlayerId.PLAYER_2, publicPlayerStateMap, null);
        private static final PlayerState playerState = new PlayerState(SortedBag.of(tickets), SortedBag.of(cards), routes);
    }
}
