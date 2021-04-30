package ch.epfl.tchu.net;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


// TODO dans la consigne retour à la ligne

public class RemotePlayerProxy implements Player {
    private final Socket SOCKET;
    private final BufferedReader READER;
    private final BufferedWriter WRITER;

    public RemotePlayerProxy(Socket socket) throws IOException {
        this.SOCKET = socket;
        this.READER = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.US_ASCII));
        this.WRITER = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
    }

    @Override
    public void initPlayers(PlayerId ownId, Map<PlayerId, String> playerNames) {
        List<String> listPlayerNames = new ArrayList<>();
        playerNames.forEach((k,v)-> listPlayerNames.add(v));
        sendMessage(MessageId.INIT_PLAYERS.name() + " " + Serdes.PLAYER_ID_SERDE.serialize(ownId) + " " +Serdes.LIST_STRING_SERDE.serialize(listPlayerNames));
    }

    @Override
    public void receiveInfo(String info) {
        sendMessage(MessageId.RECEIVE_INFO.name() + " " + Serdes.STRING_SERDE.serialize(info));
    }

    @Override
    public void updateState(PublicGameState newState, PlayerState ownState) {
        sendMessage(MessageId.UPDATE_STATE.name() + " " + Serdes.PUBLIC_GAME_STATE_SERDE.serialize(newState) + " " + Serdes.PLAYER_STATE_SERDE.serialize(ownState));
    }

    @Override
    public void setInitialTicketChoice(SortedBag<Ticket> tickets) {
        sendMessage(MessageId.SET_INITIAL_TICKETS.name() + " " + Serdes.SORTED_BAG_TICKET_SERDE.serialize(tickets));
    }

    @Override
    public SortedBag<Ticket> chooseInitialTickets() {
        sendMessage(MessageId.CHOOSE_INITIAL_TICKETS.name());
        return Serdes.SORTED_BAG_TICKET_SERDE.deserialize(getMessage());
    }

    @Override
    public TurnKind nextTurn() {
        sendMessage(MessageId.NEXT_TURN.name());
        return Serdes.TURN_KIND_SERDE.deserialize(getMessage());
    }

    @Override
    public SortedBag<Ticket> chooseTickets(SortedBag<Ticket> options) {
        sendMessage(MessageId.CHOOSE_TICKETS.name() + " " + Serdes.SORTED_BAG_TICKET_SERDE.serialize(options));
        return Serdes.SORTED_BAG_TICKET_SERDE.deserialize(getMessage());
    }

    @Override
    public int drawSlot() {
        sendMessage(MessageId.DRAW_SLOT.name());
        return Serdes.INTEGER_SERDE.deserialize(getMessage());
    }

    @Override
    public Route claimedRoute() {
        sendMessage(MessageId.ROUTE.name());
        return Serdes.ROUTE_SERDE.deserialize(getMessage());
    }

    @Override
    public SortedBag<Card> initialClaimCards() {
        sendMessage(MessageId.CARDS.name());
        return Serdes.SORTED_BAG_CARD_SERDE.deserialize(getMessage());
    }

    @Override
    public SortedBag<Card> chooseAdditionalCards(List<SortedBag<Card>> options) {
        sendMessage(MessageId.CHOOSE_ADDITIONAL_CARDS.name() + " " + Serdes.LIST_SORTEDBAG_CARD_SERDE.serialize(options));
        return Serdes.SORTED_BAG_CARD_SERDE.deserialize(getMessage());
    }

    private void sendMessage(String message)  {
        try {
            WRITER.write(message);
            WRITER.write('\n');
            WRITER.flush();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }

    private String getMessage(){
        try {
            return READER.readLine();
        }catch (IOException e){
            throw new UncheckedIOException(e);
        }
    }
}
