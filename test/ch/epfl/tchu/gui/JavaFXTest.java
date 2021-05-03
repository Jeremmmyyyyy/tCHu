package ch.epfl.tchu.gui;

import javafx.scene.paint.Color;
import org.junit.jupiter.api.Test;

public final class JavaFXTest {

    @Test
    public void JavaFXisCorrectlyImplemented(){
        Color c = Color.RED;
        System.out.println(c.getRed());
    }
}
