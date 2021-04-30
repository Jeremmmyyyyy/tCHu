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

    private static Map<PlayerId, String> playerNames = Map.of(PlayerId.PLAYER_1, "BOB", PlayerId.PLAYER_2, "ALICE");
    private static List<Card> cards = List.of(RED, WHITE, BLUE, BLACK, RED);
    private static List<Ticket> tickets = List.of(ChMapPublic.ALL_TICKETS.get(0), ChMapPublic.ALL_TICKETS.get(1), ChMapPublic.ALL_TICKETS.get(2), ChMapPublic.ALL_TICKETS.get(3));
    private static List<Route> routes = List.of(ChMapPublic.ALL_ROUTES.get(0), ChMapPublic.ALL_ROUTES.get(1), ChMapPublic.ALL_ROUTES.get(2), ChMapPublic.ALL_ROUTES.get(3));
    private static PublicCardState publicCardState = new PublicCardState(cards, 30, 31);
    private static List<Route> routeList = ChMap.routes().subList(0, 2);
    private static Map<PlayerId, PublicPlayerState> publicPlayerStateMap = Map.of(
            PlayerId.PLAYER_1, new PublicPlayerState(10, 11, routeList),
            PlayerId.PLAYER_2, new PublicPlayerState(20, 21, List.of()));
    private static PublicGameState publicGameState =
            new PublicGameState(40, publicCardState, PlayerId.PLAYER_2, publicPlayerStateMap, null);
    private static PlayerState playerState = new PlayerState(SortedBag.of(tickets), SortedBag.of(cards), routes);



    public static void main(String[] args) throws Exception {
        System.out.println("Server started");
        try (ServerSocket serverSocket = new ServerSocket(5108); Socket socket = serverSocket.accept()) {
            Player remotePlayerProxy = new RemotePlayerProxy(socket);


            remotePlayerProxy.initPlayers(PlayerId.PLAYER_1, playerNames);
            remotePlayerProxy.receiveInfo("receiveInfo works");
//            remotePlayerProxy.updateState(publicGameState, playerState); // TODO marche pas
//            SortedBag<Ticket> tickets = remotePlayerProxy.chooseInitialTickets();
//            System.out.println(tickets);
            Player.TurnKind turnKind = remotePlayerProxy.nextTurn();
            System.out.println("NextTurn is : " + turnKind);


            Thread.sleep(10000);
        }
        System.out.println("Server Done");
    }
}







