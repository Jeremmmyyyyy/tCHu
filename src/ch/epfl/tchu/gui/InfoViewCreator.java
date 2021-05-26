package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;

/**
 * Abstract class that creates the left panel of the GUI containing all the information of the Game
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
abstract class InfoViewCreator {

    private static final int CIRCLE_RADIUS = 5;

    /**
     * Create a Node that corresponds to the left panel of the game
     * @param playerId of the current player
     * @param playerNames that maps the PlayerId to the names
     * @param observableGameState current observableGameState so the points and other informations can be updated
     * @param gameMessages ObservableList containing the last 5 actions of the game
     * @return a VBox corresponding to the left panel of the game
     */
    public static Node createInfoView(Map<PlayerId, String> playerNames,
                                      ObservableGameState observableGameState, ObservableList<Text> gameMessages) {

        VBox vBox = new VBox();
        vBox.getStylesheets().addAll("info.css", "colors.css");

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        TextFlow messages = new TextFlow();
        messages.setId("game-info");
        messages.getChildren().addAll(gameMessages);
        Bindings.bindContent(messages.getChildren(), gameMessages);

        for (PlayerId id : PlayerId.ALL) {

            VBox playerVBox = new VBox();
            playerVBox.setId("player-stats");

            TextFlow playerStats = new TextFlow();
            Circle circle = new Circle(CIRCLE_RADIUS);
            Text text = new Text();
            text.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS,
                    playerNames.get(id),
                    observableGameState.ticketCounts(id),
                    observableGameState.cardCounts(id),
                    observableGameState.carCounts(id),
                    observableGameState.claimPoints(id)));
            circle.getStyleClass().add("filled");

            playerStats.getStyleClass().add(id == PLAYER_1 ? "PLAYER_1" : "PLAYER_2");
            playerStats.getChildren().addAll(circle, text);

            playerVBox.getChildren().add(playerStats);
            vBox.getChildren().add(playerVBox);
        }

        vBox.getChildren().addAll(separator, messages);


        return vBox;
    }
}
