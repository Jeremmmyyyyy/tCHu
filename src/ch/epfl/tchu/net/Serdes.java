package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import java.nio.charset.StandardCharsets;
import java.util.*;


/**
 * Non instantiable final class that contains all the possible Serdes for the Game
 *
 * @author Jérémy Barghorn (328403)
 */
public final class Serdes{

    private static Base64.Encoder encoder = Base64.getEncoder();
    private static Base64.Decoder decoder = Base64.getDecoder();

    /**
     * Serde for the Integers
     */
    public static final Serde<Integer> intSerde = Serde.of(i -> Integer.toString(i), Integer::parseInt);
    /**
     * Serde for the Strings (Serialize an object into a String in base 64, Deserialize an object from base 64)
     */
    public static final Serde<String> stringSerde = Serde
            .of(i -> encoder.encodeToString(i.getBytes(java.nio.charset.StandardCharsets.UTF_8)),
               s -> new String(decoder.decode(s), StandardCharsets.UTF_8));
    /**
     * Serde for the Enum PLayerId
     */
    public static final Serde<PlayerId> playerIdSerde = Serde.oneOf(PlayerId.ALL);
    /**
     * Serde for the enum TurnKind
     */
    public static final Serde<Player.TurnKind> turnKindSerde = Serde.oneOf(Player.TurnKind.ALL);
    /**
     * Serde for the Enum Cards
     */
    public static final Serde<Card> cardSerde = Serde.oneOf(Card.ALL);
    /**
     * Serde for all the Routes
     */
    public static final Serde<Route> routeSerde = Serde.oneOf(ChMap.routes());
    /**
     * Serde for all the Tickets
     */
    public static final Serde<Ticket> ticketSerde = Serde.oneOf(ChMap.tickets());
    /**
     * Serde for the List of Strings, separated by ","
     */
    public static final Serde<List<String>> listStringSerde = Serde.listOf(stringSerde, ",");
    /**
     * Serde for the List of Cards, separated by ","
     */
    public static final Serde<List<Card>> listCardSerde = Serde.listOf(cardSerde, ",");
    /**
     * Serde for the List of Routes, separated by ","
     */
    public static final Serde<List<Route>> listRouteSerde = Serde.listOf(routeSerde, ",");
    /**
     * Serde for the SortedBag of Cards, separated by ","
     */
    public static final Serde<SortedBag<Card>> sortedBagCardSerde = Serde.bagOf(cardSerde, ",");
    /**
     * Serde for the SortedBag of Tickets, separated by ","
     */
    public static final Serde<SortedBag<Ticket>> sortedBagTicketSerde = Serde.bagOf(ticketSerde, ",");
    /**
     * Serde for the List of SortedBag of Cards, separated by ";"
     */
    public static final Serde<List<SortedBag<Card>>> listSortedBagCardSerde = Serde.listOf(sortedBagCardSerde, ";"); // TODO juste ?

//    public static final Serde<PublicCardState> publicCardStateSerde = Serde.(PublicCardState);
//    PublicPlayerState	point-virgule (;)
//    PlayerState	point-virgule (;)
//    PublicGameState	deux-points (:)

    private Serdes(){

    }
}
