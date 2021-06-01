package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import ch.epfl.tchu.net.RemotePlayerClient;
import ch.epfl.tchu.net.RemotePlayerProxy;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.Random;
import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
import static javafx.application.Platform.isFxApplicationThread;

public final class Launcher {

    private static final int PORT = 5108;
    private final Stage launcher;
    private final int STAGE_WIDTH = 700;
    private final int STAGE_HEIGHT = 500;
    private static String namePlayer1 = "Joueur 1";
    private static String namePlayer2 = "Joueur 2";
    private static Color color1;
    private static Color color2;
    private static String hostComputer = "localhost";
    private static int portComputer = 5108;
    private static GameType gameType = GameType.TUTORIAL;
    private static final StringProperty stringProperty = new SimpleStringProperty();
    private static BooleanProperty radioButtonSelectionProperty = new SimpleBooleanProperty();

    public enum GameType{
        TUTORIAL("Tutoriel"),
        LOCAL_GAME("Jeu en local"),
        ONLINE_GAME("Jouer en ligne"),
        TEST_GAME("Partie de test");

        private final String gameType;

        GameType(String gameType){
            this.gameType = gameType;
        }

        @Override
        public String toString() {
            return this.gameType;
        }
    }



    public Launcher(PlayerId playerId, Map<PlayerId, String> playerNames) {
        assert isFxApplicationThread();

        launcher = new Stage();

        Node launcherCreator = Launcher.center();
        Node top = Launcher.top();
        Node bottom = Launcher.bottom(launcher);
        Node right = Launcher.right();
        Node left = Launcher.left();

        launcher.setScene(new Scene(new BorderPane(launcherCreator, top, right, bottom, left)));
        launcher.setTitle("Launcher tCHu " + playerNames.get(playerId));
        launcher.setWidth(STAGE_WIDTH);
        launcher.setHeight(STAGE_HEIGHT);
        launcher.setResizable(false);
        launcher.show();

    }

