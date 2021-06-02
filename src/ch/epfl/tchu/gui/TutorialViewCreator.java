package ch.epfl.tchu.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

final class TutorialViewCreator {

    private TutorialViewCreator() {
        throw new UnsupportedOperationException();
    }

    public static Node createTutorialView(ObservableGameState observableGameState, ObservableList<Text> tutorialText,
                                          ObjectProperty<TutorialHandler> tutorialHandler) {

        HBox tutorialView = new HBox();

        VBox tutorialVBox = new VBox();
        tutorialVBox.getStylesheets().add("tutorial-box.css");

        tutorialVBox.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));




        Button nextButton = new Button("Continuer");
        Button leaveButton = new Button("Quitter le tutoriel");

        nextButton.setOnAction(e -> tutorialHandler.get().onButtonClick(false));
        nextButton.disableProperty().bind(tutorialHandler.isNull());
        leaveButton.setOnAction(e -> tutorialHandler.get().onButtonClick(true));
        leaveButton.disableProperty().bind(tutorialHandler.isNull());

        FlowPane buttons = new FlowPane(nextButton, leaveButton);

        TextFlow text = new TextFlow();
        text.setMaxWidth(400);




        Bindings.bindContent(text.getChildren(), tutorialText);

        tutorialVBox.getChildren().addAll(text, buttons);

        tutorialVBox.setPadding(new Insets(0, 0, 0, 200));

        ImageView image = new ImageView("train-driver.png");
        image.setFitWidth(100);
        image.setFitHeight(120);

        tutorialView.getChildren().addAll(tutorialVBox, image);



        return tutorialView;
    }



}
