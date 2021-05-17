package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.US_ASCII;

public class RemotePlayerClient {

    private Player player;
    private String name;
    private int port;

    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;
        this.name = name;
        this.port = port;
    }

    public void run() {
        try (
            Socket s = new Socket(name, port);
            BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream(), US_ASCII));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),US_ASCII))) {

            String readLine;

            while ((readLine = r.readLine()) != null) {

                String[] m = readLine.split(Pattern.quote(" "), -1);
                MessageId messageId = MessageId.valueOf(m[0]);
                String o = "";

                switch (messageId) {

                    case INIT_PLAYERS :
                        List<String> l = Serdes.LIST_STRING_SERDE.deserialize(m[2]);
                        player.initPlayers(
                                Serdes.PLAYER_ID_SERDE.deserialize(m[1]), Map.of(
                                        PlayerId.PLAYER_1, l.get(0),
                                        PlayerId.PLAYER_2, l.get(1)));
                        o = null;
                        break;
                    case RECEIVE_INFO :
                        player.receiveInfo(Serdes.STRING_SERDE.deserialize(m[1]));
                        o = null;
                        break;
                    case UPDATE_STATE :
                        player.updateState(
                                Serdes.PUBLIC_GAME_STATE_SERDE.deserialize(m[1]),
                                Serdes.PLAYER_STATE_SERDE.deserialize(m[2]));
                        o = null;
                        break;
                    case SET_INITIAL_TICKETS :
                        player.setInitialTicketChoice(Serdes.SORTED_BAG_TICKET_SERDE.deserialize(m[1]));
                        o = null;
                        break;
                    case CHOOSE_INITIAL_TICKETS :
                        o = Serdes.SORTED_BAG_TICKET_SERDE.serialize(player.chooseInitialTickets());
                        break;
                    case NEXT_TURN :
                        o = Serdes.TURN_KIND_SERDE.serialize(player.nextTurn());
                        break;
                    case CHOOSE_TICKETS :
                        o = Serdes.SORTED_BAG_TICKET_SERDE.serialize(
                                player.chooseTickets(Serdes.SORTED_BAG_TICKET_SERDE.deserialize(m[1])));
                        break;
                    case DRAW_SLOT :
                        o = Serdes.INTEGER_SERDE.serialize(player.drawSlot());
                        break;
                    case ROUTE :
                        o = Serdes.ROUTE_SERDE.serialize(player.claimedRoute());
                        break;
                    case CARDS:
                        o = Serdes.SORTED_BAG_CARD_SERDE.serialize(player.initialClaimCards());
                        break;
                    case CHOOSE_ADDITIONAL_CARDS:
                        o = Serdes.SORTED_BAG_CARD_SERDE.serialize(
                                player.chooseAdditionalCards(Serdes.LIST_SORTEDBAG_CARD_SERDE.deserialize(m[1])));
                        break;
                }
                if(o != null){
                    w.write(o);
                    w.write("\n");
                    w.flush();
                }
            }


        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

