package ch.epfl.tchu.gui;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

final class TutorialViewCreator {

    private TutorialViewCreator() {
        throw new UnsupportedOperationException();
    }

    public static Node createTutorialView(ObservableGameState observableGameState, ObservableList<Text> tutorialText,
                                          ObjectProperty<TutorialHandler> tutorialHandler) {


        VBox tutorialView = new VBox();
        tutorialView.getStylesheets().add("tutorial-box.css");

        tutorialView.setPadding(new Insets(0, 200, 0, 200));

        Button nextButton = new Button("Continuer");
        Button leaveButton = new Button("Quitter le tutoriel");

        nextButton.setOnAction(e -> tutorialHandler.get().onButtonClick(false));
        nextButton.disableProperty().bind(tutorialHandler.isNull());
        leaveButton.setOnAction(e -> tutorialHandler.get().onButtonClick(true));



        FlowPane buttons = new FlowPane(nextButton, leaveButton);

        TextFlow text = new TextFlow();

        Bindings.bindContent(text.getChildren(), tutorialText);

        tutorialView.getChildren().addAll(text, buttons);


        return tutorialView;
    }



}
