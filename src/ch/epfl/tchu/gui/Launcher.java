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
import javafx.scene.image.Image;
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
    private static Stage launcher;
    private final int STAGE_WIDTH = 700;
    private final int STAGE_HEIGHT = 500;
    private static String namePlayer1 = "Joueur 1";
    private static String namePlayer2 = "Joueur 2";
    private static ColorPicker colorPicker1 = new ColorPicker(Color.LIGHTBLUE);
    private static ColorPicker colorPicker2 = new ColorPicker(Color.LIGHTPINK);
    private static Color color1;
    private static Color color2;
    private static String hostComputer = "localhost";
    private static int portComputer = 5108;
    private static GameType gameType = GameType.TUTORIAL;
    private static final StringProperty stringProperty = new SimpleStringProperty();
    private static BooleanProperty radioButtonSelectionProperty = new SimpleBooleanProperty();
    private static final Color COLOR = Color.LIGHTBLUE;

    public enum GameType{
        TUTORIAL("Tutoriel"),
        LOCAL_GAME("Jeu en local"),
        ONLINE_GAME("Jouer en ligne"),
        TEST_GAME("Entraînement");

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
        Node bottom = Launcher.bottom();
        Node right = Launcher.right();
        Node left = Launcher.left();

        launcher.setScene(new Scene(new BorderPane(launcherCreator, top, right, bottom, left)));
        launcher.setTitle("Launcher tCHu " + playerNames.get(playerId));
        launcher.setWidth(STAGE_WIDTH);
        launcher.setHeight(STAGE_HEIGHT);
        launcher.setResizable(false);
        launcher.getIcons().add(new Image("launcher.png"));
        launcher.show();

    }

    public static Node center(){

        HBox startWindow = new HBox();
        startWindow.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        VBox vBox1 = new VBox();
        vBox1.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox1.setSpacing(0);
        vBox1.setPadding(new Insets(0, 0, 0, 0));

        VBox vBox2 = new VBox();
        vBox2.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox2.setSpacing(20);
        vBox2.setPadding(new Insets(20, 10, 0, 10));

        VBox vBox3 = new VBox();
        vBox3.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        vBox3.setSpacing(20);
        vBox3.setPadding(new Insets(20, 10, 0, 10));

        Text text = new Text("Choississez le mode de jeu : ");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 13));

        Text infoText = new Text(Strings.tutorial());
        infoText.setFill(Color.WHITE);
        infoText.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        infoText.wrappingWidthProperty().setValue(150);

        ImageView imageView = new ImageView("launcher.png");
        imageView.setFitHeight(250);
        imageView.setFitWidth(250);

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
            radioButtonSelectionProperty.set(true);
            updateInfoText(infoText);
        });

        TextField host = new TextField(hostComputer);
        TextField port = new TextField(String.valueOf(portComputer));

        vBox2.getChildren().addAll(text, choiceBox);
        creatRadioButtonGroupAndLinkClientServer(vBox2, host, port, colorPicker1, colorPicker2);
        vBox2.getChildren().addAll(host, port);
        vBox3.getChildren().add(infoText);
        vBox1.getChildren().addAll(imageView);

        startWindow.getChildren().addAll(vBox1, vBox2, vBox3);

        return startWindow;
    }

    private static void creatRadioButtonGroupAndLinkClientServer(VBox vBox,
                                                                 TextField host,
                                                                 TextField port,
                                                                 ColorPicker colorPicker1,
                                                                 ColorPicker colorPicker2){
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
                colorPicker1.setDisable(!radioButtonSelectionProperty.getValue());
                colorPicker2.setDisable(!radioButtonSelectionProperty.getValue());
            }else {
                radioButton1.setSelected(true);
                radioButton1.setDisable(true);
                radioButton2.setDisable(true);
                host.setDisable(true);
                port.setDisable(true);
                colorPicker1.setDisable(false);
                colorPicker2.setDisable(false);
            }
        });
        radioButton1.setOnAction(event -> {
            radioButtonSelectionProperty.set(true);
            host.setDisable(true);
            port.setDisable(true);
            colorPicker1.setDisable(false);
            colorPicker2.setDisable(false);
        });
        radioButton2.setOnAction(event -> {
            radioButtonSelectionProperty.set(false);
            host.setDisable(false);
            port.setDisable(false);
            colorPicker1.setDisable(true);
            colorPicker2.setDisable(true);
        });
        vBox.getChildren().addAll(radioButton1, radioButton2);
    }

    private static void updateInfoText(Text text){
        switch (gameType){
            case TUTORIAL:
                text.setText(Strings.tutorial());
                break;
            case LOCAL_GAME:
                text.setText(Strings.local());
                break;
            case ONLINE_GAME:
                text.setText(Strings.online());
                break;
            case TEST_GAME:
                text.setText(Strings.training());
                break;
            default:
                throw new Error("no such a game Type");
        }
    }

    public static Node top(){

        VBox vBox = new VBox();
        vBox.setPrefSize(700, 100);
        HBox hBox1 = new HBox();
        hBox1.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox1.setSpacing(40);
        hBox1.setPadding(new Insets(20, 10, 20, 30));

        HBox hBox2 = new HBox();
        hBox2.setSpacing(30);
        hBox2.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox2.setPadding(new Insets(0, 10 , 15 ,30));

        HBox hBox3 = new HBox();
        hBox3.setSpacing(20);
        hBox3.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox3.setPadding(new Insets(10, 0 , 10 ,0));

        HBox hBox4 = new HBox();
        hBox4.setSpacing(10);
        hBox4.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox4.setPadding(new Insets(3, 10 , 0 ,0));

        Text text = new Text(" Versus ");
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 25));

        Button button = new Button("Valider les joueurs");
        button.requestFocus();

        TextField textField1 = new TextField(namePlayer1);
        TextField textField2 = new TextField(namePlayer2);

        Text playerText = new Text(Strings.findBetterNames());
        playerText.setFill(Color.WHITE);
        playerText.setFont(Font.font("Arial", FontWeight.BOLD, 13));
        playerText.wrappingWidthProperty().setValue(300);


        radioButtonSelectionProperty.addListener((o, oV, nV)->{
            if (gameType == GameType.ONLINE_GAME && !nV) {
                playerText.setText(Strings.clientSelected());
                textField1.setDisable(true);
                textField2.setDisable(true);
                button.setDisable(true);
            }else {
                playerText.setText(Strings.findBetterNames());
                textField1.setDisable(false);
                textField2.setDisable(false);
                textField1.setText(namePlayer1);
                textField2.setText(namePlayer2);
                button.setDisable(false);
            }
        });

        colorPicker1.setOnAction(event -> {
            color1 = colorPicker1.getValue();
            color2 = colorPicker2.getValue();
            System.out.println(color1 + " " + color2);
            try {
                test(color1, color2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        colorPicker2.setOnAction(event -> {
            color1 = colorPicker1.getValue();
            color2 = colorPicker2.getValue();
            System.out.println(color1 + " " + color2);
            try {
                test(color1, color2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        button.setOnAction(event -> {
            namePlayer1 = textField1.getText();
            namePlayer2 = textField2.getText();
            if (namePlayer1.length() > 10 || namePlayer2.length() > 10){
                playerText.setText(Strings.tooLongNames());
            }
            else if (namePlayer1.contains("Joueur 1") || namePlayer2.contains("Joueur 2")){
                playerText.setText(Strings.anotherChance());
            }else{
                playerText.setText(Strings.twoNames(namePlayer1, namePlayer2));
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
        vBox.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        return vBox;
    }

    public static Node left(){
        VBox vBox = new VBox();
        vBox.setPrefSize(50, 500);
        vBox.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));

        return vBox;
    }

    public static Node bottom(){
        HBox hBox = new HBox();
        hBox.setPrefSize(700, 75);
        hBox.setSpacing(20);
        hBox.setPadding(new Insets(20, 10, 20, 550));

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
                    startTutorialGame(tickets, names, players, rng);
                    break;

                case ONLINE_GAME:
                    startOnlineGame(tickets, names, rng, radioButtonSelectionProperty.getValue());
                    break;

                case LOCAL_GAME:
                    startLocalGame(tickets, names, players, rng);
                    break;

                case TEST_GAME:
                    startTrainingGame(tickets, names, players, rng);
                    break;

                default:
                    throw new Error("No such game found");
            }
            launcher.hide();
            Platform.setImplicitExit(false);

//            //Map<PlayerId, String> colors = Map.of(PLAYER_1, namePlayer1, PLAYER_2, namePlayer2); //TODO colors for players

        });

        close.setOnAction(event -> launcher.hide());
        hBox.setBackground(new Background(new BackgroundFill(COLOR, CornerRadii.EMPTY, Insets.EMPTY)));
        hBox.getChildren().addAll(play, close);

        return hBox;
    }

    private static void startLocalGame(SortedBag<Ticket> tickets,
                                       Map<PlayerId, String> names,
                                       Map<PlayerId, Player> players,
                                       Random rng){
        new Thread(() -> Game.play(players, names, tickets, rng)).start();
    }

    private static void startTrainingGame(SortedBag<Ticket> tickets,
                                          Map<PlayerId, String> names,
                                          Map<PlayerId, Player> players,
                                          Random rng){



        new Thread(() -> Game.play(
                Map.of(PLAYER_1, players.get(PLAYER_1), PLAYER_2, new TrainingPlayer()),
                Map.of(PLAYER_1, names.get(PLAYER_1), PLAYER_2, "Assistant d'entrainement"),
                tickets,
                rng))
                .start();
    }

    private static void startTutorialGame(SortedBag<Ticket> tickets,
                                          Map<PlayerId, String> names,
                                          Map<PlayerId, Player> players,
                                          Random rng){
        System.out.println("TUTO");
    }

    private static void startOnlineGame(SortedBag<Ticket> tickets,
                                        Map<PlayerId, String> names,
                                        Random rng, boolean radioButtonSelection){

        if (radioButtonSelection){
            try {
                System.out.println("Server Started");
                ServerSocket serverSocket = new ServerSocket(PORT);
                Socket socket = serverSocket.accept();
                GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
                Player remotePlayerProxy = new RemotePlayerProxy(socket);
                Map<PlayerId, Player> players = Map.of(PLAYER_1, graphicalPlayer , PLAYER_2, remotePlayerProxy);
                new Thread(() -> Game.play(players, names, tickets, rng)).start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            System.out.println("Server Started");

            GraphicalPlayerAdapter graphicalPlayer = new GraphicalPlayerAdapter();
            RemotePlayerClient distantClient = new RemotePlayerClient(graphicalPlayer, hostComputer, portComputer);
            new Thread(distantClient::run).start();

        }
    }

    private static final class Strings {
        private Strings(){
            throw new Error("no such a class instance");
        }

        public static String tutorial(){
            return "Ceci lance un tutoriel de jeu.\nVous y apprendrez les règles du jeu au travers d'une partie guidée.";
        }

        public static String online(){
            return "Ceci lance une partie en ligne.\nVous devez spécifier si vous êtes le serveur ou le client du jeu. " +
                    "\nATTENTION : si vous êtes le client veillez à ne lancer la partie qu'une fois le serveur lancé sur l'autre ordinateur.";
        }

        public static String local(){
            return "Ceci lance une partie pour deux joueurs en local.\nVous jouerez à deux sur le même ordinateur.";
        }

        public static String training(){
            return "Cecei lance le mode d'entraînement.\nVous jouerez contre un ordinateur effectuant des actions aléatoires.";
        }

        public static String findBetterNames(){
            return "Trouvez des noms un peu plus originaux !";
        }

        public static String tooLongNames(){
            return "Les noms sont un peu trop longs !";
        }

        public static String anotherChance(){
            return "Encore un petit effort pour l'autre joueur !";
        }

        public static String twoNames(String namePlayer1, String namePlayer2){
            return "Les joueurs qui vont s'affronter sont " + namePlayer1 + " et " + namePlayer2 + ".";
        }

        public static String clientSelected(){
            return "Désactivée lorsque vous êtes client.";
        }

    }




    public static void test(Color color1, Color color2) throws IOException {
        PrintToTxt.deleteFile("./resources/launcher.css");
        PrintToTxt.createFile("./resources/launcher.css");
        PrintToTxt.writeToFile("./resources/launcher.css",
                ".PLAYER_1 .filled { -fx-fill: #" + color1.toString().substring(2) + " ; }\n.PLAYER_2 .filled { -fx-fill: #" + color2.toString().substring(2) + "; }");

    }
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
