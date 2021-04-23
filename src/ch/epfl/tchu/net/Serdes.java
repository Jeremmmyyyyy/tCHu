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

    private Serdes(){}

    /**
     * Serde for the Integers
     */
    public static final Serde<Integer> INTEGER_SERDE = Serde.of(i -> Integer.toString(i), Integer::parseInt);
    /**
     * Serde for the Strings (Serialize an object into a String in base 64, Deserialize an object from base 64)
     */
    public static final Serde<String> STRING_SERDE = Serde
            .of(i -> encoder.encodeToString(i.getBytes(java.nio.charset.StandardCharsets.UTF_8)),
               s -> new String(decoder.decode(s), StandardCharsets.UTF_8));
    /**
     * Serde for the Enum PLayerId
     */
    public static final Serde<PlayerId> PLAYER_ID_SERDE = Serde.oneOf(PlayerId.ALL);
    /**
     * Serde for the enum TurnKind
     */
    public static final Serde<Player.TurnKind> TURN_KIND_SERDE = Serde.oneOf(Player.TurnKind.ALL);
    /**
     * Serde for the Enum Cards
     */
    public static final Serde<Card> CARD_SERDE = Serde.oneOf(Card.ALL);
    /**
     * Serde for all the Routes
     */
    public static final Serde<Route> ROUTE_SERDE = Serde.oneOf(ChMap.routes());
    /**
     * Serde for all the Tickets
     */
    public static final Serde<Ticket> TICKET_SERDE = Serde.oneOf(ChMap.tickets());
    /**
     * Serde for the List of Strings, separated by ","
     */
    public static final Serde<List<String>> LIST_STRING_SERDE = Serde.listOf(STRING_SERDE, ",");
    /**
     * Serde for the List of Cards, separated by ","
     */
    public static final Serde<List<Card>> LIST_CARD_SERDE = Serde.listOf(CARD_SERDE, ",");
    /**
     * Serde for the List of Routes, separated by ","
     */
    public static final Serde<List<Route>> LIST_ROUTE_SERDE = Serde.listOf(ROUTE_SERDE, ",");
    /**
     * Serde for the SortedBag of Cards, separated by ","
     */
    public static final Serde<SortedBag<Card>> SORTED_BAG_CARD_SERDE = Serde.bagOf(CARD_SERDE, ",");
    /**
     * Serde for the SortedBag of Tickets, separated by ","
     */
    public static final Serde<SortedBag<Ticket>> SORTED_BAG_TICKET_SERDE = Serde.bagOf(TICKET_SERDE, ",");
    /**
     * Serde for the List of SortedBag of Cards, separated by ";"
     */
    public static final Serde<List<SortedBag<Card>>> LIST_SORTEDBAG_CARD_SERDE = Serde.listOf(SORTED_BAG_CARD_SERDE, ";");

    /**
     * Serde of PublicCardState, separated by ";"
     */
    public static final Serde<PublicCardState> PUBLIC_CARD_STATE_SERDE = Serde.
            of(cardState -> String.join(
            ";",
            LIST_CARD_SERDE.serialize(cardState.faceUpCards()),
            INTEGER_SERDE.serialize(cardState.deckSize()),
            INTEGER_SERDE.serialize(cardState.discardsSize())),

            string -> {
                String[] split = string.split(";");
                return new PublicCardState(
                        LIST_CARD_SERDE.deserialize(split[0]),
                        INTEGER_SERDE.deserialize(split[1]),
                        INTEGER_SERDE.deserialize(split[2]));
            });

    /**
     * Serde of PublicPlayerState, separated by ";"
     */
    public static final Serde<PublicPlayerState> PUBLIC_PLAYER_STATE_SERDE = Serde.
            of(playerState -> String.join(
                    ";",
                    INTEGER_SERDE.serialize(playerState.ticketCount()),
                    INTEGER_SERDE.serialize(playerState.cardCount()),
                    LIST_ROUTE_SERDE.serialize(playerState.routes())),

                    string -> {
                        String[] split = string.split(";");
                        return new PublicPlayerState(
                                INTEGER_SERDE.deserialize(split[0]),
                                INTEGER_SERDE.deserialize(split[1]),
                                LIST_ROUTE_SERDE.deserialize(split[2]));
                    });

    /**
     * Serde of PlayerState, separated by ";"
     */
    public static final Serde<PlayerState> PLAYER_STATE_SERDE = Serde.
            of(playerState -> String.join(
                    ";",
                    SORTED_BAG_TICKET_SERDE.serialize(playerState.tickets()),
                    SORTED_BAG_CARD_SERDE.serialize(playerState.cards()),
                    LIST_ROUTE_SERDE.serialize(playerState.routes())),

                    string -> {
                        String[] split = string.split(";");
                        return new PlayerState(
                                SORTED_BAG_TICKET_SERDE.deserialize(split[0]),
                                SORTED_BAG_CARD_SERDE.deserialize(split[1]),
                                LIST_ROUTE_SERDE.deserialize(split[2]));
                    });

    /**
     * Serde of PublicGameState, separated by ":"
     */
    public static final Serde<PublicGameState> GAME_STATE_SERDE = Serde.
            of(gameState -> String.join(
                    ":",
                    INTEGER_SERDE.serialize(gameState.ticketsCount()),
                    PUBLIC_CARD_STATE_SERDE.serialize(gameState.cardState()),
                    PLAYER_ID_SERDE.serialize(gameState.currentPlayerId()),
                    PUBLIC_PLAYER_STATE_SERDE.serialize(gameState.playerState(PlayerId.PLAYER_1)),
                    PUBLIC_PLAYER_STATE_SERDE.serialize(gameState.playerState(PlayerId.PLAYER_2)),
                    PLAYER_ID_SERDE.serialize(gameState.lastPlayer())),

                    string -> {
                        String[] split = string.split(":");
                        return new PublicGameState(
                                INTEGER_SERDE.deserialize(split[0]),
                                PUBLIC_CARD_STATE_SERDE.deserialize(split[1]),
                                PLAYER_ID_SERDE.deserialize(split[2]),
                                Map.of(PlayerId.PLAYER_1, PUBLIC_PLAYER_STATE_SERDE.deserialize(split[3]),
                                        PlayerId.PLAYER_2, PUBLIC_PLAYER_STATE_SERDE.deserialize(split[4])),
                                PLAYER_ID_SERDE.deserialize(split[5]));
                    });

}