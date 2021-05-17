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

public abstract class ServerMain extends Application  {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<PlayerId, String> playerNames = Map.of(
                PLAYER_1, getParameters().getRaw().get(0),
                PLAYER_2, getParameters().getRaw().get(1));

        try {
            ServerSocket serverSocket = new ServerSocket(5108);
            Socket socket = serverSocket.accept();

            GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
            Player remotePlayerProxy = new RemotePlayerProxy(socket);

            Map<PlayerId, Player> players = Map.of(PLAYER_1, graphicalPlayer , PLAYER_2, remotePlayerProxy);

            new Thread(() -> Game.play(players, playerNames, SortedBag.of(ChMap.tickets()), new Random()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
