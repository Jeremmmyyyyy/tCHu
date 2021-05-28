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
import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
/**
 * Game server supposed to be launched by one of the players
 * Can take program execution arguments
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class ServerMain extends Application  {

    //By default, the program execution arguments are set to : Ada Charles
    private static Map<PlayerId, String> playerNames = Map.of(PLAYER_1, "Ada", PLAYER_2, "Charles");

    public static final int PORT = 5108;

    /**
     * Main class that launches a game server, especially the start(...) method
     * @param args PlayerId of the first and second player
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Creates the game server, waits for an incoming connection and launches the server-side player gui
     * @param primaryStage not used
     */
    @Override
    public void start(Stage primaryStage){

        //Gets the program execution arguments
        List<String> parameters = getParameters().getRaw();
        if (!parameters.isEmpty()){
            playerNames = Map.of(PLAYER_1, parameters.get(0), PLAYER_2, parameters.get(1));
        }

        try {
            ServerSocket serverSocket = new ServerSocket(PORT);
            //Waits for an incoming connection
            Socket socket = serverSocket.accept();

            //Creates the two players
            GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
            Player remotePlayerProxy = new RemotePlayerProxy(socket);
            Map<PlayerId, Player> players = Map.of(PLAYER_1, graphicalPlayer , PLAYER_2, remotePlayerProxy);

            //Launches the game's main thread
            new Thread(() -> Game.play(players, playerNames, SortedBag.of(ChMap.tickets()), new Random())).start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}