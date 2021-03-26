package ch.epfl.tchu.game;

import ch.epfl.test.ChMapPublic;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


public class PublicGameStateTest {

    public int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public PublicGameState publicGameStateCreator(int amountOfRoutesPLayer1, int amountOfRoutesPLayer2,
                                                      int ticketCountPlayer1, int ticketCountPlayer2,
                                                      int cardCountPLayer1, int cardCountPLayer2,
                                                      int deckSize, int discardSize,
                                                      PlayerId currentPlayer, PlayerId previousPlayer,
                                                      int ticketCount,
                                                      boolean print){
        List<Route> route1 = new ArrayList<>();
        List<Route> route2 = new ArrayList<>();


        for (int i = 0; i < amountOfRoutesPLayer1; i++) {
            route1.add(ChMapPublic.ALL_ROUTES.get(getRandomNumber(0, ChMapPublic.ALL_ROUTES.size())));
        }
        for (int i = 0; i < amountOfRoutesPLayer2; i++) {
            route2.add(ChMapPublic.ALL_ROUTES.get(getRandomNumber(0, ChMapPublic.ALL_ROUTES.size())));
        }
        List<Card> faceUpCards = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            faceUpCards.add(Card.ALL.get(getRandomNumber(0,Card.ALL.size())));
        }
        PublicCardState publicCardState = new PublicCardState(faceUpCards, deckSize, discardSize);
        PublicPlayerState publicPlayerState1 = new PublicPlayerState(ticketCountPlayer1,cardCountPLayer1, route1);
        PublicPlayerState publicPlayerState2 = new PublicPlayerState(ticketCountPlayer2,cardCountPLayer2, route2);
        Map<PlayerId, PublicPlayerState> playerState = Map.of(PlayerId.PLAYER_1, publicPlayerState1, PlayerId.PLAYER_2, publicPlayerState2);

        if(print){
            System.out.println("===========================");
            System.out.println("Route1 ");
            route1.forEach((s)-> System.out.print(s.id() + " "));
            System.out.println();
            System.out.println("Route2 " );
            route2.forEach((s)-> System.out.print(s.id() + " "));
            System.out.println();
            System.out.println("faceUpCards " + faceUpCards);
            System.out.println("deckSize " + publicCardState.deckSize() + " discardSize " + publicCardState.discardsSize());
            System.out.println("ticketCountPlayer1 " + publicPlayerState1.ticketCount() + " cardCountPLayer1 " + publicPlayerState1.cardCount());
            System.out.println("ticketCountPlayer2 " + publicPlayerState2.ticketCount() + " cardCountPLayer2 " + publicPlayerState2.cardCount());
            System.out.println(playerState.containsKey(PlayerId.PLAYER_1) +" "+  playerState.containsKey(PlayerId.PLAYER_2) +" "+ playerState.containsValue(publicPlayerState1) + " " + playerState.containsValue(publicPlayerState2));
            System.out.println("===========================");

        }


