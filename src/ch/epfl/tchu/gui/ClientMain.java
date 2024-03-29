package ch.epfl.tchu.gui;

import ch.epfl.tchu.net.RemotePlayerClient;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

import static ch.epfl.tchu.gui.ServerMain.PORT;

/**
 * Game client supposed to be launched by one of the players
 * Can take program execution arguments
 *
 * @author Yann Ennassih (329978)
 * @author Jérémy Barghorn (328403)
 */
public final class ClientMain extends Application {

    //By default, program execution arguments are set to : localhost 5108
    private String hostName = "localhost";
    private int port = PORT;

    /**
     * Main class that launches a game client, especially the start(...) method
     * @param args hostName and port of the destination Server
     */
    public static void main(String[] args) { launch(args); }

    /**
     * Creates the game client, connects to the server and launches the client-side player gui
     * @param primaryStage not used
     */
    @Override
    public void start(Stage primaryStage){

        //Gets the program execution arguments
        List<String> parameters = getParameters().getRaw();
        if (!parameters.isEmpty()){
            hostName = parameters.get(0);
            port = Integer.parseInt(parameters.get(1));
        }

        //Creates the remote client
        GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
        RemotePlayerClient distantClient = new RemotePlayerClient(graphicalPlayer, hostName, port);

        //Starts the network associated thread
        new Thread(distantClient::run).start();
    }
}
