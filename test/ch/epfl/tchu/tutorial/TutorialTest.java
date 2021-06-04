package ch.epfl.tchu.tutorial;

import javafx.application.Application;
import javafx.stage.Stage;

public final class TutorialTest extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) {

        String playerName = "Kevin"; //TODO chopper son nom
        new Thread(() -> Tutorial.play(playerName)).start();
    }
}