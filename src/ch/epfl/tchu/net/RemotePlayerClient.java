package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;

import java.io.*;
import java.net.Socket;
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

    public void run() { //TODO check pour enlever le retour a la ligne /n
        try (
            Socket s = new Socket(name, port);
            BufferedReader r = new BufferedReader(new InputStreamReader(s.getInputStream(), US_ASCII));
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),US_ASCII))) {

            String readLine = r.readLine();

            while (readLine != null) {

                String[] m = r.readLine().split(Pattern.quote(" "), -1);
                MessageId messageId = MessageId.valueOf(m[0]);
                String o = "";

                switch (messageId) {

                    case INIT_PLAYERS :
                        player.initPlayers(
                                Serdes.PLAYER_ID_SERDE.deserialize(m[1]), Map.of(
                                        PlayerId.PLAYER_1, Serdes.STRING_SERDE.deserialize((m[2])),
                                        PlayerId.PLAYER_2, Serdes.STRING_SERDE.deserialize((m[3]))));
                    case RECEIVE_INFO :
                        player.receiveInfo(Serdes.STRING_SERDE.deserialize(m[1]));
                    case UPDATE_STATE :
                        player.updateState(
                                Serdes.PUBLIC_GAME_STATE_SERDE.deserialize(m[1]),
                                Serdes.PLAYER_STATE_SERDE.deserialize(m[2]));
                    case SET_INITIAL_TICKETS :
                        player.setInitialTicketChoice(Serdes.SORTED_BAG_TICKET_SERDE.deserialize(m[1]));
                    case CHOOSE_INITIAL_TICKETS :
                        o = Serdes.SORTED_BAG_TICKET_SERDE.serialize(player.chooseInitialTickets());
                    case NEXT_TURN :
                        o = Serdes.TURN_KIND_SERDE.serialize(player.nextTurn());
                    case CHOOSE_TICKETS :
                        o = Serdes.SORTED_BAG_TICKET_SERDE.serialize(
                                player.chooseTickets(Serdes.SORTED_BAG_TICKET_SERDE.deserialize(m[1])));
                    case DRAW_SLOT :
                        o = Serdes.INTEGER_SERDE.serialize(player.drawSlot());
                    case ROUTE :
                        o = Serdes.ROUTE_SERDE.serialize(player.claimedRoute());
                    case CARDS:
                        o = Serdes.SORTED_BAG_CARD_SERDE.serialize(player.initialClaimCards());
                    case CHOOSE_ADDITIONAL_CARDS:
                        o = Serdes.SORTED_BAG_CARD_SERDE.serialize(
                                player.chooseAdditionalCards(Serdes.LIST_SORTEDBAG_CARD_SERDE.deserialize(m[1])));
                }

                w.write(o);
                w.write("/n"); //TODO methode pour faire un message comme celui du proxy ?
                w.flush();

            }


        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }

    }


}

