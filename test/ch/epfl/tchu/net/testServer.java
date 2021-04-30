package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.test.ChMapPublic;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Card.*;


public final class testServer{

    private static final Map<PlayerId, String> playerNames = Map.of(PlayerId.PLAYER_1, "BOB", PlayerId.PLAYER_2, "ALICE");
    private static final List<Card> cards = List.of(RED, WHITE, BLUE, BLACK, RED);
    private static final List<Ticket> tickets = List.of(ChMap.tickets().get(0), ChMap.tickets().get(1), ChMap.tickets().get(2), ChMap.tickets().get(3));
    private static final List<Route> routes = List.of(ChMap.routes().get(0), ChMap.routes().get(1), ChMap.routes().get(2), ChMap.routes().get(3));
    private static final PublicCardState publicCardState = new PublicCardState(cards, 30, 31);
    private static final List<Route> routeList = ChMap.routes().subList(0, 2);
    private static final Map<PlayerId, PublicPlayerState> publicPlayerStateMap = Map.of(
            PlayerId.PLAYER_1, new PublicPlayerState(10, 11, routeList),
            PlayerId.PLAYER_2, new PublicPlayerState(20, 21, List.of()));
    private static final PublicGameState publicGameState =
            new PublicGameState(40, publicCardState, PlayerId.PLAYER_2, publicPlayerStateMap, null);
    private static final PlayerState playerState = new PlayerState(SortedBag.of(tickets), SortedBag.of(cards), routes);



    public static void main(String[] args) throws Exception {
        System.out.println("Server started");
        try (ServerSocket serverSocket = new ServerSocket(5108); Socket socket = serverSocket.accept()) {
            Player remotePlayerProxy = new RemotePlayerProxy(socket);


            remotePlayerProxy.initPlayers(PlayerId.PLAYER_1, playerNames);
            remotePlayerProxy.receiveInfo("receiveInfo works");
            remotePlayerProxy.updateState(publicGameState, playerState);
            System.out.println("Choose initialTickets : " + remotePlayerProxy.chooseInitialTickets());
            System.out.println("chooseTickets : "+ remotePlayerProxy.chooseTickets(SortedBag.of(tickets)));
            System.out.println("NextTurn is : " + remotePlayerProxy.nextTurn());
            System.out.println("Drawn Slot is : " + remotePlayerProxy.drawSlot());
            System.out.println("ClaimedRoute is : " + remotePlayerProxy.claimedRoute().id());
            System.out.println("InitialClaimCards are : " + remotePlayerProxy.initialClaimCards());
            System.out.println("ChooseAdditionalCards are : " + remotePlayerProxy.chooseAdditionalCards(List.of(SortedBag.of(cards))));

            Thread.sleep(10000);
        }
        System.out.println("Server Done");
    }
}







