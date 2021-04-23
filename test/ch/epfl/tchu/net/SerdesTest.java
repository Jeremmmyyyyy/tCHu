package ch.epfl.tchu.net;
import ch.epfl.tchu.game.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.Card.*;

public class SerdesTest {

    @Test
    void SerdeTest(){
//        String test = Serdes.LIST_STRING_SERDE.serialize(List.of("je", "m'appelle", "jeremy"));
//        System.out.println(Serdes.LIST_STRING_SERDE.deserialize(test));


        List<Card> fu = List.of(RED, WHITE, BLUE, BLACK, RED);
        PublicCardState cs = new PublicCardState(fu, 30, 31);
        List<Route> rs1 = ChMap.routes().subList(0, 2);
        Map<PlayerId, PublicPlayerState> ps = Map.of(
                PlayerId.PLAYER_1, new PublicPlayerState(10, 11, rs1),
                PlayerId.PLAYER_2, new PublicPlayerState(20, 21, List.of())); //TODO List.of() poru routes
        PublicGameState gs =
                new PublicGameState(40, cs, PlayerId.PLAYER_2, ps, null);

        String s = Serdes.PUBLIC_GAME_STATE_SERDE.serialize(gs);
        System.out.println(s);
        PublicGameState deserialized = Serdes.PUBLIC_GAME_STATE_SERDE.deserialize(s);
        System.out.println("\nFaceUpCards : " + deserialized.cardState().faceUpCards() +
                "\nDeckSize : " + deserialized.cardState().deckSize() +
                "\nDiscardSize : " + deserialized.cardState().discardsSize() +
                "\nLastPlayer : " + deserialized.lastPlayer() +
                "\nDiscardSize : " + deserialized.ticketsCount() +
                "\nCurrentPlayerId : " + deserialized.currentPlayerId() +
                "\nClaimedRoutes : " + deserialized.claimedRoutes()
        );


    }


}