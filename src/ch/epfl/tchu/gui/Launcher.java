package ch.epfl.tchu.gui;

import ch.epfl.tchu.SortedBag;
import ch.epfl.tchu.game.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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
import java.util.Map;
import java.util.Random;

import static ch.epfl.tchu.game.PlayerId.PLAYER_1;
import static ch.epfl.tchu.game.PlayerId.PLAYER_2;
import static javafx.application.Platform.isFxApplicationThread;

public final class Launcher {

    private final Stage launcher;
    private final int STAGE_WIDTH = 700;
    private final int STAGE_HEIGHT = 500;
    private static String namePlayer1 = "Joueur 1";
    private static String namePlayer2 = "Joueur 2";


    public Launcher(PlayerId playerId, Map<PlayerId, String> playerNames){
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

        Pane startWindow = new Pane();
//        startWindow.getStylesheets().add("launcher.css"); //TODO css optionnel
        ImageView imageView = new ImageView("launcher.png");
        imageView.setFitHeight(270);
        imageView.setFitWidth(270);
        startWindow.setBackground(new Background(new BackgroundFill(Color.GREY, CornerRadii.EMPTY, Insets.EMPTY)));
        startWindow.getChildren().add(imageView);


        return startWindow;
    }

    public static Node top(){

        VBox vBox = new VBox();
        vBox.setPrefSize(700, 100);

//        hBox1.setMaxHeight(75);
//        hBox1.setMaxSize(700, 50);
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

        ColorPicker colorPicker1 = new ColorPicker(Color.LIGHTBLUE);
        ColorPicker colorPicker2 = new ColorPicker(Color.LIGHTPINK);

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

//        play.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
//        close.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        play.setOnAction(event -> {
            Platform.setImplicitExit(false);
            SortedBag<Ticket> tickets = SortedBag.of(ChMap.tickets());
            Map<PlayerId, String> names = Map.of(PLAYER_1, namePlayer1, PLAYER_2, namePlayer2);
            Map<PlayerId, Player> players =
                    Map.of(PLAYER_1, new GraphicalPlayerAdapter(), PLAYER_2, new GraphicalPlayerAdapter());
            Random rng = new Random();
            new Thread(() -> Game.play(players, names, tickets, rng)).start();
            launcher.hide();

        });

        close.setOnAction(event -> launcher.hide());

        hBox.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

        hBox.getChildren().addAll(play, close);

        return hBox;
    }

}
