package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Client part of the game supposed to be launched by 1 of the players
 * Program Arguments can be set by default or need hostName and port of the game Server
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class ClientMain extends Application {
    private String hostName = "localhost";
    private int port = 5108;

    /**
     * Main class that launches a client for the game
     * @param args hostName and port of the destination Server
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Create the Client of the game, connects to the server and launch the GUI for the Client side player
     * @param primaryStage not used
     */
    @Override
    public void start(Stage primaryStage){
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