    public static Node center(){

        HBox startWindow = new HBox();
        VBox vBox1 = new VBox();
        vBox1.setBackground(new Background(new BackgroundFill(Color.SALMON, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox1.setSpacing(0);
        vBox1.setPadding(new Insets(0, 0, 0, 0));

        VBox vBox2 = new VBox();
        vBox2.setBackground(new Background(new BackgroundFill(Color.SEAGREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox2.setSpacing(20);
        vBox2.setPadding(new Insets(20, 10, 0, 10));

        Text text = new Text("Choississez le mode de jeu");

        ImageView imageView = new ImageView("launcher.png");
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);
        startWindow.setBackground(new Background(new BackgroundFill(Color.DARKGREY, CornerRadii.EMPTY, Insets.EMPTY)));

        ChoiceBox<GameType> choiceBox = new ChoiceBox<>();
        choiceBox.getItems().addAll(
                GameType.TUTORIAL,
                GameType.LOCAL_GAME,
                GameType.ONLINE_GAME,
                GameType.TEST_GAME);

        choiceBox.getSelectionModel().selectFirst();

        choiceBox.setOnAction(event -> {
            gameType = choiceBox.getValue();
            stringProperty.set(gameType.toString());
        });

        TextField host = new TextField(hostComputer);
        TextField port = new TextField(String.valueOf(portComputer));


        vBox2.getChildren().addAll(text, choiceBox);
        creatRadioButtonGroupAndLinkClientServer(vBox2, host, port);
        vBox2.getChildren().addAll(host, port);

        vBox1.getChildren().addAll(imageView);

        startWindow.getChildren().addAll(vBox1, vBox2);


        return startWindow;
    }

    private static void creatRadioButtonGroupAndLinkClientServer(VBox vBox, TextField host, TextField port){
        host.setDisable(true);
        port.setDisable(true);
        ToggleGroup radioButtonGroup = new ToggleGroup();
        RadioButton radioButton1 = new RadioButton("Server");
        RadioButton radioButton2 = new RadioButton("Cleint");
        radioButton1.setToggleGroup(radioButtonGroup);
        radioButton1.setSelected(true);
        radioButton2.setToggleGroup(radioButtonGroup);
        radioButton1.setDisable(true);
        radioButton2.setDisable(true);
        radioButtonSelectionProperty.set(true);
        stringProperty.addListener((o, oV, nV) ->{
            if (nV.equals(GameType.ONLINE_GAME.toString())){
                radioButton1.setDisable(false);
                radioButton2.setDisable(false);
                host.setDisable(radioButtonSelectionProperty.getValue());
                port.setDisable(radioButtonSelectionProperty.getValue());
            }else {
                radioButton1.setDisable(true);
                radioButton2.setDisable(true);
                host.setDisable(true);
                port.setDisable(true);
            }
        });
        radioButton1.setOnAction(event -> {
            radioButtonSelectionProperty.set(true);
            host.setDisable(true);
            port.setDisable(true);
        });
        radioButton2.setOnAction(event -> {
            radioButtonSelectionProperty.set(false);
            host.setDisable(false);
            port.setDisable(false);
        });
        vBox.getChildren().addAll(radioButton1, radioButton2);
    }

    public static Node top(){

        VBox vBox = new VBox();
        vBox.setPrefSize(700, 100);
        HBox hBox1 = new HBox();
        hBox1.setBackground(new Background(new BackgroundFill(Color.BLUE, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox1.setSpacing(40);
        hBox1.setPadding(new Insets(20, 10, 20, 30));

        HBox hBox2 = new HBox();
        hBox2.setSpacing(40);
        hBox2.setPadding(new Insets(0, 10 , 0 ,30));

        HBox hBox3 = new HBox();
        hBox3.setSpacing(40);
        hBox3.setPadding(new Insets(10, 10 , 10 ,30));


        HBox hBox4 = new HBox();
        hBox4.setSpacing(40);
        hBox4.setPadding(new Insets(3, 10 , 0 ,0));

        Text text = new Text(" Versus ");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        Button button = new Button("Valider les joueurs");
        button.requestFocus();

        TextField textField1 = new TextField(namePlayer1);
        TextField textField2 = new TextField(namePlayer2);

        Text playerText = new Text("Trouvez des noms un peu plus originaux !");
        playerText.setFill(Color.BLACK);
        playerText.setFont(Font.font("Arial", FontWeight.BOLD, 10));


        radioButtonSelectionProperty.addListener((o, oV, nV)->{
            if (gameType == GameType.ONLINE_GAME && !nV){
                playerText.setText("ALLRIGHT");
                textField1.setDisable(true);
                textField2.setDisable(true);
                button.setDisable(true);
            }else {
                playerText.setText("Trouvez des noms un peu plus originaux !");
                textField1.setDisable(false);
                textField2.setDisable(false);
                textField1.setText(namePlayer1);
                textField2.setText(namePlayer2);
                button.setDisable(false);
            }
        });

        ColorPicker colorPicker1 = new ColorPicker(Color.LIGHTBLUE);
        ColorPicker colorPicker2 = new ColorPicker(Color.LIGHTPINK);
        colorPicker1.setOnAction(event -> {
            color1 = colorPicker1.getValue();
            color2 = colorPicker2.getValue();
            System.out.println(color1 + " " + color2);
//          test(color1, color2);
        });
        colorPicker2.setOnAction(event -> {
            color1 = colorPicker1.getValue();
            color2 = colorPicker2.getValue();
            System.out.println(color1 + " " + color2);
//          test(color1, color2);
        });

        button.setOnAction(event -> {
            namePlayer1 = textField1.getText();
            namePlayer2 = textField2.getText();
            if (namePlayer1.length() > 10 || namePlayer2.length() > 10){
                playerText.setText("Les noms sont un peu trop longs !");
            }
            else if (namePlayer1.contains("Joueur 1") || namePlayer2.contains("Joueur 2")){
                playerText.setText("Encore un petit effort pour le nom de l'autre joueur !");
            }else{
                playerText.setText("Les joueurs qui vont s'affronter sont " + namePlayer1 + " et " + namePlayer2 +
                        ".");
            }

        });

        hBox1.getChildren().addAll(textField1, text, textField2, button);
        hBox3.getChildren().addAll(playerText);
        hBox4.getChildren().addAll(colorPicker1, colorPicker2);
        hBox2.getChildren().addAll(hBox3, hBox4);


        vBox.getChildren().addAll(hBox1, hBox2);

        return vBox;
    }

    public static Node right(){
        VBox vBox = new VBox();
        vBox.setPrefSize(50, 500);
        vBox.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, Insets.EMPTY)));
        return vBox;
    }

    public static Node left(){
        VBox vBox = new VBox();
        vBox.setPrefSize(50, 500);
        vBox.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));

        return vBox;
    }

    public static Node bottom(Stage launcher){
        HBox hBox = new HBox();
        hBox.setPrefSize(700, 100);
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(75, 10, 20, 550));

        Button play = new Button("Jouer");
        Button close = new Button("Fermer");


        play.setOnAction(event -> {

            SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
            Map<PlayerId, String> names = Map.of(PLAYER_1, namePlayer1, PLAYER_2, namePlayer2);
            Map<PlayerId, Player> players =
                    Map.of(PLAYER_1, new GraphicalPlayerAdapter(), PLAYER_2, new GraphicalPlayerAdapter());
            Random rng = new Random();

            switch (gameType){
                case TUTORIAL:
                    System.out.println("tutorial");
                    break;

                case ONLINE_GAME:
                    System.out.println("onlineGame " + radioButtonSelectionProperty.getValue());
                    startOnlineGame(tickets, names, rng, radioButtonSelectionProperty.getValue());
                    break;

                case LOCAL_GAME:
                    startLocalGame(tickets, names, players, rng);
                    break;

                case TEST_GAME:
                    System.out.println("TestGame");
                    break;

                default:
                    throw new Error("No such game found");
            }
            launcher.hide();
            Platform.setImplicitExit(false);

//            //Map<PlayerId, String> colors = Map.of(PLAYER_1, namePlayer1, PLAYER_2, namePlayer2); //TODO colors for players

        });

        close.setOnAction(event -> launcher.hide());
        hBox.setBackground(new Background(new BackgroundFill(Color.DIMGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox.getChildren().addAll(play, close);

        return hBox;
    }

    private static void startLocalGame(SortedBag<Ticket> tickets,
                                       Map<PlayerId, String> names,
                                       Map<PlayerId, Player> players,
                                       Random rng){
        new Thread(() -> Game.play(players, names, tickets, rng)).start();
    }

    private static void startOnlineGame(SortedBag<Ticket> tickets,
                                        Map<PlayerId, String> names,
                                        Random rng, boolean radioButtonSelection){

        if (radioButtonSelection){
            try {
                System.out.println("Server Started");
                ServerSocket serverSocket = new ServerSocket(PORT);
                //Waits for an incoming connection
                Socket socket = serverSocket.accept();
                //Creates the two players
                GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
                Player remotePlayerProxy = new RemotePlayerProxy(socket);
                Map<PlayerId, Player> players = Map.of(PLAYER_1, graphicalPlayer , PLAYER_2, remotePlayerProxy);

                //Launches the game's main thread
                new Thread(() -> Game.play(players, names, tickets, rng)).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("Server Started");

            GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
            RemotePlayerClient distantClient = new RemotePlayerClient(graphicalPlayer, hostComputer, portComputer);

            //Starts the network associated thread
            new Thread(distantClient::run).start();

        }
    }




//    public static void test(Color color1, Color color2) throws IOException {
//            PrintToTxt.deleteFile("./resources/launcher.css");
//            PrintToTxt.createFile("./resources/launcher.css");
//            PrintToTxt.writeToFile("./resources/launcher.css",
//                    ".PLAYER_1 .filled { -fx-fill: " + color1.toString() + " ; }\n.PLAYER_2 .filled { -fx-fill: " + color2.toString() + "; }");
//        System.out.println(".PLAYER_1 .filled { -fx-fill: " + color1.toString() + " ; }\n.PLAYER_2 .filled { -fx-fill: " + color2.toString() + "; }");

//        Properties properties = new Properties();
//        String filename = "./resources/launcher.css";
//
//        FileInputStream configStream = new FileInputStream(filename);
//        properties.load(configStream);
//        configStream.close();
//
//        properties.(".PLAYER_1 .filled { -fx-fill: -my-background1; }", );
//        properties.setProperty("newProperty", "newValue");
//
//        FileOutputStream outputStream = new FileOutputStream(filename);
//        properties.store(outputStream, null);
//        outputStream.close();
//    }

//    public void testTXT() {
//        Path path = FileSystems.getDefault().getPath("./resources/launcher.css");
//        try {
//            Files.delete(path);
//        } catch (NoSuchFileException x) {
//            System.err.format("%s: no such" + " file or directory%n", path);
//        } catch (IOException x) {
//            System.err.println(x);
//        }
//    }
//
//    public void writeFile() throws IOException {
//        Path path = FileSystems.getDefault().getPath("./resources/launcher.css");
//        Files.createFile(path);
//        Files.writeString(path, ".PLAYER_1 .filled { -fx-fill: red; } \n.PLAYER_2 .filled { -fx-fill: blue; }");
//        BufferedReader bufferedReader = Files.newBufferedReader(path);
//        for (int i = 0; i < 5; i++) {
//            System.out.println(bufferedReader.readLine());
//        }
//        bufferedReader.close();
//        Files.
//
//    }

//    public static Pane cssSheet(Pane node, Color color1, Color color2){
//        try {
//
//            Path cssPath = Files.createTempFile("fx-theme-", ".css");
//            Files.writeString(cssPath, ".PLAYER1{-fx-fill :" + color1 + ";}" +
//                    ".PLAYER2{-fx-fill :" + color2 + ";}");
//            cssPath.toFile().deleteOnExit();
//            System.out.println("Wrote " + cssPath);
//            System.out.println("URL " + cssPath.toUri().toURL().toExternalForm());
//
//            node.getStyleClass().setAll("PLAYER1");
//            node.getStylesheets().setAll(cssPath.toUri().toURL().toExternalForm());
//        }catch (IOException e) {
//            e.printStackTrace();
//        }
//        return node;
//    }



}
