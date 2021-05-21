package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

public final class ClientMain extends Application {
    private String hostName = "localhost";
    private int port = 5108;

    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception {
        System.out.println("Client Started");
        if (!getParameters().getRaw().isEmpty()){
            hostName = getParameters().getRaw().get(0);
            port = Integer.parseInt(getParameters().getRaw().get(1));
        }
        System.out.println("Arguments " + hostName + " " + port + " isEmpty " + getParameters().getRaw().isEmpty());

        GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
        RemotePlayerClient distantClient = new RemotePlayerClient(graphicalPlayer, hostName, port);

        new Thread(distantClient::run).start();
    }
}
