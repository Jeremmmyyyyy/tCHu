package ch.epfl.tchu.net;

import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;

import java.io.*;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import static ch.epfl.tchu.gui.StringsFr.SPACE_SEPARATOR;
import static ch.epfl.tchu.net.Serdes.SPLIT_LIMIT;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * Represents a remote client for a given player
 *
 * @author Jérémy Barghorn (328403)
 * @author Yann Ennassih (329978)
 */
public final class RemotePlayerClient {

    private final Player player;
    private final String name;
    private final int port;

    /**
     * Class constructor, initializes the Player instance attached to this
     * @param player attached to the client
     * @param name of the host
     * @param port of the host
     */
    public RemotePlayerClient(Player player, String name, int port) {
        this.player = player;
        this.name = name;
        this.port = port;
    }

    /**
     * Runs the connection between the remote client and the server
     * For each player possible action, calls the corresponding method of the attribute player
     * with the deserialized received arguments.
     * And if the method has a return type, communicates its serialized return object.
     */
    public void run() {

        try (
            Socket s = new Socket(name, port);
            BufferedReader reader = new BufferedReader(new InputStreamReader(s.getInputStream(), US_ASCII));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(s.getOutputStream(),US_ASCII))) {

            String readLine;

            while ((readLine = reader.readLine()) != null) {

                //Splits the received serialized message into its different arguments
                String[] splitMessage = readLine.split(Pattern.quote(SPACE_SEPARATOR), SPLIT_LIMIT);
                MessageId messageId = MessageId.valueOf(splitMessage[0]);
                StringBuilder answer = new StringBuilder();

                switch (messageId) {

                    case INIT_PLAYERS :
                        //splitMessage[1] = ownId | splitMessage[2] = playerNames
                        List<String> playerNames = Serdes.LIST_STRING_SERDE.deserialize(splitMessage[2]);
                        player.initPlayers(
                                Serdes.PLAYER_ID_SERDE.deserialize(splitMessage[1]),
                                Map.of(
                                        PlayerId.PLAYER_1, playerNames.get(0),
                                        PlayerId.PLAYER_2, playerNames.get(1)));
                        break;

                    case RECEIVE_INFO :
                        //splitMessage[1] = info
                        player.receiveInfo(Serdes.STRING_SERDE.deserialize(splitMessage[1]));
                        break;

                    case UPDATE_STATE :
                        //splitMessage[1] = newState | splitMessage[2] = ownState
                        player.updateState(
                                Serdes.PUBLIC_GAME_STATE_SERDE.deserialize(splitMessage[1]),
                                Serdes.PLAYER_STATE_SERDE.deserialize(splitMessage[2]));
                        break;

                    case SET_INITIAL_TICKETS :
                        //splitMessage[1] = tickets
                        player.setInitialTicketChoice(Serdes.SORTED_BAG_TICKET_SERDE.deserialize(splitMessage[1]));
                        break;

                    case CHOOSE_INITIAL_TICKETS :
                        answer.append(Serdes.SORTED_BAG_TICKET_SERDE.serialize(player.chooseInitialTickets()));
                        break;

                    case NEXT_TURN :
                        answer.append(Serdes.TURN_KIND_SERDE.serialize(player.nextTurn()));
                        break;

                    case CHOOSE_TICKETS :
                        //splitMessage[1] = options
                        answer.append(Serdes.SORTED_BAG_TICKET_SERDE.serialize(
                                player.chooseTickets(Serdes.SORTED_BAG_TICKET_SERDE.deserialize(splitMessage[1]))));
                        break;

                    case DRAW_SLOT :
                        answer.append(Serdes.INTEGER_SERDE.serialize(player.drawSlot()));
                        break;

                    case ROUTE :
                        answer.append(Serdes.ROUTE_SERDE.serialize(player.claimedRoute()));
                        break;

                    case CARDS:
                        answer.append(Serdes.SORTED_BAG_CARD_SERDE.serialize(player.initialClaimCards()));
                        break;

                    case CHOOSE_ADDITIONAL_CARDS:
                        //splitMessage[1] = options
                        answer.append(Serdes.SORTED_BAG_CARD_SERDE.serialize(
                                player.chooseAdditionalCards(Serdes.LIST_SORTEDBAG_CARD_SERDE.deserialize(splitMessage[1]))));
                        break;

                    default:
                        throw new Error();
                }

                //If the player has to communicate, enters this loop
                if(answer.length() != 0) {
                    writer.write(String.format("%s\n", answer));
                    writer.flush();
                }
            }

        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}

