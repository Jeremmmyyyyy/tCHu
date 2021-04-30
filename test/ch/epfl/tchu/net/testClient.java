package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.util.List;
import java.util.Map;

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
            System.out.println("----------------------------------------");
        }

        @Override
        public SortedBag<Ticket> chooseInitialTickets() {

            System.out.println("----------------------------------------");
            return null;
        }

        @Override
        public TurnKind nextTurn() {

            System.out.println("----------------------------------------");
            return null;
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
    }
}
