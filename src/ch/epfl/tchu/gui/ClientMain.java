package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

public final class ClientMain extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {

        String hostName = getParameters().getRaw().get(0);
        int port = Integer.parseInt(getParameters().getRaw().get(1));

        GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
        RemotePlayerClient distantClient = new RemotePlayerClient(graphicalPlayer, hostName, port);

        new Thread(distantClient::run);
    }
}
