import ch.epfl.tchu.game.PlayerId;
import ch.epfl.tchu.gui.Launcher;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

public class GameLauncher extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<PlayerId, String> playerNames = Map.of(PLAYER_1, "Player 1", PLAYER_2, "Player 2");

        Launcher launcher = new Launcher(PLAYER_1, playerNames);
    }
}
