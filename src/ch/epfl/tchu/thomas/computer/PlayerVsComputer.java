package ch.epfl.tchu.thomas.computer;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.gui.GraphicalPlayerAdapter;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public final class PlayerVsComputer extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {
        SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());

        List<Route> allRoutes = ChMap.routes();
        Random rng = new Random();
        Map<PlayerId, String> names = Map.of(PLAYER_1, "Computer", PLAYER_2, "Player");

        Map<PlayerId, Player> players =
                Map.of(PLAYER_1, new ComputerPlayer(rng.nextLong(), allRoutes), PLAYER_2, new GraphicalPlayerAdapter());

        new Thread(() -> Game.play(players, names, tickets, rng)).start();
    }
}