        return new PublicGameState(ticketCount, publicCardState, currentPlayer, playerState, previousPlayer);
    }

    public PublicGameState publicGameStateWithOneElementInMap(){
        PublicGameState p = publicGameStateCreator(5,6,5,5,5,5,10,0, PlayerId.PLAYER_1,PlayerId.PLAYER_2, 5, false);
        Map<PlayerId, PublicPlayerState> playerState1Element = Map.of(PlayerId.PLAYER_1,  p.playerState(PlayerId.PLAYER_1));
        return new PublicGameState(p.ticketsCount(), p.cardState(), p.currentPlayerId(), playerState1Element, p.lastPlayer());
    }

    public PublicGameState publicGameStateWithNull1(){
        PublicGameState p = publicGameStateCreator(5,6,5,5,5,5,10,0, PlayerId.PLAYER_1,PlayerId.PLAYER_2, 5, false);
        Map<PlayerId, PublicPlayerState> playerState = Map.of(PlayerId.PLAYER_1,  p.playerState(PlayerId.PLAYER_1), PlayerId.PLAYER_2, p.playerState(PlayerId.PLAYER_2));
        return new PublicGameState(p.ticketsCount(), null, p.currentPlayerId(), playerState, p.lastPlayer());
    }

    public PublicGameState publicGameStateWithNull2(){
        PublicGameState p = publicGameStateCreator(5,6,5,5,5,5,10,0, PlayerId.PLAYER_1,PlayerId.PLAYER_2, 5, false);
        Map<PlayerId, PublicPlayerState> playerState = Map.of(PlayerId.PLAYER_1,  p.playerState(PlayerId.PLAYER_1), PlayerId.PLAYER_2, p.playerState(PlayerId.PLAYER_2));
        return new PublicGameState(p.ticketsCount(), p.cardState(), null, playerState, p.lastPlayer());
    }

    public PublicGameState publicGameStateWithNull3(){
        PublicGameState p = publicGameStateCreator(5,6,5,5,5,5,10,0, PlayerId.PLAYER_1,PlayerId.PLAYER_2, 5, false);
        Map<PlayerId, PublicPlayerState> playerState = Map.of(PlayerId.PLAYER_1,  p.playerState(PlayerId.PLAYER_1), PlayerId.PLAYER_2, p.playerState(PlayerId.PLAYER_2));
        return new PublicGameState(p.ticketsCount(), p.cardState(), p.currentPlayerId(), null, p.lastPlayer());
    }

    public PublicGameState publicGameStateWithNull4(){
        PublicGameState p = publicGameStateCreator(5,6,5,5,5,5,10,0, PlayerId.PLAYER_1,PlayerId.PLAYER_2, 5, false);
        Map<PlayerId, PublicPlayerState> playerState = Map.of(PlayerId.PLAYER_1,  p.playerState(PlayerId.PLAYER_1), PlayerId.PLAYER_2, p.playerState(PlayerId.PLAYER_2));
        return new PublicGameState(p.ticketsCount(), p.cardState(), p.currentPlayerId(), playerState, null);
    }

    @Test
    public void constructorWorksWell(){
        // Route de test
        PublicGameState publicGameState = publicGameStateCreator(5,6,5,5,5,5,10,0, PlayerId.PLAYER_1,PlayerId.PLAYER_2, 5, true);

        assertThrows(IllegalArgumentException.class, ()->{
            publicGameStateCreator(5,5,5,5,5,5,10,0, PlayerId.PLAYER_1,PlayerId.PLAYER_2, -5, false);
        });
        assertThrows(IllegalArgumentException.class, this::publicGameStateWithOneElementInMap);
        assertThrows(NullPointerException.class, this::publicGameStateWithNull1);
        assertThrows(NullPointerException.class, this::publicGameStateWithNull2);
        assertThrows(NullPointerException.class, this::publicGameStateWithNull3);
        publicGameStateWithNull4();
    }

    @Test
    public void getterAreOk(){
        PublicGameState publicGameState = publicGameStateCreator(5,6,5,5,5,5,10,0, PlayerId.PLAYER_1,PlayerId.PLAYER_2, 5, false);
        assertEquals(5, publicGameState.ticketsCount());
        assertTrue(publicGameState.canDrawTickets());

        PublicGameState publicGameState1 = publicGameStateCreator(5,6,5,6,10,12,10,0, PlayerId.PLAYER_1,PlayerId.PLAYER_2, 0, true);
        assertEquals(0, publicGameState1.ticketsCount());
        assertFalse(publicGameState1.canDrawTickets());

        // Decomenter pour print mais ca marche
//        assertEquals(List.of(publicGameState1.cardState().faceUpCards()), publicGameState1.cardState().faceUpCards());
        assertEquals(10, publicGameState1.cardState().deckSize());
        assertEquals(0, publicGameState1.cardState().discardsSize());
        publicGameState1.claimedRoutes().forEach((s)->System.out.print(s.id() + " "));
        System.out.println();

        assertEquals(PlayerId.PLAYER_1, publicGameState1.currentPlayerId());
        assertEquals(PlayerId.PLAYER_2, publicGameState1.lastPlayer());

        PublicGameState publicGameState2 = publicGameStateWithNull4();
        assertEquals(null, publicGameState2.lastPlayer());

        publicGameState1.playerState(PlayerId.PLAYER_1).routes().forEach((s)->System.out.print(s.id() + " "));
        System.out.println();
        publicGameState1.playerState(PlayerId.PLAYER_2).routes().forEach((s)->System.out.print(s.id() + " "));
        System.out.println();
        assertEquals(publicGameState1.playerState(PlayerId.PLAYER_1).routes(), publicGameState1.currentPlayerState().routes());
        assertEquals(5, publicGameState1.currentPlayerState().ticketCount());
        assertEquals(10, publicGameState1.currentPlayerState().cardCount());
        assertEquals(6, publicGameState1.playerState(PlayerId.PLAYER_2).ticketCount());
        assertEquals(12, publicGameState1.playerState(PlayerId.PLAYER_2).cardCount());

    }
}
