package ch.epfl.tchu.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

        Button nextButton = new Button("Continuer");
        Button leaveButton = new Button("Quitter le tutoriel");

        nextButton.setOnAction(e -> tutorialHandler.get().onButtonClick(false));
        nextButton.disableProperty().bind(tutorialHandler.isNull());
        leaveButton.setOnAction(e -> tutorialHandler.get().onButtonClick(true));
        leaveButton.disableProperty().bind(tutorialHandler.isNull());

        FlowPane buttons = new FlowPane(nextButton, leaveButton);

        TextFlow text = new TextFlow();
        text.setMaxWidth(450);




        Bindings.bindContent(text.getChildren(), tutorialText);

        tutorialVBox.getChildren().addAll(text, buttons);

        ImageView image = new ImageView("train-driver.png");
        image.setFitWidth(100);
        image.setFitHeight(120);

        tutorialView.getChildren().addAll(tutorialVBox, image);

        tutorialView.setMaxSize(500, 100);

        return tutorialView;
    }



}
