package ch.epfl.tchu.gui;

import ch.epfl.tchu.gui.ActionHandlers.TutorialHandler;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

final class TutorialViewCreator {

    private TutorialViewCreator() {
        throw new UnsupportedOperationException();
    }

    /**
     * Creates the top node for the main borderPane
     * @param tutorialText containing the instructions of the tutorial
     * @param tutorialHandler to handle the two buttons
     * @return a tutorialView node
     */
    public static Node createTutorialView(ObservableList<Text> tutorialText,
                                          ObjectProperty<TutorialHandler> tutorialHandler) {

        HBox tutorialView = new HBox();

        tutorialView.setBackground(new Background(new BackgroundFill(Color.RED, CornerRadii.EMPTY, Insets.EMPTY)));
        System.out.println(tutorialView.getWidth() + ' ' + tutorialView.getHeight());

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

        tutorialView.setMaxSize(500, 20);

        return tutorialView;
    }



}
