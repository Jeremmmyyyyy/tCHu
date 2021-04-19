package ch.epfl.tchu.net;

import ch.epfl.tchu.game.*;

import java.util.*;

import static java.util.Base64.getDecoder;


public final class Serdes{

    public final Serde<Integer> intSerde = Serde.of(i -> Integer.toString(i), Integer::parseInt);
    public final Serde<String> stringSerde = Serde.of(String::toString , String::toString); //TODO WTF
    public final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);
    public final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);
    public final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);
    public final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());
    public final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());

    Base64.Encoder encoder = Base64.getEncoder();
    Base64.Decoder decoder = Base64.getDecoder();

//    public final Serde<List<String>> base64Encoder = Serde.of(i -> encoder.encodeToString(i.getBytes(java.nio.charset.StandardCharsets.UTF_8)), s -> decoder.decode(s));
//    public final Serde<List<String>> listStringSerde = Serde.listOf(base64Encoder, ",");

    private Serdes(){

    }


}
