package ch.epfl.tchu.gui;

import ch.epfl.tchu.game.PlayerId;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Separator;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.util.Map;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;

/**
 * Abstract class that creates the left panel of the GUI containing all the information of the Game (package private)
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
final class InfoViewCreator {

    private static final int CIRCLE_RADIUS = 5;

    //In order to make the class non instantiable
    private InfoViewCreator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates a Node that corresponds to the left panel of the game
     * @param playerNames that maps the PlayerId to the names
     * @param observableGameState current observableGameState so that the points and other information can be updated
     * @param gameMessages ObservableList containing the last 5 actions of the game
     * @return a VBox corresponding to the left panel of the game, for the game global information
     */
    public static Node createInfoView(Map<PlayerId, String> playerNames,
                                      ObservableGameState observableGameState, ObservableList<Text> gameMessages) {
        VBox vBox = new VBox();
        vBox.getStylesheets().addAll("info.css", "colors.css");

        Separator separator = new Separator();
        separator.setOrientation(Orientation.HORIZONTAL);

        TextFlow gameInfo = new TextFlow();
        gameInfo.setId("game-info");
        Bindings.bindContent(gameInfo.getChildren(), gameMessages);

        //Creates the two player information displays
        for (PlayerId id : PlayerId.ALL) {

            VBox playerVBox = new VBox();
            playerVBox.setId("player-stats");

            Circle circle = new Circle(CIRCLE_RADIUS);
            circle.getStyleClass().add("filled"); //TODO changer couleur ici

            Text text = new Text();
            text.textProperty().bind(Bindings.format(StringsFr.PLAYER_STATS,
                    playerNames.get(id),
                    observableGameState.ticketCounts(id),
                    observableGameState.cardCounts(id),
                    observableGameState.carCounts(id),
                    observableGameState.claimPoints(id)));

            TextFlow playerStats = new TextFlow();
            playerStats.getStyleClass().add(id == PLAYER_1 ? PLAYER_1.name() : PLAYER_2.name());
            playerStats.getChildren().addAll(circle, text);

            playerVBox.getChildren().add(playerStats);
            vBox.getChildren().add(playerVBox);
        }

        vBox.getChildren().addAll(separator, gameInfo);

        return vBox;
    }
}
