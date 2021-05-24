package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.ChMap;
import ch.epfl.tchu.game.Game;
import ch.epfl.tchu.game.Player;
import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
/**
 * Server part of the game supposed to be launched by 1 of the players
 * Program Arguments can be set by default or need the two names of the players
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class ServerMain extends Application  {
    private Map<PlayerId, String> playerNames = Map.of(PLAYER_1, "Player1", PLAYER_2, "Player2");

    /**
     * Main class that launches a server for the game
     * @param args PlayerId of the first and second player
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Create the Server of the game, wait for an incoming connection and launch the GUI for the Server side player
     * @param primaryStage not used
     */
    @Override
    public void start(Stage primaryStage){
        System.out.println("Server Started");
        if (!getParameters().getRaw().isEmpty()){
            playerNames = Map.of(
                    PLAYER_1, getParameters().getRaw().get(0),
                    PLAYER_2, getParameters().getRaw().get(1));
        }
        System.out.println("Arguments " + playerNames.get(PLAYER_1) + " " + playerNames.get(PLAYER_2) + " isEmpty " + getParameters().getRaw().isEmpty());
        try {
            ServerSocket serverSocket = new ServerSocket(5108);
            Socket socket = serverSocket.accept();

            GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
            Player remotePlayerProxy = new RemotePlayerProxy(socket);

            Map<PlayerId, Player> players = Map.of(PLAYER_1, graphicalPlayer , PLAYER_2, remotePlayerProxy);

            new Thread(() -> Game.play(players, playerNames, SortedBag.of(ChMap.tickets()), new Random())).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}