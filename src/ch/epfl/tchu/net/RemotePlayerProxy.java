package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Represents a proxy player and overrides all Player methods in order to send information to a client player
 *
 * @author Jérémy Barghorn (328403)
 * @author Yann Ennassih (329978)
 */
public final class RemotePlayerProxy implements Player {

    private final BufferedReader READER;
    private final BufferedWriter WRITER;

    /**
     * Class constructor
     * @param socket used to communicate with the client through the network
     * @throws IOException when the socket isn't correctly connected
     */
    public RemotePlayerProxy(Socket socket) throws IOException {
        this.READER = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
        this.WRITER = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
    }

    /**
     * Sends a serialized message to describe the players initialization with the given playerId and playerNames
     * @param ownId Id of the player
     * @param playerNames names of all the players
     */
    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        List<String> listPlayerNames = new ArrayList<>();
        playerNames.forEach((k,v)-> listPlayerNames.add(v));

        sendMessage(
                MessageId.INIT_PLAYERS.name(),
                Serdes.PLAYER_ID_SERDE.serialize(ownId),
                Serdes.LIST_STRING_SERDE.serialize(listPlayerNames));
    }

    /**
     * Sends a serialized message to communicate a receiving information
     * @param info that has to be transmitted : generally created with Info.java
     */
    @Override
    public void receiveInfo(String info) {
        sendMessage(MessageId.RECEIVE_INFO.name(), Serdes.STRING_SERDE.serialize(info));
    }

    /**
     * Sends a serialized message to communicate a state update
     * @param newState new PublicGameState that has to be known by the player
     * @param ownState PlayerState of the PublicPlayerState
     */
    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        sendMessage(
                MessageId.UPDATE_STATE.name(),
                Serdes.PUBLIC_GAME_STATE_SERDE.serialize(newState),
                Serdes.PLAYER_STATE_SERDE.serialize(ownState));
    }

    /**
     * Sends a serialized message to communicate the initial ticket choice
     * @param tickets SortedBag containing all the 5 drawn tickets
     */
    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        sendMessage(MessageId.SET_INITIAL_TICKETS.name(), Serdes.SORTED_BAG_TICKET_SERDE.serialize(tickets));
    }

    /**
     * Sends a serialized message to communicate the initial ticket choice
     * @return the initial ticket choice as a SortedBag of Ticket
     */
    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS.name());
        return Serdes.SORTED_BAG_TICKET_SERDE.deserialize(getMessage());
    }

    /**
     * Sends a serialized message to communicate the next turn kind of the player
     * @return the next turn kind the player wants to play
     */
    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN.name());
        return Serdes.TURN_KIND_SERDE.deserialize(getMessage());
    }

    /**
     * Sends a serialized message to communicate the ticket choice of the player when drawing tickets
     * @param options possible tickets to draw
     * @return the tickets chosen by the player
     */
    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS.name(), Serdes.SORTED_BAG_TICKET_SERDE.serialize(options));
        return Serdes.SORTED_BAG_TICKET_SERDE.deserialize(getMessage());
    }

    /**
     * Sends a serialized message to communicate the draw slot chosen by the player when drawing cards
     * @return the draw slot chosen by the player
     */
    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT.name());
        return Serdes.INTEGER_SERDE.deserialize(getMessage());
    }

    /**
     * Sends a serialized message to communicate the route the player tries to claim
     * @return the Route the player tries to claim
     */
    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE.name());
        return Serdes.ROUTE_SERDE.deserialize(getMessage());
    }

    /**
     * Sends a serialized message to communicate the initial claim cards of the player when claiming a route
     * @return the initial claim cards the player wants to play
     */
    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS.name());
        return Serdes.SORTED_BAG_CARD_SERDE.deserialize(getMessage());
    }

    /**
     * Sends a serialized message to communicate the additional cards chosen on a tunnel claim attempt
     * @param options list of the possible additional cards combinations
     * (empty if the player can't control or doesn't want to control the route)
     * @return the chosen additional cards
     */
    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS.name(), Serdes.LIST_SORTEDBAG_CARD_SERDE.serialize(options));
        return Serdes.SORTED_BAG_CARD_SERDE.deserialize(getMessage());
    }

    /**
     * Private method called by the above overridden Player's methods
     * in order to send a given message from the connected socket
     * @param args String serialized arguments
     */
    private void sendMessage(String... args) {
        String message = String.join(" ", args);
        try {
            WRITER.write(String.format("%s\n", message));
            WRITER.flush();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Private method called by the above overridden Player's methods
     * in order to read a received message
     * @return the message received
     */
    private String getMessage(){
        try {
            return READER.readLine();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

}
